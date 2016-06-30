package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.board.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by kas0610 on 16. 1. 15.
 */
@Controller
@RequestMapping("/user/board")
public class BoardCommController {
    private final static Logger LOG = LoggerFactory.getLogger(BoardCommController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confAdmin;


    /**
     * 게시물을 등록 한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param documentEntity 추가할 게시물 정보
     * @param bindingResult BindingResult object
     * @return model and view
     */
    @RequestMapping(value = "/document/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addDocumentSubmit(HttpServletRequest request,
                                          @PathVariable("tid") String tid,
                                          @Valid @RequestBody DocumentEntity documentEntity,
                                          BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            // ajax 로 hashtag url 로 접근하여 페이지 교체 방식 이므로 에러 메시지를 view 로 전달 할 수 없다.
            // view 로 전달하려면 redirect 하지 않고, view 를 바로 보여야 하는데
            // 이러면 view frame 형태가 깨져 버린다. 로그만 찍자.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> reason = new HashMap<>();

            for(FieldError fieldError : fieldErrors) {
                reason.put(fieldError.getField(), fieldError.getDefaultMessage());
                LOG.warn("invalid field. field name["+fieldError.getField()+
                        "], message["+fieldError.getDefaultMessage()+"]");
            }

            // NOTE 400 을 리턴 시키면 jQuery 에서 cors 가 발생 한다.
            throw new CustomException("add_document_error", reason);
        }

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        documentEntity.setMember_srl(userDetails.getMember_srl());
        documentEntity.setUser_id(userDetails.getUser_id());
        documentEntity.setUser_name(userDetails.getUser_name());
        documentEntity.setNick_name(userDetails.getNick_name());
        documentEntity.setEmail_address(userDetails.getEmail_address());
        documentEntity.setDocument_password("");
        documentEntity.setIpaddress(webUtilService.getRequestIp(request));

        // 혹시 urlencoder로 들어 올 수 있으므로 decoder 한번 돌린다.
        try {
            documentEntity.setDocument_title(URLDecoder.decode(documentEntity.getDocument_title(), "utf-8"));
            documentEntity.setTemplate_extra(URLDecoder.decode(documentEntity.getTemplate_extra(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_title", "invalid encode document_title");
            LOG.error(reason.get("document_title")+". exception [" + e.getMessage() + "]");
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentEntity.getOuter_link() != null) {
            try {
                documentEntity.setOuter_link(URLDecoder.decode(documentEntity.getOuter_link(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                Map<String, String> reason = new HashMap<>();
                reason.put("outer_link", "invalid encode outer_link");
                LOG.error(reason.get("outer_link") + ". exception [" + e.getMessage() + "]");
                throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
        }

        documentService.addDocument(documentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("document_srl", documentEntity.getDocument_srl());

        return mav;
    }

    /**
     * 게시물/게시물 링크를 삭제 한다.
     *
     * @param tid transaction id
     * @param keyBean l_keys 에 삭제할 게시물/게시물 링크의 시리얼 넘버 리스트가 포함되어 있다.
     *                i_keys 에는 게시물인지 게시물 링크 인지 구분값이 포함되어 있다.
     * @return model and view object
     */
    @RequestMapping(value = "/document/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteDocument(@PathVariable("tid") String tid,
                                       @RequestBody KeyBean keyBean) {
        if(keyBean.getL_keys() == null || keyBean.getL_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("l_keys", "failed delete document. l_keys is null or empty");
            LOG.error(reason.get("l_keys"));
            throw new CustomException("delete_document_error", reason);
        }

        boolean isLink = false;
        if(keyBean.getI_keys() != null && keyBean.getI_keys().size() > 0) {
            if(keyBean.getI_keys().get(0) == MDV.YES) isLink = true;
        }

        if(isLink)  documentService.deleteDocumentLink(keyBean.getL_keys());
        else        documentService.deleteDocument(keyBean.getL_keys());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("document_srls", keyBean.getL_keys());
        mav.addObject("link_flag", isLink ? MDV.YES : MDV.NO);

        return mav;
    }

    /**
     * 게시물을 수정 한다.
     *
     * @param documentSrl 수정할 게시물 시리얼 넘버
     * @param tid transaction id
     * @param documentEntity 수정할 게시물 정보
     * @param bindingResult BindingResult object
     * @return model and view
     */
    @RequestMapping(value = "/document/{document_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyDocument(@PathVariable("document_srl") int documentSrl,
                                       @PathVariable("tid") String tid,
                                       @Valid @RequestBody DocumentEntity documentEntity,
                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            // ajax 로 hashtag url 로 접근하여 페이지 교체 방식 이므로 에러 메시지를 view 로 전달 할 수 없다.
            // view 로 전달하려면 redirect 하지 않고, view 를 바로 보여야 하는데
            // 이러면 view frame 형태가 깨져 버린다. 로그만 찍자.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> reason = new HashMap<>();

            for(FieldError fieldError : fieldErrors) {
                reason.put(fieldError.getField(), fieldError.getDefaultMessage());
                LOG.warn("invalid field. field name["+fieldError.getField()+
                        "], message["+fieldError.getDefaultMessage()+"]");
            }

            // NOTE 400 을 리턴 시키면 jQuery 에서 cors 가 발생 한다.
            throw new CustomException("modify_document_error", reason);
        }

        // 혹시 urlencoder로 들어 올 수 있으므로 decoder 한번 돌린다.
        try {
            documentEntity.setDocument_title(URLDecoder.decode(documentEntity.getDocument_title(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_title", "invalid encode document_title");
            LOG.error(reason.get("document_title")+". exception [" + e.getMessage() + "]");
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentEntity.getOuter_link() != null) {
            try {
                documentEntity.setOuter_link(URLDecoder.decode(documentEntity.getOuter_link(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                Map<String, String> reason = new HashMap<>();
                reason.put("outer_link", "invalid encode outer_link");
                LOG.error(reason.get("outer_link") + ". exception [" + e.getMessage() + "]");
                throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
        }

        documentService.modifyDocument(documentEntity, documentSrl);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("document_srl", documentSrl);

        return mav;
    }

}
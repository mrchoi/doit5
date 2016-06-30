package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.board.DocumentCommentEntity;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.board.DocumentLinkEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.board.CommentService;
import com.ckstack.ckpush.service.board.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by kodaji on 2016. 1. 31..
 */
@Controller
@RequestMapping("/admin/board/document/comment")
public class CommentController {
    private final static Logger LOG = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confDym;

    /**
     * 댓글을 생성 한다.
     *
     * @param tid transaction id
     * @param documentCommentEntity 생성할 댓글 정보.
     * @param bindingResult BindingResult object
     * @return model and view
     */
    @RequestMapping(value = "/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addDocumentComment(HttpServletRequest request,
                                           @PathVariable("tid") String tid,
                                           @Valid @RequestBody DocumentCommentEntity documentCommentEntity,
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
            throw new CustomException("add_link_document_error", reason);
        }

        DocumentEntity documentEntity = documentService.getDocument(documentCommentEntity.getDocument_srl());
        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "not found document. document_srl [" + documentCommentEntity.getDocument_srl() + "]");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("add_link_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        documentCommentEntity.setMember_srl(userDetails.getMember_srl());
        documentCommentEntity.setUser_id(userDetails.getUser_id());
        documentCommentEntity.setUser_name(userDetails.getUser_name());
        documentCommentEntity.setNick_name(userDetails.getNick_name());
        documentCommentEntity.setEmail_address(userDetails.getEmail_address());
        //documentCommentEntity.setDocument_password("");
        documentCommentEntity.setIpaddress(webUtilService.getRequestIp(request));

        commentService.addDocumentComment(documentCommentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("document_comment_srl", documentCommentEntity.getComment_srl());
        mav.addObject("document_srl", documentEntity.getDocument_srl());
        mav.addObject("document_title", documentEntity.getDocument_title());

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
    @RequestMapping(value = "/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteDocument(@PathVariable("tid") String tid,
                                       @RequestBody KeyBean keyBean) {
        if(keyBean.getL_keys() == null || keyBean.getL_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("l_keys", "failed delete document. l_keys is null or empty");
            LOG.error(reason.get("l_keys"));
            throw new CustomException("delete_document_error", reason);
        }

        LOG.info("==================== tid : " + tid + " / keyBean.getL_keys() " + keyBean.getL_keys());
        //boolean isLink = false;
        //if(keyBean.getI_keys() != null && keyBean.getI_keys().size() > 0) {
        //    if(keyBean.getI_keys().get(0) == MDV.YES) isLink = true;
        //}

        commentService.deleteComments(MDV.NONE, keyBean.getL_keys());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("document_srls", keyBean.getL_keys());

        return mav;
    }
}

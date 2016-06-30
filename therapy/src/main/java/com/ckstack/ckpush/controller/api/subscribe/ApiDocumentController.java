package com.ckstack.ckpush.controller.api.subscribe;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.data.cache.AccessTokenExtra;
import com.ckstack.ckpush.data.request.DocumentListBean;
import com.ckstack.ckpush.domain.board.DocumentBoardEntity;
import com.ckstack.ckpush.domain.board.DocumentCategoryEntity;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.board.DocumentLinkEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.board.DocumentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

/**
 * Created by dhkim94 on 15. 8. 27..
 */
@Controller
@RequestMapping("/api/document")
public class ApiDocumentController {
    private final static Logger LOG = LoggerFactory.getLogger(ApiDocumentController.class);

    @Autowired
    private DocumentService documentService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;

    /**
     * 게시물을 조회 한다.
     *
     * @param request HttpServletRequest object
     * @param documentSrl 조회할 게시물의 시리얼 넘버
     * @param tid transaction id
     * @return model and view object
     */
    @RequestMapping(value = "/{document_srl}/t/{tid}", method = RequestMethod.GET)
    public ModelAndView getDocument(HttpServletRequest request,
                                    @PathVariable("document_srl") long documentSrl,
                                    @PathVariable("tid") String tid) {
        // 접속 토큰 정보를 구한다.
        AccessTokenExtra accessTokenExtra = (AccessTokenExtra) request.getAttribute(
                confCommon.getProperty("access_token_info"));

        DocumentEntity documentEntity = documentService.getDocument(documentSrl);
        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H1)");
            LOG.error(reason.get("invalid") + " not found document. document_srl [" + documentSrl + "]");
            throw new CustomException("read_document_error", reason);
        }

        if(documentEntity.getBlock() == MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H2)");
            LOG.error(reason.get("invalid") + " blocked document. document_srl [" + documentSrl + "]");
            throw new CustomException("read_document_error", reason);
        }

        if(documentEntity.getSecret() > 0 && documentEntity.getSecret() != accessTokenExtra.getMember_srl()) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H3)");
            LOG.error(reason.get("invalid") + " secret document. owner [" + documentEntity.getSecret() +
                    "], try read member [" + accessTokenExtra.getMember_srl() + "]");
            throw new CustomException("read_document_error", reason);
        }

        Map<String, Object> document = documentService.cleanUpDocumentForAPI(documentEntity, request);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("document", document);

        return mav;
    }

    /**
     * 카테고리 내의 게시물 리스트를 구한다. 태그 검색도 지원 한다.
     * 일반 카테고리, 링크 카테고리 구분 없이 게시물 리스트를 구한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param documentListBean 리스트 검색, 소팅 조건
     * @param bindingResult BindingResult object
     * @return model and view
     */
    @RequestMapping(value = "/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listDocument(HttpServletRequest request,
                                     @PathVariable("tid") String tid,
                                     @Valid @RequestBody DocumentListBean documentListBean,
                                     BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> reason = new HashMap<>();

            for(FieldError fieldError : fieldErrors) {
                reason.put(fieldError.getField(), fieldError.getDefaultMessage());
                LOG.warn("invalid field. field name["+fieldError.getField()+
                        "], message["+fieldError.getDefaultMessage()+"]");
            }

            // NOTE 400 을 리턴 시키면 jQuery 에서 cors 가 발생 한다.
            throw new CustomException("list_document_error", reason);
        }

        // 접속 토큰 정보를 구한다.
        AccessTokenExtra accessTokenExtra = (AccessTokenExtra) request.getAttribute(
                confCommon.getProperty("access_token_info"));

        DocumentCategoryEntity documentCategoryEntity = documentService.getDocumentCategory(
                documentListBean.getCategory_srl());
        if(documentCategoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H1)");
            LOG.error(reason.get("invalid") + " not category. category_srl [" + documentListBean.getCategory_srl() + "]");
            throw new CustomException("list_document_error", reason);
        }

        if(documentCategoryEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H2)");
            LOG.error(reason.get("invalid") + " disabled category. category_srl [" + documentListBean.getCategory_srl() + "]");
            throw new CustomException("list_document_error", reason);
        }

        DocumentBoardEntity documentBoardEntity = documentService.getDocumentBoard(documentCategoryEntity.getBoard_srl());
        if(documentBoardEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H3)");
            LOG.error(reason.get("invalid") + " disabled board. board_srl [" +
                    documentCategoryEntity.getBoard_srl() + "]");
            throw new CustomException("list_document_error", reason);
        }

        Map<String, String> searchFilter = new HashMap<>();
        searchFilter.put("category_srl", Long.toString(documentListBean.getCategory_srl()));
        searchFilter.put("block", Integer.toString(MDV.NO));

        if(documentListBean.getTitle() != null) {
            String documentTitle = StringUtils.trim(documentListBean.getTitle());
            if(!StringUtils.equals(documentTitle, "")) searchFilter.put("document_title", documentTitle);
        }

        int offset = documentListBean.getOffset(), limit = documentListBean.getLimit();
        if(offset < 0) offset = 0;
        if(limit <= 0) {
            offset = MDV.NUSE;
            limit = MDV.NUSE;
        }

        List<Long> secrets = new ArrayList<>();
        secrets.add((long) MDV.NONE);
        secrets.add((long) MDV.SECRET_DOCUMENT_OF_PUBLIC_CATEGORY);
        secrets.add(accessTokenExtra.getMember_srl());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        // sort를 정리 한다.
        // 공지 사항은 항상 리스트 처음에 나타나도록 한다.
        Map<String, String> sort = new LinkedHashMap<>();
        sort.put("allow_notice", "ASC");

        String documentSrlSortName = "document_srl";
        if(documentCategoryEntity.getCategory_type() == MDV.LINK_CATEGORY)
            documentSrlSortName = "document_link_srl";

        boolean isUseDocumentSrlSort = false;
        if(documentListBean.getSort() != null) {
            Set<String> keys = documentListBean.getSort().keySet();
            for(String key : keys) {
                if(StringUtils.equals(key, "title")) {
                    if(StringUtils.equalsIgnoreCase(documentListBean.getSort().get(key), "desc"))
                        sort.put("document_title", "DESC");
                    else
                        sort.put("document_title", "ASC");

                } else if(StringUtils.equals(key, "document_srl")) {
                    isUseDocumentSrlSort = true;

                    if(StringUtils.equalsIgnoreCase(documentListBean.getSort().get(key), "desc"))
                        sort.put(documentSrlSortName, "DESC");
                    else
                        sort.put(documentSrlSortName, "ASC");

                } else if(StringUtils.equals(key, "list_order")) {
                    if(StringUtils.equalsIgnoreCase(documentListBean.getSort().get(key), "desc"))
                        sort.put("list_order", "DESC");
                    else
                        sort.put("list_order", "ASC");

                } else {
                    LOG.warn("not support sort column for document list. column [" + key + "]");
                }
            }
        }

        if(!isUseDocumentSrlSort) sort.put(documentSrlSortName, "DESC");

        long totalRows;
        if(documentCategoryEntity.getCategory_type() == MDV.NORMAL_CATEGORY) {
            List<DocumentEntity> documentEntities;

            if(documentListBean.getTags() == null) {
                totalRows = documentService.countDocument(searchFilter, secrets);
                documentEntities = documentService.getDocument(searchFilter, secrets,
                        sort, offset, limit);
            } else {
                totalRows = documentService.countDocumentOfTag(searchFilter, documentListBean.getTags(), secrets);
                documentEntities = documentService.getDocumentOfTag(searchFilter,
                        documentListBean.getTags(), secrets, sort, offset, limit);
            }

            mav.addObject("documents", documentService.cleanUpDocumentListForAPI(documentEntities,
                    documentListBean.isOpt_content(), request));
        } else {
            List<DocumentLinkEntity> documentLinkEntities;

            if(documentListBean.getTags() == null) {
                totalRows = documentService.countDocumentLink(searchFilter, secrets);
                documentLinkEntities = documentService.getDocumentLink(searchFilter,
                        secrets, sort, offset, limit);
            } else {
                totalRows = documentService.countDocumentLinkOfTag(searchFilter, documentListBean.getTags(), secrets);
                documentLinkEntities = documentService.getDocumentLinkOfTag(searchFilter,
                        documentListBean.getTags(), secrets, sort, offset, limit);
            }

            mav.addObject("documents", documentService.cleanUpDocumentLinkListForAPI(documentLinkEntities,
                    documentListBean.isOpt_content(), request));
        }

        mav.addObject("total_rows", totalRows);
        mav.addObject("offset", offset < 0 ? MDV.NONE : offset);
        mav.addObject("limit", limit <= 0 ? MDV.NONE : limit);

        return mav;
    }

    /**
     * 게시판 리스트및 게시판에 포함되어 있는 카테고리 리스트를 구한다.
     * 카테고리 리스트에서는 카테고리 내의 게시물 카운트도 포함되어 있다.
     * 로그인 필요한 게시판, 로그인 필요 없는 게시판 모두 출력 된다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return model and view
     */
    @RequestMapping(value = "/board/list/t/{tid}", method = RequestMethod.GET)
    public ModelAndView listBoard(HttpServletRequest request,
                                  @PathVariable("tid") String tid) {
        // 접속 토큰 정보를 구한다.
        AccessTokenExtra accessTokenExtra = (AccessTokenExtra) request.getAttribute(
                confCommon.getProperty("access_token_info"));

        List<Long> secrets = new ArrayList<>();
        secrets.add((long) MDV.NONE);
        secrets.add((long) MDV.SECRET_DOCUMENT_OF_PUBLIC_CATEGORY);
        secrets.add(accessTokenExtra.getMember_srl());
        List<Map<String, Object>> boardList = documentService.getBoardCategoryInApp(accessTokenExtra.getApp_srl(),
                MDV.NUSE, MDV.NUSE, MDV.YES, secrets, MDV.NO);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("board", boardList);

        return mav;
    }

}

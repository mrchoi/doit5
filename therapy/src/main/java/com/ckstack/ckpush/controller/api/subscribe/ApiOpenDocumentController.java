package com.ckstack.ckpush.controller.api.subscribe;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.data.request.DocumentListBean;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.board.DocumentBoardEntity;
import com.ckstack.ckpush.domain.board.DocumentCategoryEntity;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.board.DocumentLinkEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
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
 * Created by dhkim94 on 15. 9. 14..
 */
@Controller
@RequestMapping("/api/open/document")
public class ApiOpenDocumentController {
    private final static Logger LOG = LoggerFactory.getLogger(ApiOpenDocumentController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confSvc;

    /**
     * 게시물 리스트를 구한다.
     *
     * @param request HttpServletRequest object
     * @param documentListBean 게시물 리스트를 구할 조건
     * @param tid transaction id
     * @return model and view object
     */
    private ModelAndView listDocument(HttpServletRequest request, DocumentListBean documentListBean,
                                      String tid) {
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

        if(documentCategoryEntity.getOpen_type() != MDV.PUBLIC) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H3)");
            LOG.error(reason.get("invalid") + " private category but request in public. category_srl [" +
                    documentListBean.getCategory_srl() + "], open_type [" + documentCategoryEntity.getOpen_type() + "]");
            throw new CustomException("list_document_error", reason);
        }

        DocumentBoardEntity documentBoardEntity = documentService.getDocumentBoard(documentCategoryEntity.getBoard_srl());

        if(documentBoardEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H4)");
            LOG.error(reason.get("invalid") + " disabled board. board_srl [" +
                    documentCategoryEntity.getBoard_srl() + "]");
            throw new CustomException("list_document_error", reason);
        }

        if(documentBoardEntity.getOpen_type() != MDV.PUBLIC) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H5)");
            LOG.error(reason.get("invalid") + " private board but request in public. category_srl [" +
                    documentListBean.getCategory_srl() + "], board_srl [" + documentBoardEntity.getBoard_srl() +
                    "], open_type [" + documentCategoryEntity.getOpen_type() + "]");
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
        mav.addObject("category_srl", documentCategoryEntity.getCategory_srl());
        mav.addObject("category_name", documentCategoryEntity.getCategory_name());

        return mav;
    }

    /**
     * 웹브라우저에서 보는 용도로 게시물 리스트와 게시판, 카테고리 리스트를 모두 포함한 api 이다.
     *
     * @param request HttpServletRequest object
     * @param categorySrl 게시물 리스트 할 카테고리 시리얼 넘버
     * @param page 게시판 리스트의 페이지
     * @param tid transaction id
     * @return model and view object
     */
    @RequestMapping(value = "/list/category/{category_srl}/pg/{page}/t/{tid}", method = RequestMethod.GET)
    public ModelAndView listDocument(HttpServletRequest request,
                                     @PathVariable("category_srl") long categorySrl,
                                     @PathVariable("page") int page,
                                     @PathVariable("tid") String tid) {
        // 게시판, 카테고리 리스트를 구한다.
        DocumentCategoryEntity documentCategoryEntity = documentService.getDocumentCategory(categorySrl);
        ModelAndView mav2 = this.listBoard(documentCategoryEntity.getApp_srl(), tid);

        int onePageRows = Integer.parseInt(confSvc.getProperty("list_one_page_row"), 10);

        DocumentListBean documentListBean = new DocumentListBean();
        documentListBean.setCategory_srl(categorySrl);
        documentListBean.setTitle(null);
        documentListBean.setTags(null);
        documentListBean.setSort(null);
        documentListBean.setOffset(page * onePageRows);
        documentListBean.setLimit(onePageRows);
        documentListBean.setOpt_content(true);

        // 서비스 웹페이지 용으로 몇몇개를 추가 한다.
        ModelAndView mav = this.listDocument(request, documentListBean, tid);
        mav.setViewName("f_api/board/list_document_blog_type");

        Map<String, Object> modelMap = mav.getModel();
        long totalRows = (long) modelMap.get("total_rows");

        int totalPage = (int) (totalRows / onePageRows);
        if(totalRows % onePageRows != 0) totalPage += 1;

        mav.addObject("board_srl", documentCategoryEntity.getBoard_srl());
        mav.addObject("currentPage", page);
        mav.addObject("totalPage", totalPage);
        mav.addObject("board", mav2.getModel().get("board"));

        return mav;
    }

    /**
     * 공개된 카테고리의 게시물 리스트를 구한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param documentListBean 리스팅을 할 게시물의 조건
     * @param bindingResult BindingResult object
     * @return model and view object
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

        return this.listDocument(request, documentListBean, tid);
    }

    /**
     * 공개된 게시판, 카테고리 리스트를 구한다.
     *
     * @param appSrl 게시판, 카테고리 리스트를 구할 앱 시리얼 넘버
     * @param tid transaction id
     * @return model and view object
     */
    @RequestMapping(value = "/app/{app_srl}/board/list/t/{tid}", method = RequestMethod.GET)
    public ModelAndView listBoard(@PathVariable("app_srl") int appSrl,
                                  @PathVariable("tid") String tid) {
        AppEntity appEntity = appService.getAppInfo(appSrl);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H1)");
            LOG.error(reason.get("invalid") + " not found app. app_srl [" + appSrl + "]");
            throw new CustomException("list_document_board_error", reason);
        }

        if(appEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H2)");
            LOG.error(reason.get("invalid") + " disabled app. app_srl [" + appSrl +
                    "], enabled [" + appEntity.getEnabled() + "]");
            throw new CustomException("list_document_board_error", reason);
        }

        List<Long> secrets = new ArrayList<>();
        secrets.add((long) MDV.NONE);
        secrets.add((long) MDV.SECRET_DOCUMENT_OF_PUBLIC_CATEGORY);
        List<Map<String, Object>> boardList = documentService.getBoardCategoryInApp(appEntity.getApp_srl(),
                MDV.NUSE, MDV.PUBLIC, MDV.YES, secrets, MDV.NO);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("board", boardList);

        return mav;
    }
}

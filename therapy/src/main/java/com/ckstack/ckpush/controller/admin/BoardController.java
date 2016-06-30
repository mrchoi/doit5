package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.board.*;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.board.DocumentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by dhkim94 on 15. 7. 21..
 */
@Controller
@RequestMapping("/admin/board")
public class BoardController {
    private final static Logger LOG = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confDym;


    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String manageBoardForm(Model model) {

        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_public", MDV.PUBLIC);

        model.addAttribute("normal_category_type", MDV.NORMAL_CATEGORY);
        model.addAttribute("link_category_type", MDV.LINK_CATEGORY);

        return "f_admin/board/manage_board";
    }

    /**
     * 링크 게시물을 생성 한다.
     *
     * @param tid transaction id
     * @param documentLinkEntity 생성할 링크 게시물 정보. app_srl은 빠져 있음
     * @param bindingResult BindingResult object
     * @return model and view
     */
    @RequestMapping(value = "/document/link/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addLinkDocument(@PathVariable("tid") String tid,
                                        @Valid @RequestBody DocumentLinkEntity documentLinkEntity,
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

        DocumentEntity documentEntity = documentService.getDocument(documentLinkEntity.getDocument_srl());
        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "not found document. document_srl [" + documentLinkEntity.getDocument_srl() + "]");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("add_link_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        documentLinkEntity.setApp_srl(documentEntity.getApp_srl());
        documentService.addDocumentLink(documentLinkEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("document_link_srl", documentLinkEntity.getDocument_link_srl());
        mav.addObject("document_srl", documentEntity.getDocument_srl());
        mav.addObject("document_title", documentEntity.getDocument_title());

        return mav;
    }

    /**
     * 게시판, 카테고리, 게시물 카운트를 포함하는 리스트를 구한다.
     * 일반 게시물에서 링크 게시물을 생성 하기 위해서 타겟 되는 게시판, 카테고리 리스트를 구하기 위해서 주로 사용 된다.
     *
     * @param categoryType 리스트를 구할 카테고리 타입. 링크냐 일반이냐, 아니면 모든 것이냐 구분
     * @param tid tansaction id
     * @param keyBean 게시물 시리얼 넘버를 포함하고 있는 object. l_key에 포함되어야 한다.
     * @return model and object
     */
    @RequestMapping(value = "/category/list/ctype/{category_type}/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listBoardCategoryInApp(@PathVariable("category_type") int categoryType,
                                               @PathVariable("tid") String tid,
                                               @RequestBody KeyBean keyBean) {
        if(keyBean.getL_keys() == null || keyBean.getL_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("l_keys", "failed list category. l_keys is null or empty");
            LOG.error(reason.get("l_keys"));
            throw new CustomException("list_document_category_error", reason);
        }

        long documentSrl = keyBean.getL_keys().get(0);
        DocumentEntity documentEntity = documentService.getDocument(documentSrl);
        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "failed no document_srl for list category. document_srl [" +
                    documentSrl + "]");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("list_document_category_error", reason);
        }

        DocumentCategoryEntity documentCategoryEntity = documentService.getDocumentCategory(
                documentEntity.getCategory_srl());

        List<Map<String, Object>> boardList = documentService.getBoardCategoryInApp(documentEntity.getApp_srl(),
                categoryType, documentCategoryEntity.getOpen_type(), MDV.NUSE, null, MDV.NUSE);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("board_list", boardList);

        return mav;
    }


    /**
     * 게시물 등록하는 폼을 보여 준다.
     *
     * @param categorySrl 게시물이 속할 카테고리 시리얼 넘버
     * @param model model object
     * @return view name
     */
    @RequestMapping(value = "/category/{category_srl}/document/add", method = RequestMethod.GET)
    public String addDocumentForm(@PathVariable("category_srl") long categorySrl,
                                  Model model) {
        DocumentCategoryEntity documentCategoryEntity = documentService.getDocumentCategory(categorySrl);
        if(documentCategoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_srl", "not found category. category_srl [" + categorySrl + "]");
            LOG.error(reason.get("category_srl"));
            throw new CustomException("read_document_category_error", reason);
        }

        DocumentBoardEntity documentBoardEntity = documentService.getDocumentBoard(documentCategoryEntity.getBoard_srl());
        AppEntity appEntity = appService.getAppInfo(documentCategoryEntity.getApp_srl());

        model.addAttribute("documentCategoryEntity", documentCategoryEntity);
        model.addAttribute("documentBoardEntity", documentBoardEntity);
        model.addAttribute("appEntity", appEntity);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("mdv_none", MDV.NONE);

        // 게시물을 비밀글로 적용 할때 비밀글 적용값
        long secretValue = MDV.SECRET_DOCUMENT_OF_PUBLIC_CATEGORY;
        if(documentCategoryEntity.getOpen_type() == MDV.PRIVATE) {
            CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            secretValue = userDetails.getMember_srl();
        }
        model.addAttribute("secret_value", secretValue);

        // 앱에 할당되어 있는 템플릿 리스트를 구한다.
        List<TemplateEntity> templateEntities = documentService.getTemplateOfApp(documentCategoryEntity.getApp_srl());
        model.addAttribute("templateEntities", templateEntities);

        return "f_admin/board/add_document";
    }

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
     * 게시물 뷰/수정 폼을 보여 준다.
     *
     * @param documentSrl 뷰/수정 폼을 보여 줄 게시물 시리얼 넘버
     * @param model model object
     * @return view name
     */
    @RequestMapping(value = "/document/{document_srl}/link/{link_flag}", method = RequestMethod.GET)
    public String detailDocument(@PathVariable("document_srl") long documentSrl,
                                 @PathVariable("link_flag") int linkFlag,
                                 Model model) {
        DocumentEntity documentEntity = documentService.getDocument(documentSrl);
        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "not found document. document_srl [" +
                    documentSrl + "]");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("read_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity appEntity = appService.getAppInfo(documentEntity.getApp_srl());
        DocumentBoardEntity documentBoardEntity = documentService.getDocumentBoard(documentEntity.getBoard_srl());
        DocumentCategoryEntity documentCategoryEntity = documentService.getDocumentCategory(documentEntity.getCategory_srl());

        // 게시물이 이동 할 수 있는 카테고리 리스트를 구한다.
        // 동일 게시판의 카테고리로 이동 가능 하다.
        Map<String, String> sort = new HashMap<>();
        sort.put("category_name", "ASC");
        List<DocumentCategoryEntity> categoryEntities = documentService.getDocumentCategory(MDV.NUSE,
                documentCategoryEntity.getBoard_srl(), null, documentCategoryEntity.getCategory_type(),
                MDV.NUSE, MDV.NUSE, sort, MDV.NUSE, MDV.NUSE);
        model.addAttribute("categoryEntities", categoryEntities);

        // javascript 에서 사용 가능 하도록 urlencoder 한다. javascript에서 +를 decoding 하기가 귀찮으므로
        // + 는 %20 으로 강제로 바꾼다.
        // 아래 encoding 할 필요 없다. freemarker 의 js_string 덕택에 javascript 에서 js_string 으로 처리 하면 된다.
        // 아래는 참고용으로 주석 처리로 그대로 둔다.
        //try {
        //    String enc = URLEncoder.encode(documentEntity.getDocument_title(), "utf-8");
        //    documentEntity.setDocument_title(StringUtils.replace(enc, "+", "%20"));
        //} catch (UnsupportedEncodingException e) {
        //    Map<String, String> reason = new HashMap<>();
        //    reason.put("document_title", "can't encode document_title.");
        //    LOG.error(reason.get("document_title") +" exception [" + e.getMessage());
        //    throw new CustomException("read_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        //}

        model.addAttribute("documentEntity", documentEntity);

        model.addAttribute("appEntity", appEntity);
        model.addAttribute("documentBoardEntity", documentBoardEntity);
        model.addAttribute("documentCategoryEntity", documentCategoryEntity);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("image_server_host", confDym.getProperty("image_server_host"));
        model.addAttribute("document_attach_uri", confCommon.getProperty("document_attach_uri"));
        model.addAttribute("link_flag", linkFlag);

        // 게시물을 비밀글로 적용 할때 비밀글 적용값
        long secretValue = MDV.SECRET_DOCUMENT_OF_PUBLIC_CATEGORY;
        if(documentCategoryEntity.getOpen_type() == MDV.PRIVATE) {
            CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            secretValue = userDetails.getMember_srl();
        }
        model.addAttribute("secret_value", secretValue);

        // 앱에 할당되어 있는 템플릿 리스트를 구한다.
        List<TemplateEntity> templateEntities = documentService.getTemplateOfApp(documentCategoryEntity.getApp_srl());
        model.addAttribute("templateEntities", templateEntities);

        return "f_admin/board/detail_document";
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

        documentService.modifyDocument(documentEntity, documentSrl);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("document_srl", documentSrl);

        return mav;
    }

    /**
     * 게시물/링크 게시물 리스트를 보여 준다.
     *
     * @param request HttpServletRequest object
     * @param categorySrl 게시물/링크 게시물 리스트를 구할 카테고리의 시리얼 넘버
     * @param tid transaction id
     * @return 게시물 리스트
     */
    @RequestMapping(value = "/category/{category_srl}/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listDocument(HttpServletRequest request,
                                     @PathVariable("category_srl") int categorySrl,
                                     @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_document_error", reason);
        }

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("category_srl", Integer.toString(categorySrl));

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows, filterRows;

        DocumentCategoryEntity documentCategoryEntity = documentService.getDocumentCategory(categorySrl);

        if(documentCategoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_category", "not found category. category_srl [" + categorySrl + "]");
            LOG.error(reason.get("no_category"));
            throw new CustomException("list_document_error", reason);
        }

        if(documentCategoryEntity.getCategory_type() == MDV.NORMAL_CATEGORY) {
            totalRows = documentService.countDocument(MDV.NUSE, MDV.NUSE, categorySrl, null, MDV.NUSE,
                    null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
            filterRows = documentService.countDocument(searchFilter, null);

            List<DocumentEntity> documentEntities = documentService.getDocument(searchFilter, null, sortValue, offset, limit);
            for (DocumentEntity documentEntity : documentEntities) {
                Map<String, Object> row = new HashMap<>();

                // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
                row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + documentEntity.getDocument_srl());
                row.put("document_srl", documentEntity.getDocument_srl());
                row.put("app_srl", documentEntity.getApp_srl());
                row.put("board_srl", documentEntity.getBoard_srl());
                row.put("category_srl", documentEntity.getCategory_srl());
                row.put("document_title", documentEntity.getDocument_title());
                //row.put("document_content", documentEntity.getDocument_content());
                row.put("read_count", documentEntity.getRead_count());
                row.put("like_count", documentEntity.getLike_count());
                row.put("blame_count", documentEntity.getBlame_count());
                row.put("comment_count", documentEntity.getComment_count());
                row.put("file_count", documentEntity.getFile_count());
                row.put("outer_link", documentEntity.getOuter_link());
                row.put("secret", documentEntity.getSecret());
                row.put("block", documentEntity.getBlock());
                row.put("allow_comment", documentEntity.getAllow_comment());
                row.put("allow_notice", documentEntity.getAllow_notice());
                row.put("list_order", documentEntity.getList_order());
                row.put("member_srl", documentEntity.getMember_srl());
                row.put("user_id", documentEntity.getUser_id());
                row.put("user_name", documentEntity.getUser_name());
                row.put("nick_name", documentEntity.getNick_name());
                table.add(row);
            }
        } else {
            totalRows = documentService.countDocumentLink(MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null, categorySrl,
                    null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
            filterRows = documentService.countDocumentLink(searchFilter, null);

            List<DocumentLinkEntity> documentLinkEntities = documentService.getDocumentLink(searchFilter, null,
                    sortValue, offset, limit);
            for(DocumentLinkEntity documentLinkEntity : documentLinkEntities) {
                Map<String, Object> row = new HashMap<>();

                // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
                row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + documentLinkEntity.getDocument_link_srl());
                row.put("document_srl", documentLinkEntity.getDocument_link_srl());
                row.put("app_srl", documentLinkEntity.getApp_srl());
                row.put("board_srl", documentLinkEntity.getBoard_srl());
                row.put("category_srl", documentLinkEntity.getCategory_srl());
                row.put("document_title", documentLinkEntity.getDocumentEntity().getDocument_title());
                //row.put("document_content", documentLinkEntity.getDocumentEntity().getDocument_content());
                row.put("read_count", documentLinkEntity.getDocumentEntity().getRead_count());
                row.put("like_count", documentLinkEntity.getDocumentEntity().getLike_count());
                row.put("blame_count", documentLinkEntity.getDocumentEntity().getBlame_count());
                row.put("comment_count", documentLinkEntity.getDocumentEntity().getComment_count());
                row.put("file_count", documentLinkEntity.getDocumentEntity().getFile_count());
                row.put("outer_link", documentLinkEntity.getDocumentEntity().getOuter_link());
                row.put("secret", documentLinkEntity.getDocumentEntity().getSecret());
                row.put("block", documentLinkEntity.getDocumentEntity().getBlock());
                row.put("allow_comment", documentLinkEntity.getDocumentEntity().getAllow_comment());
                row.put("allow_notice", documentLinkEntity.getDocumentEntity().getAllow_notice());
                row.put("list_order", documentLinkEntity.getDocumentEntity().getList_order());
                row.put("member_srl", documentLinkEntity.getDocumentEntity().getMember_srl());
                row.put("user_id", documentLinkEntity.getDocumentEntity().getUser_id());
                row.put("user_name", documentLinkEntity.getDocumentEntity().getUser_name());
                row.put("nick_name", documentLinkEntity.getDocumentEntity().getNick_name());
                row.put("orig_document_srl", documentLinkEntity.getDocument_srl());
                table.add(row);
            }
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject(confCommon.getProperty("dtresp_data"), table);
        mav.addObject(confCommon.getProperty("dtresp_draw"),
                Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_draw")), 10));
        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject(confCommon.getProperty("dtresp_filter_row"), filterRows);

        return mav;
    }

    /**
     * 카테고리 추가하는 폼을 보여 준다.
     *
     * @param boardSrl 게시판 시리얼 넘버
     * @param model model object
     * @return view name
     */
    @RequestMapping(value = "/{board_srl}/category/add", method = RequestMethod.GET)
    public String addCategoryForm(@PathVariable("board_srl") long boardSrl,
                                  Model model) {
        DocumentBoardEntity documentBoardEntity = documentService.getDocumentBoard(boardSrl);
        if(documentBoardEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_srl", "not found board. board_srl [" + boardSrl + "]");
            LOG.error(reason.get("board_srl"));
            throw new CustomException("read_document_board_error", reason);
        }

        AppEntity appEntity = appService.getAppInfo(documentBoardEntity.getApp_srl());
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "not found app. app_srl [" + documentBoardEntity.getApp_srl() + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("read_app_error", reason);
        }

        model.addAttribute("documentBoardEntity", documentBoardEntity);
        model.addAttribute("appEntity", appEntity);
        model.addAttribute("link_category_type", MDV.LINK_CATEGORY);
        model.addAttribute("normal_category_type", MDV.NORMAL_CATEGORY);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("mdv_private", MDV.PRIVATE);

        return "f_admin/board/add_category";
    }

    /**
     * 카테고리를 추가 한다.
     *
     * @param tid transaction id
     * @param documentCategoryEntity 추가할 카테고리 정보
     * @param bindingResult BindingResult object
     * @return model and view object
     */
    @RequestMapping(value = "/category/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addBoardSubmit(@PathVariable("tid") String tid,
                                       @Valid @RequestBody DocumentCategoryEntity documentCategoryEntity,
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
            throw new CustomException("add_document_category_error", reason);
        }

        documentService.addDocumentCategory(documentCategoryEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("category_srl", documentCategoryEntity.getCategory_srl());

        return mav;
    }

    /**
     * 카테고리를 삭제 한다.
     *
     * @param tid transaction id
     * @param keyBean 삭제할 카테고리 시리얼 넘버가 들어 있는 리스트
     * @return model and view object
     */
    @RequestMapping(value = "/category/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteCategory(@PathVariable("tid") String tid,
                                       @RequestBody KeyBean keyBean) {
        if(keyBean.getL_keys() == null || keyBean.getL_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("l_keys", "failed delete document category. l_keys is null or empty");
            LOG.error(reason.get("l_keys"));
            throw new CustomException("delete_document_category_error", reason);
        }

        documentService.deleteDocumentCategory(keyBean.getL_keys());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("category_srls", keyBean.getL_keys());

        return mav;
    }

    /**
     * 카테고리 뷰/수정 폼을 보여 준다.
     *
     * @param categorySrl 카테고리 시리얼 넘버
     * @param model model object
     * @return view name
     */
    @RequestMapping(value = "/category/{category_srl}", method = RequestMethod.GET)
    public String detailCategory(@PathVariable("category_srl") long categorySrl,
                                 Model model) {
        DocumentCategoryEntity documentCategoryEntity = documentService.getDocumentCategory(categorySrl);
        if(documentCategoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_srl", "not found document category. category_srl [" +
                    categorySrl + "]");
            LOG.error(reason.get("category_srl"));
            throw new CustomException("read_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DocumentBoardEntity documentBoardEntity = documentService.getDocumentBoard(documentCategoryEntity.getBoard_srl());
        AppEntity appEntity = appService.getAppInfo(documentCategoryEntity.getApp_srl());

        model.addAttribute("appEntity", appEntity);
        model.addAttribute("documentBoardEntity", documentBoardEntity);
        model.addAttribute("documentCategoryEntity", documentCategoryEntity);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("mdv_private", MDV.PRIVATE);
        model.addAttribute("normal_category_type", MDV.NORMAL_CATEGORY);
        model.addAttribute("link_category_type", MDV.LINK_CATEGORY);

        return "f_admin/board/detail_category";
    }

    /**
     * 카테고리 정보를 수정 한다.
     *
     * @param categorySrl 수정 할 카테고리 시리얼 넘버
     * @param tid transaction id
     * @param keyBean 수정 할 카테고리 정보가 들어 있는 bean
     * @return model and view object
     */
    @RequestMapping(value = "/category/{category_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyCategory(@PathVariable("category_srl") long categorySrl,
                                       @PathVariable("tid") String tid,
                                       @RequestBody KeyBean keyBean) {
        if(keyBean.getM_key() == null || keyBean.getM_key().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("m_key", "failed modify document category. m_key is null or empty");
            LOG.error(reason.get("m_key"));
            throw new CustomException("modify_document_category_error", reason);
        }

        Map<String, String> categoryData = new HashMap<>();
        Set<String> keys = keyBean.getM_key().keySet();
        for(String key : keys) categoryData.put(key, keyBean.getM_key().get(key).toString());

        documentService.modifyDocumentCategory(categorySrl, categoryData);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

    /**
     * 카테고리 리스트를 구한다.
     *
     * @param request HttpServletRequest object
     * @param boardSrl 카테고리 리스트를 구할 게시판 시리얼 넘버
     * @param tid transaction id
     * @return model and view object
     */
    @RequestMapping(value = "/{board_srl}/category/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listCategory(HttpServletRequest request,
                                  @PathVariable("board_srl") int boardSrl,
                                  @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_document_category_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("board_srl", Integer.toString(boardSrl));

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = documentService.countDocumentCategory(MDV.NUSE, boardSrl, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        long filterRows = documentService.countDocumentCategory(searchFilter);

        List<DocumentCategoryEntity> documentCategoryEntities = documentService.getDocumentCategory(searchFilter,
                sortValue, offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(DocumentCategoryEntity documentCategoryEntity : documentCategoryEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-category-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-category-row-"+documentCategoryEntity.getCategory_srl());
            row.put("category_srl", documentCategoryEntity.getCategory_srl());
            row.put("board_srl", documentCategoryEntity.getBoard_srl());
            row.put("app_srl", documentCategoryEntity.getApp_srl());
            row.put("category_name", documentCategoryEntity.getCategory_name());
            row.put("category_description", documentCategoryEntity.getCategory_description());
            row.put("category_type", documentCategoryEntity.getCategory_type());
            row.put("enabled", documentCategoryEntity.getEnabled());
            row.put("open_type", documentCategoryEntity.getOpen_type());
            table.add(row);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject(confCommon.getProperty("dtresp_data"), table);
        mav.addObject(confCommon.getProperty("dtresp_draw"),
                Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_draw")), 10));
        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject(confCommon.getProperty("dtresp_filter_row"), filterRows);

        return mav;
    }

    /**
     * 게시판을 삭제 한다.
     *
     * @param tid transaction id
     * @param keyBean 삭제할 게시판 시리얼 넘버 리스트. l_keys 에 포함되어 있다.
     * @return model and view object
     */
    @RequestMapping(value = "/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteBoard(@PathVariable("tid") String tid,
                                    @RequestBody KeyBean keyBean) {
        if(keyBean.getL_keys() == null || keyBean.getL_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("l_keys", "failed delete document board. l_keys is null or empty");
            LOG.error(reason.get("l_keys"));
            throw new CustomException("delete_document_board_error", reason);
        }

        documentService.deleteDocumentBoard(keyBean.getL_keys());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("board_srls", keyBean.getL_keys());

        return mav;
    }

    /**
     * 앱별 게시판 리스트를 구한다.
     *
     * @param request HttpServletRequest object
     * @param appSrl 게시판 리스트를 구할 앱 시리얼 넘버
     * @param tid transaction id
     * @return model and view object
     */
    @RequestMapping(value = "/list/app/{app_srl}/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listBoard(HttpServletRequest request,
                                  @PathVariable("app_srl") int appSrl,
                                  @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_document_board_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("app_srl", Integer.toString(appSrl));

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = documentService.countDocumentBoard(appSrl, null, MDV.NUSE, MDV.NUSE);
        long filterRows = documentService.countDocumentBoard(searchFilter);

        List<DocumentBoardEntity> documentBoardEntities = documentService.getDocumentBoard(searchFilter,
                sortValue, offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(DocumentBoardEntity documentBoardEntity : documentBoardEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-board-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-board-row-"+documentBoardEntity.getBoard_srl());
            row.put("board_srl", documentBoardEntity.getBoard_srl());
            row.put("app_srl", documentBoardEntity.getApp_srl());
            row.put("board_name", documentBoardEntity.getBoard_name());
            row.put("board_description", documentBoardEntity.getBoard_description());
            row.put("enabled", documentBoardEntity.getEnabled());
            row.put("open_type", documentBoardEntity.getOpen_type());
            table.add(row);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject(confCommon.getProperty("dtresp_data"), table);
        mav.addObject(confCommon.getProperty("dtresp_draw"),
                Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_draw")), 10));
        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject(confCommon.getProperty("dtresp_filter_row"), filterRows);

        return mav;
    }

    /**
     * 게시판 추가하는 폼을 보여 준다.
     *
     * @param appSrl 게시판을 소유 할 앱 시리얼 넘버
     * @param model model object
     * @return 뷰 이름
     */
    @RequestMapping(value = "/add/app/{app_srl}", method = RequestMethod.GET)
    public String addBoardForm(@PathVariable("app_srl") int appSrl,
                               Model model) {
        AppEntity appEntity = appService.getAppInfo(appSrl);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "not found app. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("read_app_error", reason);
        }

        model.addAttribute("appEntity", appEntity);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("mdv_private", MDV.PRIVATE);

        return "f_admin/board/add_board";
    }

    /**
     * 게시판을 추가한다.
     *
     * @param tid transaction id
     * @param documentBoardEntity 추가할 게시판 정보
     * @param bindingResult BindingResult object
     * @return model and view object
     */
    @RequestMapping(value = "/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addBoardSubmit(@PathVariable("tid") String tid,
                                       @Valid @RequestBody DocumentBoardEntity documentBoardEntity,
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
            throw new CustomException("add_document_board_error", reason);
        }

        documentService.addDocumentBoard(documentBoardEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("board_srl", documentBoardEntity.getBoard_srl());

        return mav;
    }

    /**
     * 게시판 뷰/수정 폼을 보여 준다.
     *
     * @param boardSrl 뷰/수정 할 게시판 시리얼 넘버
     * @param model model object
     * @return view name
     */
    @RequestMapping(value = "/{board_srl}", method = RequestMethod.GET)
    public String detailBoard(@PathVariable("board_srl") long boardSrl,
                               Model model) {
        DocumentBoardEntity documentBoardEntity = documentService.getDocumentBoard(boardSrl);
        if(documentBoardEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_srl", "not found document board. board_srl [" +
                    boardSrl + "]");
            LOG.error(reason.get("board_srl"));
            throw new CustomException("read_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity appEntity = appService.getAppInfo(documentBoardEntity.getApp_srl());

        model.addAttribute("appEntity", appEntity);
        model.addAttribute("documentBoardEntity", documentBoardEntity);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("mdv_private", MDV.PRIVATE);

        return "f_admin/board/detail_board";
    }

    /**
     * 게시판 정보를 수정 한다.
     *
     * @param boardSrl 수정할 게시판 시리얼 넘버
     * @param tid transaction id
     * @param keyBean 수정할 게시판 정보
     * @return model and view object
     */
    @RequestMapping(value = "/{board_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyBoard(@PathVariable("board_srl") long boardSrl,
                                    @PathVariable("tid") String tid,
                                    @RequestBody KeyBean keyBean) {
        if(keyBean.getM_key() == null || keyBean.getM_key().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("m_key", "failed modify document board. m_key is null or empty");
            LOG.error(reason.get("m_key"));
            throw new CustomException("modify_document_board_error", reason);
        }

        Map<String, String> boardData = new HashMap<>();
        Set<String> keys = keyBean.getM_key().keySet();
        for(String key : keys) boardData.put(key, keyBean.getM_key().get(key).toString());

        documentService.modifyDocumentBoard(boardSrl, boardData);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

    /**
     * 게시물에서 사용할 템플릿을 등록할 폼을 보여 준다.
     *
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/template/add", method = RequestMethod.GET)
    public String addTemplateForm(Model model) {
        model.addAttribute("mdv_yes", MDV.YES);
        return "f_admin/board/add_template";
    }

    /**
     * 게시물에서 사용할 템플릿을 등록 한다.
     *
     * @param tid transaction id
     * @param templateEntity 추가할 템플릿 정보
     * @param bindingResult BindingResult object
     * @return model and view object
     */
    @RequestMapping(value = "/template/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addTemplateSubmit(@PathVariable("tid") String tid,
                                          @Valid @RequestBody TemplateEntity templateEntity,
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
            throw new CustomException("add_document_template_error", reason);
        }

        if(templateEntity.getApp_srls() == null || templateEntity.getApp_srls().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srls", messageSource.getMessage("valid.app_srls", null,
                    LocaleContextHolder.getLocale()));
            throw new CustomException("add_document_template_error", reason);
        }

        documentService.addDocumentTemplate(templateEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("template_srl", templateEntity.getTemplate_srl());

        return mav;
    }

    /**
     * 게시물 템플릿 리스팅을 위한 폼을 보여 준다.
     *
     * @return view name
     */
    @RequestMapping(value = "/template/list", method = RequestMethod.GET)
    public String listTemplateForm() {
        return "f_admin/board/list_template";
    }

    /**
     * select2 용도의 게시판 리스트를 구한다.
     *
     * @param appSrl 게시판이 속해 있는 앱 시리얼 넘버
     * @param tid transaction id
     * @param query 검색할 게시판 이름
     * @param offset list offset
     * @param limit list limit
     * @return 게시판 리스트
     */
    @RequestMapping(value = "/list/app/{app_srl}/select2/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listBoardForSelect2(@PathVariable("app_srl") int appSrl,
                                          @PathVariable("tid") String tid,
                                          @RequestParam("query") String query,
                                          @RequestParam("offset") int offset,
                                          @RequestParam("limit") int limit) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        List<Map<String, Object>> list = new ArrayList<>();

        // 검색값이 empty string 이면 무시함
        query = StringUtils.trim(query);
        if(StringUtils.equals(query, "")) {
            mav.addObject(confCommon.getProperty("dtresp_total_row"), 0);
            mav.addObject("list", list);
            return mav;
        }

        Map<String, String> searchFilter = new HashMap<>();
        searchFilter.put("board_name", query);
        searchFilter.put("app_srl", Integer.toString(appSrl));

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("board_name", "asc");

        long totalRows = documentService.countDocumentBoard(appSrl, query, MDV.NUSE, MDV.NUSE);
        List<DocumentBoardEntity> documentBoardEntities = documentService.getDocumentBoard(searchFilter,
                sortValue, offset, limit);

        for(DocumentBoardEntity documentBoardEntity : documentBoardEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("app_srl", documentBoardEntity.getApp_srl());
            map.put("board_srl", documentBoardEntity.getBoard_srl());
            map.put("board_name", documentBoardEntity.getBoard_name());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject("list", list);

        return mav;
    }

    /**
     * 게시물 템플릿 리스트를 보여 준다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return model and view object
     */
    @RequestMapping(value = "/template/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listTemplate(HttpServletRequest request,
                                     @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            LOG.error(reason.get("parameter_parsing"));
            throw new CustomException("list_document_template_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = documentService.countDocumentTemplate(MDV.NUSE, null);
        long filterRows = documentService.countDocumentTemplate(searchFilter);

        List<TemplateEntity> templateEntities = documentService.getDocumentTemplate(searchFilter, sortValue,
                offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(TemplateEntity templateEntity : templateEntities) {
            Map<String, Object> row = new HashMap<>();

            row.put(confCommon.getProperty("dtresp_row_id"), "dt-row-"+templateEntity.getTemplate_srl());
            row.put("template_srl", templateEntity.getTemplate_srl());
            row.put("template_title", templateEntity.getTemplate_title());
            row.put("template_description", templateEntity.getTemplate_description());
            row.put("app_count", templateEntity.getApp_count());
            table.add(row);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject(confCommon.getProperty("dtresp_data"), table);
        mav.addObject(confCommon.getProperty("dtresp_draw"),
                Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_draw")), 10));
        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject(confCommon.getProperty("dtresp_filter_row"), filterRows);

        return mav;
    }

    /**
     * 게시물 템플릿을 삭제 한다.
     *
     * @param tid transaction id
     * @param keyBean 삭제할 템플릿 정보. keyBean.i_keys 에 삭제할 템플릿 시리얼 넘버가 들어 있음.
     * @return model and view object
     */
    @RequestMapping(value = "/template/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteTemplate(@PathVariable("tid") String tid,
                                       @RequestBody KeyBean keyBean) {
        if(keyBean.getI_keys() == null || keyBean.getI_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("i_keys", "failed delete document template. i_keys is null or empty");
            LOG.error(reason.get("i_keys"));
            throw new CustomException("delete_document_template_error", reason);
        }

        documentService.deleteDocumentTemplate(keyBean.getI_keys());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("template_srls", keyBean.getI_keys());

        return mav;
    }

    /**
     * 템플릿 수정 폼을 보여 준다.
     *
     * @param templateSrl 템플릿 시리얼 넘버
     * @param model model object
     * @return 뷰 네임
     */
    @RequestMapping(value = "/template/{template_srl}", method = RequestMethod.GET)
    public String detailTemplate(@PathVariable("template_srl") int templateSrl,
                                 Model model) {
        TemplateEntity templateEntity = documentService.getDocumentTemplate(templateSrl);
        if(templateEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template_srl", "failed detail document template. not found template of template_srl. template_srl [" +
                    templateSrl + "]");
            LOG.error(reason.get("template_srl"));
            throw new CustomException("detail_document_template_error", reason);
        }
        model.addAttribute("templateEntity", templateEntity);

        List<AppEntity> appEntities = documentService.getAppInfoUsingTemplate(templateSrl);
        model.addAttribute("appEntities", appEntities);
        model.addAttribute("mdv_yes", MDV.YES);

        return "f_admin/board/detail_template";
    }

    /**
     * 템플릿을 수정 한다.
     *
     * @param templateSrl 템플릿 시리얼 넘버
     * @param tid transaction id
     * @param templateEntity 수정할 템플릿 정보
     * @param bindingResult bindingResult object
     * @return model and view object
     */
    @RequestMapping(value = "/template/{template_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyTemplate(@PathVariable("template_srl") int templateSrl,
                                       @PathVariable("tid") String tid,
                                       @Valid @RequestBody TemplateEntity templateEntity,
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
            throw new CustomException("modify_document_template_error", reason);
        }

        if(templateEntity.getApp_srls() == null || templateEntity.getApp_srls().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srls", messageSource.getMessage("valid.app_srls", null,
                    LocaleContextHolder.getLocale()));
            throw new CustomException("modify_document_template_error", reason);
        }

        documentService.modifyDocumentTemplate(templateEntity, templateSrl);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("template_srl", templateSrl);

        return mav;
    }


    /**
     * select2 에서 사용할 태그 리스트를 구한다.
     *
     * @param appSrl 태그가 포함된 앱의 시리얼 넘버
     * @param adminTag 관리자용 태그 여부. 1: 관리자태그, 2:일반 태그
     * @param tid transaction id
     * @param query 태그 이름
     * @param offset list offset
     * @param limit list limit
     * @return 태그 리스트
     */
    @RequestMapping(value = "/list/app/{app_srl}/tag/{admin_tag}/select2/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listTagForSelect2(@PathVariable("app_srl") int appSrl,
                                          @PathVariable("admin_tag") int adminTag,
                                          @PathVariable("tid") String tid,
                                          @RequestParam("query") String query,
                                          @RequestParam("offset") int offset,
                                          @RequestParam("limit") int limit) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        List<Map<String, Object>> list = new ArrayList<>();

        // 검색값이 empty string 이면 무시함
        query = StringUtils.trim(query);
        if(StringUtils.equals(query, "")) {
            mav.addObject(confCommon.getProperty("dtresp_total_row"), 0);
            mav.addObject("list", list);
            return mav;
        }

        Map<String, String> searchFilter = new HashMap<>();
        searchFilter.put("app_srl", Integer.toString(appSrl));
        searchFilter.put("admin_tag", Integer.toString(adminTag));
        searchFilter.put("tag_name", query);

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("tag_name", "asc");

        long totalRows = documentService.countDocumentTag(appSrl, MDV.NUSE, null, adminTag);
        List<TagEntity> tagEntities = documentService.getDocumentTag(searchFilter, sortValue, offset, limit);

        for(TagEntity tagEntity : tagEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("tag_srl", tagEntity.getTag_srl());
            map.put("tag_name", tagEntity.getTag_name());
            map.put("admin_tag", tagEntity.getAdmin_tag());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject("list", list);

        return mav;
    }

}

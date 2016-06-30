package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.board.*;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.board.CommentService;
import com.ckstack.ckpush.service.board.DocumentService;
import com.ckstack.ckpush.service.board.SupervisorService;
import com.ckstack.ckpush.service.user.MemberService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by kas0610 on 16. 1. 15.
 */
@Controller
@RequestMapping("/admin/one")
public class AdminOneController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminOneController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confAdmin;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listForm(Model model) {

        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_public", MDV.PUBLIC);

        model.addAttribute("normal_category_type", MDV.NORMAL_CATEGORY);
        model.addAttribute("link_category_type", MDV.LINK_CATEGORY);

        return "f_admin/plymind/one/one_list";
    }

    /**
     * 게시물 리스트를 보여 준다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return 게시물 리스트
     */
    @RequestMapping(value = "/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listDocument(HttpServletRequest request,
                                     @PathVariable("tid") String tid) {

        String strCategorySrl = confAdmin.getProperty("plymind.one.category.srl");
        int categorySrl = Integer.valueOf(strCategorySrl);

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
        searchFilter.put("category_srl", strCategorySrl);

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows, filterRows;

        DocumentCategoryEntity documentCategoryEntity = documentService.getDocumentCategory(categorySrl);

        if(documentCategoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_category", "not found category. category_srl [" + categorySrl + "]");
            LOG.error(reason.get("no_category"));
            throw new CustomException("list_document_error", reason);
        }

        totalRows = documentService.countDocument(MDV.NUSE, MDV.NUSE, categorySrl, null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
        filterRows = documentService.countDocument(searchFilter, null);

        Map<String, String> advisor = memberService.getAdvisorAuthority();
        long login_advisor_srl = MDV.NUSE;
        if(advisor.get("advisor").equals("true")) {
            login_advisor_srl = Long.valueOf(advisor.get("member_srl"));
        }

        List<DocumentEntity> documentEntities = documentService.getDocument(searchFilter, null, sortValue, offset, limit);
        for (DocumentEntity documentEntity : documentEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + documentEntity.getDocument_srl());
            row.put("document_srl", documentEntity.getDocument_srl());
            row.put("document_title", documentEntity.getDocument_title());
            //row.put("list_order", documentEntity.getList_order());
            //row.put("read_count", documentEntity.getRead_count());
            row.put("user_name", documentEntity.getUser_name());
            row.put("user_id", documentEntity.getUser_id());
            row.put("c_date", documentEntity.getC_date());
            row.put("u_date", documentEntity.getU_date());

            Map<String, Object> extra = supervisorService.getJsonTemplateExtra(documentEntity);

            Map<String, Object> documentExtraInfo = (Map<String, Object>) extra.get("template");

            long advisor_srl = 0;
            for(String key : documentExtraInfo.keySet()) {

                String advisorSrl = documentExtraInfo.get("advisor_srl").toString();
                String st_advisor_srl = (advisorSrl.equals("") || advisorSrl == null ? "0" : advisorSrl);
                advisor_srl = Long.valueOf(st_advisor_srl);

                row.put("advisor_nick_name", "");
                row.put("advisor_srl", "");

                if(advisor_srl > 0){
                    MemberEntity memberEntity = memberService.getMemberInfo(advisor_srl);
                    row.put("advisor_nick_name", memberEntity.getNick_name());
                    row.put("advisor_srl", st_advisor_srl);
                }

            }

            //if(login_advisor_srl != advisor_srl) break;

            // 최신 댓글 하나만 받기 위해 offset 0, limit 1
            List<DocumentCommentEntity> documentCommentEntities = commentService.getDocumentComment(documentEntity.getApp_srl(), documentEntity.getBoard_srl(), documentEntity.getCategory_srl(), documentEntity.getDocument_srl(),
                    new HashMap<String, String>(), 0, 1);

            if(documentCommentEntities.size() > 0 ){
                for (DocumentCommentEntity documentCommentEntity : documentCommentEntities) {

                    // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
                    //row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + documentEntity.getDocument_srl());
                    row.put("comment_user_id", documentCommentEntity.getUser_id());
                    row.put("comment_date", documentCommentEntity.getC_date());
                }
            }
            else{
                row.put("comment_user_id", "");
                row.put("comment_date", "");
            }

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
     * 게시물 뷰/수정 폼을 보여 준다.
     *
     * @param documentSrl 뷰/수정 폼을 보여 줄 게시물 시리얼 넘버
     * @param model model object
     * @return view name
     */
    @RequestMapping(value = "/{document_srl}", method = RequestMethod.GET)
    public String detailDocument(@PathVariable("document_srl") long documentSrl,
                                 Model model) {
        DocumentEntity documentEntity = documentService.getDocument(documentSrl);
        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "not found document. document_srl [" +
                    documentSrl + "]");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("read_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }
        documentService.getReadCount(documentSrl);

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

        model.addAttribute("documentEntity", documentEntity);

        model.addAttribute("appEntity", appEntity);
        model.addAttribute("documentBoardEntity", documentBoardEntity);
        model.addAttribute("documentCategoryEntity", documentCategoryEntity);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("document_attach_uri", confCommon.getProperty("document_attach_uri"));
        //model.addAttribute("link_flag", linkFlag);

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

        //댓글 리스트
        long count = commentService.countDocumentComment(documentEntity.getApp_srl(), documentEntity.getBoard_srl(), documentEntity.getCategory_srl(), documentSrl);
        List<DocumentCommentEntity> documentCommentEntities = commentService.getDocumentComment(documentEntity.getApp_srl(), documentEntity.getBoard_srl(), documentEntity.getCategory_srl(), documentSrl,
                new HashMap<String, String>(), 0, (int) count);

        model.addAttribute("documentCommentEntities", documentCommentEntities);

        return "f_admin/plymind/one/one_detail";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addDocumentForm(Model model) {

        String strCategorySrl = confAdmin.getProperty("plymind.one.category.srl");
        int categorySrl = Integer.valueOf(strCategorySrl);


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

        return "f_admin/plymind/one/one_add";
    }


}
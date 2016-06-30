package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.domain.board.DocumentCategoryEntity;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.board.DocumentService;
import com.ckstack.ckpush.service.board.SupervisorService;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

/**
 * Created by kas0610 on 16. 1. 18..
 */
@Controller
@RequestMapping("/user")
public class IndexController {
    private final static Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confDym;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confAdmin;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    protected MemberService memberService;
/**
     * 로그인 완료 후 어드민 페이지에 접근 한다. 웹페이지의 layout 을 설정 한다.
     * @param model Model object
     * @return 어드민 layout 을 가지고 있는 view
     *//*

    @RequestMapping(method = RequestMethod.GET)
    public String welcom(Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userId", userDetails.getUser_id());

        // 사용자 프로필 이미지 URL을 넣어야 한다.
        String profileImageURL = null;
        if(userDetails.getProfile_thumb_url() == null) {
            if(userDetails.getProfile_url() != null) profileImageURL = userDetails.getProfile_url();
        } else {
            profileImageURL = userDetails.getProfile_thumb_url();
        }
        if(profileImageURL != null) model.addAttribute("userProfileImage", profileImageURL);

        String backOfficeType = "";
        if(confCommon.containsKey("backoffice_by_member_support")) {
            backOfficeType = confCommon.getProperty("backoffice_by_member_support");
        }
        model.addAttribute("backOfficeType", backOfficeType);

        //System.out.println("welcom page");

        return "f_service/dash/welcom";
    }
*/

    /**
     * 로그인 완료 후 어드민 페이지에 접근 한다. 웹페이지의 layout 을 설정 한다.
     * @param model Model object
     * @return 어드민 layout 을 가지고 있는 view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {

        //CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //model.addAttribute("userId", userDetails.getUser_id());

        System.out.println("index page");

        return "f_service/open/index";
    }

    @RequestMapping(value = "/open/login")
    public String plymindLogin(Model model,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        // ajax GET 요청이 아니라면 자동으로 로그인 페이지로 튕귄다.

        // 만일 ajax 요청이라면 (브라우저에서 요청) 로그인 페이지로 이동하는 view 를 보여 준다.
        // 브라우저에서 새로운 탭이나 새로운 창으로 신규 session 생성 되었을때
        // 기존 세션에서 action 을 하면 security error 발생 한다.
        // 이럴때 로그인 페이지로 강제로 튕겨 버리기 위해서 로그인에서도 체크 되어야 한다.(필터에서 100% 체크 안됨)
        String ajaxHeader = request.getHeader("X-Requested-With");
        if(StringUtils.equals(ajaxHeader, "XMLHttpRequest")) {
            LOG.info("user session broken. redirect login page");
            response.setStatus(MDV.HTTP_ERR_AJAX_SESSION_TIMEOUT);
            return "f_comm/login_redirect";
        }

        model.addAttribute("error", request.getParameter("error"));

        return "f_service/open/login";
    }

    @RequestMapping(value = "/open/about")
    public String about(Model model) throws IOException {

        return "f_service/open/about";
    }

    @RequestMapping(value = "/open/company_introduce")
    public String company_introduce(HttpServletRequest request, Model model) throws IOException {

        long file_srl = 1;
        String applicationFormFileURL = confDym.getProperty("image_server_host") +
                    webUtilService.getFileURI(request, file_srl, "repository_file_download_uri");
        if(applicationFormFileURL.equals("") || applicationFormFileURL == null) applicationFormFileURL = "";
        model.addAttribute("applicationFormFileURL", applicationFormFileURL);

        return "f_service/open/company_introduce";
    }

    @RequestMapping(value = "/open/company_counsel", method = RequestMethod.GET)
    public String company_counsel(Model model) throws IOException {

        //MDV.APP_USER_ADVISOR == 상담사
       // long totalRows = memberService.countGroupMemberInfo(null, null, MDV.NUSE, MDV.NUSE, MDV.APP_USER_ADVISOR);

        List<MemberEntity> memberEntities = memberService.getGroupMemberList(null, null, null, MDV.NUSE, MDV.NUSE, MDV.APP_USER_ADVISOR,
        null, MDV.NUSE, MDV.NUSE);

        for(MemberEntity memberEntity : memberEntities) {
            MemberEntity memberInfo = memberService.getMemberInfo(memberEntity.getMember_srl());
            memberEntity.setMemberExtraEntity(memberInfo.getMemberExtraEntity());
        }

        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("normal_category_type", MDV.NORMAL_CATEGORY);
        model.addAttribute("link_category_type", MDV.LINK_CATEGORY);

        model.addAttribute("mdv_nouse", MDV.NUSE);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_deny", MDV.DENY);
        model.addAttribute("category_srl_group", confAdmin.getProperty("plymind.group.category.srl"));

        model.addAttribute("image_server_host", confDym.getProperty("image_server_host"));
        model.addAttribute("document_attach_uri", confCommon.getProperty("document_attach_uri"));
        model.addAttribute("document_attach_download_uri", confCommon.getProperty("document_attach_download_uri"));

        model.addAttribute("memberEntities", memberEntities);

        return "f_service/open/company_counsel";
    }

    @RequestMapping(value = "/open/company_super", method = RequestMethod.GET)
    public String company_super(Model model) throws IOException {

        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("normal_category_type", MDV.NORMAL_CATEGORY);
        model.addAttribute("link_category_type", MDV.LINK_CATEGORY);

        model.addAttribute("category_srl_supervisor", confAdmin.getProperty("plymind.supervisor.category.srl"));

        return "f_service/open/company_super";
    }

    /**
     * 게시물 리스트를 보여 준다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return 게시물 리스트
     */
    @RequestMapping(value = "/open/company_super/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listDocument(HttpServletRequest request,
                                     @PathVariable("tid") String tid) {

        String strCategorySrl = confAdmin.getProperty("plymind.supervisor.category.srl");
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

        List<DocumentEntity> documentEntities = documentService.getDocument(searchFilter, null, sortValue, offset, limit);
        for (DocumentEntity documentEntity : documentEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + documentEntity.getDocument_srl());
            row.put("document_srl", documentEntity.getDocument_srl());
            row.put("document_title", documentEntity.getDocument_title());
            row.put("list_order", documentEntity.getList_order());
            row.put("allow_notice", documentEntity.getAllow_notice());
            row.put("c_date", documentEntity.getC_date());
            Map<String, Object> extra = supervisorService.getJsonTemplateExtra(documentEntity);

            row.put("template_extra", extra.get("template"));


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

    @RequestMapping(value = "/open/register", method = RequestMethod.GET)
    public String register(Model model) throws IOException {

        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("normal_category_type", MDV.NORMAL_CATEGORY);
        model.addAttribute("link_category_type", MDV.LINK_CATEGORY);

        model.addAttribute("mdv_nouse", MDV.NUSE);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_deny", MDV.DENY);
        model.addAttribute("category_srl_group", confAdmin.getProperty("plymind.group.category.srl"));

        return "f_service/open/register";
    }

    /**
     * 단체 리스트를 보여 준다.
     *
     * @param tid transaction id
     * @return 게시물 리스트
     */
    @RequestMapping(value = "/open/register/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listGroupDocument(@PathVariable("tid") String tid,
                                          @RequestParam("query") String query,
                                          @RequestParam("offset") int offset,
                                          @RequestParam("limit") int limit) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        List<Map<String, Object>> list = new ArrayList<>();

        query = StringUtils.trim(query);
        if (StringUtils.equals(query, "")) {
            mav.addObject(confCommon.getProperty("dtresp_total_row"), 0);
            mav.addObject("list", list);
            return mav;
        }

        Map<String, String> searchFilter = new HashMap<>();
        searchFilter.put("document_title", query);
        searchFilter.put("block", String.valueOf(MDV.NUSE));

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("document_srl", "asc");

        List<DocumentEntity> documentEntities = documentService.getDocument(searchFilter, null, null, MDV.NUSE, MDV.NUSE);

        for (DocumentEntity documentEntity : documentEntities) {
            Map<String, Object> map = new HashMap<>();

            map.put("document_srl", documentEntity.getDocument_srl());
            map.put("document_title", documentEntity.getDocument_title());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), documentEntities.size());
        mav.addObject("list", list);

        return mav;

    }

    @RequestMapping(value = "/open/register_optional")
    public String registerOptional(Model model, @RequestParam(value="user_id") String userId) throws IOException {

        LOG.info("try to login using user_id[" + userId + "]");

        MemberEntity memberEntity = memberService.getMemberInfo(userId);
        if(memberEntity == null) {
            LOG.info("failed login. not found user_id["+userId+"]");
            throw new UsernameNotFoundException("not found user_id["+userId+"]");
        }

        model.addAttribute("member_srl", memberEntity.getMember_srl());
        model.addAttribute("user_id", memberEntity.getUser_id());

        return "f_service/open/register_optional";
    }

    @RequestMapping(value = "/open/forget")
    public String forget(Model model) throws IOException {

        return "f_service/open/forget";
    }
}

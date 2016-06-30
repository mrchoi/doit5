package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.user.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
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
 * Created by kodaji on 16. 2. 15.
 */
@Controller
@RequestMapping("/admin/advisor")
public class AdminAdvisorController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminAdvisorController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confSvc;
    @Autowired
    private MessageSource messageSource;

    /**
     * 사용자 추가 폼을 보여 준다.
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addMemberForm(Model model) {
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_deny", MDV.DENY);
        model.addAttribute("backoffice_root", MDV.USER_ROOT);
        model.addAttribute("backoffice_user", MDV.USER_NORMAL);
        model.addAttribute("app_root", MDV.APP_USER_ROOT);
        model.addAttribute("app_root", MDV.APP_USER_ROOT);
        model.addAttribute("app_member", MDV.APP_USER_NORMAL);
        model.addAttribute("app_visitor", MDV.APP_USER_VISITOR);
        model.addAttribute("app_advisor", MDV.APP_USER_ADVISOR);
        model.addAttribute("backoffice_mode", confCommon.getProperty("backoffice_by_member_support"));

        return "f_admin/plymind/advisor/advisor_add";
    }

    /**
     * 사용자 리스트를 보여주는 폼을 출력 한다.
     *
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listMemberForm(Model model) {
        model.addAttribute("mdv_nouse", MDV.NUSE);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_deny", MDV.DENY);

        return "f_admin/plymind/advisor/advisor_list";
    }

    /**
     * 사용자 리스트 데이터를 출력 한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return model and view
     */
    @RequestMapping(value = "/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listMember(HttpServletRequest request,
                                   @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_member_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("group_srl", confSvc.getProperty("member.group.advisor.srl"));

        Map<String, String> advisor = memberService.getAdvisorAuthority();
        if(advisor.get("advisor").equals("true")) {
            searchFilter.put("user_id", advisor.get("user_id"));
        }

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = memberService.countMemberByGroup(Long.parseLong(confSvc.getProperty("member.group.advisor.srl")), searchFilter);
        long filterRows = memberService.countMemberByGroup(Long.parseLong(confSvc.getProperty("member.group.advisor.srl")), searchFilter);

        List<MemberEntity> memberEntities = memberService.getMemberListByGroup(Long.parseLong(confSvc.getProperty("member.group.advisor.srl")), searchFilter, sortValue, offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(MemberEntity memberEntity : memberEntities) {
            Map<String, Object> row = new HashMap<>();
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-row-"+memberEntity.getMember_srl());
            row.put("member_srl", memberEntity.getMember_srl());
            row.put("user_id", memberEntity.getUser_id());
            row.put("user_name", memberEntity.getUser_name());
            row.put("nick_name", memberEntity.getNick_name());
            row.put("class_srl", memberEntity.getMemberExtraEntity().getClass_srl());
            row.put("enabled", memberEntity.getEnabled());
            row.put("sign_out", memberEntity.getSign_out());
            row.put("c_date", memberEntity.getC_date());
            row.put("last_login_date", memberEntity.getLast_login_date());
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
     * 사용자 정보를 보여주는 폼을 출력 한다.
     *
     * @param memberSrl 사용자 시리얼 넘버
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/{member_srl}", method = RequestMethod.GET)
    public String detailMemberForm(@PathVariable("member_srl") int memberSrl,
                                   Model model) {
        MemberEntity memberEntity = memberService.getMemberInfo(memberSrl);
        if(memberEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "failed detail member. not found member of member_srl. member_srl [" + memberSrl + "]");
            LOG.error(reason.get("member_srl"));
            throw new CustomException("member_not_found_error", reason);
        }
        model.addAttribute("memberEntity", memberEntity);

        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_deny", MDV.DENY);

        List<Map<String, Object>> appGroupMapList = memberService.getUserAppGroup(memberSrl);
        model.addAttribute("appGroupMapList", appGroupMapList);

        return "f_admin/plymind/advisor/advisor_detail";
    }
}

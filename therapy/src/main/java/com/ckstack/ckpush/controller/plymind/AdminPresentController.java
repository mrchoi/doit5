package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.plymind.PresentService;
import com.ckstack.ckpush.service.user.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
@RequestMapping("/admin/present")
public class AdminPresentController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminPresentController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confSvc;
    @Autowired
    protected PresentService presentService;

    /**
     * 사용자 리스트를 보여주는 폼을 출력 한다.
     *
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/present_list", method = RequestMethod.GET)
    public String listMemberForm(Model model) {
        return "f_admin/plymind/present/present_list";
    }

    /**
     * 사용자 리스트 데이터를 출력 한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return model and view
     */
    @RequestMapping(value = "/present_list/t/{tid}", method = RequestMethod.POST)
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

            Map<String, Object> presentMap = presentService.getAdvisorPresent(memberEntity.getMember_srl());
            row.put("presentMap", presentMap);

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
}

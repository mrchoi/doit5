package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.user.GroupEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.domain.user.MemberExtraEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.user.MemberService;
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
import java.util.*;

/**
 * Created by dhkim94 on 15. 4. 22..
 */
@Controller
@RequestMapping("/admin/member")
public class MemberController {
    private final static Logger LOG = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
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

        return "f_admin/user/add_user";
    }

    /**
     * 사용자를 추가 한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param memberEntity 추가할 사용자 정보
     * @param bindingResult bindingResult object
     * @return 사용자 추가한 결과
     */
    @RequestMapping(value = "/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addMemberSubmit(HttpServletRequest request,
                                        @PathVariable("tid") String tid,
                                        @Valid @RequestBody MemberEntity memberEntity,
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
            throw new CustomException("add_member_error", reason);
        }

        // 일반 사용자 일때 app_srl 체크 및 group_srl 확인
        int groupSrl;
        if(memberEntity.getAccount_type() == MDV.USER_NORMAL) {
            if(memberEntity.getApp_srl() == null ||
                    StringUtils.equals(StringUtils.trim(memberEntity.getApp_srl()), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_srl", messageSource.getMessage("valid.app_srl", null,
                        LocaleContextHolder.getLocale()));
                throw new CustomException("add_member_error", reason);
            }

            groupSrl = MDV.APP_USER_ADVISOR;

            if (memberEntity.getApp_account_type() == MDV.APP_USER_NORMAL) {
                GroupEntity groupEntity = memberService.getGroup(confCommon.getProperty("group_role_user"));
                groupSrl = groupEntity.getGroup_srl();
            }

        } else if(memberEntity.getAccount_type() == MDV.USER_ROOT) {
            GroupEntity groupEntity = memberService.getGroup(confCommon.getProperty("group_role_root"));
            groupSrl = groupEntity.getGroup_srl();

        } else {
            Map<String, String> reason = new HashMap<>();
            reason.put("account_type", messageSource.getMessage("valid.account_type", null,
                    LocaleContextHolder.getLocale()));
            throw new CustomException("add_member_error", reason);
        }

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberService.signUp(memberEntity, MDV.NUSE, null, groupSrl, userDetails.getMember_srl(),
                webUtilService.getRequestIp(request));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("member_srl", memberEntity.getMember_srl());

        return mav;
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
        model.addAttribute("backoffice_root", MDV.USER_ROOT);
        model.addAttribute("backoffice_user", MDV.USER_NORMAL);

        return "f_admin/user/list_user";
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

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        List<Long> group_srls = new ArrayList<Long>();
        group_srls.add(Long.valueOf("1"));
        group_srls.add(Long.valueOf("2"));
        group_srls.add(Long.valueOf("3"));

        long totalRows = memberService.countMemberByGroups(MDV.NUSE, null, null);
        long filterRows = memberService.countMemberByGroups(MDV.NUSE, group_srls ,searchFilter);

        List<MemberEntity> memberEntities = memberService.getMemberListByGroups(MDV.NUSE, group_srls, searchFilter, sortValue, offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(MemberEntity memberEntity : memberEntities) {
            Map<String, Object> row = new HashMap<>();

            row.put(confCommon.getProperty("dtresp_row_id"), "dt-row-"+memberEntity.getMember_srl());
            row.put("member_srl", memberEntity.getMember_srl());
            row.put("user_id", memberEntity.getUser_id());
            row.put("user_name", memberEntity.getUser_name());
            row.put("enabled", memberEntity.getEnabled());
            row.put("sign_out", memberEntity.getSign_out());
            row.put("c_date", memberEntity.getC_date());
            row.put("last_login_date", memberEntity.getLast_login_date());

            //사용자 그룹
            List<GroupEntity> memberGroupEntities = memberService.getMemberGroup(memberEntity.getMember_srl());
            row.put("account_type", memberGroupEntities);

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
     * 사용자를 삭제 한다.
     *
     * @param tid transaction id
     * @param keyBean 사용자 시리얼 넘버 리스트. l_key에 담겨 있어야 한다.
     * @return ModelAndView
     */
    @RequestMapping(value = "/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteMember(@PathVariable("tid") String tid,
                                     @RequestBody KeyBean keyBean) {
        if(keyBean.getL_keys() == null || keyBean.getL_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("l_keys", "failed delete member. l_keys is null or empty");
            LOG.error(reason.get("l_keys"));
            throw new CustomException("delete_member_error", reason);
        }

        // 사용자를 삭제 하고, 프로필이 있다면 삭제 flag를 delete로 바꾼다.
        memberService.deleteMember(keyBean.getL_keys());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("member_srls", keyBean.getL_keys());

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

        return "f_admin/user/detail_user";
    }

    /**
     * 사용자 정보를 수정 한다.
     * @param memberSrl 수정 할 사용자 시리얼 넘버
     * @param tid transaction id
     * @param keyBean 수정할 정보
     * @return model and view object
     */
    @RequestMapping(value = "/{member_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyMemberSubmit(@PathVariable("member_srl") int memberSrl,
                                           @PathVariable("tid") String tid,
                                           @RequestBody KeyBean keyBean) {
        if(keyBean.getM_key() == null || keyBean.getM_key().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("m_key", "failed modify member. m_key is null or empty");
            LOG.error(reason.get("m_key"));
            throw new CustomException("modify_member_error", reason);
        }

        Map<String, String> memberData = new HashMap<>();
        Set<String> keys = keyBean.getM_key().keySet();
        for(String key : keys) memberData.put(key, keyBean.getM_key().get(key).toString());

        memberService.modifyMember(memberSrl, memberData);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

    /**
     * 사용자 extra 정보를 수정 한다.
     * @param memberSrl 수정 할 사용자 시리얼 넘버
     * @param tid transaction id
     * @param keyBean 수정할 정보
     * @return model and view object
     */
    @RequestMapping(value = "/extra/{member_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyMemberExtraSubmit(@PathVariable("member_srl") int memberSrl,
                                               @PathVariable("tid") String tid,
                                               @RequestBody KeyBean keyBean) {
        if(keyBean.getM_key() == null || keyBean.getM_key().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("m_key", "failed modify member extra. m_key is null or empty");
            LOG.error(reason.get("m_key"));
            throw new CustomException("modify_member_extra_error", reason);
        }

        Map<String, String> memberData = new HashMap<>();
        Set<String> keys = keyBean.getM_key().keySet();
        for(String key : keys) memberData.put(key, keyBean.getM_key().get(key).toString());

        memberService.modifyMemberExtra(memberSrl, memberData);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

    /**
     * jquery select2 를 위한 사용자 리스트 api.
     * 사용자 그룹 상관 없이 모든 사용자 리스트를 보여 준다.
     *
     * @param enabled 사용자 활성 여부
     * @param tid transaction id
     * @param query like 검색을 할 사용 아이디
     * @param offset 리스트의 offset
     * @param limit 리스트의 limit
     * @return 사용자 리스트
     */
    @RequestMapping(value = "/list/select2/{enabled}/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listUserForSelect2(@PathVariable("enabled") int enabled,
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

        long totalRows = memberService.countMemberInfo(query, null, enabled, MDV.NUSE);
        List<MemberEntity> memberEntities = memberService.getMemberList(query, null, enabled, MDV.NUSE, null, offset, limit);

        for(MemberEntity memberEntity : memberEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("member_srl", memberEntity.getMember_srl());
            map.put("user_id", memberEntity.getUser_id());
            map.put("user_name", memberEntity.getUser_name());
            map.put("nick_name", memberEntity.getNick_name());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject("list", list);

        return mav;
    }
}

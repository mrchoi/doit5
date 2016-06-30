package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.plymind.HolidayEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.plymind.HolidayService;
import com.ckstack.ckpush.service.user.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by je7646 on 16. 1. 21.
 */
@Controller
@RequestMapping("/admin/holiday")
public class AdminHolidayController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminHolidayController.class);

    @Autowired
    private AppService appService;

    @Autowired
    private WebUtilService webUtilService;

    @Autowired
    protected Properties confCommon;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private MemberService memberService;

    @Autowired
    protected Properties confSvc;


    @RequestMapping(value = "/holiday_list", method = RequestMethod.GET)
    public String listForm(Model model) {
        return "f_admin/plymind/holiday/holiday_list";
    }

    /**
     * 게시물 리스트를 보여 준다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return 게시물 리스트
     */
    @RequestMapping(value = "/holiday_list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listDocument(HttpServletRequest request,
                                     @PathVariable("tid") String tid) {
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

        List<Map<String, Object>> table = new ArrayList<>();

        long totalRows = holidayService.countHoliday(null);
        long filterRows = holidayService.countHoliday(searchFilter);

        List<HolidayEntity> holidayEntities = holidayService.getHolidayList(searchFilter, sortValue, offset, limit);
        for (HolidayEntity holidayEntity : holidayEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + holidayEntity.getHoliday_srl());
            row.put("holiday_srl", holidayEntity.getHoliday_srl());
            row.put("holiday_title", holidayEntity.getHoliday_title());
            row.put("holiday_date", holidayEntity.getHoliday_date());
            row.put("c_date", holidayEntity.getC_date());

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

    @RequestMapping(value = "/holiday_add", method = RequestMethod.GET)
    public String holidayAddForm(Model model) {
        return "f_admin/plymind/holiday/holiday_add";
    }

    /**
     * 컨텐츠케어세라피 인문컨텐츠 등록 처리
     * @return model and view
     */
    @RequestMapping(value = "/holiday_add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView holidayAdd(HttpServletRequest request,
                                @Valid @RequestBody HolidayEntity holidayEntity,
                                @PathVariable("tid") String tid) {

        int result = holidayService.addHoliday(holidayEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 등록된 휴일을 삭제 한다.
     *
     * @param tid transaction id
     * @param keyBean l_keys 에 삭제할 휴일의 시리얼 넘버 리스트가 포함되어 있다.
     *                i_keys 에는 게시물인지 게시물 링크 인지 구분값이 포함되어 있다.
     * @return model and view object
     */
    @RequestMapping(value = "/holiday_del/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteDocument(@PathVariable("tid") String tid,
                                       @RequestBody KeyBean keyBean) {
        if(keyBean.getL_keys() == null || keyBean.getL_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("l_keys", "failed delete document. l_keys is null or empty");
            LOG.error(reason.get("l_keys"));
            throw new CustomException("delete_document_error", reason);
        }

        holidayService.deleteHoliday(keyBean.getL_keys());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("holiday_srls", keyBean.getL_keys());

        return mav;
    }

    /**
     * 상담사 일정
     * @param model
     * @return
     */
    @RequestMapping(value = "/advisor_holiday_list", method = RequestMethod.GET)
    public String advisorListForm(Model model) {
        return "f_admin/plymind/holiday/advisor_holiday_list";
    }

    /**
     * 상담사일정 리스트를 보여 준다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return 게시물 리스트
     */
    @RequestMapping(value = "/advisor_holiday_list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView advisorListDocument(HttpServletRequest request,
                                     @PathVariable("tid") String tid) {
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        long member_srl = MDV.NUSE;
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
        for(GrantedAuthority grantedAuthority : authorities) {
            LOG.debug("=============================>" + grantedAuthority.getAuthority());
            if(grantedAuthority.getAuthority().equals("ROLE_ADVISOR")) {
                member_srl = userDetails.getMember_srl();
            }
        }

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

        List<Map<String, Object>> table = new ArrayList<>();

        long totalRows = holidayService.countAdvisorHoliday(member_srl, null);
        long filterRows = holidayService.countAdvisorHoliday(member_srl, searchFilter);

        List<HolidayEntity> holidayEntities = holidayService.getAdvisorHolidayList(member_srl, searchFilter, sortValue, offset, limit);
        for (HolidayEntity holidayEntity : holidayEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + holidayEntity.getHoliday_srl());
            row.put("holiday_srl", holidayEntity.getHoliday_srl());
            row.put("holiday_title", holidayEntity.getHoliday_title());
            row.put("nick_name", holidayEntity.getNick_name());
            row.put("holiday_date", holidayEntity.getHoliday_date());
            row.put("holiday_time", holidayEntity.getHoliday_time());
            row.put("c_date", holidayEntity.getC_date());

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

    @RequestMapping(value = "/advisor_holiday_add", method = RequestMethod.GET)
    public String advisorHolidayAddForm(Model model) {
        return "f_admin/plymind/holiday/advisor_holiday_add";
    }

    /**
     * 컨텐츠케어세라피 인문컨텐츠 등록 처리
     * @return model and view
     */
    @RequestMapping(value = "/advisor_holiday_add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView advisorHolidayAdd(HttpServletRequest request,
                                          @Valid @RequestBody HolidayEntity holidayEntity,
                                          @PathVariable("tid") String tid) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        holidayEntity.setMember_srl(userDetails.getMember_srl());

        int result = holidayService.addHoliday(holidayEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 관리자. 휴일 캘린더 정보를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/holiday_info_list", method = RequestMethod.GET)
    public ModelAndView holidayInfoList(HttpServletRequest request) {
        List<HolidayEntity> holidayEntities = holidayService.getHolidayList(null, null, MDV.NONE, MDV.NONE);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("holidayEntities", holidayEntities);

        return mav;
    }

    /**
     * 상담사. 일정 캘린더 정보를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/advisor_info_list", method = RequestMethod.GET)
    public ModelAndView advisorHolidayInfoList(HttpServletRequest request) {

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<HolidayEntity> advisorHolidayEntities = holidayService.getAdvisorHolidayList(userDetails.getMember_srl(), null, null, MDV.NONE, MDV.NONE);

        List<HolidayEntity> holidayEntities = holidayService.getHolidayList(null, null, MDV.NONE, MDV.NONE);

        String[] array_time = {"9", "10", "11", "12", "14", "15", "16", "17", "18", "19", "20", "21"};
        for(HolidayEntity holidayEntity : holidayEntities) {
            for (int i=0; i<array_time.length; i++) {
                holidayEntity.setHoliday_time(array_time[i]);

                advisorHolidayEntities.add(holidayEntity);
            }
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("advisorHolidayEntities", advisorHolidayEntities);

        return mav;
    }
}

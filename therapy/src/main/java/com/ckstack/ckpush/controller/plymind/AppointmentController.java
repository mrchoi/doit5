package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.domain.plymind.AppointmentEntity;
import com.ckstack.ckpush.domain.plymind.ProductEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.plymind.AppointmentService;
import com.ckstack.ckpush.service.plymind.ProductService;
import com.ckstack.ckpush.service.user.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by wypark on 16. 1. 18.
 */
@Controller
@RequestMapping("/user/appointment")
public class AppointmentController {
    private final static Logger LOG = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    protected Properties confCommon;

    @Autowired
    protected Properties confSvc;

    @Autowired
    private WebUtilService webUtilService;

    @Autowired
    private MemberService memberService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected AppointmentService appointmentService;

    /**
     * 예약 현황 목록 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/appointment_list", method = RequestMethod.GET)
    public String completeList(Model model) {
        return "f_service/plymind/appointment/appointment_list";
    }

    /**
     * 예약 현황 리스트를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView list(HttpServletRequest request,
                             @PathVariable("tid") String tid) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_payment_error", reason);
        }

        List<MemberEntity> memberEntities = memberService.getMemberListByGroup(Long.parseLong(confSvc.getProperty("member.group.advisor.srl")), null, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        List<Integer> application_statues = new ArrayList<Integer>();
        application_statues.add(MDV.APPLICATION_STATUS_READY);// 준비중
        application_statues.add(MDV.APPLICATION_STATUS_PROGRESS);// 진행중

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows = appointmentService.countAppointment(userDetails.getMember_srl(), MDV.NUSE, application_statues);
        long filterRows = appointmentService.countSearchAppointment(userDetails.getMember_srl(), MDV.NUSE, application_statues, searchFilter);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(userDetails.getMember_srl(), MDV.NUSE, application_statues, searchFilter, sortValue, offset, limit);

        for (AppointmentEntity appointmentEntity : appointmentEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + appointmentEntity.getAppointment_srl());
            row.put("appointment_srl", appointmentEntity.getAppointment_srl());
            row.put("member_srl", appointmentEntity.getMember_srl());
            row.put("advisor_srl", appointmentEntity.getAdvisor_srl());
            row.put("application_srl", appointmentEntity.getApplication_srl());
            row.put("appointment_date", appointmentEntity.getAppointment_date());
            row.put("appointment_time", appointmentEntity.getAppointment_time());
            row.put("appointment_status", appointmentEntity.getAppointment_status());
            row.put("application_group", appointmentEntity.getApplication_group());
            row.put("application_number", appointmentEntity.getApplication_number());
            row.put("product_srl", appointmentEntity.getProduct_srl());

            for (MemberEntity memberEntity : memberEntities) {
                if (appointmentEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                    row.put("advisor_name", memberEntity.getNick_name());
                    break;
                }
            }

            for (ProductEntity productEntity : productEntities) {
                if (appointmentEntity.getProduct_srl() == productEntity.getProduct_srl()) {
                    row.put("kind", productEntity.getKind());
                    row.put("title", productEntity.getTitle());
                    row.put("advice_type", productEntity.getAdvice_type());
                    row.put("advice_period", productEntity.getAdvice_period());
                    row.put("advice_number", productEntity.getAdvice_number());
                    row.put("advice_price", productEntity.getAdvice_price());
                    break;
                }
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
     * 예약 정보를 변경페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/changeForm/{appointment_srl}", method = RequestMethod.GET)
    public String changeForm(HttpServletRequest request,
                             @PathVariable("appointment_srl") long appointment_srl,
                             Model model) {
        int kind_srl = Integer.parseInt(confSvc.getProperty("plymind.product.kind.advice.srl"));

        /* 심리상담 정보 리스트 조회 START */
        List<ProductEntity> productEntities = productService.getProductList(kind_srl);
        /* 심리상담 정보 리스트 조회 END */

        /* 심리상담 정보 리스트 조회 START */
        List<MemberEntity> memberEntities = memberService.getMemberListByGroup(Long.parseLong(confSvc.getProperty("member.group.advisor.srl")), null, null, MDV.NUSE, MDV.NUSE);
        /* 심리상담 정보 리스트 조회 END */

        AppointmentEntity appointmentEntity = appointmentService.getAppointmentBySrl(appointment_srl);

        for (ProductEntity productEntity : productEntities) {
            if(productEntity.getProduct_srl() == appointmentEntity.getProduct_srl()) {
                appointmentEntity.setTitle(productEntity.getTitle());
                appointmentEntity.setAdvice_type(productEntity.getAdvice_type());
                appointmentEntity.setAdvice_period(productEntity.getAdvice_period());
                appointmentEntity.setAdvice_number(productEntity.getAdvice_number());
                appointmentEntity.setAdvice_price(productEntity.getAdvice_price());
                break;
            }
        }

        for (MemberEntity memberEntity : memberEntities) {
            if(memberEntity.getMember_srl() == appointmentEntity.getAdvisor_srl()) {
                appointmentEntity.setAdvisor_srl(memberEntity.getMember_srl());
                appointmentEntity.setAdvisor_name(memberEntity.getNick_name());
                break;
            }
        }

        model.addAttribute("productEntities", productEntities);
        model.addAttribute("memberEntities", memberEntities);
        model.addAttribute("appointmentEntity", appointmentEntity);

        return "f_service/plymind/appointment/appointment_change";
    }

    /**
     * 예약 정보를 변경한다.
     * @return model and view
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public ModelAndView change(HttpServletRequest request,
                                @Valid @RequestBody AppointmentEntity AppointmentEntity) {
        int result = appointmentService.change(AppointmentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 예약 정보를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/check/{advisor_srl}/{yearMonth}", method = RequestMethod.GET)
    public ModelAndView appointmentList(HttpServletRequest request,
                                        @PathVariable("advisor_srl") String advisor_srl,
                                        @PathVariable("yearMonth") String yearMonth) {
        List<AppointmentEntity> appointmentEntites = appointmentService.getAppointmentCheckList(Long.parseLong(advisor_srl), yearMonth);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("appointmentEntites", appointmentEntites);

        return mav;
    }
}

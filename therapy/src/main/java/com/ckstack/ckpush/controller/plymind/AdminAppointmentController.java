package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
import com.ckstack.ckpush.domain.plymind.AppointmentEntity;
import com.ckstack.ckpush.domain.plymind.PaymentEntity;
import com.ckstack.ckpush.domain.plymind.ProductEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.plymind.ApplicationService;
import com.ckstack.ckpush.service.plymind.AppointmentService;
import com.ckstack.ckpush.service.plymind.PaymentService;
import com.ckstack.ckpush.service.plymind.ProductService;
import com.ckstack.ckpush.service.user.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/plymind/appointment")
public class AdminAppointmentController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminAppointmentController.class);

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
    private PaymentService paymentService;

    @Autowired
    protected ApplicationService applicationService;

    @Autowired
    protected AppointmentService appointmentService;

    /**
     * 예약 등록을 위한 목록 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/appointment_group_list", method = RequestMethod.GET)
    public String appointmentGroupList(Model model) {
        return "f_admin/plymind/appointment/appointment_group_list";
    }

    /**
     * 예약 현황 리스트를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/group_list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView applicationGroupList(HttpServletRequest request, @PathVariable("tid") String tid) {
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

        List< MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        List<Long> contents_srls = new ArrayList<Long>();
        contents_srls.add(Long.parseLong("3"));
        contents_srls.add(Long.parseLong("4"));

        List<Integer> application_statues = new ArrayList<Integer>();
        application_statues.add(MDV.APPLICATION_STATUS_READY);
        application_statues.add(MDV.APPLICATION_STATUS_PROGRESS);

        //상담사 로그인 시 처리부분
        Map<String, String> advisor = memberService.getAdvisorAuthority();
        long advisor_srl = MDV.NUSE;
        if(advisor.get("advisor").equals("true")) {
            advisor_srl = Long.valueOf(advisor.get("member_srl"));
        }

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows = applicationService.countApplicationGroup(MDV.NUSE, advisor_srl, application_statues, contents_srls);
        long filterRows = applicationService.countSearchApplicationGroup(MDV.NUSE, advisor_srl, application_statues, contents_srls, searchFilter);

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationGroupList(MDV.NUSE, advisor_srl, application_statues, contents_srls, searchFilter, sortValue, offset, limit);

        long numb = 0;
        for (ApplicationEntity applicationEntity : applicationEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + applicationEntity.getApplication_group());
            row.put("application_srl", applicationEntity.getApplication_srl());
            row.put("application_group", applicationEntity.getApplication_group());
            row.put("member_srl", applicationEntity.getMember_srl());
            row.put("advisor_srl", applicationEntity.getAdvisor_srl());
            row.put("product_srl", applicationEntity.getProduct_srl());

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getMember_srl() == memberEntity.getMember_srl()) {
                    row.put("user_name", memberEntity.getUser_name());
                    break;
                }
            }

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                    row.put("advisor_name", memberEntity.getNick_name());
                    break;
                }
            }

            for (ProductEntity productEntity : productEntities) {
                if (applicationEntity.getProduct_srl() == productEntity.getProduct_srl()) {
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

            numb++;
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
     * 예약 등록을 위한 심리상담 회차 목록 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/appointment_application_list/{application_group}", method = RequestMethod.GET)
    public String appointmentApplicationList(@PathVariable("application_group") String application_group,
                                             Model model) {
        model.addAttribute("application_group", application_group);

        return "f_admin/plymind/appointment/appointment_application_list";
    }

    /**
     * 예약 등록을 위한 심리상담 회차 목록을 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/application_list/{application_group}/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addList(HttpServletRequest request,
                                @PathVariable("application_group") int application_group,
                                @PathVariable("tid") String tid) {
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

        List< MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(MDV.NUSE);

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows = applicationService.countApplicationByGroup(application_group);
        long filterRows = totalRows;

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationListByGroup(MDV.NUSE, application_group, sortValue, offset, limit);

        boolean isCheck = false;
        boolean isAppointment = false;
        for (ApplicationEntity applicationEntity : applicationEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + applicationEntity.getApplication_srl());
            row.put("application_srl", applicationEntity.getApplication_srl());
            row.put("application_group", applicationEntity.getApplication_group());
            row.put("application_number", applicationEntity.getApplication_number());
            row.put("member_srl", applicationEntity.getMember_srl());
            row.put("advisor_srl", applicationEntity.getAdvisor_srl());
            row.put("product_srl", applicationEntity.getProduct_srl());
            row.put("application_status", applicationEntity.getApplication_status());

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getMember_srl() == memberEntity.getMember_srl()) {
                    row.put("user_name", memberEntity.getUser_name());
                    break;
                }
            }

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                    row.put("advisor_name", memberEntity.getNick_name());
                    break;
                }
            }

            for (ProductEntity productEntity : productEntities) {
                if (applicationEntity.getProduct_srl() == productEntity.getProduct_srl()) {
                    row.put("kind", productEntity.getKind());
                    row.put("title", productEntity.getTitle());
                    row.put("advice_type", productEntity.getAdvice_type());
                    row.put("advice_period", productEntity.getAdvice_period());
                    row.put("advice_number", productEntity.getAdvice_number());
                    row.put("advice_price", productEntity.getAdvice_price());
                    break;
                }
            }

            if(!isCheck) {
                isAppointment = false;
                for (AppointmentEntity appointmentEntity : appointmentEntities) {
                    if (applicationEntity.getApplication_srl() == appointmentEntity.getApplication_srl()) {
                        row.put("appointment_date", appointmentEntity.getAppointment_date());
                        row.put("appointment_time", appointmentEntity.getAppointment_time());
                        isAppointment = true;

                        if (applicationEntity.getApplication_status() != 3) {
                            isCheck = true;
                        }
                        break;
                    }
                }

                if (!isAppointment) {
                    row.put("appointment_date", "NONE");
                    row.put("appointment_time", "NONE");

                    isCheck = true;
                }
            } else {
                row.put("appointment_date", "-");
                row.put("appointment_time", "-");
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
     * 심리상담 회차별 예약 등록 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/appointment_add/{application_srl}", method = RequestMethod.GET)
    public String appointmentAddForm(@PathVariable("application_srl") long application_srl,
                                     Model model) {
        List< MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(MDV.NUSE);

        ApplicationEntity applicationEntity = applicationService.getApplication(application_srl);

        for (MemberEntity memberEntity : memberEntities) {
            if (applicationEntity.getMember_srl() == memberEntity.getMember_srl()) {
                applicationEntity.setUser_name(memberEntity.getUser_name());
                break;
            }
        }

        for (MemberEntity memberEntity : memberEntities) {
            if (applicationEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                applicationEntity.setAdvisor_name(memberEntity.getNick_name());
                break;
            }
        }

        for (ProductEntity productEntity : productEntities) {
            if (applicationEntity.getProduct_srl() == productEntity.getProduct_srl()) {
                applicationEntity.setKind(productEntity.getKind());
                applicationEntity.setTitle(productEntity.getTitle());
                applicationEntity.setAdvice_type(productEntity.getAdvice_type());
                applicationEntity.setAdvice_period(productEntity.getAdvice_period());
                applicationEntity.setAdvice_number(productEntity.getAdvice_number());
                applicationEntity.setAdvice_price(productEntity.getAdvice_price());
                break;
            }
        }


        LOG.debug("=================================>" + applicationEntity.getAdvisor_srl());

        model.addAttribute("applicationEntity", applicationEntity);

        return "f_admin/plymind/appointment/appointment_add";
    }

    /**
     * 예약 정보(날짜 및 시간)를 저장한다.
     * @return model and view
     */
    @RequestMapping(value = "/appointment_add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView appointmentAdd(HttpServletRequest request,
                                       @Valid @RequestBody AppointmentEntity AppointmentEntity,
                                       @PathVariable("tid") String tid) {
        int result = appointmentService.add(AppointmentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 예약 목록 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/appointment_list", method = RequestMethod.GET)
    public String appointmentList(Model model) {
        return "f_admin/plymind/appointment/appointment_list";
    }

    /**
     * 예약 현황 리스트를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView list(HttpServletRequest request,
                             @PathVariable("tid") String tid) {

        //상담사 로그인 시 처리부분
        Map<String, String> advisor = memberService.getAdvisorAuthority();
        long advisor_srl = MDV.NUSE;
        if(advisor.get("advisor").equals("true")) {
            advisor_srl = Long.valueOf(advisor.get("member_srl"));
        }

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

        List< MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<PaymentEntity> paymentEntities = paymentService.getPaymentList(MDV.NUSE, MDV.NUSE, null, null, null, MDV.NUSE, MDV.NUSE);

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        List<Integer> application_statues = new ArrayList<Integer>();
        application_statues.add(MDV.APPLICATION_STATUS_READY);
        application_statues.add(MDV.APPLICATION_STATUS_PROGRESS);

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows = appointmentService.countAppointment(MDV.NUSE, advisor_srl, application_statues);
        long filterRows = appointmentService.countSearchAppointment(MDV.NUSE, advisor_srl, application_statues, searchFilter);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(MDV.NUSE, advisor_srl, application_statues, searchFilter, sortValue, offset, limit);

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
            row.put("user_name", appointmentEntity.getUser_name());

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

            for (PaymentEntity paymentEntity : paymentEntities) {
                if (appointmentEntity.getApplication_group() == paymentEntity.getApplication_group()) {
                    row.put("payment_status", paymentEntity.getPayment_status());
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
     * 예약 상세 정보를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/detail/{appointment_srl}", method = RequestMethod.GET)
    public String detail(@PathVariable("appointment_srl") String appointment_srl, Model model) {
        List< MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        AppointmentEntity appointmentEntity = appointmentService.getAppointmentBySrl(Long.parseLong(appointment_srl));

        for (MemberEntity memberEntity : memberEntities) {
            if (appointmentEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                appointmentEntity.setAdvisor_name(memberEntity.getNick_name());
                break;
            }
        }

        for (ProductEntity productEntity : productEntities) {
            if (appointmentEntity.getProduct_srl() == productEntity.getProduct_srl()) {
                appointmentEntity.setKind(productEntity.getKind());
                appointmentEntity.setTitle(productEntity.getTitle());
                appointmentEntity.setAdvice_type(productEntity.getAdvice_type());
                appointmentEntity.setAdvice_period(productEntity.getAdvice_period());
                appointmentEntity.setAdvice_number(productEntity.getAdvice_number());
                break;
            }
        }

        model.addAttribute("appointmentEntity", appointmentEntity);

        return "f_admin/plymind/appointment/appointment_detail";
    }

    /**
     * 예약 정보(날짜 및 시간)를 변경한다.
     * @return model and view
     */
    @RequestMapping(value = "/change/t/{tid}", method = RequestMethod.POST)
    public ModelAndView change(HttpServletRequest request,
                               @Valid @RequestBody AppointmentEntity AppointmentEntity,
                               @PathVariable("tid") String tid) {
        int result = appointmentService.change(AppointmentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 예약 상태를 변경한다. ( 0=접수, 1=접수완료, 2=취소)
     * @return model and view
     */
    @RequestMapping(value = "/{appointment_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyMemberSubmit(@PathVariable("appointment_srl") long appointment_srl,
                                           @PathVariable("tid") String tid,
                                           @RequestBody KeyBean keyBean) {
        String appointment_status = "";

        Set<String> keys = keyBean.getM_key().keySet();
        for(String key : keys) {
            if(key.equals("appointment_status")) {
                appointment_status = keyBean.getM_key().get(key).toString();
            }
        }

        appointmentService.changeStatus(appointment_srl, Integer.parseInt(appointment_status));

        AppointmentEntity appointmentEntity = appointmentService.getAppointmentBySrl(appointment_srl);
        applicationService.statusProgress(appointmentEntity.getApplication_srl(), MDV.NUSE);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

    /**
     * 선택된 달의 예약 정보를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/check/{advisor_srl}/{yearMonth}", method = RequestMethod.GET)
    public ModelAndView appointmentList(HttpServletRequest request,
                                        @PathVariable("advisor_srl") long advisor_srl,
                                        @PathVariable("yearMonth") String yearMonth) {
        List<AppointmentEntity> appointmentEntites = appointmentService.getAppointmentCheckList(advisor_srl, yearMonth);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("appointmentEntites", appointmentEntites);

        return mav;
    }
}

package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
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
@RequestMapping("/user/payment")
public class PaymentController {
    private final static Logger LOG = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    protected Properties confCommon;

    @Autowired
    protected Properties confSvc;

    @Autowired
    private WebUtilService webUtilService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    protected ProductService productService;

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 결제 정보 입력 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/paymentForm/{price}/{application_group}", method = RequestMethod.GET)
    public String paymentForm(@PathVariable("price") int price,
                              @PathVariable("application_group") int application_group,
                              Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List< MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(MDV.NUSE);

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationListByGroup(userDetails.getMember_srl(), application_group, null, MDV.NUSE, MDV.NUSE);

        ApplicationEntity returnApplicationEntity = new ApplicationEntity();

        for (ApplicationEntity applicationEntity : applicationEntities) {
            returnApplicationEntity = applicationEntity;

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getMember_srl() == memberEntity.getMember_srl()) {
                    returnApplicationEntity.setUser_name(memberEntity.getUser_name());
                    break;
                }
            }

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                    returnApplicationEntity.setAdvisor_name(memberEntity.getNick_name());
                    break;
                }
            }

            for (ProductEntity productEntity : productEntities) {
                if (applicationEntity.getProduct_srl() == productEntity.getProduct_srl()) {
                    returnApplicationEntity.setContents_srl(productEntity.getContents_srl());
                    returnApplicationEntity.setKind(productEntity.getKind());
                    returnApplicationEntity.setTitle(productEntity.getTitle());
                    returnApplicationEntity.setAdvice_type(productEntity.getAdvice_type());
                    returnApplicationEntity.setAdvice_period(productEntity.getAdvice_period());
                    returnApplicationEntity.setAdvice_number(productEntity.getAdvice_number());
                    returnApplicationEntity.setAdvice_price(productEntity.getAdvice_price());
                    break;
                }
            }

            break;
        }

        model.addAttribute("price", price);
        model.addAttribute("applicationEntity", returnApplicationEntity);

        return "f_service/plymind/payment/payment_add";
    }

    /**
     * 결제 정보를 저장한다.
     * @return model and view
     */
    @RequestMapping(value = "/paymentAdd", method = RequestMethod.POST)
    public ModelAndView paymentAdd(HttpServletRequest request,
                                @Valid @RequestBody PaymentEntity paymentEntity) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> resultMap = paymentService.paymentAdd(userDetails.getMember_srl(), paymentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("resultMap", resultMap);

        return mav;
    }

    /**
     * 저장된 결제 정보를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/paymentResult/{payment_srl}", method = RequestMethod.GET)
    public String paymentResult(@PathVariable("payment_srl") int payment_srl,
                                Model model) {
        PaymentEntity paymentEntity = paymentService.getPaymentInfoBySrl(payment_srl);

        model.addAttribute("paymentEntity", paymentEntity);

        return "f_service/plymind/payment/payment";
    }

    /**
     * 결제 내역 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/payment_list", method = RequestMethod.GET)
    public String completeList(Model model) {
        return "f_service/plymind/payment/payment_list";
    }

    /**
     * 결제 취소 내역 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/cancel_list", method = RequestMethod.GET)
    public String cancelList(Model model) {
        return "f_service/plymind/payment/cancel_list";
    }

    /**
     * 결제 변경 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/modify_list", method = RequestMethod.GET)
    public String modifyList(Model model) { return "f_service/plymind/payment/modify_list"; }

    /**
     * 결제 내역 리스트를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/list/{kind}/t/{tid}", method = RequestMethod.POST)
    public ModelAndView list(HttpServletRequest request,
                                   @PathVariable("kind") String kind,
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

        List< MemberEntity > memberEntities = memberService.getMemberListByGroup(Long.parseLong(confSvc.getProperty("member.group.advisor.srl")), null, null, MDV.NUSE, MDV.NUSE);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(userDetails.getMember_srl());

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationList(userDetails.getMember_srl(), MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<Integer> payment_statues = new ArrayList<>();
        if(kind.equals("COMPLETE")) {
            payment_statues.add(0);
            payment_statues.add(1);
        } else {
            payment_statues.add(2);
            payment_statues.add(3);
            payment_statues.add(4);
        }

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows = paymentService.countPayment(userDetails.getMember_srl(), MDV.NUSE, payment_statues);
        long filterRows = paymentService.countSearchPayment(userDetails.getMember_srl(), MDV.NUSE, payment_statues, searchFilter);

        List<PaymentEntity> paymentEntities = paymentService.getPaymentList(userDetails.getMember_srl(), MDV.NUSE, payment_statues, searchFilter, sortValue, offset, limit);

        for (PaymentEntity paymentEntity : paymentEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + paymentEntity.getPayment_srl());
            row.put("payment_srl", paymentEntity.getPayment_srl());
            row.put("member_srl", paymentEntity.getMember_srl());
            row.put("product_srl", paymentEntity.getProduct_srl());
            row.put("contents_srl", paymentEntity.getContents_srl());
            row.put("application_group", paymentEntity.getApplication_group());
            row.put("payment_name", paymentEntity.getPayment_name());
            row.put("payment_phone", paymentEntity.getPayment_phone());
            row.put("price", paymentEntity.getPrice());
            row.put("receipt_kind", paymentEntity.getReceipt_kind());
            row.put("receipt_phone", paymentEntity.getReceipt_phone());
            row.put("payment_status", paymentEntity.getPayment_status());
            row.put("refund_bank", paymentEntity.getRefund_bank());
            row.put("refund_account", paymentEntity.getRefund_account());
            row.put("refund_name", paymentEntity.getRefund_name());
            row.put("reporting_date", paymentEntity.getReporting_date());
            row.put("cancel_date", paymentEntity.getCancel_date());
            row.put("payment_date", paymentEntity.getPayment_date());
            row.put("refund_req_date", paymentEntity.getRefund_req_date());
            row.put("refund_date", paymentEntity.getRefund_date());

            row.put("appointment_srl", "");
            row.put("appointment_date", "");
            row.put("appointment_time", "");
            row.put("advisor_name", "");
            row.put("isProgress", false);

            for (AppointmentEntity appointmentEntity : appointmentEntities) {
                if (paymentEntity.getApplication_group() == appointmentEntity.getApplication_group()) {
                    row.put("appointment_srl", appointmentEntity.getAppointment_srl());
                    row.put("appointment_date", appointmentEntity.getAppointment_date());
                    row.put("appointment_time", appointmentEntity.getAppointment_time());

                    for (MemberEntity memberEntity : memberEntities) {
                        if (appointmentEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                            row.put("advisor_name", memberEntity.getNick_name());
                            break;
                        }
                    }
                    break;
                }
            }


            for(ApplicationEntity applicationEntity : applicationEntities) {
                if (paymentEntity.getApplication_group() == applicationEntity.getApplication_group() && applicationEntity.getApplication_status() != 3) {
                    row.put("isProgress", true);
                    break;
                }
            }

            for (ProductEntity productEntity : productEntities) {
                if (paymentEntity.getProduct_srl() == productEntity.getProduct_srl()) {
                    row.put("kind", productEntity.getKind());
                    row.put("title", productEntity.getTitle());
                    row.put("advice_type", productEntity.getAdvice_type());
                    row.put("advice_period", productEntity.getAdvice_period());
                    row.put("advice_number", productEntity.getAdvice_number());
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
     * 저장된 결제 정보를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/refundForm/{payment_srl}", method = RequestMethod.GET)
    public String paymentRefundForm(@PathVariable("payment_srl") String payment_srl,
                                Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List< MemberEntity > memberEntities = memberService.getMemberListByGroup(Long.parseLong(confSvc.getProperty("member.group.advisor.srl")), null, null, MDV.NUSE, MDV.NUSE);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(userDetails.getMember_srl());

        PaymentEntity paymentEntity = paymentService.getPaymentInfoBySrl(Integer.parseInt(payment_srl));

        for (AppointmentEntity appointmentEntity : appointmentEntities) {
            if (paymentEntity.getApplication_group() == appointmentEntity.getApplication_group()) {
                paymentEntity.setAppointment_srl(appointmentEntity.getAppointment_srl());
                paymentEntity.setAppointment_date(appointmentEntity.getAppointment_date());
                paymentEntity.setAppointment_time(appointmentEntity.getAppointment_time());

                for (MemberEntity memberEntity : memberEntities) {
                    if (appointmentEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                        paymentEntity.setAdvisor_name(memberEntity.getNick_name());
                        break;
                    }
                }
                break;
            }
        }

        model.addAttribute("paymentEntity", paymentEntity);

        return "f_service/plymind/payment/payment_refund";
    }

    /**
     * 환불 요청을 한다..
     * @return model and view
     */
    @RequestMapping(value = "/paymentRefund", method = RequestMethod.POST)
    public ModelAndView paymentRefund(HttpServletRequest request,
                                   @Valid @RequestBody PaymentEntity paymentEntity) {
        int result = paymentService.paymentRefundReq(paymentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 환불 요청을 한다..
     * @return model and view
     */
    @RequestMapping(value = "/paymentCancel", method = RequestMethod.POST)
    public ModelAndView paymentCancel(HttpServletRequest request,
                                      @Valid @RequestBody PaymentEntity paymentEntity) {
        int result = paymentService.paymentCancel(paymentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }
}

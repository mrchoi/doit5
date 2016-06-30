package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.push.PushMessageEntity;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.push.PushMessageService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by dhkim94 on 15. 4. 23..
 */
@Controller
@RequestMapping("/admin/message")
public class PushController {
    private final static Logger LOG = LoggerFactory.getLogger(PushController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confAccessory;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PushMessageService pushMessageService;

    /**
     * gcm, apns 메시지 발송용 앱 리스트 화면 폼을 보여 준다.
     *
     * @return view name
     */
    @RequestMapping(value = "/gcm/apns/app/list", method = RequestMethod.GET)
    public String listAppForGcmApnsForm() {
        return "f_admin/message/list_app_for_gcm_apns";
    }

    /**
     * gcm, apns 메시지 발송용 앱 리스트 데이터를 리턴한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return gcm, apns 메시지 발송용 앱 리스트
     */
    @RequestMapping(value = "/gcm/apns/app/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listAppForGcmApns(HttpServletRequest request,
                                          @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_app_for_gcm_apns_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("enabled", Integer.toString(MDV.YES));

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = appService.getAppCount(null, MDV.YES);
        long filterRows = appService.getAppCount(searchFilter);

        List<AppEntity> appEntities = appService.getAppInfo(searchFilter, sortValue, offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(AppEntity appEntity : appEntities) {
            Map<String, Object> row = new HashMap<>();

            row.put(confCommon.getProperty("dtresp_row_id"), "dt-row-"+appEntity.getApp_srl());
            row.put("app_srl", appEntity.getApp_srl());
            row.put("app_name", appEntity.getApp_name());
            row.put("reg_terminal", appEntity.getReg_terminal());
            row.put("reg_push_terminal", appEntity.getReg_push_terminal());
            row.put("push_count", appEntity.getPush_count());
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
     * push 메시지를 발송 할 수 있는 form 을 보여 준다.
     *
     * @param appSrl push 메시지 발송할 앱 시리얼 넘버
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/gcm/apns/app/{app_srl}", method = RequestMethod.GET)
    public String pushGcmApnsForm(@PathVariable("app_srl") int appSrl,
                                  Model model) {
        AppEntity appEntity = appService.getAppInfo(appSrl);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "not found app. app_srl ["+appSrl+"]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("push_gcm_apns_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        model.addAttribute("appEntity", appEntity);
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("target_all", confCommon.getProperty("push_target_all_in_app"));
        model.addAttribute("target_unit", confCommon.getProperty("push_target_unit_in_app"));

        model.addAttribute("android_device_class",
                Integer.parseInt(confCommon.getProperty("user_terminal_android"), 10));
        model.addAttribute("ios_device_class",
                Integer.parseInt(confCommon.getProperty("user_terminal_iphone"), 10));

        return "f_admin/message/push_gcm_apns_form";
    }

    /**
     * GCM, APNs 메시지를 발송 한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param pushMessageEntity 발송할 메시지 데이터
     * @param bindingResult 데이터 검증
     * @return ModelAndView object
     */
    @RequestMapping(value = "/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView pushGcmApns(HttpServletRequest request,
                                    @PathVariable("tid") String tid,
                                    @Valid @RequestBody PushMessageEntity pushMessageEntity,
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
            throw new CustomException("push_gcm_apns_error", reason);
        }

        // 개별 대상 발송인데 개발 대상이 없다면 에러
        if(StringUtils.equalsIgnoreCase(StringUtils.trim(pushMessageEntity.getPush_target()), "__unit")) {
            if(StringUtils.equals(StringUtils.trim(pushMessageEntity.getSingle_push_target()), "")) {
                Locale locale = LocaleContextHolder.getLocale();

                Map<String, String> reason = new HashMap<>();
                reason.put("single_push_target", messageSource.getMessage("valid.single_push_target",  null, locale));
                LOG.warn("invalid field. field name[single_push_target], message[It is mandatory value]");
                // NOTE 400 을 리턴 시키면 jQuery 에서 cors 가 발생 한다.
                throw new CustomException("push_gcm_apns_error", reason);
            }
        }

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        pushMessageEntity.setUser_id(userDetails.getUser_id());

        pushMessageService.pushGcmApns(pushMessageEntity, webUtilService.getRequestIp(request));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");
        mav.addObject("push_srl", pushMessageEntity.getPush_srl());
        mav.addObject("push_title", pushMessageEntity.getPush_title());
        mav.addObject("push_target", pushMessageEntity.getPush_target());

        return mav;
    }

    /**
     * 앱 별로 push 메시지 발송 이력 데이터를 구한다.
     *
     * @param request HttpServletRequest object
     * @param appSrl 앱 시리얼 넘버
     * @param tid transaction id
     * @return ModelAndView object
     */
    @RequestMapping(value = "/gcm/apns/app/{app_srl}/history/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listPushMessageHistory(HttpServletRequest request,
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
            throw new CustomException("list_push_message_history_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("app_srl", Integer.toString(appSrl));

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = pushMessageService.getPushMessageHistoryCount(appSrl, null);
        long filterRows = pushMessageService.getPushMessageHistoryCount(searchFilter);

        List<PushMessageEntity> pushMessageEntities = pushMessageService.getPushMessageHistory(searchFilter,
                sortValue, offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(PushMessageEntity pushMessageEntity : pushMessageEntities) {
            Map<String, Object> row = new HashMap<>();

            row.put(confCommon.getProperty("dtresp_row_id"), "dt-row-push-message-"+pushMessageEntity.getPush_srl());
            row.put("push_srl", pushMessageEntity.getPush_srl());
            row.put("push_title", pushMessageEntity.getPush_title());
            row.put("user_id", pushMessageEntity.getUser_id());
            row.put("push_target", pushMessageEntity.getPush_target());
            row.put("total_count", pushMessageEntity.getTotal_count());
            row.put("total_real_count", pushMessageEntity.getTotal_real_count());
            row.put("success_count", pushMessageEntity.getSuccess_count());
            row.put("fail_count", pushMessageEntity.getFail_count());
            row.put("fail_real_count", pushMessageEntity.getFail_real_count());
            row.put("received_count", pushMessageEntity.getReceived_count());
            row.put("confirm_count", pushMessageEntity.getConfirm_count());
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

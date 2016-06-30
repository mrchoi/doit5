package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.accessory.DeviceService;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.domain.app.AppEntity;
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
 * Created by dhkim94 on 15. 4. 6..
 */
@Controller
@RequestMapping("/admin/app")
public class AppController {
    private final static Logger LOG = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    protected Properties confAccessory;
    @Autowired
    protected Properties confCommon;
    @Autowired
    private MessageSource messageSource;

    /**
     * app 을 추가 하기 위한 폼을 보여 준다.
     *
     * @param model model object
     * @return 앱 추가 하는 form 의 view name
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addAppForm(Model model) {
        if(!confCommon.containsKey("user_terminal_support")) {
            LOG.error("not found environment value. key is user_terminal_support");
            Map<String, String> reason = new HashMap<>();
            reason.put("user_terminal_support", "server error. not found user_terminal_support configuration");
            throw new CustomException("server_config_error", 500, reason);
        }

        AppEntity appEntity = new AppEntity();
        model.addAttribute("appEntity", appEntity);

        return "f_admin/app/add_app";
    }

    /**
     * 앱을 추가 한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param appEntity 추가할 앱 정보
     * @param bindingResult bindingResult object
     * @return ModelAndView object
     */
    @RequestMapping(value = "/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addAppSubmit(HttpServletRequest request,
                                     @PathVariable("tid") String tid,
                                     @Valid @RequestBody AppEntity appEntity,
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
            throw new CustomException("add_app_error", reason);
        }

        // 중복 체크
        if(appService.isDuplicate(appEntity.getApi_key())) {
            LOG.error("duplicate api_key. api_key["+StringUtils.trim(appEntity.getApi_key())+"]");
            Map<String, String> reason = new HashMap<>();
            reason.put("api_key", "duplicate api_key ["+ appEntity.getApi_key()+"]");
            throw new CustomException("duplicate_app_error", reason);
        }

        // 앱을 추가 한다.
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        appService.createApp(userDetails.getMember_srl(), appEntity, userDetails.getUser_id(),
                webUtilService.getRequestIp(request));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("app_srl", appEntity.getApp_srl());
        mav.addObject("api_key", appEntity.getApi_key());
        mav.addObject("api_secret", appEntity.getApi_secret());
        mav.addObject("app_version", appEntity.getApp_version());
        mav.addObject("app_name", appEntity.getApp_name());

        return mav;
    }

    /**
     * 등록 된 앱 리스트를 보여 주기 위한 구조를 가진 페이지를 보여 준다.
     *
     * @param model Model object
     * @return 앱 리스트 보여 주기 위한 구조를 가지고 있는 view
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAppForm(Model model) {
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_deny", MDV.DENY);
        model.addAttribute("mdv_nouse", MDV.NUSE);

        return "f_admin/app/list_app";
    }

    /**
     * 등록 된 앱 리스트를 구한다. jQuery datatable 에 맞추어 request와 response 해야 한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return 데이터
     */
    @RequestMapping(value = "/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listApp(HttpServletRequest request,
                                @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_app_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = appService.getAppCount(null, MDV.NUSE);
        long filterRows = appService.getAppCount(searchFilter);

        List<AppEntity> appEntities = appService.getAppInfo(searchFilter, sortValue, offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(AppEntity appEntity : appEntities) {
            Map<String, Object> row = new HashMap<>();

            row.put(confCommon.getProperty("dtresp_row_id"), "dt-row-"+appEntity.getApp_srl());
            row.put("app_srl", appEntity.getApp_srl());
            row.put("app_name", appEntity.getApp_name());
            row.put("api_key", appEntity.getApi_key());
            row.put("app_version", appEntity.getApp_version());
            row.put("api_secret", appEntity.getApi_secret());
            row.put("enabled", appEntity.getEnabled());
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
     * select2 용도를 위한 앱 리스트를 구한다.
     *
     * @param enabled 앱 활성 여부
     * @param tid transaction id
     * @param query 검색 하려는 앱 이름 like 검색함
     * @param offset list offset
     * @param limit list limit
     * @return select2 용도의 앱 리스트
     */
    @RequestMapping(value = "/list/{enabled}/select2/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listAppForSelect2(@PathVariable("enabled") int enabled,
                                          @PathVariable("tid") String tid,
                                          @RequestParam("query") String query,
                                          @RequestParam("offset") int offset,
                                          @RequestParam("limit") int limit) {
        if(enabled != MDV.YES && enabled != MDV.DENY && enabled != MDV.NO) enabled = MDV.YES;

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

        Map<String, String> searchFilter = new HashMap<>();
        searchFilter.put("enabled", Integer.toString(enabled));
        searchFilter.put("app_name", query);

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("app_name", "asc");

        long totalRows = appService.getAppCount(null, enabled);
        List<AppEntity> appEntities = appService.getAppInfo(searchFilter, sortValue, offset, limit);

        for(AppEntity appEntity : appEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("app_srl", appEntity.getApp_srl());

            // 구분을 위해서 app_name 에 버전 정보도 추가함
            map.put("app_name", appEntity.getApp_name() + " (" + appEntity.getApp_version() + ")");
            //map.put("app_version", appEntity.getApp_version());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject("list", list);

        return mav;
    }

    /**
     * 앱 삭제 한다.
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param keyBean 삭제할 app_srl list. KeyBean.i_keys 에 매핑 된다.
     *                즉, request 에서는 { i_keys:[] } 로 요청 해야 한다.
     * @return ModelAndView object
     */
    @RequestMapping(value = "/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteApp(HttpServletRequest request,
                                  @PathVariable("tid") String tid,
                                  @RequestBody KeyBean keyBean) {
        if(keyBean.getI_keys() == null || keyBean.getI_keys().size() <= 0) {
            LOG.error("failed delete app. i_keys is null or empty");
            Map<String, String> reason = new HashMap<>();
            reason.put("i_keys", "failed delete app. i_keys is null or empty");
            throw new CustomException("delete_app_error", reason);
        }

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<Integer, String> deletedApps = appService.deleteApp(keyBean.getI_keys(), userDetails.getUser_id(),
                userDetails.getMember_srl(), webUtilService.getRequestIp(request));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");
        mav.addObject("apps", deletedApps);

        return mav;
    }

    /**
     * 앱 정보를 뷰 하고 수정 하는 폼을 보여 준다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param model Model object
     * @return 앱 수정하는 뷰의 view name
     */
    @RequestMapping(value = "/{app_srl}", method = RequestMethod.GET)
    public String detailApp(@PathVariable("app_srl") int appSrl,
                            Model model) {
        AppEntity appEntity = appService.getAppInfo(appSrl);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "failed detail app. not found app of app_srl. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("read_app_error", reason);
        }
        model.addAttribute("appEntity", appEntity);

        // 앱 관리자 정보를 포함 시킨다.
        model.addAttribute("appManager", appService.getAppManager(appSrl));

        return "f_admin/app/detail_app";
    }

    /**
     * 앱 정보를 수정 한다. 앱 정보와 앱 관리자 변경은 동시에 수정 할 수 없다.
     * 원래 지원하는 컨트롤러 자체가 항목 하나씩 변경하는 컨트롤러 이다.
     *
     * @param appSrl 수정할 앱 시리얼 넘버
     * @param tid transaction id
     * @param keyBean m_key 를 포함하고 있는 KeyBean object. m_key 는 hashmap 으로 다음 값 중의 하나가 사용 가능 하다.
     *                {app_name: 변경할 앱 이름},
     *                {app_version : 변경할 앱 버전},
     *                {enabled: 앱의 상태}
     *                {app_manager: 앱 매니저 array} ==> {app_namager: ['dhkim1', 'dhkim2']} 형태
     * @return ModelAndView object
     */
    @RequestMapping(value = "/{app_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyApp(@PathVariable("app_srl") int appSrl,
                                  @PathVariable("tid") String tid,
                                  @RequestBody KeyBean keyBean) {
        if(keyBean.getM_key() == null || keyBean.getM_key().size() <= 0) {
            LOG.error("failed modify app. m_key is null or empty");
            Map<String, String> reason = new HashMap<>();
            reason.put("m_key", "failed modify app. m_key is null or empty");
            throw new CustomException("modify_app_error", reason);
        }

        if(keyBean.getM_key().containsKey("app_manager")) {
            @SuppressWarnings("unchecked")
            List<String> appManager = (List<String>) keyBean.getM_key().get("app_manager");
            appService.modifyAppManager(appSrl, appManager);

        } else {
            Map<String, String> appData = new HashMap<>();
            Set<String> keys = keyBean.getM_key().keySet();
            for(String key : keys) appData.put(key, keyBean.getM_key().get(key).toString());

            appService.modifyApp(appSrl, appData);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

    /**
     * 앱에 등록된 단말 리스트를 구한다.
     *
     * @param request HttpServletRequest object
     * @param appSrl 앱의 시리얼 넘버
     * @param regPushId push id 등록된 단말기의 리스트인지 아닌지 여부.
     *                  - 0 : 앱에 등록된 단말기 전체 리스트
     *                  - 1 : 앱에 등록된 단말기 중 push id 등록된 단말기 리스트
     * @param enabled 앱에 등록된 단말기의 상태. 1: 활성, 2: 일시중지, 3: 차단
     * @param tid transaction id
     * @return ModelAndView object
     */
    @RequestMapping(value = "/{app_srl}/device/list/rpi/{reg_push_id}/eald/{enabled}/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listDeviceInApp(HttpServletRequest request,
                                        @PathVariable("app_srl") int appSrl,
                                        @PathVariable("reg_push_id") int regPushId,
                                        @PathVariable("enabled") int enabled,
                                        @PathVariable("tid") String tid) {
        // 어떤 단말기 대상으로 하는 앱인지 판단
        AppEntity appEntity = appService.getAppInfo(appSrl);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "failed list device in app. not found app. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("list_app_device_error", reason);
        }

        if(enabled != MDV.YES && enabled != MDV.DENY && enabled != MDV.NO) enabled = MDV.YES;

        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_app_device_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("app_srl", Integer.toString(appSrl));
        searchFilter.put("enabled", Integer.toString(enabled));
        if(regPushId == MDV.YES)    searchFilter.put("reg_push_id", Integer.toString(regPushId));
        else                        regPushId = MDV.NUSE;

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = appService.getAppDeviceCount(appSrl, null, null, null, null, null, MDV.NUSE, regPushId, MDV.NUSE, enabled);
        long filterRows = appService.getAppDeviceCount(searchFilter);
        List<DeviceEntity> deviceEntities = appService.getAppDevice(searchFilter, sortValue, offset, limit);

        List<Map<String, Object>> table = new ArrayList<>();
        assert deviceEntities != null;
        for(DeviceEntity deviceEntity : deviceEntities) {
            Map<String, Object> row = new HashMap<>();

            row.put("device_srl", deviceEntity.getDevice_srl());
            row.put("device_id", deviceEntity.getDevice_id());
            row.put("device_class", deviceEntity.getDevice_class());

            // 상태에 따라서 push 여부를 구한다.
            if(deviceEntity.getAppDeviceEntity().getEnabled() == MDV.YES &&
                    deviceEntity.getAppDeviceEntity().getReg_push_id() == MDV.YES &&
                    deviceEntity.getAppDeviceEntity().getAllow_push() == MDV.YES)
                row.put("push_ok", MDV.YES);
            else
                row.put("push_ok", MDV.NO);

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
     * jquery select2 를 위한 앱에 등록되어 있는 단말기 아이디 리스트를 구한다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param regPushId push id 등록 여부
     * @param enabled 앱에 등록된 단말 활성화 여부
     * @param tid transaction id
     * @param query 검색키
     * @param offset list offset
     * @param limit list limit
     * @return model and view object
     */
    @RequestMapping(value = "/{app_srl}/device/select2/list/rpi/{reg_push_id}/eald/{enabled}/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listDeviceInAppForSelect2(@PathVariable("app_srl") int appSrl,
                                                  @PathVariable("reg_push_id") int regPushId,
                                                  @PathVariable("enabled") int enabled,
                                                  @PathVariable("tid") String tid,
                                                  @RequestParam("query") String query,
                                                  @RequestParam("offset") int offset,
                                                  @RequestParam("limit") int limit) {
        // 어떤 단말기 대상으로 하는 앱인지 판단
        AppEntity appEntity = appService.getAppInfo(appSrl);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "failed list device in app. not found app. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("list_app_device_error", reason);
        }

        if(enabled != MDV.YES && enabled != MDV.DENY && enabled != MDV.NO) enabled = MDV.YES;

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

        long totalRows;
        List<DeviceEntity> deviceEntities;

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("device_id", "asc");
        sortValue.put("device_srl", "desc");

        totalRows = appService.getAppDeviceCount(appSrl, null, query, null, null, null, MDV.NUSE, regPushId,
                MDV.NUSE, enabled);
        deviceEntities = appService.getAppDevice(appSrl, null, MDV.NUSE, query, null, null, null, MDV.NUSE, regPushId,
                MDV.NUSE, enabled, sortValue, offset, limit);

        for(DeviceEntity deviceEntity : deviceEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("device_srl", deviceEntity.getDevice_srl());
            map.put("device_id", deviceEntity.getDevice_id());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject("list", list);

        return mav;
    }

    /**
     * 앱에 단말기 임의 등록하는 폼을 보여 준다.(앱과 단말기의 매핑 폼)
     *
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/device/add", method = RequestMethod.GET)
    public String addAppDeviceForm(Model model) {
        DeviceEntity deviceEntity = new DeviceEntity();
        model.addAttribute("deviceEntity", deviceEntity);

        // 지원하는 단말기 타입을 넣는다.
        String[] arrSupportTerminal = StringUtils.split(confCommon.getProperty("user_terminal_support"), ",");
        Map<String, String> supportTerminal = new HashMap<>();

        assert arrSupportTerminal != null;
        for(String element : arrSupportTerminal) {
            String[] values = StringUtils.split(element, ":");

            if(values.length < 2) {
                LOG.warn("invalid support terminal define. value["+element+"]");
                continue;
            }

            supportTerminal.put(StringUtils.trim(values[0]), StringUtils.trim(values[1]));
        }
        model.addAttribute("supportTerminal", supportTerminal);
        model.addAttribute("mdv_yes", MDV.YES);

        return "f_admin/app/add_device";
    }

    /**
     * 앱 별 단말기를 등록 한다.
     *
     * @param tid transaction id
     * @param deviceEntity 등록할 단말 정보
     * @param bindingResult binding object
     * @return ModelAndView object
     */
    @RequestMapping(value = "/device/add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addAppDeviceSubmit(@PathVariable("tid") String tid,
                                           @Valid @RequestBody DeviceEntity deviceEntity,
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
            throw new CustomException("add_device_in_app_error", reason);
        }

        if(deviceEntity.getApp_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", messageSource.getMessage("valid.app_srl", null,
                    LocaleContextHolder.getLocale()));
            throw new CustomException("add_device_in_app_error", reason);
        }

        appService.joinDeviceInApp(deviceEntity, deviceEntity.getApp_srl(), deviceEntity.getPush_id());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("app_srl", deviceEntity.getApp_srl());
        mav.addObject("device_srl", deviceEntity.getDevice_srl());
        mav.addObject("device_class", deviceEntity.getDevice_class());
        mav.addObject("push_id", deviceEntity.getPush_id());

        return mav;
    }

    /**
     * 단말 리스트를 보여 주는 폼을 출력 한다.
     *
     * @param model model object
     * @return view name
     */
    @RequestMapping(value = "/device/list", method = RequestMethod.GET)
    public String listDevice(Model model) {
        model.addAttribute("mdv_nuse", MDV.NUSE);
        model.addAttribute("android_device_class",
                Integer.parseInt(confCommon.getProperty("user_terminal_android"), 10));
        model.addAttribute("ios_device_class",
                Integer.parseInt(confCommon.getProperty("user_terminal_iphone"), 10));

        return "f_admin/app/list_device";
    }

    /**
     * 단말 리스트 데이터를 구한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return 단말 리스트 데이터
     */
    @RequestMapping(value = "/device/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listDevice(HttpServletRequest request,
                                   @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_app_device_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        if(searchFilter.containsKey("app_name")) {
            String appName = StringUtils.trim(searchFilter.get("app_name"));
            if(!StringUtils.equals(appName, "")) {
                List<AppEntity> appEntities = appService.getAppInfo(appName, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
                List<Integer> searchAppSrls = new ArrayList<>();

                for(AppEntity appEntity : appEntities) searchAppSrls.add(appEntity.getApp_srl());

                searchFilter.put("app_srls", StringUtils.join(searchAppSrls, ","));
            }
        }

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = appService.getAppDeviceCount(MDV.NUSE, null, null, null, null, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE);
        long filterRows = appService.getAppDeviceCount(searchFilter);
        List<DeviceEntity> deviceEntities = appService.getAppDevice(searchFilter, sortValue, offset, limit);

        List<Integer> appSrls = new ArrayList<>();
        for(DeviceEntity deviceEntity : deviceEntities) appSrls.add(deviceEntity.getAppDeviceEntity().getApp_srl());

        Map<Integer, AppEntity> appEntities = appService.getAppInfo(appSrls, MDV.NUSE, MDV.NUSE, MDV.NUSE);

        List<Map<String, Object>> table = new ArrayList<>();
        for(DeviceEntity deviceEntity : deviceEntities) {
            Map<String, Object> row = new HashMap<>();

            row.put("device_srl", deviceEntity.getDevice_srl());
            row.put("app_srl", deviceEntity.getAppDeviceEntity().getApp_srl());
            row.put("device_id", deviceEntity.getDevice_id());
            row.put("device_type", deviceEntity.getDevice_type());
            row.put("device_class", deviceEntity.getDevice_class());
            row.put("os_version", deviceEntity.getOs_version());
            row.put("mobile_phone_number", deviceEntity.getMobile_phone_number());

            int appSrl = deviceEntity.getAppDeviceEntity().getApp_srl();
            if(appEntities.containsKey(appSrl)) row.put("app_name", appEntities.get(appSrl).getApp_name());
            else                                row.put("app_name", "");

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
     * 앱에 매핑되어 있는 단말기를 삭제 한다.
     *
     * @param tid transaction id
     * @param keyBean 삭제할 단말 정보
     * @return request 데이터를 그대로 리턴한다.
     */
    @RequestMapping(value = "/device/t/{tid}", method = RequestMethod.DELETE)
    public ModelAndView deleteAppDevice(@PathVariable("tid") String tid,
                                        @RequestBody KeyBean keyBean) {
        if(keyBean.getC_keys() == null || keyBean.getC_keys().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("c_keys", "failed delete app device. c_keys is null or empty");
            LOG.error(reason.get("c_keys"));
            throw new CustomException("delete_device_in_app_error", reason);
        }

        appService.deleteDeviceInApp(keyBean.getC_keys());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");
        mav.addObject("device", keyBean.getC_keys());

        return mav;
    }

    /**
     * 앱에 등록되어 있는 단말기 정보를 보여 준다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param deviceSrl 단말기 시리얼 넘버
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/{app_srl}/device/{device_srl}", method = RequestMethod.GET)
    public String detailAppDevice(@PathVariable("app_srl") int appSrl,
                                  @PathVariable("device_srl") long deviceSrl,
                                  Model model) {
        List<DeviceEntity> deviceEntities = appService.getAppDevice(appSrl, null, deviceSrl, null, null, null,
                null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        if(deviceEntities.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_device", "failed detail app device. not found app device. app_srl [" + appSrl + "], device_srl [" + deviceSrl + "]");
            LOG.error(reason.get("no_device"));
            throw new CustomException("read_app_error", reason);
        }
        model.addAttribute("deviceEntity", deviceEntities.get(0));

        AppEntity appEntity = appService.getAppInfo(appSrl);
        model.addAttribute("appEntity", appEntity);

        model.addAttribute("android_device_class",
                Integer.parseInt(confCommon.getProperty("user_terminal_android"), 10));
        model.addAttribute("ios_device_class",
                Integer.parseInt(confCommon.getProperty("user_terminal_iphone"), 10));

        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_deny", MDV.DENY);

        return "f_admin/app/detail_app_device";
    }

    /**
     * 단말 정보를 수정 한다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param deviceSrl 단말기 시리얼 넘버
     * @param tid transaction id
     * @param keyBean 변경 할 정보
     * @return model and view object
     */
    @RequestMapping(value = "/{app_srl}/device/{device_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyAppDevice(@PathVariable("app_srl") int appSrl,
                                        @PathVariable("device_srl") long deviceSrl,
                                        @PathVariable("tid") String tid,
                                        @RequestBody KeyBean keyBean) {
        if(keyBean.getM_key() == null || keyBean.getM_key().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("m_key", "failed modify app device. m_key is null or empty");
            LOG.error(reason.get("m_key"));
            throw new CustomException("modify_device_in_app_error", reason);
        }

        Map<String, String> deviceData = new HashMap<>();
        Set<String> keys = keyBean.getM_key().keySet();
        for(String key : keys) deviceData.put(key, keyBean.getM_key().get(key).toString());

        deviceService.modifyDevice(deviceSrl, appSrl, deviceData);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

}

package com.ckstack.ckpush.controller.api.nosubscribe;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.data.request.RegDeviceBean;
import com.ckstack.ckpush.domain.accessory.DeviceAccessTokenEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.user.AuthService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 8. 21..
 * 비가입형 서비스 인증 필요 없는 api
 */
@Controller
@RequestMapping("/dapi/open")
public class DApiOpenController {
    private final static Logger LOG = LoggerFactory.getLogger(DApiOpenController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private AuthService authService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;

    /**
     * exception 방식으로 처리 하지 못하는 에러에 대해 에러 결과를 강제로 보여 준다.
     *
     * @param request HttpServletRequest object
     * @return Model and View object
     */
    @RequestMapping(value = "/show/error")
    public ModelAndView showError(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        String tid = (String) request.getAttribute(confCommon.getProperty("json_tid"));
        String errCode = (String) request.getAttribute("error_code");

        webUtilService.setCommonApiResponse(mav, tid, errCode);

        mav.addObject(confCommon.getProperty("json_method"),
                request.getAttribute(confCommon.getProperty("json_method")));
        mav.addObject(confCommon.getProperty("json_request_uri"),
                request.getAttribute(confCommon.getProperty("json_request_uri")));

        Object reason = request.getAttribute(confCommon.getProperty("json_reason"));

        if(reason == null)
            mav.addObject(confCommon.getProperty("json_reason"), new HashMap<String, String>());
        else
            mav.addObject(confCommon.getProperty("json_reason"), reason);

        return mav;
    }

    /**
     * 회원 관리 모듈을 사용하지 않고 단말기 아이디로 관리를 할때 단말 등록/인증키를 발급 한다.
     *
     * @param tid transaction id
     * @param apiKey 단말을 등록할 앱의 api key
     * @param apiSecret 단말을 등록할 앱의 api secret
     * @param regDeviceBean 등록할 단말의 정보
     * @param bindingResult bindingResult object
     * @return 단말 등록/인증키 발급 결과
     */
    @RequestMapping(value = "/reg/auth/device/t/{tid}", method = RequestMethod.POST)
    public ModelAndView registDevice(HttpServletResponse response,
                                     @PathVariable("tid") String tid,
                                     @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
                                     @RequestHeader(value = "X-Api-Secret", required = false) String apiSecret,
                                     @Valid @RequestBody RegDeviceBean regDeviceBean,
                                     BindingResult bindingResult) {
        if(apiKey == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H1)");
            LOG.error(reason.get("invalid") + " not found X-Api-Key header. apiKey is null.");
            throw new CustomException("add_device_in_app_error", reason);
        }

        apiKey = StringUtils.trim(apiKey);
        if(StringUtils.equals(apiKey, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H2)");
            LOG.error(reason.get("invalid") + " invalid X-Api-Key header. apiKey is empty string.");
            throw new CustomException("add_device_in_app_error", reason);
        }

        if(apiSecret == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H3)");
            LOG.error(reason.get("invalid") + " not found X-Api-Secret header. apiSecret is null.");
            throw new CustomException("add_device_in_app_error", reason);
        }

        apiSecret = StringUtils.trim(apiSecret);
        if(StringUtils.equals(apiSecret, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H4)");
            LOG.error(reason.get("invalid") + " invalid X-Api-Secret header. apiSecret is empty string.");
            throw new CustomException("add_device_in_app_error", reason);
        }

        if(bindingResult.hasErrors()) {
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

        // 단말기를 매핑 시키려는 앱 정보를 찾는다.
        AppEntity appEntity = appService.getAppInfo(apiKey);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H5)");
            LOG.error(reason.get("invalid") + " not found app by api_key [" + apiKey + "]");
            throw new CustomException("add_device_in_app_error", reason);
        }

        if(!StringUtils.equals(apiSecret, appEntity.getApi_secret())) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H6)");
            LOG.error(reason.get("invalid") + " invalid api_secret [" + apiSecret + "] by api_key [" + apiKey + "]");
            throw new CustomException("add_device_in_app_error", reason);
        }

        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.init();
        deviceEntity.setDevice_id(regDeviceBean.getDevice_id());
        deviceEntity.setDevice_type(regDeviceBean.getModel());
        deviceEntity.setDevice_class(regDeviceBean.getType());
        deviceEntity.setOs_version(regDeviceBean.getOs_version());
        deviceEntity.setMobile_phone_number(regDeviceBean.getMobile_phone_number());

        // 단말기를 등록 시키고, 앱과 매핑 시킨다.
        appService.joinDeviceInApp(deviceEntity, appEntity.getApp_srl(), regDeviceBean.getPush_id());

        // 신규 토큰을 발급하거나 연장 한다.
        DeviceAccessTokenEntity deviceAccessTokenEntity = authService.createAccessTokenUsingDevice(
                deviceEntity.getDevice_srl(), appEntity.getApp_srl());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        response.setHeader(confCommon.getProperty("access_token_header_name"),
                deviceAccessTokenEntity.getAccess_token());
        response.setHeader(confCommon.getProperty("access_token_alive_header_name"),
                confCommon.getProperty("access_token_valid_sec"));

        return mav;
    }
}

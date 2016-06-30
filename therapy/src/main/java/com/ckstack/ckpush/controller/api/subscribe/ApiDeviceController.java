package com.ckstack.ckpush.controller.api.subscribe;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.data.cache.AccessTokenExtra;
import com.ckstack.ckpush.data.request.RegDeviceBean;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.user.MemberDeviceEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.user.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 8. 21..
 * 비가입자형에서 push 메시지 관련 api
 */
@Controller
@RequestMapping("/api/device")
public class ApiDeviceController {
    private final static Logger LOG = LoggerFactory.getLogger(ApiDeviceController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;

    @RequestMapping(value = "/reg/t/{tid}", method = RequestMethod.POST)
    public ModelAndView registDevice(HttpServletRequest request,
                                     @PathVariable("tid") String tid,
                                     @Valid @RequestBody RegDeviceBean regDeviceBean,
                                     BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> reason = new HashMap<>();

            for(FieldError fieldError : fieldErrors) {
                reason.put(fieldError.getField(), fieldError.getDefaultMessage());
                LOG.warn("invalid field. field name["+fieldError.getField()+
                        "], message["+fieldError.getDefaultMessage()+"]");
            }

            // NOTE 400 을 리턴 시키면 jQuery 에서 cors 가 발생 한다.
            throw new CustomException("reg_member_device_error", reason);
        }

        // 접속 토큰 정보를 구한다.
        AccessTokenExtra accessTokenExtra = (AccessTokenExtra) request.getAttribute(
                confCommon.getProperty("access_token_info"));

        // 단말기를 등록 시키고, 앱과 매핑 시킨다.
        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.init();
        deviceEntity.setDevice_id(regDeviceBean.getDevice_id());
        deviceEntity.setDevice_type(regDeviceBean.getModel());
        deviceEntity.setDevice_class(regDeviceBean.getType());
        deviceEntity.setOs_version(regDeviceBean.getOs_version());
        deviceEntity.setMobile_phone_number(regDeviceBean.getMobile_phone_number());

        appService.joinDeviceInApp(deviceEntity, accessTokenExtra.getApp_srl(), regDeviceBean.getPush_id());

        // 사용자와 단말을 매핑 시킨다.
        MemberDeviceEntity memberDeviceEntity = new MemberDeviceEntity();
        memberDeviceEntity.setApp_srl(accessTokenExtra.getApp_srl());
        memberDeviceEntity.setMember_srl(accessTokenExtra.getMember_srl());
        memberDeviceEntity.setDevice_srl(deviceEntity.getDevice_srl());
        memberDeviceEntity.setMobile_phone_number(regDeviceBean.getMobile_phone_number());

        memberService.addMemberDevice(memberDeviceEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }
}

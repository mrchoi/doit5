package com.ckstack.ckpush.controller.api.nosubscribe;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.domain.accessory.DeviceAccessTokenEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.push.PushMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 7. 13..
 */
@Controller
@RequestMapping("/dapi/push")
public class DApiPushController {
    private final static Logger LOG = LoggerFactory.getLogger(DApiPushController.class);

    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private PushMessageService pushMessageService;
    @Autowired
    protected Properties confCommon;

    /**
     * Push ID를 추가/수정 한다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param pushID 수정할 push id(Android 는 GCM RID, iOS는 device token)
     * @return model and view
     */
    @RequestMapping(value = "/reg/psid/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView upsertPushID(HttpServletRequest request,
                                     @PathVariable("tid") String tid,
                                     @RequestBody Map<String, String> pushID) {
        if(!pushID.containsKey(confCommon.getProperty("json_push_id"))) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H1)");
            LOG.error(reason.get("invalid") + " no push_id in request body.");
            throw new CustomException("add_push_id_error", reason);
        }

        DeviceAccessTokenEntity deviceAccessTokenEntity = (DeviceAccessTokenEntity) request.getAttribute(
                confCommon.getProperty("access_token_info"));

        pushMessageService.modifyGCMRid(deviceAccessTokenEntity.getApp_srl(),
                deviceAccessTokenEntity.getDevice_srl(), pushID.get(confCommon.getProperty("json_push_id")));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

}

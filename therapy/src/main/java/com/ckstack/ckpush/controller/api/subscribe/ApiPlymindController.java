package com.ckstack.ckpush.controller.api.subscribe;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.data.cache.AccessTokenExtra;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.push.NotificationEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.push.PlymindMessageService;
import com.ckstack.ckpush.service.user.MemberService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kodaji on 16. 3. 6.
 */
@Controller
@RequestMapping("/api")
public class ApiPlymindController {
    private final static Logger LOG = LoggerFactory.getLogger(ApiPlymindController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    private PlymindMessageService plymindMessageService;

    private final long ONE_DAY = 1 * 24 * 60 * 60 * 1000L;

    /**
     * 사용자 알림 정보를 수정 한다
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param keyBean 수정할 정보
     * @return model and view
     */
    @RequestMapping(value = "/member/noti/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView modifyMemberNoti(HttpServletRequest request,
                                         @PathVariable("tid") String tid,
                                         @RequestBody KeyBean keyBean) {
        // 접속 토큰 정보를 구한다.
        AccessTokenExtra accessTokenExtra = (AccessTokenExtra) request.getAttribute(
                confCommon.getProperty("access_token_info"));
        if(keyBean.getM_key() == null || keyBean.getM_key().size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H1)");
            LOG.error(reason.get("invalid") + " no modify value. m_key [" + keyBean.getM_key() + "]");
            throw new CustomException("modify_member_error", reason);
        }

        int allowCall = MDV.NUSE;

        if(keyBean.getM_key().containsKey("call")) {
            try {
                boolean val = (boolean) keyBean.getM_key().get("call");
                allowCall = (val ? MDV.YES : MDV.NO);
            } catch(Exception e) {
                LOG.warn("call value is not boolean. ignore it");
            }
        }

        if(allowCall == MDV.NUSE) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H2)");
            LOG.error(reason.get("invalid") + " no modify value. value is not boolean. m_key [" +
                    keyBean.getM_key() + "]");
            throw new CustomException("modify_member_error", reason);
        }

        memberService.modifyMemberNotification(accessTokenExtra.getMember_srl(), allowCall);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        MemberEntity memberEntity = memberService.getMemberInfo(accessTokenExtra.getMember_srl());

        Map<String, Boolean> notiSetup = new HashMap<>();
        notiSetup.put("call", memberEntity.getMemberExtraEntity().getAllow_call() == MDV.YES);

        mav.addObject("noti", notiSetup);

        return mav;
    }

    /**
     * Push Message 목록을 조회 한다.
     *
     * @param request HttpServletRequest object
     * @param notiType 조회할 push message type
     * @param tid transaction id
     * @return model and view object
     */
    @RequestMapping(value = "/push/list/{notiType}/{tid}", method = RequestMethod.GET)
    public ModelAndView getDocument(HttpServletRequest request,
                                    @PathVariable("notiType") int notiType,
                                    @PathVariable("tid") String tid) {
        // 접속 토큰 정보를 구한다.
        AccessTokenExtra accessTokenExtra = (AccessTokenExtra) request.getAttribute(
                confCommon.getProperty("access_token_info"));

        long memberSrl = MDV.NUSE;
        if (notiType > 2) memberSrl = accessTokenExtra.getMember_srl();

        // 1. 발송요청(발송전), 2.발송완료.
        int pushStatus = 2;

        // 발송일 기준 3달전 (3 month ago)
        int notiTime = (int)((System.currentTimeMillis() - (90 * ONE_DAY))/1000);

        List<NotificationEntity> notificationEntities = plymindMessageService.notificationList(notiType, memberSrl, null, pushStatus, MDV.NUSE, notiTime);
        List<Map<String, Object>> list = new ArrayList<>();

        for (NotificationEntity notificationEntity : notificationEntities) {
            Map<String, Object> map = plymindMessageService.getContent(notificationEntity);
            list.add(map);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("notification", list);

        return mav;
    }
}

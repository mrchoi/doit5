package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.domain.push.NotificationEntity;
import com.ckstack.ckpush.service.push.PlymindMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kodaji on 16. 2. 21.
 */
@Controller
@RequestMapping("/push/notification")
public class NotificationController {
    private final static Logger LOG = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private PlymindMessageService plymindMessageService;

    /**
     * push message 테스트용 URL
     * @param request
     * @param notiType
     * @return
     */
    @RequestMapping(value = "/add/{notiType}")
    @ResponseBody
    public String addNotification(HttpServletRequest request,
                            @PathVariable("notiType") int notiType) {

        if (notiType < 1 || notiType > 5) return "Request NOTI_TYPE [" + notiType + "]. NOTI_TYPE is 1-5.\n" ;

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = simple.format(date);

        NotificationEntity notificationEntity = new NotificationEntity();

        notificationEntity.setNoti_type(notiType);
        notificationEntity.setPush_status(1);
        notificationEntity.setBook_time((int) (System.currentTimeMillis()/1000L));

        switch(notiType) {
            case 1 : // 너나들이
                notificationEntity.setPush_text("[PlyMind] 너나들이 메시지 테스트 (" + strDate + ")");
                break;
            case 2 : // 공지사항
                notificationEntity.setPush_text("[PlyMind] 공지사항 입니다. (" + strDate + ")");
                break;
            case 3 : // 그룹테라피
                notificationEntity.setPush_text("[PlyMind] 커플싸이케어테라피 메시지 테스트 (" + strDate + ")");
                notificationEntity.setMember_srl(12);
                notificationEntity.setUser_id("user");
                break;
            case 4 : // 개인테라피
                notificationEntity.setPush_text("[PlyMind] 컨텐츠, 텍스트 케어테라피 메시지 (" + strDate + ")");
                notificationEntity.setMember_srl(12);
                notificationEntity.setUser_id("user");
                break;
            case 5 : // 실시간
                notificationEntity.setNoti_type(4);
                notificationEntity.setPush_status(2);
                notificationEntity.setPush_text("[PlyMind] 실시간 메시지 전송 (" + strDate + ")");
                notificationEntity.setMember_srl(12);
                notificationEntity.setUser_id("user");
                break;
        }

        plymindMessageService.add(notificationEntity);

        if (notiType == 5) {
            plymindMessageService.pushMessage(notificationEntity);
        }

        return "";
    }
}

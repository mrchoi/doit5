package com.ckstack.ckpush.service.push.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.push.NotificationDao;
import com.ckstack.ckpush.data.ras.RasEventBean;
import com.ckstack.ckpush.domain.push.NotificationEntity;
import com.ckstack.ckpush.service.plymind.ApplicationService;
import com.ckstack.ckpush.service.push.PlymindMessageService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kodaji on 16. 2. 22.
 */
@Service
@Transactional(value = "transactionManager")
public class PlymindMessageServiceImpl implements PlymindMessageService {
    private final static Logger LOG = LoggerFactory.getLogger(PlymindMessageServiceImpl.class);

    @Autowired
    private Properties confPlymind;
    @Autowired
    private NotificationDao notificationDao;
    @Resource(name = "redisGcmApnsTemplate")
    private RedisTemplate<String, Object> redisGcmApnsTemplate;
    @Autowired
    private ApplicationService applicationService;

    private int updateNotification(NotificationEntity notificationEntity) {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simple  = new SimpleDateFormat("yyyyMMddHHmmss");
        int notiTime1 = (int) (date.getTime()/1000L);
        String notiTime2 = simple.format(date);

        notificationEntity.setPush_status(2);
        notificationEntity.setNoti_time1(notiTime1);
        notificationEntity.setNoti_time2(notiTime2);

        return notificationDao.modify(notificationEntity, notificationEntity.getNoti_srl());
    }

    private void eventSend(RasEventBean rasEventBean) {
        redisGcmApnsTemplate.setKeySerializer(new StringRedisSerializer());
        redisGcmApnsTemplate.setValueSerializer(new JacksonJsonRedisSerializer<>(RasEventBean.class));
        redisGcmApnsTemplate.opsForList().rightPush(confPlymind.getProperty("ras_event_queue"), rasEventBean);

        LOG.info("push subscription(data) send message data to ras event queue. rasEventBean [" + rasEventBean.toString() + "]");
    }

    @Override
    public int add(NotificationEntity notificationEntity) {
        if(notificationEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("notificationEntity", "invalid notificationEntity is null");
            LOG.error(reason.get("notificationEntity"));
            throw new CustomException("add_notification_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }
        // push_text 체크
        if(notificationEntity.getPush_text() == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("push_text", "invalid push_text. category_srl is null");
            LOG.error(reason.get("push_text"));
            throw new CustomException("add_notification_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }
        // book_time 체크
        if(notificationEntity.getBook_time() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("book_time", "invalid book_time. book_time is less then zero");
            LOG.error(reason.get("book_time"));
            throw new CustomException("add_notification_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        notificationEntity.setC_date(ltm);

        return notificationDao.add(notificationEntity);
    }

    @Override
    public long count(long applicationGroup, long applicationSrl, int notiType,
                      long memberSrl, String userId, int pushStatus, int bookTime, int notiTime) {

        return notificationDao.count(applicationGroup, applicationSrl, notiType, memberSrl, userId, pushStatus, bookTime, notiTime);
    }

    @Override
    public List<NotificationEntity> get(long applicationGroup, long applicationSrl, int notiType,
                                        long memberSrl, String userId, int pushStatus, int bookTime, int notiTime,
                                        Map<String, String> sort, int offset, int limit) {
        return notificationDao.get(applicationGroup, applicationSrl, notiType, memberSrl, userId, pushStatus, bookTime, notiTime, sort, offset, limit);
    }

    @Override
    public long notificationCount(int notiType, long memberSrl, String userId, int pushStatus, int bookTime, int notiTime) {

        return this.count(MDV.NUSE, MDV.NUSE, notiType, memberSrl, userId, pushStatus, bookTime, notiTime);
    }

    @Override
    public List<NotificationEntity> notificationList(int notiType, long memberSrl, String userId, int pushStatus, int bookTime, int notiTime) {
        return this.get(MDV.NUSE, MDV.NUSE, notiType, memberSrl, userId, pushStatus, bookTime, notiTime, null, MDV.NUSE, MDV.NUSE);
    }

    @Override
    public Map<String, Object> getContent(Object object)
    {
        Map<String, Object> ud = new HashMap<>();
        try
        {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ud.put(field.getName(), (field.get(object) != null ? field.get(object).toString() : ""));
            }
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return ud;
    }

    @Override
    public void pushMessage(NotificationEntity notificationEntity) {

        if (notificationEntity == null) return;
        Map<String, Object> ud = this.getContent(notificationEntity);

        try {
            // 최종 push bean 을 만든다.
            RasEventBean rasEventBean = new RasEventBean();
            rasEventBean.setApp_id(confPlymind.getProperty("ras_gcm_apns_app_id"));
            rasEventBean.setPid(confPlymind.getProperty("ras_gcm_apns_push_pid"));
            rasEventBean.setTid(System.nanoTime() + "");
            rasEventBean.setCtime(DateTime.now().getMillis());
            rasEventBean.setUd(ud);

            switch (notificationEntity.getNoti_type()) {
                case 1: // 너나들이
                case 2: // 공지사항
                    rasEventBean.setEt(confPlymind.getProperty("ras_plymind_gcm_event_type"));  // android
                    this.eventSend(rasEventBean);

                    rasEventBean.setEt(confPlymind.getProperty("ras_plymind_apns_event_type")); // iOS
                    this.eventSend(rasEventBean);
                    break;
                case 3 : // 그룹테라피
                case 4 : // 개인테라피
                    rasEventBean.setEt(confPlymind.getProperty("ras_gcm_apns_event_type"));
                    this.eventSend(rasEventBean);

                    if (notificationEntity.getNoti_type() == 4) { // 개인테라피
                        applicationService.statusComplete(notificationEntity.getApplication_srl(), MDV.NUSE);
                    }
                    break;
            }

            this.updateNotification(notificationEntity);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public int updatePushText(long notiSrl, String pushText) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setPush_text(pushText);
        return notificationDao.modify(notificationEntity, notiSrl);
    }
}

package com.ckstack.ckpush.message.impl;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.plymind.ApplicationDao;
import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
import com.ckstack.ckpush.domain.push.NotificationEntity;
import com.ckstack.ckpush.message.PlymindMessage;
import com.ckstack.ckpush.service.push.PlymindMessageService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 16. 2. 22.
 */
@Repository
public class PlymindMessageImpl implements PlymindMessage {
    private final static Logger LOG = LoggerFactory.getLogger(PlymindMessageImpl.class);

    @Autowired
    private PlymindMessageService plymindMessageService;

    @Autowired
    private ApplicationDao applicationDao;

    @Scheduled(cron="00 * * * * *")
    public void sendList() {

        try {
            String username = System.getProperties().getProperty("user.name");
            String hostname = InetAddress.getLocalHost().getHostName();

            if (!(username.equals("root") && hostname.equals("engels")) &&
                !(username.equals("plymind") && hostname.equals("danbplus")) &&
                !(username.equals("plymind") && hostname.equals("doitfive-web"))) return;

            int currentTime = (int) (System.currentTimeMillis() / 1000L);
            long count = plymindMessageService.notificationCount(MDV.NUSE, MDV.NUSE, null, 1, currentTime, MDV.NUSE);
            if (count <= 0) {
                LOG.info("NOT FOUND PUSH Message !!!");
                return;
            }

            List<NotificationEntity> notificationEntities = plymindMessageService.notificationList(MDV.NUSE, MDV.NUSE, null, 1, currentTime, MDV.NUSE);
            for (NotificationEntity notificationEntity : notificationEntities) {
                plymindMessageService.pushMessage(notificationEntity);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Scheduled(cron="01 00 * * * *")
    public void applicationStatusChange() {

        try {
            int now_date = (int)(DateTime.now().getMillis() / 1000);

            List<Integer> application_statues = new ArrayList<Integer>();
            application_statues.add(MDV.APPLICATION_STATUS_READY);// 준비중
            application_statues.add(MDV.APPLICATION_STATUS_PROGRESS);// 진행중

            List<Long> contents_srls = new ArrayList<Long>();
            contents_srls.add(1L);// 싸이케어테라피
            contents_srls.add(2L);// 커플싸이테라피

            List<ApplicationEntity> applicationEntities = applicationDao.getApplicationList(MDV.NUSE, MDV.NUSE, application_statues, contents_srls, null, null, MDV.NUSE, MDV.NUSE);

            for(ApplicationEntity applicationEntity : applicationEntities) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Date s_date = dateFormat.parse(applicationEntity.getStart_date());
                Date e_date = dateFormat.parse(applicationEntity.getEnd_date());

                int start_date = (int)(s_date.getTime()/1000L);
                int end_date = (int)(e_date.getTime()/1000L);

                /* 준비중 => 진행중 변경 */
                if(applicationEntity.getApplication_status() == MDV.APPLICATION_STATUS_READY && start_date < now_date &&  now_date < end_date) {
                    applicationDao.statusModify(applicationEntity.getApplication_srl(), MDV.NUSE, MDV.APPLICATION_STATUS_PROGRESS);
                }

                /* 진행중 => 완료 변경 */
                if(applicationEntity.getApplication_status() == MDV.APPLICATION_STATUS_PROGRESS && end_date < now_date) {
                    applicationDao.statusModify(applicationEntity.getApplication_srl(), MDV.NUSE, MDV.APPLICATION_STATUS_COMPLETE);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}

package com.ckstack.ckpush.service.push.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.app.AppDeviceDao;
import com.ckstack.ckpush.dao.accessory.DeviceDao;
import com.ckstack.ckpush.dao.mineral.FileDao;
import com.ckstack.ckpush.dao.mineral.PushMessagePicDao;
import com.ckstack.ckpush.dao.push.PushMessageDao;
import com.ckstack.ckpush.data.ras.RasEventBean;
import com.ckstack.ckpush.domain.app.AppDeviceEntity;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.mineral.FileEntity;
import com.ckstack.ckpush.domain.mineral.PushMessagePicEntity;
import com.ckstack.ckpush.domain.push.PushMessageEntity;
import com.ckstack.ckpush.message.GcmApns;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.mineral.CkFileService;
import com.ckstack.ckpush.service.push.PushMessageService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by dhkim94 on 15. 5. 11..
 */
@Service
@Transactional(value = "transactionManager")
public class PushMessageServiceImpl implements PushMessageService {
    private final static Logger LOG = LoggerFactory.getLogger(PushMessageServiceImpl.class);

    @Autowired
    private AppDao appDao;
    @Autowired
    private AppDeviceDao appDeviceDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private PushMessageDao pushMessageDao;
    @Autowired
    private PushMessagePicDao pushMessagePicDao;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private GcmApns gcmApns;
    @Autowired
    private CkFileService ckFileService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confTblFile;
    @Autowired
    protected Properties confAccessory;
    @Autowired
    protected Properties confCommon;

    /**
     * Push 메시지에서 외부 이미지를 사용 할때, 외부 이미지를 내부 파일 관리 테이블에 등록 시키고
     * push 메시지와 매핑 시킨다.
     *
     * @param imageURL 등록 시킬 외부 이미지의 URL
     */
    private void addPushMessageOuterPicAndMapping(long pushSrl, String imageURL, String requestIP) {

        // 파일 다운로드
        byte[] fileData = webUtilService.serverToServerDownload(imageURL);
        if(fileData == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("image_url", "invalid image url [" + imageURL + "]");
            throw new CustomException("save_push_gcm_apns_image_error", reason);
        }

        FileEntity fileEntity = ckFileService.savePushImage(imageURL, fileData, requestIP, false);

        PushMessagePicEntity pushMessagePicEntity = new PushMessagePicEntity();
        pushMessagePicEntity.setFile_srl(fileEntity.getFile_srl());
        pushMessagePicEntity.setPush_srl(pushSrl);
        pushMessagePicEntity.setC_date(fileEntity.getC_date());

        pushMessagePicDao.add(pushMessagePicEntity);
        LOG.info("mapping between push message and and outer pic. push_srl [" +
                pushSrl + "], file_srl [" + fileEntity.getFile_srl() + "]");
    }

    @Override
    public void pushGcmApns(PushMessageEntity pushMessageEntity, String requestIP) {
        if(pushMessageEntity == null) {
            LOG.error("invalid pushMessageEntity is null");
            Map<String, String> reason = new HashMap<>();
            reason.put("pushMessageEntity", "pushMessageEntity is null");
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(pushMessageEntity.getApp_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "failed push gcm. app_srl is less then zero");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("push_gcm_apns_error", reason);
        }

        AppEntity appEntity = appDao.get(pushMessageEntity.getApp_srl(), null, null);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "not found app. app_srl ["+pushMessageEntity.getApp_srl()+"]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("push_gcm_apns_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(pushMessageEntity.getUser_id() == null ||
                StringUtils.equals(StringUtils.trim(pushMessageEntity.getUser_id()), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "failed push gcm. user_id is null or empty string");
            LOG.error(reason.get("user_id"));
            throw new CustomException("push_gcm_apns_error", reason);
        }

        if(pushMessageEntity.getPush_title() == null ||
                StringUtils.equals(StringUtils.trim(pushMessageEntity.getPush_title()), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("push_title", "failed push gcm. push_title is null or empty string");
            LOG.error(reason.get("push_title"));
            throw new CustomException("push_gcm_apns_error", reason);
        }

        if(pushMessageEntity.getPush_text() == null ||
                StringUtils.equals(StringUtils.trim(pushMessageEntity.getPush_text()), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("push_text", "failed push gcm. push_text is null or empty string");
            LOG.error(reason.get("push_text"));
            throw new CustomException("push_gcm_apns_error", reason);
        }

        if(pushMessageEntity.getCallback_url() == null) pushMessageEntity.setCallback_url("");
        pushMessageEntity.setCallback_url(StringUtils.trim(pushMessageEntity.getCallback_url()));

        if(pushMessageEntity.getImage_url() == null) pushMessageEntity.setImage_url("");
        pushMessageEntity.setImage_url(StringUtils.trim(pushMessageEntity.getImage_url()));

        // NOTE 타겟이 추가 되면(그룹핑 추가) 제대로 값을 사용하는지 확인 해야 한다.

        if(pushMessageEntity.getPush_target() == null) pushMessageEntity.setPush_target("");

        String targetUnit = confCommon.getProperty("push_target_unit_in_app");
        if(StringUtils.equals(pushMessageEntity.getPush_target(), targetUnit)) {
            pushMessageEntity.setTotal_count(1);
            pushMessageEntity.setTotal_real_count(1);
        } else {
            pushMessageEntity.setTotal_count(0);
            pushMessageEntity.setTotal_real_count(0);
        }

        pushMessageEntity.setSuccess_count(0);
        pushMessageEntity.setFail_count(0);
        pushMessageEntity.setFail_real_count(0);
        pushMessageEntity.setReceived_count(0);
        pushMessageEntity.setConfirm_count(0);

        int ltm = (int) (DateTime.now().getMillis() / 1000);
        pushMessageEntity.setC_date(ltm);
        pushMessageEntity.setU_date(ltm);

        // push 메시지 이력 추가
        pushMessageDao.add(pushMessageEntity);
        LOG.info("add push message. pushMessageEntity [" + pushMessageEntity.toString() + "]");

        // push 메시지 카운트 증가
        appDao.increase(pushMessageEntity.getApp_srl(), null, false, false, true);
        LOG.info("increase push message history count in app. app_srl [" + pushMessageEntity.getApp_srl() + "]");

        // 사용하는 이미지가 존재하면 이미지 매핑
        if(!StringUtils.equals(pushMessageEntity.getImage_url(), "")) {
            if (pushMessageEntity.getFile_srl() > 0) {
                FileEntity fileEntity = fileDao.get(confTblFile.getProperty("tbl_name_push_file"),
                        pushMessageEntity.getFile_srl(), MDV.NUSE);
                if (fileEntity != null) {
                    FileEntity modifyVo = new FileEntity();
                    modifyVo.init();
                    modifyVo.setDeleted(MDV.NO);

                    fileDao.modify(confTblFile.getProperty("tbl_name_push_file"), modifyVo,
                            fileEntity.getFile_srl(), null);

                    // 내부 이미지 매핑
                    PushMessagePicEntity pushMessagePicEntity = new PushMessagePicEntity();
                    pushMessagePicEntity.setFile_srl(pushMessageEntity.getFile_srl());
                    pushMessagePicEntity.setPush_srl(pushMessageEntity.getPush_srl());
                    pushMessagePicEntity.setC_date(ltm);

                    pushMessagePicDao.add(pushMessagePicEntity);
                    LOG.info("mapping between push message and and pic. app_srl [" +
                            pushMessageEntity.getApp_srl() + "], push_srl [" +
                            pushMessageEntity.getPush_srl() + "], file_srl [" +
                            pushMessageEntity.getFile_srl() + "]");
                } else {
                    // 외부 이미지를 등록 후 매핑
                    this.addPushMessageOuterPicAndMapping(pushMessageEntity.getPush_srl(),
                            pushMessageEntity.getImage_url(), requestIP);
                }
            } else {
                // 외부 이미지를 등록 후 매핑
                this.addPushMessageOuterPicAndMapping(pushMessageEntity.getPush_srl(),
                        pushMessageEntity.getImage_url(), requestIP);
            }
        }

        // 단일 타겟 메시지 일때 단일 타겟을 구한다.
        DeviceEntity singleDeviceEntity = null;
        if(StringUtils.equals(pushMessageEntity.getPush_target(), targetUnit)) {
            if(pushMessageEntity.getSingle_push_target() == null) {
                Map<String, String> reason = new HashMap<>();
                reason.put("push_target", "not found single push target device. device_id is null.");
                LOG.error(reason.get("push_target"));
                throw new CustomException("push_gcm_apns_error", reason);
            }

            pushMessageEntity.setSingle_push_target(StringUtils.trim(pushMessageEntity.getSingle_push_target()));

            List<DeviceEntity> deviceEntities = deviceDao.getInApp(pushMessageEntity.getApp_srl(), null, MDV.NUSE,
                    pushMessageEntity.getSingle_push_target(),
                    null, MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, 0, 1);
            if(deviceEntities.size() <= 0) {
                Map<String, String> reason = new HashMap<>();
                reason.put("push_target", "no device in app.");
                LOG.error(reason.get("push_target"));
                throw new CustomException("push_gcm_apns_error", reason);
            }

            singleDeviceEntity = deviceEntities.get(0);
        }

        // 최종 push bean 을 만든다.
        RasEventBean rasEventBean = new RasEventBean();
        rasEventBean.setApp_id(appEntity.getApp_id());
        rasEventBean.setPid(confCommon.getProperty("ras_gcm_apns_push_pid"));
        rasEventBean.setEt(confCommon.getProperty("ras_gcm_apns_event_type"));
        rasEventBean.setTid(Long.toString(pushMessageEntity.getPush_srl()));
        rasEventBean.setCtime(DateTime.now().getMillis());

        // ud 값을 넣어야 한다.
        Map<String, Object> ud = new HashMap<>();
        ud.put("app_srl", pushMessageEntity.getApp_srl());
        ud.put("app_id", appEntity.getApp_id());
        ud.put("user_id", pushMessageEntity.getUser_id());
        ud.put("push_title", pushMessageEntity.getPush_title());
        ud.put("push_text", pushMessageEntity.getPush_text());
        ud.put("callback_url", pushMessageEntity.getCallback_url());
        ud.put("image_url", pushMessageEntity.getImage_url());
        ud.put("push_type", pushMessageEntity.getPush_target());
        ud.put("c_date", pushMessageEntity.getC_date());
        if(singleDeviceEntity == null)  ud.put("single_target", MDV.NONE);
        else                            ud.put("single_target", singleDeviceEntity.getDevice_srl());

        rasEventBean.setUd(ud);

        gcmApns.send(rasEventBean);
        LOG.info("try send gcm or apns. rasEventBean [" + rasEventBean.toString() + "]");
    }

    @Transactional(readOnly = true)
    @Override
    public long getPushMessageHistoryCount(int appSrl, String pushTitle) {
        if(pushTitle != null) {
            pushTitle = StringUtils.trim(pushTitle);
            if(StringUtils.equals(pushTitle, "")) pushTitle = null;
        }

        return pushMessageDao.count(appSrl, pushTitle, MDV.NUSE);
    }

    @Transactional(readOnly = true)
    @Override
    public long getPushMessageHistoryCount(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.getPushMessageHistoryCount(MDV.NUSE, null);

        int appSrl = MDV.NUSE;
        String pushTitle = null;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("push_title")) pushTitle = searchFilter.get("push_title");

        return this.getPushMessageHistoryCount(appSrl, pushTitle);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PushMessageEntity> getPushMessageHistory(int appSrl, String pushTitle, Map<String, String> sort,
                                                         int offset, int limit) {
        if(pushTitle != null) {
            pushTitle = StringUtils.trim(pushTitle);
            if(StringUtils.equals(pushTitle, "")) pushTitle = null;
        }

        // sort 의 direction 체크
        if(sort == null || sort.size() <= 0) sort = null;
        else {
            Set<String> keys = sort.keySet();
            for(String key : keys) {
                if(!StringUtils.equals(sort.get(key), "desc") && !StringUtils.equals(sort.get(key), "asc")) {
                    LOG.warn("invalid column sort direction. column [" + key +"], direction [" +
                            sort.get(key) + "]. set default desc direction");
                    sort.put(key, "desc");
                }
            }
        }

        return pushMessageDao.get(appSrl, null, pushTitle, MDV.NUSE, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PushMessageEntity> getPushMessageHistory(Map<String, String> searchFilter, Map<String, String> sort,
                                                         int offset, int limit) {
        if(searchFilter == null) return this.getPushMessageHistory(MDV.NUSE, null, sort, offset, limit);

        int appSrl = MDV.NUSE;
        String pushTitle = null;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("push_title")) pushTitle = searchFilter.get("push_title");

        return this.getPushMessageHistory(appSrl, pushTitle, sort, offset, limit);
    }

    @Override
    public void modifyGCMRid(int appSrl, long deviceSrl, String rid) {
        AppDeviceEntity appDeviceEntity = appDeviceDao.get(appSrl, deviceSrl, MDV.NUSE);
        if(appDeviceEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid_method_parameter", "not found device in app.");
            LOG.error(reason.get("invalid_method_parameter") + " device_srl [" + deviceSrl +
                    "], app_srl [" + appSrl + "]");
            throw new CustomException("invalid_method_parameter", reason);
        }

        // enabled 체크 하지 않음.

        AppDeviceEntity updateEntity = new AppDeviceEntity();
        updateEntity.init();

        if(rid == null || StringUtils.equals(StringUtils.trim(rid), ""))
            updateEntity.setReg_push_id(MDV.NO);
        else
            updateEntity.setReg_push_id(MDV.YES);

        updateEntity.setApp_srl(appSrl);
        updateEntity.setDevice_srl(deviceSrl);
        updateEntity.setPush_id(rid);

        appDeviceDao.modify(updateEntity, appSrl, deviceSrl);
        LOG.info("modify gcm rid. app_srl [" + appSrl + "], device_srl [" + deviceSrl + "]");
    }


}

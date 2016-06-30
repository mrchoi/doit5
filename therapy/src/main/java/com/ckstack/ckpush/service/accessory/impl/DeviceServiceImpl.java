package com.ckstack.ckpush.service.accessory.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.app.AppDeviceDao;
import com.ckstack.ckpush.dao.accessory.DeviceDao;
import com.ckstack.ckpush.domain.app.AppDeviceEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.service.accessory.DeviceService;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 6. 9..
 */
@Service
@Transactional(value = "transactionManager")
public class DeviceServiceImpl implements DeviceService {
    private final static Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private AppDeviceDao appDeviceDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private WebUtilService webUtilService;

    @Override
    public void modifyDevice(long deviceSrl, int appSrl, Map<String, String> modifyValue) {
        if(deviceSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("device_srl", "invalid device_srl. device_srl is less then zero. device_srl [" + deviceSrl + "]");
            LOG.error(reason.get("device_srl"));
            throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(modifyValue == null || modifyValue.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "invalid modify value. modify value is null or empty. device_srl [" + deviceSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(!modifyValue.containsKey("device_type") && !modifyValue.containsKey("os_version") &&
                !modifyValue.containsKey("mobile_phone_number") && !modifyValue.containsKey("push_id") &&
                !modifyValue.containsKey("allow_push") && !modifyValue.containsKey("enabled")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "not found app device modify value. modifyValue [" +
                    modifyValue.toString() + "], device_srl [" + deviceSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DeviceEntity savedDeviceEntity = deviceDao.get(deviceSrl, null);
        if(savedDeviceEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("not_found", "not found app. device_srl [" + deviceSrl + "]");
            LOG.error(reason.get("not_found"));
            throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppDeviceEntity savedAppDeviceEntity = appDeviceDao.get(appSrl, deviceSrl, MDV.NUSE);
        if(savedAppDeviceEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("not_found", "not found app device. device_srl [" + deviceSrl + "], app_srl [" + appSrl + "]");
            LOG.error(reason.get("not_found"));
            throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        boolean willDeviceModify = false, willAppDeviceModify = false;
        DeviceEntity deviceEntity = new DeviceEntity();
        AppDeviceEntity appDeviceEntity = new AppDeviceEntity();

        deviceEntity.init();
        appDeviceEntity.init();

        if(modifyValue.containsKey("device_type")) {
            String deviceType;

            if(modifyValue.get("device_type") == null)
                deviceType = "";
            else
                deviceType = StringUtils.trim(modifyValue.get("device_type"));

            int length = deviceType.getBytes().length;
            if(length > 128) {
                Map<String, String> reason = new HashMap<>();
                reason.put("device_type", "failed modify device_type. device_type length less then 128 byte. length [" + length + "]");
                LOG.error(reason.get("device_type"));
                throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!StringUtils.equals(savedDeviceEntity.getDevice_type(), deviceType)) {
                deviceEntity.setDevice_type(deviceType);
                willDeviceModify = true;
            }
        }

        if(modifyValue.containsKey("os_version")) {
            String osVersion;

            if(modifyValue.get("os_version") == null)
                osVersion = "";
            else
                osVersion = StringUtils.trim(modifyValue.get("os_version"));

            int length = osVersion.getBytes().length;
            if(length > 32) {
                Map<String, String> reason = new HashMap<>();
                reason.put("os_version", "failed modify os_version. os_version length less then 32 byte. length [" + length + "]");
                LOG.error(reason.get("os_version"));
                throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!StringUtils.equals(savedDeviceEntity.getOs_version(), osVersion)) {
                deviceEntity.setOs_version(osVersion);
                willDeviceModify = true;
            }
        }

        if(modifyValue.containsKey("mobile_phone_number")) {
            String mobilePhoneNumber;

            if(modifyValue.get("mobile_phone_number") == null)
                mobilePhoneNumber = "";
            else
                mobilePhoneNumber = webUtilService.getPhoneNumber(StringUtils.trim(modifyValue.get("mobile_phone_number")));

            int length = mobilePhoneNumber.getBytes().length;
            if(length > 16) {
                Map<String, String> reason = new HashMap<>();
                reason.put("mobile_phone_number", "failed modify mobile_phone_number. mobile_phone_number length less then 16 byte. length [" + length + "]");
                LOG.error(reason.get("mobile_phone_number"));
                throw new CustomException("modify_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!StringUtils.equals(savedDeviceEntity.getMobile_phone_number(), mobilePhoneNumber)) {
                deviceEntity.setMobile_phone_number(mobilePhoneNumber);
                willDeviceModify = true;
            }
        }

        if(modifyValue.containsKey("push_id")) {
            String pushId;

            if(modifyValue.get("push_id") == null)
                pushId = "";
            else
                pushId = StringUtils.trim(modifyValue.get("push_id"));

            if(!StringUtils.equals(savedAppDeviceEntity.getPush_id(), pushId)) {
                appDeviceEntity.setPush_id(pushId);

                if(StringUtils.equals(pushId, ""))  appDeviceEntity.setReg_push_id(MDV.NO);
                else                                appDeviceEntity.setReg_push_id(MDV.YES);

                willAppDeviceModify = true;
            }
        }

        if(modifyValue.containsKey("allow_push")) {
            String allowPush = modifyValue.get("allow_push");
            if(allowPush != null && !StringUtils.equals(allowPush=StringUtils.trim(allowPush), "") &&
                    NumberUtils.isNumber(allowPush)) {
                int iAllowPush = Integer.parseInt(allowPush, 10);
                if(iAllowPush == MDV.YES || iAllowPush == MDV.NO) {
                    if (savedAppDeviceEntity.getAllow_push() != iAllowPush) {
                        appDeviceEntity.setAllow_push(iAllowPush);
                        willAppDeviceModify = true;
                    }
                }
            }
        }

        if(modifyValue.containsKey("enabled")) {
            String enabled = modifyValue.get("enabled");
            if(enabled != null && !StringUtils.equals(enabled=StringUtils.trim(enabled), "") &&
                    NumberUtils.isNumber(enabled)) {
                int iEnabled = Integer.parseInt(enabled, 10);
                if(iEnabled == MDV.YES || iEnabled == MDV.NO || iEnabled == MDV.DENY) {
                    if (savedAppDeviceEntity.getEnabled() != iEnabled) {
                        appDeviceEntity.setEnabled(iEnabled);
                        willAppDeviceModify = true;
                    }
                }
            }
        }

        if(savedAppDeviceEntity.getReg_push_id() == MDV.YES && savedAppDeviceEntity.getAllow_push() == MDV.YES &&
                savedAppDeviceEntity.getEnabled() == MDV.YES) {
            // 이전은 push 받는 설정이었다가 바뀌는건 push 받는 설정이 아니면 push 대상 단말 카운트를 감소
            if(appDeviceEntity.getReg_push_id() == MDV.NO || appDeviceEntity.getAllow_push() == MDV.NO ||
                    appDeviceEntity.getEnabled() == MDV.NO || appDeviceEntity.getEnabled() == MDV.DENY) {
                appDao.decrease(appSrl, null, false, true);
                LOG.info("decrease registed push terminal count in app. app_srl [" + appSrl + "]");
            }
        } else {
            boolean willIncrease = true;

            if(savedAppDeviceEntity.getReg_push_id() != MDV.YES) {
                if(appDeviceEntity.getReg_push_id() != MDV.YES) willIncrease = false;
            }

            if(willIncrease && savedAppDeviceEntity.getAllow_push() != MDV.YES) {
                if(appDeviceEntity.getAllow_push() != MDV.YES)  willIncrease = false;
            }

            if(willIncrease && savedAppDeviceEntity.getEnabled() != MDV.YES) {
                if(appDeviceEntity.getEnabled() != MDV.YES)     willIncrease = false;
            }

            if(willIncrease) {
                appDao.increase(appSrl, null, false, true, false);
                LOG.info("increase registed push terminal count in app. app_srl [" + appSrl + "]");
            }
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        if(willDeviceModify) {
            deviceEntity.setU_date(ltm);
            deviceDao.modify(deviceEntity, deviceSrl, null);
            LOG.info("modify device. deviceEntity [" + deviceEntity.toString() + "]");
        } else {
            LOG.info("ignore modify device. device_srl [" + deviceSrl +"]. it is same between saved value and changed value");
        }

        if(willAppDeviceModify) {
            appDeviceEntity.setU_date(ltm);
            appDeviceDao.modify(appDeviceEntity, appSrl, deviceSrl);
            LOG.info("modify appDeviceEntity [" + appDeviceEntity.toString() + "]");
        } else {
            LOG.info("ignore modify app device. app_srl [" + appSrl + "], device_srl [" +
                    deviceSrl +"]. it is same between saved value and changed value");
        }
    }
}

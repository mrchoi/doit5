package com.ckstack.ckpush.service.user.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.app.AppDeviceDao;
import com.ckstack.ckpush.dao.accessory.DeviceAccessTokenDao;
import com.ckstack.ckpush.dao.app.AppMemberDao;
import com.ckstack.ckpush.dao.user.MemberAccessTokenDao;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.data.cache.AccessTokenExtra;
import com.ckstack.ckpush.domain.app.AppDeviceEntity;
import com.ckstack.ckpush.domain.accessory.DeviceAccessTokenEntity;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.app.AppMemberEntity;
import com.ckstack.ckpush.domain.user.MemberAccessTokenEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.message.CustomCache;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.user.AuthService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by dhkim94 on 15. 7. 13..
 */
@Service
@Transactional(value = "transactionManager")
public class AuthServiceImpl implements AuthService {
    private final static Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AppDeviceDao appDeviceDao;
    @Autowired
    private DeviceAccessTokenDao deviceAccessTokenDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AppMemberDao appMemberDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private MemberAccessTokenDao memberAccessTokenDao;
    @Autowired
    private CustomCache customCache;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;

    /**
     * 메모리에 들어있는 접속 토큰을 재사용 하거나 신규 발급 한다.
     *
     * @param memberEntity access_token에 연관된 사용자 정보
     * @param appMemberEntity access_token에 연관된 사용자, 앱 매핑 정보
     * @param ltm 현재 시간. unix timestamp
     * @return 신규 발급한 access_token 정보. rdbms에 저장되지 않기 때문에 token_srl은 무의미한 값 MDV.NONE 으로 설정된다.
     */
    private MemberAccessTokenEntity createOrReuseMemberAccessTokenInMemory(MemberEntity memberEntity,
                                                                           AppMemberEntity appMemberEntity, int ltm) {
        int extensionSec = Integer.parseInt(confCommon.getProperty("access_token_valid_sec"), 10);

        AccessTokenExtra prevAccessTokenExtra = customCache.getMemberAccessToken(memberEntity.getMember_srl(),
                appMemberEntity.getApp_srl());
        if(prevAccessTokenExtra != null && ltm <= prevAccessTokenExtra.getToken_expire()) {
            // 기존 것의 기간 연장
            prevAccessTokenExtra.setToken_expire(ltm + extensionSec);
            customCache.upsertMemberAccessToken(memberEntity.getMember_srl(), appMemberEntity.getApp_srl(),
                    prevAccessTokenExtra.getAccess_token(), prevAccessTokenExtra, extensionSec);
            LOG.info("renewal access_token by member in memory. access_token [ " +
                    prevAccessTokenExtra.getAccess_token() + "]");

            MemberAccessTokenEntity memberAccessTokenEntity = new MemberAccessTokenEntity();
            memberAccessTokenEntity.setToken_srl(MDV.NONE);
            memberAccessTokenEntity.setMember_srl(prevAccessTokenExtra.getMember_srl());
            memberAccessTokenEntity.setApp_srl(prevAccessTokenExtra.getApp_srl());
            memberAccessTokenEntity.setAccess_token(prevAccessTokenExtra.getAccess_token());
            memberAccessTokenEntity.setToken_expire(prevAccessTokenExtra.getToken_expire());
            memberAccessTokenEntity.setC_date(MDV.NONE);
            memberAccessTokenEntity.setU_date(MDV.NONE);

            byte[] objByte = webUtilService.convertObjectToJsonBytes(prevAccessTokenExtra);
            String jsonStr = "";
            try {
                jsonStr = new String(objByte, "utf-8");
            } catch (UnsupportedEncodingException e) {
                LOG.warn("can't convert object to json. unsupport utf-8 encoding");
            }
            memberAccessTokenEntity.setUser_data(jsonStr);

            return memberAccessTokenEntity;
        }

        // 기존 것이 존재하는데 이미 만기 되어서 access_token을 바꿔야 하는 경우라면 기존 것을 삭제
        if(prevAccessTokenExtra != null) {
            customCache.deleteMemberAccessToken(prevAccessTokenExtra.getMember_srl(),
                    prevAccessTokenExtra.getApp_srl(), prevAccessTokenExtra.getAccess_token());
            LOG.info("delete expired access_token in memory. memberSrl [" +
                    prevAccessTokenExtra.getMember_srl() + "], appSrl [" +
                    prevAccessTokenExtra.getApp_srl() + "], access_token [" +
                    prevAccessTokenExtra.getAccess_token() + "]");
        }

        MemberAccessTokenEntity memberAccessTokenEntity = new MemberAccessTokenEntity();
        memberAccessTokenEntity.setToken_srl(MDV.NONE);
        memberAccessTokenEntity.setMember_srl(memberEntity.getMember_srl());
        memberAccessTokenEntity.setApp_srl(appMemberEntity.getApp_srl());
        memberAccessTokenEntity.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        memberAccessTokenEntity.setToken_expire(ltm + extensionSec);

        AccessTokenExtra accessTokenExtra = new AccessTokenExtra();
        accessTokenExtra.setMember_srl(memberEntity.getMember_srl());
        accessTokenExtra.setApp_srl(appMemberEntity.getApp_srl());
        accessTokenExtra.setAccess_token(memberAccessTokenEntity.getAccess_token());
        accessTokenExtra.setUser_id(memberEntity.getUser_id());
        accessTokenExtra.setEmail_address(memberEntity.getEmail_address());
        accessTokenExtra.setUser_name(memberEntity.getUser_name());
        accessTokenExtra.setNick_name(appMemberEntity.getNick_name());
        accessTokenExtra.setAllow_mailing(appMemberEntity.getAllow_mailing());
        accessTokenExtra.setAllow_message(appMemberEntity.getAllow_message());
        accessTokenExtra.setLast_login_date(ltm);
        accessTokenExtra.setToken_expire(ltm + extensionSec);
        accessTokenExtra.setEnabled(appMemberEntity.getEnabled());

        byte[] objByte = webUtilService.convertObjectToJsonBytes(accessTokenExtra);
        String jsonStr = "";
        try {
            jsonStr = new String(objByte, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOG.warn("can't convert object to json. unsupport utf-8 encoding");
        }
        memberAccessTokenEntity.setUser_data(jsonStr);

        memberAccessTokenEntity.setC_date(ltm);
        memberAccessTokenEntity.setU_date(ltm);

        // access_token 발급
        customCache.upsertMemberAccessToken(memberEntity.getMember_srl(), appMemberEntity.getApp_srl(),
                memberAccessTokenEntity.getAccess_token(), accessTokenExtra, extensionSec);
        LOG.info("create access_token by member in memory. accessToken [" +
                memberAccessTokenEntity.getAccess_token() + "], cache_data [" + accessTokenExtra.toString() + "]");

        return memberAccessTokenEntity;

    }

    /**
     * 가입형 서비스에서 RDBMS를 사용하여 사용자를 위한 access_token을 발급하거나 기존 발급 된 것을 재사용 하도록 한다.
     *
     * @param memberEntity access_token에 연관된 사용자 정보
     * @param appMemberEntity access_token에 연관된 사용자, 앱 매핑 정보
     * @param ltm 현재 시간. unix timestamp
     * @return 신규 발급 또는 재사용 하는 access_token 정보
     */
    private MemberAccessTokenEntity createOrReuseMemberAccessTokenInRDBMS(MemberEntity memberEntity,
                                                                          AppMemberEntity appMemberEntity, int ltm) {
        int extensionSec = Integer.parseInt(confCommon.getProperty("access_token_valid_sec"), 10);

        // 기존에 발급된 인증키 확인
        List<MemberAccessTokenEntity> memberAccessTokenEntities = memberAccessTokenDao.get(
                memberEntity.getMember_srl(), appMemberEntity.getApp_srl(),
                null, MDV.NUSE, 0, 1);

        if(memberAccessTokenEntities.size() > 0) {
            MemberAccessTokenEntity prevAccessTokenEntity = memberAccessTokenEntities.get(0);
            MemberAccessTokenEntity updateAccessTokenEntity = new MemberAccessTokenEntity();
            updateAccessTokenEntity.init();

            if(ltm > prevAccessTokenEntity.getToken_expire())
                updateAccessTokenEntity.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));

            updateAccessTokenEntity.setToken_expire(ltm + extensionSec);
            updateAccessTokenEntity.setU_date(ltm);

            if(prevAccessTokenEntity.getUser_data() != null) {
                AccessTokenExtra accessTokenExtra = null;
                try {
                    accessTokenExtra = (AccessTokenExtra) webUtilService.convertJsonBytesToObject(
                            "com.ckstack.ckpush.data.cache.AccessTokenExtra",
                            prevAccessTokenEntity.getUser_data().getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    LOG.warn("can't convert user json string to object. e [" +
                            e.getMessage() + "]");
                }

                if(accessTokenExtra != null) {
                    accessTokenExtra.setAccess_token(updateAccessTokenEntity.getAccess_token());
                    accessTokenExtra.setToken_expire(ltm + extensionSec);
                    byte[] objByte = webUtilService.convertObjectToJsonBytes(accessTokenExtra);
                    String jsonStr = "";
                    try {
                        jsonStr = new String(objByte, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        LOG.warn("can't convert object to json. unsupport utf-8 encoding");
                    }
                    if(!StringUtils.equals(jsonStr, "")) updateAccessTokenEntity.setUser_data(jsonStr);
                }
            }

            memberAccessTokenDao.modify(updateAccessTokenEntity, prevAccessTokenEntity.getToken_srl(), null, MDV.NUSE);
            LOG.info("renewal access_token by member in rdbms. updateAccessTokenEntity [ " +
                    updateAccessTokenEntity + "]");

            // last login 시간 업데이트(member 기본 정보)
            MemberEntity updateMemberEntity = new MemberEntity();
            updateMemberEntity.init();
            updateMemberEntity.setLast_login_date(ltm);
            memberDao.modify(updateMemberEntity, memberEntity.getMember_srl(), null);
            LOG.info("update member's last login date [" + ltm + "]");

            // last login 시간 업데이트(app member 정보)
            AppMemberEntity updateAppMemberEntity = new AppMemberEntity();
            updateAppMemberEntity.init();
            updateAppMemberEntity.setLast_login_date(ltm);
            appMemberDao.modify(updateAppMemberEntity, appMemberEntity.getApp_srl(), memberEntity.getMember_srl());
            LOG.info("update app member's last login date [" + ltm + "]");

            return memberAccessTokenDao.get(prevAccessTokenEntity.getToken_srl(), null);
        }

        // 기존에 발급된 것이 없다면 신규 발급
        MemberAccessTokenEntity memberAccessTokenEntity = new MemberAccessTokenEntity();
        memberAccessTokenEntity.setMember_srl(memberEntity.getMember_srl());
        memberAccessTokenEntity.setApp_srl(appMemberEntity.getApp_srl());
        memberAccessTokenEntity.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        memberAccessTokenEntity.setToken_expire(ltm + extensionSec);

        AccessTokenExtra accessTokenExtra = new AccessTokenExtra();
        accessTokenExtra.setMember_srl(memberEntity.getMember_srl());
        accessTokenExtra.setApp_srl(appMemberEntity.getApp_srl());
        accessTokenExtra.setAccess_token(memberAccessTokenEntity.getAccess_token());
        accessTokenExtra.setUser_id(memberEntity.getUser_id());
        accessTokenExtra.setEmail_address(memberEntity.getEmail_address());
        accessTokenExtra.setUser_name(memberEntity.getUser_name());
        accessTokenExtra.setNick_name(appMemberEntity.getNick_name());
        accessTokenExtra.setAllow_mailing(appMemberEntity.getAllow_mailing());
        accessTokenExtra.setAllow_message(appMemberEntity.getAllow_message());
        accessTokenExtra.setLast_login_date(ltm);
        accessTokenExtra.setToken_expire(ltm + extensionSec);
        accessTokenExtra.setEnabled(appMemberEntity.getEnabled());

        byte[] objByte = webUtilService.convertObjectToJsonBytes(accessTokenExtra);
        String jsonStr = "";
        try {
            jsonStr = new String(objByte, "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOG.warn("can't convert object to json. unsupport utf-8 encoding");
        }
        memberAccessTokenEntity.setUser_data(jsonStr);

        memberAccessTokenEntity.setC_date(ltm);
        memberAccessTokenEntity.setU_date(ltm);

        // access_token 발급
        memberAccessTokenDao.add(memberAccessTokenEntity);
        LOG.info("create access_token by member in rdbms. memberAccessTokenEntity [" +
                memberAccessTokenEntity.toString() + "]");

        return memberAccessTokenEntity;
    }

    @Override
    public DeviceAccessTokenEntity createAccessTokenUsingDevice(long deviceSrl, int appSrl) {
        if(deviceSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("device_srl", "device_srl is less then zero. device_srl [" + deviceSrl + "]");
            LOG.error("invalid device_srl [" + deviceSrl + "] " + reason.get("device_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "app_srl is less then zero. app_srl [" + appSrl + "]");
            LOG.error("invalid app_srl [" + appSrl + "] " + reason.get("app_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        AppDeviceEntity appDeviceEntity = appDeviceDao.get(appSrl, deviceSrl, MDV.NUSE);
        if(appDeviceEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_device", "not found device in app. device_srl [" + deviceSrl + "], app_srl [" + appSrl + "]");
            LOG.error(reason.get("no_device"));
            throw new CustomException("create_access_token_error", reason);
        }

        if(appDeviceEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_device", "disabled device. device_srl [" + deviceSrl + "], app_srl [" +
                    appSrl + "], enabled [" + appDeviceEntity.getEnabled() + "]");
            LOG.error(reason.get("no_device"));
            throw new CustomException("create_access_token_error", reason);
        }

        // TODO 비가입자형 서비스 access_token을 memory db에 저장하는 것은 추후에 진행 하도록 한다.

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        int extensionSec = Integer.parseInt(confCommon.getProperty("access_token_valid_sec"), 10);

        // 기존에 발급된 인증키 확인
        List<DeviceAccessTokenEntity> deviceAccessTokenEntityList = deviceAccessTokenDao.get(deviceSrl, appSrl,
                null, MDV.NUSE, 0, 1);

        if(deviceAccessTokenEntityList.size() > 0) {
            DeviceAccessTokenEntity prevAccessTokenEntity = deviceAccessTokenEntityList.get(0);
            DeviceAccessTokenEntity updateAccessTokenEntity = new DeviceAccessTokenEntity();
            updateAccessTokenEntity.init();

            if(ltm > prevAccessTokenEntity.getToken_expire())
                updateAccessTokenEntity.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));

            updateAccessTokenEntity.setToken_expire(ltm + extensionSec);
            updateAccessTokenEntity.setU_date(ltm);

            deviceAccessTokenDao.modify(updateAccessTokenEntity, prevAccessTokenEntity.getToken_srl(), null, MDV.NUSE);
            LOG.info("renewal access_token by device. updateAccessTokenEntity [ " +
                    updateAccessTokenEntity + "]");

            return deviceAccessTokenDao.get(prevAccessTokenEntity.getToken_srl(), null);
        }

        // 기존에 발급된 것이 없다면 신규 발급
        DeviceAccessTokenEntity deviceAccessTokenEntity = new DeviceAccessTokenEntity();
        deviceAccessTokenEntity.setDevice_srl(deviceSrl);
        deviceAccessTokenEntity.setApp_srl(appSrl);
        deviceAccessTokenEntity.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        deviceAccessTokenEntity.setToken_expire(ltm + extensionSec);
        deviceAccessTokenEntity.setC_date(ltm);
        deviceAccessTokenEntity.setU_date(ltm);

        deviceAccessTokenDao.add(deviceAccessTokenEntity);
        LOG.info("create access_token by device. deviceAccessTokenEntity [" +
                deviceAccessTokenEntity.toString() + "]");

        return deviceAccessTokenEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public DeviceAccessTokenEntity getAccessTokenUsingDevice(String accessToken) {
        // TODO 비가입자형 서비스 access_token을 memory db에 저장하는 것은 추후에 진행 하도록 한다.

        if(accessToken == null || StringUtils.equals(StringUtils.trim(accessToken), "")) {
            LOG.info("no access token for check access token information. access_token [" + accessToken + "]");
            return null;
        }

        DeviceAccessTokenEntity deviceAccessTokenEntity = deviceAccessTokenDao.get(MDV.NUSE, accessToken);
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        // access token 의 만료 시간은 넉넉하게 30초 만큼 여유 분을 주고 판단 한다.
        if(ltm >= deviceAccessTokenEntity.getToken_expire() + 30) {
            LOG.info("access token expire. access_token [" + accessToken + "], current time [" +
                    ltm + "], expire_time [" + deviceAccessTokenEntity.getToken_expire() + "]");
            return null;
        }

        // disable 인지 체크 한다.
        AppDeviceEntity appDeviceEntity = appDeviceDao.get(deviceAccessTokenEntity.getApp_srl(),
                deviceAccessTokenEntity.getDevice_srl(), MDV.NUSE);
        if(appDeviceEntity.getEnabled() != MDV.YES) {
            LOG.info("device is not enable in app. app_srl [" + deviceAccessTokenEntity.getApp_srl() +
                    "], device_srl [" + deviceAccessTokenEntity.getDevice_srl()+ "], enabled [" +
                    appDeviceEntity.getEnabled() + "]");
            return null;
        }

        return deviceAccessTokenEntity;
    }

    @Override
    public int renewalAccessTokenExpireUsingDevice(long tokenSrl) {
        // TODO 비가입자형 서비스 access_token을 memory db에 저장하는 것은 추후에 진행 하도록 한다.

        // 존재 유무는 체크 하지 않는다.
        int ltm = (int)(DateTime.now().getMillis() / 1000);
        int renewalSec = Integer.parseInt(confCommon.getProperty("access_token_valid_sec"), 10);
        DeviceAccessTokenEntity deviceAccessTokenEntity = new DeviceAccessTokenEntity();
        deviceAccessTokenEntity.init();
        deviceAccessTokenEntity.setToken_expire(ltm + renewalSec);

        deviceAccessTokenDao.modify(deviceAccessTokenEntity, tokenSrl, null, MDV.NUSE);
        LOG.info("renewal access token expire. token_srl [" + tokenSrl +
                "], renewal tm [" + deviceAccessTokenEntity.getToken_expire() + "]");

        return renewalSec;
    }

    @Override
    public MemberAccessTokenEntity createAccessTokenUsingMember(MemberEntity memberEntity,
                                                                long memberSrl, int appSrl) {
        if(memberSrl <= 0 && memberEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "member_srl is less then zero. member_srl [" + memberSrl + "]");
            LOG.error("invalid member_srl [" + memberSrl + "] " + reason.get("member_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        // NOTE 앱의 enabled 체크는 메소드 호출 하는 곳에서 해주도록 한다.

        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "app_srl is less then zero. app_srl [" + appSrl + "]");
            LOG.error("invalid app_srl [" + appSrl + "] " + reason.get("app_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(memberSrl > 0 || memberEntity == null) memberEntity = memberDao.get(memberSrl, null);
        if(memberEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_member", "not found member. member_srl [" + memberSrl + "]");
            LOG.error(reason.get("no_member"));
            throw new CustomException("create_access_token_error", reason);
        }

        memberSrl = memberEntity.getMember_srl();

        // 사용자 기본 상태 체크
        if(memberEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_member", "disabled member. member_srl [" + memberSrl + "], enabled [" +
                    memberEntity.getEnabled() + "]");
            LOG.error(reason.get("no_member"));
            throw new CustomException("create_access_token_error", reason);
        }

        // 사용자 가입 여부 확인
        if(memberEntity.getSign_out() != MDV.NO) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_member", "sign out member. member_srl [" + memberSrl + "], sign_out [" +
                    memberEntity.getSign_out() + "]");
            LOG.error(reason.get("no_member"));
            throw new CustomException("create_access_token_error", reason);
        }

        // 앱에 사용자 가입 여부 확인
        AppMemberEntity appMemberEntity = appMemberDao.get(appSrl, memberSrl);
        if(appMemberEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_member", "not found member in app. member_srl [" + memberSrl + "], app_srl [" +
                    appSrl + "]");
            LOG.error(reason.get("no_member"));
            throw new CustomException("create_access_token_error", reason);
        }

        if(appMemberEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_member", "disabled app member. member_srl [" + memberSrl + "], app_srl [" +
                    appSrl + "], member's app enabled [" + appMemberEntity.getEnabled() + "]");
            LOG.error(reason.get("no_member"));
            throw new CustomException("create_access_token_error", reason);
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        String cacheType = confCommon.getProperty("access_token_repository");
        MemberAccessTokenEntity memberAccessTokenEntity;

        if(StringUtils.equals(cacheType, "1"))
            memberAccessTokenEntity = this.createOrReuseMemberAccessTokenInRDBMS(memberEntity, appMemberEntity, ltm);
        else
            memberAccessTokenEntity = this.createOrReuseMemberAccessTokenInMemory(memberEntity, appMemberEntity, ltm);

        // last login 시간 업데이트(member 기본 정보)
        MemberEntity updateMemberEntity = new MemberEntity();
        updateMemberEntity.init();
        updateMemberEntity.setLast_login_date(ltm);
        memberDao.modify(updateMemberEntity, memberSrl, null);
        LOG.info("update member's last login date [" + ltm + "]");

        // last login 시간 업데이트(app member 정보)
        AppMemberEntity updateAppMemberEntity = new AppMemberEntity();
        updateAppMemberEntity.init();
        updateAppMemberEntity.setLast_login_date(ltm);
        appMemberDao.modify(updateAppMemberEntity, appSrl, memberSrl);
        LOG.info("update app member's last login date [" + ltm + "]");

        return memberAccessTokenEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public MemberAccessTokenEntity getAccessTokenUsingMember(String accessToken) {
        if(accessToken == null || StringUtils.equals(StringUtils.trim(accessToken), "")) {
            LOG.info("no access token for check access token information. access_token [" + accessToken + "]");
            return null;
        }

        String cacheType = confCommon.getProperty("access_token_repository");
        MemberAccessTokenEntity memberAccessTokenEntity;

        if(StringUtils.equals(cacheType, "1")) {
            memberAccessTokenEntity = memberAccessTokenDao.get(MDV.NUSE, accessToken);

            if(memberAccessTokenEntity == null) return null;

            // 사용자 상태 체크
            MemberEntity memberEntity = memberDao.get(memberAccessTokenEntity.getMember_srl(), null);
            if(memberEntity == null) {
                LOG.info("not found member from access_token. member_srl [" + memberAccessTokenEntity.getMember_srl() +
                        "], accessToken [" + accessToken + "]");
                return null;
            }

            if(memberEntity.getEnabled() != MDV.YES) {
                LOG.info("disabled member from access_token. member_srl [" + memberAccessTokenEntity.getMember_srl() +
                        "], enabled [" + memberEntity.getEnabled() + "], accessToken [" + accessToken + "]");
                return null;
            }

            // app 이 enable 인지 체크 한다.
            AppEntity appEntity = appDao.get(memberAccessTokenEntity.getApp_srl(), null, null);
            if(appEntity == null) {
                LOG.info("not found app from access_token. app_srl [" + memberAccessTokenEntity.getApp_srl() +
                        "], accessToken [" + accessToken + "]");
                return null;
            }

            if(appEntity.getEnabled() != MDV.YES) {
                LOG.info("disabled app from access_token. app_srl [" + memberAccessTokenEntity.getApp_srl() +
                        "], enabled [" + appEntity.getEnabled() + "], accessToken [" + accessToken + "]");
                return null;
            }

        } else {
            // access_token이 존재한다면 app, member가 모두 enabled true 라고 판단하면 된다.
            AccessTokenExtra accessTokenExtra = customCache.getMemberAccessToken(accessToken);
            if(accessTokenExtra != null) {
                memberAccessTokenEntity = new MemberAccessTokenEntity();
                memberAccessTokenEntity.setToken_srl(MDV.NONE);
                memberAccessTokenEntity.setMember_srl(accessTokenExtra.getMember_srl());
                memberAccessTokenEntity.setApp_srl(accessTokenExtra.getApp_srl());
                memberAccessTokenEntity.setAccess_token(accessTokenExtra.getAccess_token());
                memberAccessTokenEntity.setToken_expire(accessTokenExtra.getToken_expire());
                memberAccessTokenEntity.setC_date(MDV.NONE);
                memberAccessTokenEntity.setU_date(MDV.NONE);

                byte[] objByte = webUtilService.convertObjectToJsonBytes(accessTokenExtra);
                String jsonStr = "";
                try {
                    jsonStr = new String(objByte, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    LOG.warn("can't convert object to json. unsupport utf-8 encoding");
                }
                memberAccessTokenEntity.setUser_data(jsonStr);
            } else {
                return null;
            }
        }

        // TODO 어드민에서 앱 가입 상태 변경 하는 것이 없으므로 현재는 체크 하지 않음. 어드민에서 기능 추가 후 체크 넣도록 한다.
        // TODO 메모리 체크는 가입자 상태 변경, 앱 상태 변경과 같이 선 체크 방법을 사용해야 한다.
        // 앱에 가입된 상태 체크
        //AppMemberEntity appMemberEntity = appMemberDao.get(memberAccessTokenEntity.getApp_srl(),
        //        memberAccessTokenEntity.getMember_srl());
        //if(appMemberEntity == null) {
        //    LOG.info("not found app member from access_token. app_srl [" + memberAccessTokenEntity.getApp_srl() +
        //            "], member_srl [" + memberAccessTokenEntity.getMember_srl() + "], accessToken [" + accessToken + "]");
        //    return null;
        //}
        //
        //if(appMemberEntity.getEnabled() != MDV.YES) {
        //    LOG.info("disabled app member from access_token. app_srl [" + memberAccessTokenEntity.getApp_srl() +
        //            "], member_srl [" + memberAccessTokenEntity.getMember_srl() +
        //            "], enabled [" + appMemberEntity.getEnabled() + "], accessToken [" + accessToken + "]");
        //    return null;
        //}

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        // access token 의 만료 시간은 넉넉하게 30초 만큼 여유 분을 주고 판단 한다.
        if(ltm >= memberAccessTokenEntity.getToken_expire() + 30) {
            LOG.info("access token expire. access_token [" + accessToken + "], current time [" +
                    ltm + "], expire_time [" + memberAccessTokenEntity.getToken_expire() + "]");
            return null;
        }

        LOG.info("valid access_token [" + accessToken + "]");

        return memberAccessTokenEntity;
    }

    @Override
    public int renewalAccessTokenExpireUsingMember(String accessToken, int ltm) {
        if(accessToken == null || StringUtils.equals(StringUtils.trim(accessToken), "")) {
            LOG.info("no access token for renewal access token expire time. access_token [" + accessToken + "]");
            return 0;
        }

        int renewalSec = Integer.parseInt(confCommon.getProperty("access_token_valid_sec"), 10);
        String cacheType = confCommon.getProperty("access_token_repository");

        if(StringUtils.equals(cacheType, "1")) {
            MemberAccessTokenEntity savedAccessTokenEntity = memberAccessTokenDao.get(MDV.NUSE, accessToken);
            if(savedAccessTokenEntity == null) {
                LOG.error("not found access_token for renewal. access_token [" + accessToken + "]");
                return 0;
            }

            MemberAccessTokenEntity memberAccessTokenEntity = new MemberAccessTokenEntity();
            memberAccessTokenEntity.init();
            memberAccessTokenEntity.setToken_expire(ltm + renewalSec);

            AccessTokenExtra accessTokenExtra = null;
            try {
                accessTokenExtra = (AccessTokenExtra) webUtilService.convertJsonBytesToObject(
                        "com.ckstack.ckpush.data.cache.AccessTokenExtra",
                        savedAccessTokenEntity.getUser_data().getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                LOG.warn("can't convert user json string to object. e [" +
                        e.getMessage() + "]");
            }
            if(accessTokenExtra != null) {
                accessTokenExtra.setToken_expire(memberAccessTokenEntity.getToken_expire());

                byte[] objByte = webUtilService.convertObjectToJsonBytes(accessTokenExtra);
                String jsonStr = "";
                try {
                    jsonStr = new String(objByte, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    LOG.warn("can't convert object to json. unsupport utf-8 encoding");
                }
                memberAccessTokenEntity.setUser_data(jsonStr);
            }

            memberAccessTokenDao.modify(memberAccessTokenEntity, MDV.NUSE, accessToken, MDV.NUSE);
            LOG.info("renewal access token expire. access_token [" + accessToken +
                    "], renewal tm [" + memberAccessTokenEntity.getToken_expire() + "]");

        } else {
            AccessTokenExtra accessTokenExtra = customCache.getMemberAccessToken(accessToken);
            if(accessTokenExtra == null) {
                LOG.error("not found access_token in memory for renewal. access_token [" + accessToken + "]");
                return 0;
            }

            accessTokenExtra.setToken_expire(ltm + renewalSec);

            customCache.upsertMemberAccessToken(accessTokenExtra.getMember_srl(), accessTokenExtra.getApp_srl(),
                    accessTokenExtra.getAccess_token(), accessTokenExtra, renewalSec);
            LOG.info("renewal access token expire in memory. access_token [" + accessToken +
                    "], renewal tm [" + accessTokenExtra.getToken_expire() + "]");
        }

        return renewalSec;
    }
}

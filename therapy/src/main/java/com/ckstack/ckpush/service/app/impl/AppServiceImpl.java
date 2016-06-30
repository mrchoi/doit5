package com.ckstack.ckpush.service.app.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.*;
import com.ckstack.ckpush.dao.accessory.DeviceDao;
import com.ckstack.ckpush.domain.app.*;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.message.CustomCache;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.accessory.ServiceHistoryService;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by dhkim94 on 15. 4. 11..
 */
@Service
@Transactional(value = "transactionManager")
public class AppServiceImpl implements AppService {
    private final static Logger LOG = LoggerFactory.getLogger(AppServiceImpl.class);

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private AppDeviceDao appDeviceDao;
    @Autowired
    private ServiceHistoryService serviceHistoryService;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    protected Properties confAccessory;
    @Autowired
    protected Properties confCommon;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private AppGroupDao appGroupDao;
    @Autowired
    private AppGroupMemberDao appGroupMemberDao;
    @Autowired
    private CustomCache customCache;
    @Autowired
    private MessageSource messageSource;

    @Transactional(readOnly = true)
    @Override
    public boolean isDuplicate(String apiKey) {
        if(apiKey == null || StringUtils.trim(apiKey).equals("")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("api_key", "invalid api_key. api_key is empty string or null");
            LOG.error(reason.get("api_key"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        List<AppEntity> appList = appDao.get(null, null, StringUtils.trim(apiKey), null, MDV.NUSE,
                MDV.NUSE, null, 0, 1);
        return (appList.size() > 0);
    }

    @Override
    public void createApp(long memberSrl, AppEntity appEntity, String tryUserId, String ip) {
        if(memberSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "invalid api_key. member_srl is less then zero. member_srl[" + memberSrl + "]");
            LOG.error(reason.get("member_srl"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("appEntity", "invalid appEntity is null");
            LOG.error(reason.get("appEntity"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(appEntity.getApp_id() == null || StringUtils.trim(appEntity.getApp_id()).equals("")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_id", "failed add app. app_id is empty string or null");
            LOG.error(reason.get("app_id"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity checkAppEntity = appDao.get(MDV.NUSE, appEntity.getApp_id(), null);
        if(checkAppEntity != null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_id", "duplicated app_id. app_id [" + appEntity.getApp_id() + "]");
            LOG.error(reason.get("app_id"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(appEntity.getApp_name() == null || StringUtils.trim(appEntity.getApp_name()).equals("")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_name", "failed add app. app_name is empty string or null");
            LOG.error(reason.get("app_name"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(appEntity.getApi_key() == null || StringUtils.trim(appEntity.getApi_key()).equals("")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("api_key", "failed add app. api_key is empty string or null");
            LOG.error(reason.get("api_key"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        checkAppEntity = appDao.get(MDV.NUSE, null, appEntity.getApi_key());
        if(checkAppEntity != null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("api_key", "duplicated api_key. api_key [" + appEntity.getApi_key() + "]");
            LOG.error(reason.get("api_key"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(appEntity.getApp_version() == null || StringUtils.trim(appEntity.getApp_version()).equals("")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_version", "failed add app. api_version is empty string or null");
            LOG.error(reason.get("app_version"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(appEntity.getApi_secret() == null || StringUtils.trim(appEntity.getApi_secret()).equals("")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("api_secret", "failed add app. api_secret is empty string or null");
            LOG.error(reason.get("api_secret"));
            throw new CustomException("add_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        appEntity.setEnabled(MDV.YES);
        appEntity.setReg_terminal(0);
        appEntity.setReg_push_terminal(0);
        appEntity.setC_date(ltm);
        appEntity.setU_date(ltm);

        appDao.add(appEntity);
        LOG.info("add app. appEntity[" + appEntity.toString() + "]");

        Locale locale = LocaleContextHolder.getLocale();

        // 시스템에서 자동 생성하는 앱 기본 그룹을 만든다.
        AppGroupEntity appGroupEntity = new AppGroupEntity();
        appGroupEntity.setApp_srl(appEntity.getApp_srl());
        appGroupEntity.setGroup_type(Integer.parseInt(confCommon.getProperty("system_app_group"), 10));
        appGroupEntity.setEnabled(MDV.YES);
        appGroupEntity.setAllow_default(MDV.NO);
        appGroupEntity.setC_date(ltm);
        appGroupEntity.setU_date(ltm);

        // 앱 관리자 그룹 추가
        appGroupEntity.setGroup_name(messageSource.getMessage("system.app_manager_group_name", null, locale));
        appGroupEntity.setGroup_description(messageSource.getMessage("system.app_manager_group_desc", null, locale));
        appGroupEntity.setAuthority(confCommon.getProperty("group_role_root"));
        appGroupDao.add(appGroupEntity);
        long appRootGroupSrl = appGroupEntity.getApp_group_srl();
        LOG.info("add app manager group. appGroupEntity [" + appGroupEntity.toString() + "]");

        // 앱 정회원 그룹 추가
        appGroupEntity.setGroup_name(messageSource.getMessage("system.app_formal_member_group_name", null, locale));
        appGroupEntity.setGroup_description(messageSource.getMessage("system.app_formal_member_group_desc", null, locale));
        appGroupEntity.setAuthority(confCommon.getProperty("group_role_user"));
        appGroupDao.add(appGroupEntity);
        LOG.info("add app formal member group. appGroupEntity [" + appGroupEntity.toString() + "]");

        // 앱 준회원 그룹 추가
        appGroupEntity.setGroup_name(messageSource.getMessage("system.app_non_formal_member_group_name", null, locale));
        appGroupEntity.setGroup_description(messageSource.getMessage("system.app_non_formal_member_group_desc", null, locale));
        appGroupEntity.setAuthority(confCommon.getProperty("group_role_visitor"));
        appGroupDao.add(appGroupEntity);
        LOG.info("add app non-formal member group. appGroupEntity [" + appGroupEntity.toString() + "]");

        // 앱 루트 그룹에 앱 만든 사람 추가
        AppGroupMemberEntity appGroupMemberEntity = new AppGroupMemberEntity();
        appGroupMemberEntity.setApp_group_srl(appRootGroupSrl);
        appGroupMemberEntity.setMember_srl(memberSrl);
        appGroupMemberEntity.setC_date(ltm);

        appGroupMemberDao.add(appGroupMemberEntity);
        LOG.info("add app group member. appGroupMemberEntity [" + appGroupMemberEntity.toString() + "]");

        // service history 에 앱 등록 이력을 추가 한다.
        // 로그는 addCreateAppHistory 에서 남긴다.
        if(tryUserId == null)   tryUserId = memberDao.get(memberSrl, null).getUser_id();
        Map<Integer, String> app = new HashMap<>();
        app.put(appEntity.getApp_srl(), appEntity.getApp_name());
        serviceHistoryService.addCreateOrDeleteAppHistory(memberSrl, tryUserId, app, ip, true);
    }

    @Transactional(readOnly = true)
    @Override
    public AppEntity getAppInfo(String apiKey) {
        if(apiKey == null || StringUtils.trim(apiKey).equals("")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("api_key", "invalid api_key. api_key is null or empty string");
            LOG.error(reason.get("api_key"));
            throw new CustomException("read_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        return appDao.get(MDV.NUSE, null, StringUtils.trim(apiKey));
    }

    @Transactional(readOnly = true)
    @Override
    public AppEntity getAppInfo(int appSrl) {
        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero. app_srl["+appSrl+"]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("read_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        return appDao.get(appSrl, null, null);
    }

    @Transactional(readOnly = true)
    @Override
    public long getAppCount(String appName, int enabled) {
        if(appName != null) {
            appName = StringUtils.trim(appName);
            if(StringUtils.equals(appName, "")) appName = null;
        }

        return appDao.count(null, appName, null, null, enabled, MDV.NUSE);
    }

    @Transactional(readOnly = true)
    @Override
    public long getAppCount(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.getAppCount(null, MDV.NUSE);

        String appName = null;
        int enabled = MDV.NUSE;

        if(searchFilter.containsKey("app_name")) appName = searchFilter.get("app_name");
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }

        return this.getAppCount(appName, enabled);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Integer, AppEntity> getAppInfo(List<Integer> appSrls, int enabled, int offset, int limit) {
        if(appSrls == null || appSrls.size() <= 0) {
            LOG.warn("invalid app_srls. return zero list");
            return new HashMap<>();
        }

        Map<Integer, AppEntity> ret = new HashMap<>();
        List<AppEntity> appEntities = appDao.get(appSrls, null, null, null, enabled, MDV.NUSE, null, offset, limit);

        for(AppEntity appEntity : appEntities) {
            int appSrl = appEntity.getApp_srl();
            if(!ret.containsKey(appSrl)) ret.put(appSrl, appEntity);
        }

        return ret;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppEntity> getAppInfo(String appName, int enabled,
                                      Map<String, String> sort, int offset, int limit) {
        if(appName != null) {
            appName = StringUtils.trim(appName);
            if(StringUtils.equals(appName, "")) appName = null;
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

        return appDao.get(null, appName, null, null, enabled,
                MDV.NUSE, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppEntity> getAppInfo(Map<String, String> searchFilter, Map<String, String> sort,
                                      int offset, int limit) {
        if(searchFilter == null) return this.getAppInfo(null, MDV.NUSE, sort, offset, limit);

        String appName = null;
        int enabled = MDV.NUSE;

        if(searchFilter.containsKey("app_name")) appName = searchFilter.get("app_name");
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }

        return this.getAppInfo(appName, enabled, sort, offset, limit);
    }

    @Override
    public Map<Integer, String> deleteApp(List<Integer> appSrls, String tryUserId, long tryMemberSrl, String ip) {
        if(appSrls == null || appSrls.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srls", "invalid app_srls. app_srls is null or less then zero");
            LOG.error(reason.get("app_srls"));
            throw new CustomException("delete_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        // 삭제 하는 앱 리스트를 가져온다
        List<AppEntity> appEntities = appDao.get(appSrls, null, null, null,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        appDao.delete(MDV.NUSE, appSrls);
        LOG.info("delete apps. app_srls [" + appSrls + "]");

        // 만일 cache를 사용한다면 앱에 가입되어 있는 모든 사용자의 토큰을 삭제 한다.
        String cacheType = confCommon.getProperty("access_token_repository");
        if(StringUtils.equals(cacheType, "2")) {
            for(int appSrl : appSrls) {
                customCache.deleteMemberAccessToken(MDV.NUSE, appSrl);
                LOG.info("delete member's access_token by delete app. app_srl [" + appSrl + "]");
            }
        }


        // service history 에 앱 삭제 이력을 추가 한다.
        // 로그는 addCreateAppHistory 에서 남긴다.
        Map<Integer, String> app = new HashMap<>();
        for(AppEntity appEntity : appEntities) app.put(appEntity.getApp_srl(), appEntity.getApp_name());
        //serviceHistoryService.addCreateOrDeleteAppHistory(tryMemberSrl, tryUserId, app, ip, false);

        return app;
    }

    @Override
    public void modifyApp(int appSrl, Map<String, String> modifyValue) {
        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero. appSrl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(modifyValue == null || modifyValue.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "invalid modify value. modify value is null or empty. app_srl [" + appSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(!modifyValue.containsKey("app_name") && !modifyValue.containsKey("app_version") &&
                !modifyValue.containsKey("device_class") && !modifyValue.containsKey("enabled")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "not found app modify value. modifyValue [" + modifyValue.toString() + "], app_srl [" +appSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity savedEntity = appDao.get(appSrl, null, null);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("not_found", "not found app. app_srl [" + appSrl + "]");
            LOG.error(reason.get("not_found"));
            throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        boolean willModify = false, willModifyEnabled = false;
        AppEntity appEntity = new AppEntity();
        appEntity.init();

        if(modifyValue.containsKey("app_name")) {
            String appName = modifyValue.get("app_name");
            if(appName == null || StringUtils.equals(StringUtils.trim(appName), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_name", "failed modify app_name. app_name is null or empty");
                LOG.error(reason.get("app_name"));
                throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            int length = appName.getBytes().length;
            if(length > 64) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_name", "failed modify app_name. app_name length less then 64 byte. length [" + length + "]");
                LOG.error(reason.get("app_name"));
                throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!StringUtils.equals(savedEntity.getApp_name(), appName)) {
                appEntity.setApp_name(appName);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("app_version")) {
            String appVersion = modifyValue.get("app_version");
            if(appVersion == null || StringUtils.equals(StringUtils.trim(appVersion), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_version", "failed modify app_version. app_version is null or empty");
                LOG.error(reason.get("app_version"));
                throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            int length = appVersion.getBytes().length;
            if(length > 16) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_version", "failed modify app_version. app_version length less then 16 byte. length [" + length + "]");
                LOG.error(reason.get("app_version"));
                throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            Pattern pattern = Pattern.compile("^[0-9]+(\\.[0-9]+){0,2}");
            if(!pattern.matcher(appVersion).matches()) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_version", "failed modify app_version. app_version invalid format. it support xxx.yyy.zzz form");
                LOG.error(reason.get("app_version"));
                throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!StringUtils.equals(savedEntity.getApp_version(), appVersion)) {
                appEntity.setApp_version(appVersion);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("enabled")) {
            String enabled = modifyValue.get("enabled");
            if(enabled == null || StringUtils.equals(StringUtils.trim(enabled), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. enabled is null or empty");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(enabled)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. enabled is not numeric. enabled [" +
                        enabled +"]");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iEnabled = Integer.parseInt(enabled, 10);
            if(iEnabled != MDV.YES && iEnabled != MDV.NO && iEnabled != MDV.DENY) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. invalid enabled value [" +
                        enabled +"]");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getEnabled() != iEnabled) {
                appEntity.setEnabled(iEnabled);
                willModify = true;
                willModifyEnabled = true;
            }
        }

        // 만일 바뀌는게 하나도 없다면 update 를 하지 않고 무시 한다.
        if(!willModify) {
            LOG.info("ignore modify app. app_srl [" + appSrl +"]. it is same between saved value and changed value");
            return;
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        appEntity.setU_date(ltm);

        appDao.modify(appEntity, appSrl, null);
        LOG.info("modify app. appEntity [" + appEntity.toString() + "]");

        // 만일 cache를 사용하고 app 상태가 enabled이 아니면 cache에 들어 있는 사용자 토큰을 삭제 한다.
        String cacheType = confCommon.getProperty("access_token_repository");
        if(StringUtils.equals(cacheType, "2") && willModifyEnabled) {
            if(appEntity.getEnabled() != MDV.YES) {
                customCache.deleteMemberAccessToken(MDV.NUSE, appSrl);
                LOG.info("delete all member's access_token in app. app_srl [" + appSrl + "]");
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberEntity> getAppManager(int appSrl) {
        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero. appSrl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("get_app_manager_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        List<AppGroupEntity> appGroupEntities = appGroupDao.get(appSrl, null, null, MDV.NUSE,
                confCommon.getProperty("group_role_root"), MDV.YES, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        if(appGroupEntities.size() <= 0) return new ArrayList<>();

        List<Long> groupSrls = new ArrayList<>();
        for(AppGroupEntity appGroupEntity : appGroupEntities)
                groupSrls.add(appGroupEntity.getApp_group_srl());

        List<AppGroupMemberEntity> appGroupMemberEntities = appGroupMemberDao.get(MDV.NUSE, groupSrls,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        if(appGroupMemberEntities.size() <= 0) return new ArrayList<>();

        List<Long> memberSrls = new ArrayList<>();
        for(AppGroupMemberEntity appGroupMemberEntity : appGroupMemberEntities)
                memberSrls.add(appGroupMemberEntity.getMember_srl());

        return memberDao.getFullInfo(memberSrls, null, null, null, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, false);
    }

    @Override
    public void modifyAppManager(int appSrl, List<String> userIds) {
        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("modify_app_manager_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(userIds == null || userIds.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "not found app manager's user id. manager's user id is null or size zero");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("modify_app_manager_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity appEntity = appDao.get(appSrl, null, null);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "not found app of app_srl. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("modify_app_manager_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        // 현재 저장된 관리자의 정보
        List<AppGroupEntity> appGroupEntities = appGroupDao.get(appSrl, null, null, MDV.NUSE,
                confCommon.getProperty("group_role_root"), MDV.YES, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        if(appGroupEntities.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "not found app root group of app_srl. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("modify_app_manager_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        List<Long> groupSrls = new ArrayList<>();
        for(AppGroupEntity appGroupEntity : appGroupEntities) groupSrls.add(appGroupEntity.getApp_group_srl());

        List<AppGroupMemberEntity> prevManagerEntities = appGroupMemberDao.get(MDV.NUSE, groupSrls,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);

        List<Long> prevManagerSrls = new ArrayList<>();
        for(AppGroupMemberEntity appGroupMemberEntity : prevManagerEntities)
            prevManagerSrls.add(appGroupMemberEntity.getMember_srl());

        // 변경될 관리자들의 member_srl 획득
        List<MemberEntity> memberEntities = memberDao.get(userIds, null, null, null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        List<Long> nextManagerSrls = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntities) nextManagerSrls.add(memberEntity.getMember_srl());

        // 기존 관리자에서 빠지는 사람을 구한다.
        List<Long> willRemoveManagerSrls = new ArrayList<>();
        for(AppGroupMemberEntity appGroupMemberEntity : prevManagerEntities) {
            long memberSrl = appGroupMemberEntity.getMember_srl();
            if(!nextManagerSrls.contains(memberSrl)) willRemoveManagerSrls.add(memberSrl);
        }

        // 신규 관리자로 추가 되는 사람을 구한다.
        List<Long> willAddManagerSrls = new ArrayList<>();
        for(long memberSrl : nextManagerSrls) {
            if(!prevManagerSrls.contains(memberSrl)) willAddManagerSrls.add(memberSrl);
        }

        boolean isChanged = false;

        if(willRemoveManagerSrls.size() > 0) {
            appGroupMemberDao.delete(MDV.NUSE, groupSrls, willRemoveManagerSrls, MDV.NUSE);
            LOG.info("remove app manager. app_srl [" + appSrl + "], member_srls [" + willRemoveManagerSrls.toString() + "]");
            isChanged = true;
        }

        if(willAddManagerSrls.size() > 0) {
            int ltm = (int) (DateTime.now().getMillis() / 1000);

            AppGroupMemberEntity appGroupMemberEntity = new AppGroupMemberEntity();
            appGroupMemberEntity.setApp_group_srl(groupSrls.get(0));
            appGroupMemberEntity.setC_date(ltm);

            for (long memberSrl : willAddManagerSrls) {
                appGroupMemberEntity.setMember_srl(memberSrl);
                appGroupMemberDao.add(appGroupMemberEntity);
                LOG.info("add app manager. app_srl [" + appSrl + "], member_srl [" + memberSrl +
                        "], group_srl [" + appGroupMemberEntity.getApp_group_srl() + "]");
            }
            isChanged = true;
        }

        if(!isChanged)
            LOG.info("not changed app manager. it is same between saved manager and changed manager");
    }

    @Transactional(readOnly = true)
    @Override
    public long getAppDeviceCount(int appSrl, List<Integer> appSrls, String deviceId, String deviceType,
                                  String osVersion, String mobilePhoneNumber, int deviceClass, int existPushId,
                                  int allowPush, int enabled) {
        if(appSrls == null || appSrls.size() <= 0) appSrls = null;

        if(deviceId != null) deviceId = StringUtils.trim(deviceId);
        if(deviceId == null || StringUtils.equals(deviceId, "")) deviceId = null;

        if(deviceType != null) deviceType = StringUtils.trim(deviceType);
        if(deviceType == null || StringUtils.equals(deviceType, "")) deviceType = null;

        if(osVersion != null) osVersion = StringUtils.trim(osVersion);
        if(osVersion == null || StringUtils.equals(osVersion, "")) osVersion = null;

        if(mobilePhoneNumber != null) mobilePhoneNumber = StringUtils.trim(mobilePhoneNumber);
        if(mobilePhoneNumber == null || StringUtils.equals(mobilePhoneNumber, "")) mobilePhoneNumber = null;

        if(deviceClass <= 0) deviceClass = MDV.NUSE;

        return deviceDao.countInApp(appSrl, appSrls, MDV.NUSE, deviceId, deviceType, deviceClass, osVersion,
                mobilePhoneNumber, existPushId, allowPush, enabled);
    }

    @Transactional(readOnly = true)
    @Override
    public long getAppDeviceCount(Map<String, String> searchFilter) {
        int appSrl = MDV.NUSE;
        List<Integer> appSrls = null;
        String deviceId = null;
        String deviceType = null;
        String osVersion = null;
        String mobilePhoneNumber = null;
        int deviceClass = MDV.NUSE;
        int regPushId = MDV.NUSE;
        int allowPush = MDV.NUSE;
        int enabled = MDV.NUSE;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) num = StringUtils.trim(num);
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("app_srls")) {
            String str = searchFilter.get("app_srls");
            if(str != null) str = StringUtils.trim(str);
            if(!StringUtils.equals(str, "")) {
                String[] arr = StringUtils.split(str, ",");
                appSrls = new ArrayList<>();
                for(String element : arr) appSrls.add(Integer.parseInt(element, 10));
            }
        }
        if(searchFilter.containsKey("device_id")) deviceId = searchFilter.get("device_id");
        if(searchFilter.containsKey("device_type")) deviceType = searchFilter.get("device_type");
        if(searchFilter.containsKey("os_version")) osVersion = searchFilter.get("os_version");
        if(searchFilter.containsKey("mobile_phone_number")) mobilePhoneNumber = searchFilter.get("mobile_phone_number");
        if(searchFilter.containsKey("device_class")) {
            String num = searchFilter.get("device_class");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) deviceClass = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("reg_push_id")) {
            String num = searchFilter.get("reg_push_id");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) regPushId = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_push")) {
            String num = searchFilter.get("allow_push");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowPush = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }

        return this.getAppDeviceCount(appSrl, appSrls, deviceId, deviceType, osVersion, mobilePhoneNumber,
                deviceClass, regPushId, allowPush, enabled);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeviceEntity> getAppDevice(int appSrl, List<Integer> appSrls, long deviceSrl, String deviceId,
                                           String deviceType, String osVersion, String mobilePhoneNumber,
                                           int deviceClass, int existPushId, int allowPush, int enabled,
                                           Map<String, String> sort, int offset, int limit) {
        if(appSrls == null || appSrls.size() <= 0) appSrls = null;

        if(deviceSrl <= 0) deviceSrl = MDV.NUSE;

        if(deviceId != null) deviceId = StringUtils.trim(deviceId);
        if(deviceId == null || StringUtils.equals(deviceId, "")) deviceId = null;

        if(deviceType != null) deviceType = StringUtils.trim(deviceType);
        if(deviceType == null || StringUtils.equals(deviceType, "")) deviceType = null;

        if(osVersion != null) osVersion = StringUtils.trim(osVersion);
        if(osVersion == null || StringUtils.equals(osVersion, "")) osVersion = null;

        if(mobilePhoneNumber != null) mobilePhoneNumber = StringUtils.trim(mobilePhoneNumber);
        if(mobilePhoneNumber == null || StringUtils.equals(mobilePhoneNumber, "")) mobilePhoneNumber = null;

        if(deviceClass <= 0) deviceClass = MDV.NUSE;

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

        return deviceDao.getInApp(appSrl, appSrls, deviceSrl, deviceId, deviceType, deviceClass, osVersion,
                mobilePhoneNumber, existPushId, allowPush, enabled, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeviceEntity> getAppDevice(Map<String, String> searchFilter,
                                           Map<String, String> sort, int offset, int limit) {
        if(searchFilter == null )
            return this.getAppDevice(MDV.NUSE, null, MDV.NUSE, null, null, null, null, MDV.NUSE,
                    MDV.NUSE, MDV.NUSE, MDV.NUSE, sort, offset, limit);

        int appSrl = MDV.NUSE;
        List<Integer> appSrls = null;
        String deviceId = null;
        String deviceType = null;
        String osVersion = null;
        String mobilePhoneNumber = null;
        int deviceClass = MDV.NUSE;
        int regPushId = MDV.NUSE;
        int allowPush = MDV.NUSE;
        int enabled = MDV.NUSE;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("app_srls")) {
            String str = searchFilter.get("app_srls");
            if(str != null) str = StringUtils.trim(str);
            if(!StringUtils.equals(str, "")) {
                String[] arr = StringUtils.split(str, ",");
                appSrls = new ArrayList<>();
                for(String element : arr) appSrls.add(Integer.parseInt(element, 10));
            }
        }
        if(searchFilter.containsKey("device_id")) deviceId = searchFilter.get("device_id");
        if(searchFilter.containsKey("device_type")) deviceType = searchFilter.get("device_type");
        if(searchFilter.containsKey("os_version")) osVersion = searchFilter.get("os_version");
        if(searchFilter.containsKey("mobile_phone_number")) mobilePhoneNumber = searchFilter.get("mobile_phone_number");
        if(searchFilter.containsKey("device_class")) {
            String num = searchFilter.get("device_class");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) deviceClass = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("reg_push_id")) {
            String num = searchFilter.get("reg_push_id");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) regPushId = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_push")) {
            String num = searchFilter.get("allow_push");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowPush = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }

        return this.getAppDevice(appSrl, appSrls, MDV.NUSE, deviceId, deviceType, osVersion, mobilePhoneNumber,
                deviceClass, regPushId, allowPush, enabled, sort, offset, limit);
    }

    @Override
    public void joinDeviceInApp(DeviceEntity deviceEntity, int appSrl, String pushId) {
        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity appEntity = appDao.get(appSrl, null, null);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "failed add device in app. not found app. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(appEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "failed add device in app. disabled app. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(deviceEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("device", "failed add device in app. invalid deviceEntity. deviceEntity is null");
            LOG.error(reason.get("device"));
            throw new CustomException("add_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(deviceEntity.getDevice_id() == null ||
                StringUtils.equals(StringUtils.trim(deviceEntity.getDevice_id()), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("device_id", "failed add device in app. device_id is empty string or null");
            LOG.error(reason.get("device_id"));
            throw new CustomException("add_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        int ltm = (int) (DateTime.now().getMillis() / 1000);
        DeviceEntity savedDevice = deviceDao.get(MDV.NUSE, deviceEntity.getDevice_id());

        if(savedDevice == null) {
            if (deviceEntity.getDevice_type() == null) deviceEntity.setDevice_type("");
            deviceEntity.setDevice_type(StringUtils.trim(deviceEntity.getDevice_type()));

            // 지원하는 단말기 타입을 넣는다.
            String[] arrSupportTerminal = StringUtils.split(confCommon.getProperty("user_terminal_support"), ",");
            List<Integer> supportTerminal = new ArrayList<>();

            assert arrSupportTerminal != null;
            for (String element : arrSupportTerminal) {
                String[] values = StringUtils.split(element, ":");

                if (values.length < 2) {
                    LOG.warn("invalid support terminal define. value[" + element + "]");
                    continue;
                }

                supportTerminal.add(Integer.parseInt(StringUtils.trim(values[0]), 10));
            }

            if (!supportTerminal.contains(deviceEntity.getDevice_class())) {
                Map<String, String> reason = new HashMap<>();
                reason.put("device_class", "failed add device in app. not support device_class [" +
                        deviceEntity.getDevice_class() + "]");
                LOG.error(reason.get("device_class"));
                throw new CustomException("add_device_in_app_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if (deviceEntity.getOs_version() == null) deviceEntity.setOs_version("");
            deviceEntity.setOs_version(StringUtils.trim(deviceEntity.getOs_version()));

            if (deviceEntity.getMobile_phone_number() == null) deviceEntity.setMobile_phone_number("");
            deviceEntity.setMobile_phone_number(webUtilService.getPhoneNumber(deviceEntity.getMobile_phone_number()));

            deviceEntity.setC_date(ltm);
            deviceEntity.setU_date(ltm);

            deviceDao.add(deviceEntity);
            LOG.info("add device. deviceEntity [" + deviceEntity.toString() + "]");
        } else {
            deviceEntity.setDevice_srl(savedDevice.getDevice_srl());
        }

        AppDeviceEntity prevAppDeviceEntity = appDeviceDao.get(appSrl, deviceEntity.getDevice_srl(), MDV.NUSE);
        if(prevAppDeviceEntity != null) {
            LOG.info("already registed in app. device_srl [" + deviceEntity.getDevice_srl() +
                    "], app_srl [" + appSrl + "]");
            return;
        }

        AppDeviceEntity appDeviceEntity = new AppDeviceEntity();

        appDeviceEntity.setApp_srl(appSrl);
        appDeviceEntity.setDevice_srl(deviceEntity.getDevice_srl());

        if(pushId == null)  appDeviceEntity.setPush_id("");
        else                appDeviceEntity.setPush_id(StringUtils.trim(pushId));

        if(StringUtils.equals(appDeviceEntity.getPush_id(), ""))
            appDeviceEntity.setReg_push_id(MDV.NO);
        else
            appDeviceEntity.setReg_push_id(MDV.YES);

        appDeviceEntity.setAllow_push(MDV.YES);
        appDeviceEntity.setEnabled(MDV.YES);
        appDeviceEntity.setC_date(ltm);
        appDeviceEntity.setU_date(ltm);

        appDeviceDao.add(appDeviceEntity);
        LOG.info("mapping between app and device. appDeviceEntity [" + appDeviceEntity.toString() + "]");

        appDao.increase(appSrl, null, true, appDeviceEntity.getReg_push_id() == MDV.YES, false);
        LOG.info("increase terminal count in app. app_srl [" + appSrl + "]");
    }

    @Override
    public void deleteDeviceInApp(List<Map<String, Object>> deleteData) {
        if(deleteData == null || deleteData.size() <= 0) {
            LOG.warn("ignore delete device in app. request data is null or empty list");
            return;
        }

        // 하나씩 삭제 해야 한다.
        boolean isDecreasePushTerminalCount;
        for(Map<String, Object> element : deleteData) {
            int appSrl = Integer.parseInt(element.get("app_srl").toString(), 10);
            long deviceSrl = Long.parseLong(element.get("device_srl").toString(), 10);

            // 삭제시에 카운트도 같이 변경 되어야 하기 때문에 간단하게 삭제는 되지 않는다.
            // 어드민이기 때문에 비효율적인 쿼리 동작으로 돌아가도 그대로 둔다.
            // 서비스에서는 현재 방식으로 삭제하는 로직이 들어가면 성능 이슈가 생긴다.
            AppDeviceEntity appDeviceEntity = appDeviceDao.get(appSrl, deviceSrl, MDV.NUSE);
            if(appDeviceEntity == null) {
                LOG.warn("not found app device. ignore delete. app_srl [" + appSrl + "], device_srl [" + deviceSrl + "]");
                continue;
            }

            isDecreasePushTerminalCount = false;

            if(appDeviceEntity.getReg_push_id() == MDV.YES && appDeviceEntity.getAllow_push() == MDV.YES &&
                    appDeviceEntity.getEnabled() == MDV.YES) {
                isDecreasePushTerminalCount = true;
            }

            appDeviceDao.delete(appSrl, deviceSrl);
            LOG.info("delete app device. app_srl [" + appSrl + "], device_srl [" + deviceSrl + "]");

            appDao.decrease(appSrl, null, true, isDecreasePushTerminalCount);

            if(isDecreasePushTerminalCount)
                LOG.info("decrease registed terminal count and push terminal count in app. app_srl [" + appSrl + "]");
            else
                LOG.info("decrease registed terminal count in app. app_srl [" + appSrl + "]");
        }

        // NOTE app 에 매핑 되지 않은 device 는 cron 을 돌면서 삭제 하도록 한다.
    }
}

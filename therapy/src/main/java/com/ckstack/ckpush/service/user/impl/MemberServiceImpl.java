package com.ckstack.ckpush.service.user.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.app.AppGroupDao;
import com.ckstack.ckpush.dao.app.AppGroupMemberDao;
import com.ckstack.ckpush.dao.app.AppMemberDao;
import com.ckstack.ckpush.dao.user.*;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.app.AppGroupEntity;
import com.ckstack.ckpush.domain.app.AppGroupMemberEntity;
import com.ckstack.ckpush.domain.app.AppMemberEntity;
import com.ckstack.ckpush.domain.user.*;
import com.ckstack.ckpush.message.CustomCache;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.user.MemberService;
import com.ckstack.ckpush.dao.mineral.FileDao;
import com.ckstack.ckpush.dao.mineral.MemberPicDao;
import com.ckstack.ckpush.domain.mineral.FileEntity;
import com.ckstack.ckpush.domain.mineral.MemberPicEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by dhkim94 on 15. 3. 27..
 */
@Service
@Transactional(value = "transactionManager")
public class MemberServiceImpl implements MemberService {
    private final static Logger LOG = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberExtraDao memberExtraDao;
    @Autowired
    private GroupMemberDao groupMemberDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private AppGroupDao appGroupDao;
    @Autowired
    private AppGroupMemberDao appGroupMemberDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private MemberPicDao memberPicDao;
    @Autowired
    private AppMemberDao appMemberDao;
    @Autowired
    private MemberDeviceDao memberDeviceDao;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private CustomCache customCache;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confTblFile;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ZipCodeDao zipCodeDao;

    @Transactional(readOnly = true)
    @Override
    public MemberEntity getMemberInfo(long memberSrl) {
        if(memberSrl <= 0) {
            LOG.error("invalid memeber_srl[" + memberSrl + "]");
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "member_srl is less then zero. member_srl [" + memberSrl + "]");
            throw new CustomException("invalid_method_parameter", reason);
        }

        MemberEntity memberEntity = memberDao.getFullInfo(memberSrl, null);
        if(memberEntity == null) return null;

        // 유저 프로필 이미지를 구한다.
        MemberPicEntity memberPicEntity = memberPicDao.getAndFile(memberSrl, MDV.NO);
        if(memberPicEntity != null) memberEntity.setFileEntities(memberPicEntity.getFileEntities());

        return memberEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public MemberEntity getMemberInfo(String userId) {
        if(userId == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "user_id is null");
            LOG.error(reason.get("user_id"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        MemberEntity memberEntity = memberDao.getFullInfo(MDV.NUSE, userId);
        if(memberEntity == null) return null;

        // 유저 프로필 이미지를 구한다.
        MemberPicEntity memberPicEntity = memberPicDao.getAndFile(memberEntity.getMember_srl(), MDV.NO);
        if(memberPicEntity != null) memberEntity.setFileEntities(memberPicEntity.getFileEntities());

        return memberEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public long getMemberInfoNick(String nickName) {
        if(nickName == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "user_id is null");
            LOG.error(reason.get("user_id"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        long memberEntity = memberDao.count(null, nickName, null, MDV.NUSE);
        if(memberEntity < 0) return 0;

        return memberEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public MemberExtraEntity getMemberextraInfo(long memberSrl) {
        if(memberSrl <= 0) {
            LOG.error("invalid memeber_srl[" + memberSrl + "]");
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "member_srl is less then zero. member_srl [" + memberSrl + "]");
            throw new CustomException("invalid_method_parameter", reason);
        }

        MemberExtraEntity memberExtraEntity = memberExtraDao.get(memberSrl);
        if(memberExtraEntity == null) return null;



        return memberExtraEntity;
    }


    @Transactional(readOnly = true)
    @Override
    public List<MemberEntity> getMemberList(String userId, String userName, int enabled, int signOut,
                                            Map<String, String> sort, int offset, int limit) {
        if(sort == null) sort = new HashMap<>();
        if(sort.size() <= 0) sort.put("user_id", "asc");

        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }

        // 유저 프로필 이미지를 구한다.
        List<MemberEntity> memberEntities = memberDao.getFullInfo(null, userId, userName, null, null, enabled,
                signOut, MDV.NUSE, null, MDV.NUSE, sort, offset, limit, true);

        if(memberEntities.size() <= 0) return memberEntities;

        List<Long> memberSrls = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntities) memberSrls.add(memberEntity.getMember_srl());

        // 위치에 맞게 프로필 이미지를 넣어 준다.
        List<MemberPicEntity> memberPicEntities = memberPicDao.getAndFile(memberSrls, MDV.NO);
        int index;
        for(MemberPicEntity memberPicEntity : memberPicEntities) {
            if((index=memberSrls.indexOf(memberPicEntity.getMember_srl())) <= -1) continue;
            memberEntities.get(index).setFileEntities(memberPicEntity.getFileEntities());
        }

        return memberEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberEntity> getMemberList(Map<String, String> searchFilter, Map<String, String> sort,
                                            int offset, int limit) {
        if(searchFilter == null )
            return this.getMemberList(null, null, MDV.NUSE, MDV.NUSE, sort, offset, limit);

        String userId = null;
        String userName = null;
        int enabled = MDV.NUSE;
        int signOut = MDV.NUSE;

        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("sign_out")) {
            String num = searchFilter.get("sign_out");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) signOut = Integer.parseInt(num, 10);
        }

        return this.getMemberList(userId, userName, enabled, signOut, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public long countMemberInfo(String userId, String userName, int enabled, int signOut) {
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }

        return memberDao.countFullInfo(userId, userName, null, null, enabled, signOut,
                MDV.NUSE, null, MDV.NUSE, true);
    }

    @Transactional(readOnly = true)
    @Override
    public long countMemberInfo(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.countMemberInfo(null, null, MDV.NUSE, MDV.NUSE);

        String userId = null;
        String userName = null;
        int enabled = MDV.NUSE;
        int signOut = MDV.NUSE;

        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("signOut")) {
            String num = searchFilter.get("signOut");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) signOut = Integer.parseInt(num, 10);
        }

        return this.countMemberInfo(userId, userName, enabled, signOut);
    }

    @Override
    public void enabledMember(String userId, int state) {
        if(userId == null) {
            LOG.error("invalid user_id is null");
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "invalid user_id. user_id is null");
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(state != MDV.YES && state != MDV.NO && state != MDV.DENY) {
            LOG.error("invalid user state. user_id [" + userId + "], state [" + state + "]");
            Map<String, String> reason = new HashMap<>();
            reason.put("state", "invalid state value");
            throw new CustomException("modify_member_error", reason);
        }

        MemberEntity memberEntity = memberDao.get(MDV.NUSE, userId);
        if(memberEntity == null) {
            LOG.error("not found user. user_id[" + userId + "]");
            Map<String, String> reason = new HashMap<>();
            reason.put("memberEntity", "memberEntity is null. not found user of user_id ["+userId+"]");
            throw new CustomException("member_not_found_error", reason);
        }

        MemberEntity updateVo = new MemberEntity();
        updateVo.init();
        updateVo.setEnabled(state);
        updateVo.setU_date((int) (DateTime.now().getMillis() / 1000));
        memberDao.modify(updateVo, MDV.NUSE, userId);
        LOG.info("change member's enabled from[" + memberEntity.getEnabled() + "] to[" + updateVo.getEnabled() + "]");

        // 만일 cache를 사용하고 enabled이 true가 아니면 cache에 들어 있는 사용자 상태를 삭제 한다.
        String cacheType = confCommon.getProperty("access_token_repository");
        if(StringUtils.equals(cacheType, "2")) {
            int memberState;
            if(memberEntity.getSign_out() == MDV.YES)   memberState = MDV.NO;
            else                                        memberState = state == MDV.YES ? MDV.YES : MDV.NO;

            if(memberState != MDV.YES) {
                customCache.deleteMemberAccessToken(memberEntity.getMember_srl(), MDV.NUSE);
                LOG.info("delete member's access_token in memory. member state disable. member_srl [" +
                        memberEntity.getMember_srl() + "]");
            }
        }

        // TODO history 에 넣어야 함.
    }

    @Override
    public FileEntity addMemberProfileImage(long memberSrl, long fileSrl) {
        if(memberSrl <= 0 || fileSrl <= 0) {
            LOG.error("invalid member_srl[" + memberSrl + "], file_srl[" + fileSrl + "]");
            Map<String, String> reason = new HashMap<>();
            if(memberSrl <= 0)  reason.put("member_srl", "member_srl is less then zero. member_srl ["+memberSrl+"]");
            if(fileSrl <= 0)    reason.put("file_srl", "file_srl is less then zero. file_srl ["+fileSrl+"]");
            throw new CustomException("invalid_method_parameter", reason);
        }

        memberPicDao.delete(memberSrl, MDV.NUSE);

        // 파일이 실제 등록되어 있는지 체크
        FileEntity fileEntity = fileDao.get(confTblFile.getProperty("tbl_name_profile_file"), fileSrl, MDV.NO);
        if(fileEntity == null) {
            LOG.error("failed add member profile image. no file. file_srl[" + fileSrl + "]");
            Map<String, String> reason = new HashMap<>();
            reason.put("fileEntity", "fileEntity is null. no user profile image file of file_srl ["+fileSrl+"]");
            throw new CustomException("add_member_profile_image_error", reason);
        }

        MemberPicEntity memberPicEntity = new MemberPicEntity();
        memberPicEntity.setMember_srl(memberSrl);
        memberPicEntity.setFile_srl(fileSrl);
        memberPicEntity.setC_date((int) (DateTime.now().getMillis() / 1000));

        memberPicDao.add(memberPicEntity);
        LOG.info("add member profile image. memberPicEntity["+ memberPicEntity.toString()+"]");

        return fileEntity;
    }

    @Override
    public void addMemberDevice(MemberDeviceEntity memberDeviceEntity) {
        if(memberDeviceEntity.getApp_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "failed add member device.");
            LOG.error(reason.get("app_srl") + " no app. app_srl [" + memberDeviceEntity.getApp_srl() + "]");
            throw new CustomException("reg_member_device_error", reason);
        }

        if(memberDeviceEntity.getMember_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "failed add member device.");
            LOG.error(reason.get("member_srl") + " no member. member_srl [" + memberDeviceEntity.getMember_srl() + "]");
            throw new CustomException("reg_member_device_error", reason);
        }

        AppMemberEntity appMemberEntity = appMemberDao.get(memberDeviceEntity.getApp_srl(),
                memberDeviceEntity.getMember_srl());
        if(appMemberEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "failed add member device.");
            LOG.error(reason.get("invalid") + " not registed in app. member_srl [" +
                    memberDeviceEntity.getMember_srl() + "], app_srl [" + memberDeviceEntity.getApp_srl() + "]");
            throw new CustomException("reg_member_device_error", reason);
        }

        if(memberDeviceEntity.getDevice_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("device_srl", "failed add member device.");
            LOG.error(reason.get("device_srl") + " no device. device_srl [" + memberDeviceEntity.getDevice_srl() + "]");
            throw new CustomException("reg_member_device_error", reason);
        }

        MemberDeviceEntity savedEntity = memberDeviceDao.get(memberDeviceEntity.getMember_srl(),
                memberDeviceEntity.getApp_srl(), memberDeviceEntity.getDevice_srl());
        if(savedEntity != null) {
            LOG.info("already added member device. app_srl [" + memberDeviceEntity.getApp_srl() +
                    "], member_srl [" + memberDeviceEntity.getMember_srl() + "], device_srl [" +
                    memberDeviceEntity.getDevice_srl() + "]");
            return;
        }

        memberDeviceEntity.setMobile_phone_number(webUtilService.getPhoneNumber(memberDeviceEntity.getMobile_phone_number()));

        memberDeviceEntity.setAllow_push_noti(MDV.YES);
        memberDeviceEntity.setPush_noti_type(MDV.PUSH_NOTI_SOUND);

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        memberDeviceEntity.setC_date(ltm);
        memberDeviceEntity.setU_date(ltm);

        memberDeviceDao.add(memberDeviceEntity);
        LOG.info("add member device. memberDeviceEntity [" + memberDeviceEntity.toString() + "]");
    }

    /**
     * 외부 이미지를 사용자 프로필 이미지로 등록 한다.
     *
     * @param memberSrl 프로필 이미지를 등록할 사용자 시리얼 넘버
     * @param fileURL 프로필 이미지 URL
     * @return 등록된 파일 정보
     */
    private FileEntity addMemberProfileImage(long memberSrl, String fileURL, String ipAddress) {
        if(memberSrl <= 0 || fileURL == null) {
            LOG.error("invalid member_srl[" + memberSrl + "], fileURL[" + fileURL + "]");
            Map<String, String> reason = new HashMap<>();
            if(memberSrl <= 0)  reason.put("member_srl", "member_srl is less then zero. member_srl ["+memberSrl+"]");
            if(fileURL == null)    reason.put("file_url", "file_url is null");
            throw new CustomException("invalid_method_parameter", reason);
        }

        memberPicDao.delete(memberSrl, MDV.NUSE);

        // 물리 파일이 없는 URL을 파일로 등록
        FileEntity fileEntity = new FileEntity();
        fileEntity.setOrig_name("");
        fileEntity.setFile_size(0);
        fileEntity.setFile_path("");
        fileEntity.setFile_url(fileURL);
        fileEntity.setWidth(0);
        fileEntity.setHeight(0);
        fileEntity.setThumb_path("");
        fileEntity.setThumb_url("");
        fileEntity.setThumb_width(0);
        fileEntity.setThumb_height(0);
        fileEntity.setIpaddress(ipAddress);
        fileEntity.setDeleted(MDV.NO);
        int ltm = (int)(DateTime.now().getMillis() / 1000);
        fileEntity.setC_date(ltm);
        fileEntity.setU_date(ltm);

        fileDao.add(confTblFile.getProperty("tbl_name_profile_file"), fileEntity);

        MemberPicEntity memberPicEntity = new MemberPicEntity();
        memberPicEntity.setMember_srl(memberSrl);
        memberPicEntity.setFile_srl(fileEntity.getFile_srl());
        memberPicEntity.setC_date(ltm);

        memberPicDao.add(memberPicEntity);
        LOG.info("add member profile outer image. memberPicEntity[" + memberPicEntity.toString() + "]");

        return fileEntity;
    }

    @Override
    public void signOutMember(String userId) {
        // NOTE 현재는 사용자의 상태만 탈퇴로 바꾸고 있지만, 추후 비지니스 로직에 따라 탈퇴 처리를 해 줘야 한다.

        if(userId == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "invalid user_id. user_id is null");
            LOG.error(reason.get("user_id"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        MemberEntity memberEntity = memberDao.get(MDV.NUSE, userId);
        if(memberEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "not found user for user_id ["+userId+"]");
            LOG.error(reason.get("user_id"));
            throw new CustomException("member_not_found_error", reason);
        }

        MemberEntity updateVo = new MemberEntity();
        updateVo.init();
        updateVo.setSign_out(MDV.YES);
        updateVo.setU_date((int) (DateTime.now().getMillis() / 1000));
        memberDao.modify(updateVo, MDV.NUSE, userId);
        LOG.info("sign out member. user_id["+userId+"]");

        // 만일 cache를 사용한다면 cache에 들어 있는 사용자 접속 토큰을 모두 삭제 한다.
        String cacheType = confCommon.getProperty("access_token_repository");
        if(StringUtils.equals(cacheType, "2")) {
            customCache.deleteMemberAccessToken(memberEntity.getMember_srl(), MDV.NUSE);
            LOG.info("delete member's access_token in memory. member sign out. member_srl [" +
                    memberEntity.getMember_srl() + "]");
        }

        // TODO history 에 넣어야 함
    }

    @Override
    public void deleteMember(List<Long> memberSrls) {
        if(memberSrls == null || memberSrls.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srls", "invalid member_srls. member_srls is null or size zero");
            LOG.error(reason.get("member_srls"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        // 만일 사용자의 프로필 파일이 있으면 삭제 flag로 표시 한다.
        List<MemberPicEntity> memberPicEntities = memberPicDao.get(MDV.NUSE, memberSrls, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        if(memberPicEntities.size() > 0) {
            List<Long> fileSrls = new ArrayList<>();
            for(MemberPicEntity memberPicEntity : memberPicEntities)
                fileSrls.add(memberPicEntity.getFile_srl());

            FileEntity fileEntity = new FileEntity();
            fileEntity.init();
            fileEntity.setDeleted(MDV.YES);

            fileDao.modify(confTblFile.getProperty("tbl_name_profile_file"), fileEntity, MDV.NUSE, fileSrls);
            LOG.info("set delete flag. members profile image. file_srls [" + fileSrls.toString() +
                    "], membe_srls [" + memberSrls.toString() + "]");
        }

        memberDao.delete(MDV.NUSE, null, memberSrls);
        LOG.info("delete member. member_srls [" + memberSrls.toString() + "]");

        // 만일 cache를 사용한다면 cache에 들어 있는 사용자 토큰도 삭제 한다.
        String cacheType = confCommon.getProperty("access_token_repository");
        if(StringUtils.equals(cacheType, "2")) {
            for(long memberSrl : memberSrls) {
                customCache.deleteMemberAccessToken(memberSrl, MDV.NUSE);
                LOG.info("delete access_token in cache. member_srl [" + memberSrl + "]");
            }
        }
    }

    @Override
    public void signUp(MemberEntity memberEntity, long fileSrl, String fileURL, int groupSrl,
                       long tryMemberSrl, String ip) {
        if(memberEntity == null) {
            LOG.error("invalid memberEntity is null");
            Map<String, String> reason = new HashMap<>();
            reason.put("memberEntity", "memberEntity is null");
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(memberEntity.getUser_id() == null || StringUtils.trim(memberEntity.getUser_id()).equals("")) {
            LOG.error("failed add member. user_id is empty string or null");
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "user_id is null or empty string");
            throw new CustomException("add_member_error", reason);
        }

        // 아이디 중복 체크(DB 에러로 처리하면 쿼리문을 그대로 출력함)
        MemberEntity duplicatedEntity = memberDao.get(MDV.NUSE, memberEntity.getUser_id());
        if(duplicatedEntity != null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "duplicated user id");
            LOG.error("failed add member. "+reason.get("user_id"));
            throw new CustomException("add_member_error", reason);
        }

        if(memberEntity.getUser_password() == null || StringUtils.trim(memberEntity.getUser_password()).equals("")) {
            LOG.error("failed add member. user_password is empty string or null");
            Map<String, String> reason = new HashMap<>();
            reason.put("user_password", "user_password is null or empty string");
            throw new CustomException("add_member_error", reason);
        }
        String hashPassword = passwordEncoder.encode(memberEntity.getUser_password());
        memberEntity.setUser_password(hashPassword);

        if(memberEntity.getUser_name() == null || StringUtils.trim(memberEntity.getUser_name()).equals("")) {
            LOG.error("failed add member. user_name is empty string or null");
            Map<String, String> reason = new HashMap<>();
            reason.put("user_name", "user_name is null or empty string");
            throw new CustomException("add_member_error", reason);
        }
        memberEntity.setUser_name(StringUtils.trim(memberEntity.getUser_name()));

        // 휴대폰 번호는 없는 것을 허용 한다.
        //if(memberEntity.getMobile_phone_number() == null || StringUtils.trim(memberEntity.getMobile_phone_number()).equals("")) {
        //    LOG.error("failed add member. mobile_phone_number is empty string or null");
        //    Map<String, String> reason = new HashMap<>();
        //    reason.put("mobile_phone_number", "mobile_phone_number is empty string or null");
        //    throw new CustomException("add_member_error", reason);
        //}
        memberEntity.setMobile_phone_number(webUtilService.getPhoneNumber(memberEntity.getMobile_phone_number()));

        if(memberEntity.getEmail_address() == null) memberEntity.setEmail_address("");
        else                                        memberEntity.setEmail_address(StringUtils.trim(memberEntity.getEmail_address()));

        // 사용자 추가 할때 nick_name이 없다면 user_name과 동일하게 처리 한다.
        if(memberEntity.getNick_name() == null || StringUtils.equals(StringUtils.trim(memberEntity.getNick_name()), ""))
            memberEntity.setNick_name(memberEntity.getUser_name());
        else
            memberEntity.setNick_name(StringUtils.trim(memberEntity.getNick_name()));

        if(memberEntity.getAllow_mailing() != MDV.NO)   memberEntity.setAllow_mailing(MDV.YES);
        if(memberEntity.getAllow_message() != MDV.NO)   memberEntity.setAllow_message(MDV.YES);
        if(memberEntity.getDescription() == null)       memberEntity.setDescription("");

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        memberEntity.setLast_login_date(ltm);
        memberEntity.setChange_password_date(ltm);
        memberEntity.setC_date(ltm);
        memberEntity.setU_date(ltm);

        if(memberEntity.getEnabled() != MDV.NO && memberEntity.getEnabled() != MDV.DENY) memberEntity.setEnabled(MDV.YES);
        if(memberEntity.getEmail_confirm() != MDV.YES)  memberEntity.setEmail_confirm(MDV.NO);
        if(memberEntity.getSign_out() != MDV.YES)       memberEntity.setSign_out(MDV.NO);

        memberDao.add(memberEntity);
        LOG.info("add member. memberEntity["+ memberEntity.toString()+"]");

        // extra 정보를 insert 한다.
        MemberExtraEntity memberExtraEntity = memberEntity.getMemberExtraEntity();
        if(memberExtraEntity == null) {
            memberExtraEntity = new MemberExtraEntity();
            memberExtraEntity.init();
        }

        memberExtraEntity.setMember_srl(memberEntity.getMember_srl());

        List<Integer> supportSocialType = new ArrayList<>();
        for(int element : MDV.SOCIAL_SUPPORT)  supportSocialType.add(element);

        if(memberExtraEntity.getSocial_type() <= 0) {
            memberExtraEntity.setSocial_type(MDV.SOCIAL_NONE);
            memberExtraEntity.setSocial_id("");
        } else {
            int userSocialType = memberExtraEntity.getSocial_type();
            if(!supportSocialType.contains(userSocialType)) {
                LOG.error("failed add member. not support social type["+userSocialType+"]");
                Map<String, String> reason = new HashMap<>();
                reason.put("social_type", "not support social_type ["+userSocialType+"]");
                throw new CustomException("add_member_error", reason);
            } else {
                if(memberExtraEntity.getSocial_id() == null ||
                        StringUtils.trim(memberExtraEntity.getSocial_id()).equals("")) {
                    LOG.error("failed add member. no social_id. social_type["+userSocialType+
                            "], social_id is null or empty string");
                    Map<String, String> reason = new HashMap<>();
                    reason.put("social_id", "not found social_id. social_id is empty string or null");
                    throw new CustomException("add_member_error", reason);
                }
                memberExtraEntity.setSocial_type(userSocialType);
                memberExtraEntity.setSocial_id(StringUtils.trim(memberExtraEntity.getSocial_id()));
            }
        }

        if (memberEntity.getCertificate() != null) memberExtraEntity.setCertificate(memberEntity.getCertificate());
        if (memberEntity.getKakao_id() != null) memberExtraEntity.setKakao_id(memberEntity.getKakao_id());
        if (memberEntity.getClass_srl() > MDV.NUSE) memberExtraEntity.setClass_srl(memberEntity.getClass_srl());
        if (memberEntity.getDomain_srl() > MDV.NUSE) memberExtraEntity.setDomain_srl(memberEntity.getDomain_srl());
        if (memberEntity.getSelf_introduction() != null) memberExtraEntity.setSelf_introduction(memberEntity.getSelf_introduction());
        if (memberEntity.getUser_group_srl() > MDV.NUSE) memberExtraEntity.setUser_group_srl(memberEntity.getUser_group_srl());

        memberExtraEntity.setGroup_gubun(memberEntity.getGroup_gubun());
        memberExtraEntity.setLogin_count(1);
        memberExtraEntity.setSerial_login_count(1);
        memberExtraEntity.setC_date(ltm);
        memberExtraEntity.setU_date(ltm);
        memberExtraEntity.setAcademic_srl(0);


        memberExtraDao.add(memberExtraEntity);
        memberEntity.setMemberExtraEntity(memberExtraEntity);
        LOG.info("add member. memberExtraEntity[" + memberExtraEntity.toString() + "]");

        // 사용자를 그룹에 가입 시킨다.
        // 로그는 joinMemberGroup 에서 남긴다.
        this.joinMemberGroup(memberEntity.getMember_srl(), groupSrl);

        // 사용자 프로필 이미지 추가
        // 로그는 addMemberProfileImage 에서 남긴다.
        if(fileSrl > 0) {
            List<FileEntity> fileEntities = new ArrayList<>();
            fileEntities.add(this.addMemberProfileImage(memberEntity.getMember_srl(), fileSrl));
            memberEntity.setFileEntities(fileEntities);
        } else {
            if(fileURL != null && !StringUtils.equals(fileURL, "")) {
                List<FileEntity> fileEntities = new ArrayList<>();
                fileEntities.add(this.addMemberProfileImage(memberEntity.getMember_srl(), fileURL, ip));
                memberEntity.setFileEntities(fileEntities);
            }
        }

        // 계정 여부에 따라서 app 에도 가입 시킨다.
        if(memberEntity.getAccount_type() == MDV.USER_NORMAL) {
            String strAppSrl = memberEntity.getApp_srl();
            if(strAppSrl == null || StringUtils.equals(StringUtils.trim(strAppSrl), "")) {
                LOG.warn("failed subscribe in app. not found app. app_srl [" + strAppSrl + "]");
                return;
            }

            int appSrl = Integer.parseInt(strAppSrl, 10);
            AppEntity appEntity = appDao.get(appSrl, null, null);
            if(appEntity == null) {
                LOG.warn("failed subscribe in app. not found app. app_srl [" + appSrl + "]");
                return;
            }

            String appGroupAuth;

            if(memberEntity.getApp_account_type() == MDV.APP_USER_ROOT)
                appGroupAuth = confCommon.getProperty("group_role_root");
            else if(memberEntity.getApp_account_type() == MDV.APP_USER_NORMAL)
                appGroupAuth = confCommon.getProperty("group_role_user");
            else if(memberEntity.getApp_account_type() == MDV.APP_USER_VISITOR)
                appGroupAuth = confCommon.getProperty("group_role_visitor");
            else {
                LOG.warn("failed user add app group. not support app group type [" +
                        memberEntity.getApp_account_type() + "]");
                return;
            }

            List<AppGroupEntity> appGroupEntities = appGroupDao.get(appSrl, null, null,
                    Integer.parseInt(confCommon.getProperty("system_app_group"), 10),
                    appGroupAuth, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
            if(appGroupEntities.size() <= 0) {
                LOG.warn("not found app group. app_srl [" + appSrl + "], appGroupAuth [" + appGroupAuth + "]");
                return;
            }

            long appGroupSrl = appGroupEntities.get(0).getApp_group_srl();
            AppGroupMemberEntity appGroupMemberEntity = new AppGroupMemberEntity();
            appGroupMemberEntity.setApp_group_srl(appGroupSrl);
            appGroupMemberEntity.setMember_srl(memberEntity.getMember_srl());
            appGroupMemberEntity.setC_date(ltm);

            // 사용자를 앱 그룹 매핑
            appGroupMemberDao.add(appGroupMemberEntity);
            LOG.info("subscribe member to app group. app_srl [" + appSrl + "], app_group_srl [" +
                    appGroupSrl + "], member_srl [" + memberEntity.getMember_srl() + "]");

            AppMemberEntity appMemberEntity = new AppMemberEntity();
            appMemberEntity.setApp_srl(appSrl);
            appMemberEntity.setMember_srl(memberEntity.getMember_srl());
            appMemberEntity.setNick_name(memberEntity.getNick_name());
            appMemberEntity.setAllow_mailing(memberEntity.getAllow_mailing());
            appMemberEntity.setAllow_message(memberEntity.getAllow_message());
            appMemberEntity.setLast_login_date(memberEntity.getLast_login_date());
            appMemberEntity.setEnabled(memberEntity.getEnabled());
            appMemberEntity.setC_date(ltm);
            appMemberEntity.setU_date(ltm);

            // 사용자를 앱에 매핑
            appMemberDao.add(appMemberEntity);
            LOG.info("subscribe member to app. app_srl [" + appSrl + "], member_srl [" +
                    memberEntity.getMember_srl() + "]");
        }


        // service history 에 가입 이력을 추가 한다.
        // 로그는 addCreateAccountHistory 에서 남긴다.
        //serviceHistoryService.addCreateAccountHistory(tryMemberSrl, memberEntity.getMember_srl(),
        //        null, memberEntity.getUser_id(), ip);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupEntity> getMemberGroup(long memberSrl) {
        if(memberSrl <= 0) {
            LOG.error("invalid memeber_srl[" + memberSrl + "]");
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "member_srl is less then zero. member_srl [" + memberSrl + "]");
            throw new CustomException("invalid_method_parameter", reason);
        }

        List<GroupMemberEntity> groupMemberList = groupMemberDao.get(MDV.NUSE, memberSrl, MDV.NUSE, MDV.NUSE);
        if(groupMemberList.size() <= 0) {
            LOG.error("member_srl[" + memberSrl + "] has not any group");
            Map<String, String> reason = new HashMap<>();
            reason.put("member_group", "not found member's group. member_srl [" + memberSrl + "]");
            throw new CustomException("member_has_not_group_error", 500, reason);
        }

        List<Integer> groupSrls = new ArrayList<>();
        for(GroupMemberEntity element : groupMemberList) groupSrls.add(element.getGroup_srl());

        // FK로 엮여 있기 때문에 size 가 zero 가 될 수 없음
        return groupDao.get(groupSrls, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
    }

    @Override
    public void joinMemberGroup(long memberSrl, int groupSrl) {
        if(groupSrl != MDV.NUSE && groupSrl <= 0) {
            LOG.error("invalid group_srl[" + groupSrl + "]");
            Map<String, String> reason = new HashMap<>();
            reason.put("group_srl", "invalid group_srl. group_srl less then zero [" + groupSrl + "]");
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(groupSrl == MDV.NUSE) {
            List<GroupEntity> defaultGroups = groupDao.get(null, MDV.YES, null, MDV.NUSE, 0, 1);
            if(defaultGroups.size() <= 0) {
                Map<String, String> reason = new HashMap<>();
                reason.put("default_group", "server error. not found default user group");
                LOG.error(reason.get("default_group"));
                throw new CustomException("add_member_error", 500, reason);
            }
            groupSrl = defaultGroups.get(0).getGroup_srl();
        } else {
            GroupEntity groupEntity = groupDao.get(groupSrl);
            if(groupEntity == null) {
                Map<String, String> reason = new HashMap<>();
                reason.put("groupEntity", "groupEntity is null. not found group for group_srl ["+groupSrl+"]");
                LOG.error(reason.get("groupEntity"));
                throw new CustomException("add_member_group_error", reason);
            }
        }

        GroupMemberEntity prevGroupMemberEntity = groupMemberDao.get(groupSrl, memberSrl);
        if(prevGroupMemberEntity != null) {
            LOG.warn("failed add group member. already added member in group. member_srl[" +
                    memberSrl + "], group_srl[" + groupSrl + "]");
            return;
        }

        GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
        groupMemberEntity.setGroup_srl(groupSrl);
        groupMemberEntity.setMember_srl(memberSrl);
        groupMemberEntity.setC_date((int) (DateTime.now().getMillis() / 1000));

        groupMemberDao.add(groupMemberEntity);
        LOG.info("add group member. groupMemberEntity["+ groupMemberEntity.toString()+"]");

        // TODO history 에 넣어야 함
    }

    @Transactional(readOnly = true)
    @Override
    public GroupEntity getGroup(String authority) {
        if(authority == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("authority", "authority is null");
            LOG.error(reason.get("authority"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        List<GroupEntity> groupEntities = groupDao.get(null, MDV.NUSE, authority, MDV.NUSE, 0, 1);
        if(groupEntities.size() <= 0) return null;

        return groupEntities.get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Map<String, Object>> getUserAppGroup(long memberSrl) {
        List<AppGroupMemberEntity> appGroupMemberEntities = appGroupMemberDao.get(MDV.NUSE, null,
                memberSrl, MDV.NUSE, MDV.NUSE);
        if(appGroupMemberEntities.size() <= 0) {
            LOG.debug("no app group for member_srl [" + memberSrl + "]");
            return new ArrayList<>();
        }

        List<Long> appGroupSrls = new ArrayList<>();
        for(AppGroupMemberEntity appGroupMemberEntity : appGroupMemberEntities)
            appGroupSrls.add(appGroupMemberEntity.getApp_group_srl());

        List<AppGroupEntity> appGroupEntities = appGroupDao.get(MDV.NUSE, appGroupSrls, null, MDV.NUSE, null,
                MDV.YES, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        if(appGroupEntities.size() <= 0) {
            LOG.warn("no app group using app_group_srls [" + appGroupSrls.toString() + "]");
            return new ArrayList<>();
        }

        List<Integer> appSrls = new ArrayList<>();
        for(AppGroupEntity appGroupEntity : appGroupEntities)
            appSrls.add(appGroupEntity.getApp_srl());

        List<AppEntity> appEntities = appDao.get(appSrls, null, null, null, MDV.YES, MDV.NUSE, null,
                MDV.NUSE, MDV.NUSE);
        if(appEntities.size() <= 0) {
            LOG.warn("no app for app group info. member_srl [" + memberSrl + "], app_srls [" +
                    appSrls.toString() + "]");
            return new ArrayList<>();
        }

        // mapping 을 위해서 app 의 map 을 임시로 만든다.
        Map<Integer, AppEntity> appEntityMap = new HashMap<>();
        for(AppEntity appEntity : appEntities) {
            int appSrl = appEntity.getApp_srl();
            if(!appEntityMap.containsKey(appSrl)) appEntityMap.put(appSrl, appEntity);
        }

        List<Map<String, Object>> retList = new ArrayList<>();
        for(AppGroupEntity appGroupEntity : appGroupEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("app_group_info", appGroupEntity);
            map.put("app_info", appEntityMap.get(appGroupEntity.getApp_srl()));
            retList.add(map);
        }

        return retList;
    }

    @Override
    public void modifyMember(int memberSrl, Map<String, String> modifyValue) {
        if(memberSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "invalid member_srl. member_srl is less then zero. memberSrl [" + memberSrl + "]");
            LOG.error(reason.get("member_srl"));
            throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(modifyValue == null || modifyValue.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "invalid modify value. modify value is null or empty. member_srl [" + memberSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(!modifyValue.containsKey("email_address") && !modifyValue.containsKey("user_name") &&
                !modifyValue.containsKey("nick_name") && !modifyValue.containsKey("mobile_phone_number") &&
                !modifyValue.containsKey("allow_mailing") && !modifyValue.containsKey("allow_message") &&
                !modifyValue.containsKey("description") && !modifyValue.containsKey("enabled") &&
                !modifyValue.containsKey("email_confirm") && !modifyValue.containsKey("sign_out") &&
                !modifyValue.containsKey("user_password")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "not found member modify value. modifyValue [" + modifyValue.toString() + "], memberSrl [" +memberSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        MemberEntity savedEntity = this.getMemberInfo(memberSrl);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("not_found", "not found member. memberSrl [" + memberSrl + "]");
            LOG.error(reason.get("not_found"));
            throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        boolean willModify = false, willModifyEnabled = false, willModifySignOut = false;
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.init();

        if(modifyValue.containsKey("user_password")) {
            String userPassword = modifyValue.get("user_password");
            if(userPassword == null || StringUtils.equals(StringUtils.trim(userPassword), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("user_password", "failed modify user_password. user_password is null or empty");
                LOG.error(reason.get("user_password"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            String hashPassword = passwordEncoder.encode(userPassword);
            memberEntity.setUser_password(hashPassword);
            willModify = true;
        }

        if(modifyValue.containsKey("email_address")) {
            String emailAddress = modifyValue.get("email_address");
            if(emailAddress == null) emailAddress = "";
            emailAddress = StringUtils.trim(emailAddress);

            int length = emailAddress.getBytes().length;
            if(length > 128) {
                Map<String, String> reason = new HashMap<>();
                reason.put("email_address", "failed modify email_address. email_address length less then 128 byte. length [" + length + "]");
                LOG.error(reason.get("email_address"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getEmail_address(), emailAddress)) {
                memberEntity.setEmail_address(emailAddress);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("user_name")) {
            String userName = modifyValue.get("user_name");
            if(userName == null || StringUtils.equals(StringUtils.trim(userName), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("user_name", "failed modify user_name. user_name is null or empty");
                LOG.error(reason.get("user_name"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int length = userName.getBytes().length;
            if(length > 64) {
                Map<String, String> reason = new HashMap<>();
                reason.put("user_name", "failed modify user_name. user_name length less then 64 byte. length [" + length + "]");
                LOG.error(reason.get("user_name"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getUser_name(), userName)) {
                memberEntity.setUser_name(userName);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("nick_name")) {
            String nickName = modifyValue.get("nick_name");
            if(nickName == null) nickName = "";
            nickName = StringUtils.trim(nickName);

            int length = nickName.getBytes().length;
            if(length > 64) {
                Map<String, String> reason = new HashMap<>();
                reason.put("nick_name", "failed modify nick_name. nick_name length less then 64 byte. length [" + length + "]");
                LOG.error(reason.get("nick_name"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getNick_name(), nickName)) {
                memberEntity.setNick_name(nickName);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("mobile_phone_number")) {
            String mobilePhoneNumber = modifyValue.get("mobile_phone_number");
            if(mobilePhoneNumber == null) mobilePhoneNumber = "";
            mobilePhoneNumber = webUtilService.getPhoneNumber(StringUtils.trim(mobilePhoneNumber));

            int length = mobilePhoneNumber.length();
            if(length > 16) {
                Map<String, String> reason = new HashMap<>();
                reason.put("mobile_phone_number", "failed modify mobile_phone_number. mobile_phone_number length less then 16 byte. length [" + length + "]");
                LOG.error(reason.get("mobile_phone_number"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getMobile_phone_number(), mobilePhoneNumber)) {
                memberEntity.setMobile_phone_number(mobilePhoneNumber);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("allow_mailing")) {
            String allowMailing = modifyValue.get("allow_mailing");
            if(allowMailing == null || StringUtils.equals(StringUtils.trim(allowMailing), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("allow_mailing", "failed modify allow_mailing. allow_mailing is null or empty");
                LOG.error(reason.get("allow_mailing"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(allowMailing)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("allow_mailing", "failed modify allow_mailing. allow_mailing is not numeric. allow_mailing [" +
                        allowMailing +"]");
                LOG.error(reason.get("allow_mailing"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iAllowMailing = Integer.parseInt(allowMailing, 10);
            if(iAllowMailing != MDV.YES && iAllowMailing != MDV.NO) {
                Map<String, String> reason = new HashMap<>();
                reason.put("allow_mailing", "failed modify allow_mailing. invalid allow_mailing value [" +
                        allowMailing +"]");
                LOG.error(reason.get("allow_mailing"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getAllow_mailing() != iAllowMailing) {
                memberEntity.setAllow_mailing(iAllowMailing);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("allow_message")) {
            String allowMessage = modifyValue.get("allow_message");
            if(allowMessage == null || StringUtils.equals(StringUtils.trim(allowMessage), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("allow_message", "failed modify allow_message. allow_message is null or empty");
                LOG.error(reason.get("allow_message"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(allowMessage)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("allow_message", "failed modify allow_message. allow_message is not numeric. allow_message [" +
                        allowMessage +"]");
                LOG.error(reason.get("allow_message"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iAllowMessage = Integer.parseInt(allowMessage, 10);
            if(iAllowMessage != MDV.YES && iAllowMessage != MDV.NO) {
                Map<String, String> reason = new HashMap<>();
                reason.put("allow_message", "failed modify allow_message. invalid allow_message value [" +
                        iAllowMessage +"]");
                LOG.error(reason.get("allow_message"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getAllow_message() != iAllowMessage) {
                memberEntity.setAllow_message(iAllowMessage);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("description")) {
            String description = modifyValue.get("description");
            if(description == null) description = "";
            description = StringUtils.trim(description);

            int length = description.getBytes().length;
            if(length > 256) {
                Map<String, String> reason = new HashMap<>();
                reason.put("description", "failed modify description. description length less then 256 byte. length [" + length + "]");
                LOG.error(reason.get("description"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getDescription(), description)) {
                memberEntity.setDescription(description);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("enabled")) {
            String enabled = modifyValue.get("enabled");
            if(enabled == null || StringUtils.equals(StringUtils.trim(enabled), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. enabled is null or empty");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(enabled)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. enabled is not numeric. enabled [" +
                        enabled +"]");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iEnabled = Integer.parseInt(enabled, 10);
            if(iEnabled != MDV.YES && iEnabled != MDV.NO && iEnabled != MDV.DENY) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. invalid enabled value [" +
                        iEnabled +"]");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getEnabled() != iEnabled) {
                memberEntity.setEnabled(iEnabled);
                willModifyEnabled = true;
                willModify = true;
            }
        }

        if(modifyValue.containsKey("email_confirm")) {
            String emailConfirm = modifyValue.get("email_confirm");
            if(emailConfirm == null || StringUtils.equals(StringUtils.trim(emailConfirm), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("email_confirm", "failed modify email_confirm. email_confirm is null or empty");
                LOG.error(reason.get("email_confirm"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(emailConfirm)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("email_confirm", "failed modify email_confirm. email_confirm is not numeric. email_confirm [" +
                        emailConfirm +"]");
                LOG.error(reason.get("email_confirm"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iEmailConfirm = Integer.parseInt(emailConfirm, 10);
            if(iEmailConfirm != MDV.YES && iEmailConfirm != MDV.NO) {
                Map<String, String> reason = new HashMap<>();
                reason.put("email_confirm", "failed modify email_confirm. invalid email_confirm value [" +
                        iEmailConfirm +"]");
                LOG.error(reason.get("email_confirm"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getEmail_confirm() != iEmailConfirm) {
                memberEntity.setEmail_confirm(iEmailConfirm);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("sign_out")) {
            String signOut = modifyValue.get("sign_out");
            if(signOut == null || StringUtils.equals(StringUtils.trim(signOut), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("sign_out", "failed modify sign_out. sign_out is null or empty");
                LOG.error(reason.get("sign_out"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(signOut)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("sign_out", "failed modify sign_out. sign_out is not numeric. sign_out [" +
                        signOut +"]");
                LOG.error(reason.get("sign_out"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iSignOut = Integer.parseInt(signOut, 10);
            if(iSignOut != MDV.YES && iSignOut != MDV.NO) {
                Map<String, String> reason = new HashMap<>();
                reason.put("sign_out", "failed modify sign_out. invalid sign_out value [" +
                        iSignOut +"]");
                LOG.error(reason.get("sign_out"));
                throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getSign_out() != iSignOut) {
                memberEntity.setSign_out(iSignOut);
                willModifySignOut = true;
                willModify = true;
            }
        }

        // 만일 바뀌는게 하나도 없다면 update 를 하지 않고 무시 한다.
        if(!willModify) {
            LOG.info("ignore modify member. member_srl [" + memberSrl +"]. it is same between saved value and changed value");
            return;
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        memberEntity.setU_date(ltm);

        memberDao.modify(memberEntity, memberSrl, null);
        LOG.info("modify member info. memberEntity [" + memberEntity.toString() +
                "], member_srl [" + memberSrl + "]");

        // 만일 cache를 사용하고 enabled이 true가 아니면 cache에 들어 있는 사용자 상태를 삭제 한다.
        String cacheType = confCommon.getProperty("access_token_repository");
        if(StringUtils.equals(cacheType, "2") && (willModifySignOut || willModifyEnabled)) {
            int memberState = MDV.NUSE;

            if(willModifySignOut) {
                if(memberEntity.getSign_out() == MDV.YES) memberState = MDV.NO;
            }

            if(memberState == MDV.NO) memberState = MDV.NO;
            else {
                if(willModifyEnabled) memberState = memberEntity.getEnabled() == MDV.YES ? MDV.YES : MDV.NO;
                else {
                    if(savedEntity.getEnabled() == MDV.YES) memberState = MDV.YES;
                    else                                    memberState = MDV.NO;
                }
            }

            if(memberState != MDV.YES) {
                customCache.deleteMemberAccessToken(memberSrl, MDV.NUSE);
                LOG.info("delete member's access_token in memory. member state disable. member_srl [" +
                        memberSrl + "]");
            }
        }
    }

    @Override
    public void modifyExtraMember(MemberExtraEntity memberExtraEntity) {
        if(memberExtraEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "invalid member_srl. member_srl is less then zero. memberSrl []");
            LOG.error(reason.get("member_srl"));
            throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }
        if(memberExtraEntity.getMarriage() == MDV.NO)memberExtraEntity.setChildren("");
        if(memberExtraEntity.getReligion() == MDV.NO)memberExtraEntity.setReligion_name("");
        if(memberExtraEntity.getDisability() == MDV.NO){
            memberExtraEntity.setDisability_type("");
            memberExtraEntity.setDisability_rate("");
        }

        memberExtraDao.modify(memberExtraEntity, memberExtraEntity.getMember_srl());
    }

    @Override
    public void modifyMemberExtra(int memberSrl, Map<String, String> modifyValue) {
        if(memberSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "invalid member_srl. member_srl is less then zero. memberSrl [" + memberSrl + "]");
            LOG.error(reason.get("member_srl"));
            throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(modifyValue == null || modifyValue.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "invalid modify value. modify value is null or empty. member_srl [" + memberSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(!modifyValue.containsKey("certificate") && !modifyValue.containsKey("kakao_id") &&
                !modifyValue.containsKey("class_srl") && !modifyValue.containsKey("domain_srl") &&
                !modifyValue.containsKey("self_introduction") && !modifyValue.containsKey("group_gubun") &&
                !modifyValue.containsKey("user_group_srl")) {

            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "not found member extra modify value. modifyValue [" + modifyValue.toString() + "], memberSrl [" +memberSrl +"]");
            LOG.error(reason.get("modify_extra_value"));
            throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        MemberExtraEntity savedEntity = this.getMemberextraInfo(memberSrl);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("not_found", "not found member extra. memberSrl [" + memberSrl + "]");
            LOG.error(reason.get("not_found"));
            throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        boolean willModify = false;
        MemberExtraEntity memberExtraEntity = new MemberExtraEntity();
        memberExtraEntity.init();

        if(modifyValue.containsKey("group_gubun")) {
            String group_gubun = modifyValue.get("group_gubun");
            if(group_gubun == null || StringUtils.equals(StringUtils.trim(group_gubun), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("group_gubun", "failed modify group_gubun. group_gubun is null or empty");
                LOG.error(reason.get("group_gubun"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(group_gubun)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("group_gubun", "failed modify group_gubun. group_gubun is not numeric. group_gubun [" +
                        group_gubun +"]");
                LOG.error(reason.get("group_gubun"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int igroup_gubun = Integer.parseInt(group_gubun, 10);
            if(savedEntity.getGroup_gubun() != igroup_gubun) {
                memberExtraEntity.setGroup_gubun(igroup_gubun);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("user_group_srl")) {
            String user_group_srl = modifyValue.get("user_group_srl");
            if(user_group_srl == null || StringUtils.equals(StringUtils.trim(user_group_srl), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("user_group_srl", "failed modify user_group_srl. user_group_srl is null or empty");
                LOG.error(reason.get("user_group_srl"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(user_group_srl)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("user_group_srl", "failed modify user_group_srl. user_group_srl is not numeric. user_group_srl [" +
                        user_group_srl +"]");
                LOG.error(reason.get("user_group_srl"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iuser_group_srl = Integer.parseInt(user_group_srl, 10);
            if(savedEntity.getUser_group_srl() != iuser_group_srl) {
                memberExtraEntity.setUser_group_srl(iuser_group_srl);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("certificate")) {
            String certificate = modifyValue.get("certificate");
            if(certificate == null) certificate = "";
            certificate = StringUtils.trim(certificate);

            int length = certificate.getBytes().length;
            if(length > 64) {
                Map<String, String> reason = new HashMap<>();
                reason.put("certificate", "failed modify_extra certificate. certificate length less then 64 byte. length [" + length + "]");
                LOG.error(reason.get("certificate"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getCertificate(), certificate)) {
                memberExtraEntity.setCertificate(certificate);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("kakao_id")) {
            String kakaoId = modifyValue.get("kakao_id");
            if(kakaoId == null) kakaoId = "";
            kakaoId = StringUtils.trim(kakaoId);

            int length = kakaoId.getBytes().length;
            if(length > 64) {
                Map<String, String> reason = new HashMap<>();
                reason.put("kakao_id", "failed modify_extra kakao_id. certificate length less then 128 byte. length [" + length + "]");
                LOG.error(reason.get("kakao_id"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getKakao_id(), kakaoId)) {
                memberExtraEntity.setKakao_id(kakaoId);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("class_srl")) {
            String classSrl = modifyValue.get("class_srl");
            if(classSrl == null || StringUtils.equals(StringUtils.trim(classSrl), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("class_srl", "failed modify class_srl. class_srl is null or empty");
                LOG.error(reason.get("class_srl"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(classSrl)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("class_srl", "failed modify class_srl. class_srl is not numeric. class_srl [" +
                        classSrl +"]");
                LOG.error(reason.get("class_srl"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iClassSrl = Integer.parseInt(classSrl, 10);
            if(savedEntity.getClass_srl() != iClassSrl) {
                memberExtraEntity.setClass_srl(iClassSrl);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("domain_srl")) {
            String domainSrl = modifyValue.get("domain_srl");
            if(domainSrl == null || StringUtils.equals(StringUtils.trim(domainSrl), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("domain_srl", "failed modify domain_srl. domain_srl is null or empty");
                LOG.error(reason.get("domain_srl"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(domainSrl)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("domain_srl", "failed modify domain_srl. domain_srl is not numeric. domain_srl [" +
                        domainSrl +"]");
                LOG.error(reason.get("domain_srl"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iDomainSrl = Integer.parseInt(domainSrl, 10);
            if(savedEntity.getDomain_srl() != iDomainSrl) {
                memberExtraEntity.setDomain_srl(iDomainSrl);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("self_introduction")) {
            String selfIntroduction = modifyValue.get("self_introduction");
            if(selfIntroduction == null) selfIntroduction = "";
            selfIntroduction = StringUtils.trim(selfIntroduction);

            int length = selfIntroduction.getBytes().length;
            if(length > 512) {
                Map<String, String> reason = new HashMap<>();
                reason.put("self_introduction", "failed modify_extra self_introduction. self_introduction length less then 512 byte. length [" + length + "]");
                LOG.error(reason.get("self_introduction"));
                throw new CustomException("modify_member_extra_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getSelf_introduction(), selfIntroduction)) {
                memberExtraEntity.setSelf_introduction(selfIntroduction);
                willModify = true;
            }
        }

        // 만일 바뀌는게 하나도 없다면 update 를 하지 않고 무시 한다.
        if(!willModify) {
            LOG.info("ignore modify member extra. member_srl [" + memberSrl +"]. it is same between saved value and changed value");
            return;
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        memberExtraEntity.setU_date(ltm);
        if(memberExtraEntity.getGroup_gubun() == 1) memberExtraEntity.setUser_group_srl(0);

        memberExtraDao.modify(memberExtraEntity, memberSrl);
        LOG.info("modify member extra info. memberExtraEntity [" + memberExtraEntity.toString() +
                "], member_srl [" + memberSrl + "]");
    }

    @Override
    public void modifyMemberNotification(long memberSrl, int allowCall) {
        if(memberSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "invalid member_srl. member_srl is less then zero. memberSrl [" + memberSrl + "]");
            LOG.error(reason.get("member_srl"));
            throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(allowCall != MDV.YES && allowCall != MDV.NO) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid_value", "invalid value. not modify notification value");
            LOG.error(reason.get("invalid_value"));
            throw new CustomException("modify_member_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        MemberExtraEntity memberExtraEntity = new MemberExtraEntity();
        memberExtraEntity.init();

        if(allowCall == MDV.YES || allowCall == MDV.NO)
            memberExtraEntity.setAllow_call(allowCall);

        memberExtraDao.modify(memberExtraEntity, memberSrl);
        LOG.info("modify member notification. memberExtraEntity [" + memberExtraEntity.toString() + "]");
    }

    @Transactional(readOnly = true)
    @Override
    public long countMemberByGroup(long group_srl, Map<String, String> searchFilter) {
        if(searchFilter == null) {
            return memberDao.countMemberByGroup(group_srl, null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        }

        String user_id = null;
        String user_name = null;
        String nick_name = null;

        int enabled = MDV.NUSE;
        int signOut = MDV.NUSE;
        int classSrl = MDV.NUSE;

        if(searchFilter.containsKey("user_id")) user_id = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nick_name = searchFilter.get("nick_name");

        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }

        if(searchFilter.containsKey("sign_out")) {
            String num = searchFilter.get("sign_out");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) signOut = Integer.parseInt(num, 10);
        }

        if(searchFilter.containsKey("class_srl")) {
            String num = searchFilter.get("class_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) classSrl = Integer.parseInt(num, 10);
        }

        return memberDao.countMemberByGroup(group_srl, null, user_id, user_name, nick_name, enabled, signOut, classSrl);
    }

    @Transactional(readOnly = true)
    @Override
    public long countMemberByGroups(long group_srl, List<Long> group_srls, Map<String, String> searchFilter) {
        if(searchFilter == null) {
            return memberDao.countMemberByGroup(group_srl, group_srls, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        }

        String user_id = null;
        String user_name = null;
        String nick_name = null;

        int enabled = MDV.NUSE;
        int signOut = MDV.NUSE;
        int classSrl = MDV.NUSE;

        if(searchFilter.containsKey("user_id")) user_id = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nick_name = searchFilter.get("nick_name");

        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }

        if(searchFilter.containsKey("sign_out")) {
            String num = searchFilter.get("sign_out");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) signOut = Integer.parseInt(num, 10);
        }

        if(searchFilter.containsKey("class_srl")) {
            String num = searchFilter.get("class_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) classSrl = Integer.parseInt(num, 10);
        }

        return memberDao.countMemberByGroup(group_srl, group_srls, user_id, user_name, nick_name, enabled, signOut, classSrl);
    }


    @Transactional(readOnly = true)
    @Override
    public List<MemberEntity> getMemberListByGroup(long group_srl, Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit) {
        if(searchFilter == null )
            return memberDao.getMemberListByGroup(group_srl, null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, sortValue, offset, limit);

        String user_id = null;
        String user_name = null;
        String nick_name = null;

        int enabled = MDV.NUSE;
        int signOut = MDV.NUSE;
        int classSrl = MDV.NUSE;

        if(searchFilter.containsKey("user_id")) user_id = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nick_name = searchFilter.get("nick_name");

        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }

        if(searchFilter.containsKey("sign_out")) {
            String num = searchFilter.get("sign_out");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) signOut = Integer.parseInt(num, 10);
        }

        if(searchFilter.containsKey("class_srl")) {
            String num = searchFilter.get("class_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) classSrl = Integer.parseInt(num, 10);
        }

        return memberDao.getMemberListByGroup(group_srl, null, user_id, user_name, nick_name, enabled, signOut, classSrl, sortValue, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberEntity> getMemberListByGroups(long group_srl, List<Long> group_srls, Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit) {
        if(searchFilter == null )
            return memberDao.getMemberListByGroup(group_srl, group_srls, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, sortValue, offset, limit);

        String user_id = null;
        String user_name = null;
        String nick_name = null;

        int enabled = MDV.NUSE;
        int signOut = MDV.NUSE;
        int classSrl = MDV.NUSE;

        if(searchFilter.containsKey("user_id")) user_id = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nick_name = searchFilter.get("nick_name");

        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }

        if(searchFilter.containsKey("sign_out")) {
            String num = searchFilter.get("sign_out");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) signOut = Integer.parseInt(num, 10);
        }

        if(searchFilter.containsKey("class_srl")) {
            String num = searchFilter.get("class_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) classSrl = Integer.parseInt(num, 10);
        }

        return memberDao.getMemberListByGroup(group_srl, group_srls, user_id, user_name, nick_name, enabled, signOut, classSrl, sortValue, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberEntity> searchUserId(String userName, String emailAddress) { return memberDao.get(emailAddress, userName); }

    @Transactional(readOnly = true)
    @Override
    public List<MemberEntity> getGroupMemberList(String userId, String userName, String nickName, int enabled, int signOut, int groupSrl,
                                            Map<String, String> sort, int offset, int limit) {
        if(sort == null) sort = new HashMap<>();
        if(sort.size() <= 0) sort.put("user_id", "asc");

        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }

        // 유저 프로필 이미지를 구한다.
        List<MemberEntity> memberEntities = memberDao.getGroupFullInfo(null, userId, userName, nickName, null, enabled,
                signOut, groupSrl, MDV.NUSE, sort, offset, limit, true);

        if(memberEntities.size() <= 0) return memberEntities;

        List<Long> memberSrls = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntities) memberSrls.add(memberEntity.getMember_srl());

        // 위치에 맞게 프로필 이미지를 넣어 준다.
        List<MemberPicEntity> memberPicEntities = memberPicDao.getAndFile(memberSrls, MDV.NO);
        int index;
        for(MemberPicEntity memberPicEntity : memberPicEntities) {
            if((index=memberSrls.indexOf(memberPicEntity.getMember_srl())) <= -1) continue;
            memberEntities.get(index).setFileEntities(memberPicEntity.getFileEntities());
        }

        return memberEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberEntity> getGroupMemberList(Map<String, String> searchFilter, Map<String, String> sort,
                                            int offset, int limit) {
        if(searchFilter == null )
            return this.getGroupMemberList(null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, sort, offset, limit);

        String userId = null;
        String userName = null;
        String nickName = null;
        int enabled = MDV.NUSE;
        int signOut = MDV.NUSE;
        int groupSrl = MDV.NUSE;

        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("sign_out")) {
            String num = searchFilter.get("sign_out");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) signOut = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("group_srl")) {
            String num = searchFilter.get("group_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) groupSrl = Integer.parseInt(num, 10);
        }

        return this.getGroupMemberList(userId, userName, nickName, enabled, signOut, groupSrl, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public long countGroupMemberInfo(String userId, String userName, int enabled, int signOut, int groupSrl) {
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }

        return memberDao.countGroupFullInfo(userId, userName, null, null, enabled, signOut,
                groupSrl, MDV.NUSE, true);
    }

    @Transactional(readOnly = true)
    @Override
    public long countGroupMemberInfo(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.countGroupMemberInfo(null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);

        String userId = null;
        String userName = null;
        int enabled = MDV.NUSE;
        int signOut = MDV.NUSE;
        int groupSrl = MDV.NUSE;

        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("sign_out")) {
            String num = searchFilter.get("sign_out");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) signOut = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("group_srl")) {
            String num = searchFilter.get("group_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) groupSrl = Integer.parseInt(num, 10);
        }

        return this.countGroupMemberInfo(userId, userName, enabled, signOut, groupSrl);
    }

    public boolean getmatches(CharSequence rawPassword, String encodedPassword){
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        return matches;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ZipCodeEntity> getZipcodeList(String query) {

        List<ZipCodeEntity> zipCodeEntities = zipCodeDao.get(query);

        if(zipCodeEntities.size() <= 0) return zipCodeEntities;

        return zipCodeEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ZipCodeEntity> getZipcodejibunList(String query) {

        List<ZipCodeEntity> zipCodeEntities = zipCodeDao.getjibun(query);

        if(zipCodeEntities.size() <= 0) return zipCodeEntities;

        return zipCodeEntities;
    }

    /*
    *   상담사 로그인 시 본인 정보면 보여지게 처리
    */
    public Map<String, String> getAdvisorAuthority(){

        Map<String, String> temp = new HashMap<>();
        temp.put("advisor", "false");

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
        int num = 0;
        for(GrantedAuthority grantedAuthority : authorities){
            if(num == 0 && grantedAuthority.getAuthority().equals("ROLE_ADVISOR")) temp.put("advisor", "true");
        }

        if(userDetails.getUser_id() == null || userDetails.getUser_id() == "") {
            Map<String, String> reason = new HashMap<>();
            reason.put("user_id", "not found member. member []");
            LOG.error(reason.get("member"));
            throw new CustomException("read_member_error", reason);
        }

        temp.put("user_id", userDetails.getUser_id());
        temp.put("member_srl", String.valueOf(userDetails.getMember_srl()));

        return temp;
    }

}

package com.ckstack.ckpush.service.user;

//import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
//import com.ckstack.ckpush.service.accessory.ServiceHistoryService;
import com.ckstack.ckpush.dao.mineral.FileDao;
import com.ckstack.ckpush.domain.mineral.FileEntity;
import com.ckstack.ckpush.dao.user.GroupDao;
import com.ckstack.ckpush.domain.user.GroupEntity;
import com.ckstack.ckpush.domain.user.MemberExtraEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.joda.time.DateTime;
//import org.joda.time.Period;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 3. 27..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Properties confCommon;
    @Autowired
    private Properties confTblFile;

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private FileDao fileDao;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberEntity memberEntity3;
    private MemberEntity memberEntity4;

    private MemberExtraEntity memberExtraEntity3;
    private FileEntity fileEntity4;


    @Before
    public void setUp() {
        String password = "abcd1234";
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        password = md5PasswordEncoder.encodePassword(password, "");
        password = passwordEncoder.encode(password);

        memberEntity1 = new MemberEntity();
        memberEntity1.setUser_id("junit1@ckstack.com");
        memberEntity1.setEmail_address("junit_test_member1@ckstack.com");
        memberEntity1.setUser_password(password);
        memberEntity1.setUser_name("junit test1");
        memberEntity1.setMobile_phone_number("010-5372-8336");

        memberEntity2 = new MemberEntity();
        memberEntity2.setUser_id("junit_test_member2");
        memberEntity2.setEmail_address("junit_test_member2@ckstack.com");
        memberEntity2.setUser_password(password);
        memberEntity2.setUser_name("junit test2");
        memberEntity2.setNick_name("others nicks");
        memberEntity2.setMobile_phone_number("010-5372-8337");
        memberEntity2.setAllow_mailing(MDV.NO);
        memberEntity2.setAllow_message(MDV.NO);
        memberEntity2.setDescription("user description");
        memberEntity2.setEnabled(MDV.NO);
        memberEntity2.setEmail_confirm(MDV.YES);
        memberEntity2.setSign_out(MDV.YES);

        memberEntity3 = new MemberEntity();
        memberEntity3.setUser_id("junit_test_member3");
        memberEntity3.setUser_password(password);
        memberEntity3.setUser_name("junit test3");
        memberEntity3.setNick_name("others nicks3");
        memberEntity3.setMobile_phone_number("010-5372-8338");
        memberEntity3.setAllow_mailing(MDV.NO);
        memberEntity3.setAllow_message(MDV.NO);
        memberEntity3.setDescription("user description");
        memberEntity3.setEnabled(MDV.NO);
        memberEntity3.setEmail_confirm(MDV.YES);
        memberEntity3.setSign_out(MDV.YES);

        memberEntity4 = new MemberEntity();
        memberEntity4.setUser_id("junit4@ckstack.com");
        memberEntity4.setEmail_address("junit_test_member4@ckstack.com");
        memberEntity4.setUser_password(password);
        memberEntity4.setUser_name("junit test4");
        memberEntity4.setMobile_phone_number("010-5372-8336");

        memberExtraEntity3 = new MemberExtraEntity();
        memberExtraEntity3.setSocial_type(MDV.SOCIAL_KAKAOTOC);
        memberExtraEntity3.setSocial_id("123456789");
        memberEntity3.setMemberExtraEntity(memberExtraEntity3);

        // TODO file domain 만들면 수정 해야 한다.

        fileEntity4 = new FileEntity();
        fileEntity4.setOrig_name("");
        fileEntity4.setFile_size(1024);
        fileEntity4.setFile_path("/home/dhkim/a.png");
        fileEntity4.setFile_url("/a.png");
        fileEntity4.setWidth(100);
        fileEntity4.setHeight(100);
        fileEntity4.setThumb_path("/home/dhkim/b.png");
        fileEntity4.setThumb_url("/b.png");
        fileEntity4.setThumb_width(50);
        fileEntity4.setThumb_height(50);
        fileEntity4.setIpaddress("127.0.0.1");
        fileEntity4.setDeleted(MDV.NO);
        fileEntity4.setC_date((int)(DateTime.now().getMillis() / 1000));
        fileEntity4.setU_date((int)(DateTime.now().getMillis() / 1000));
    }

    /**
     * 회원 가입 테스트
     * - signUp
     * - getMemberInfo(String)
     * - getMemberInfo(long)
     * - getMemberGroup(long)
     */
    @Test
    @Rollback
    public void signUpTest() {
        //String strStartMonth = "201503";
        //DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");
        //DateTime dtStartTime = fmtYYYYMM.parseDateTime(strStartMonth);
        //DateTime dtCurrTime = DateTime.now();
        //Period period = new Period(dtStartTime, dtCurrTime);
        //int iPeriod = period.getMonths() + 1;

        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        // 기본으로 회원 가입하면 다음과 같이 설정 된다.
        // 1. nick_name 은 user_name 과 동일하게(nick_name 이 없을때)
        // 2. allow_mailing 은 1(YES) 로
        // 3. allow_message 는 1(YES) 로
        // 4. mobile_phone_number 는 숫자로만 이루어 진 것으로
        // 5. description 은 empty string 으로
        // 6. enabled 는 1(YES) 로
        // 7. email_confirm 은 2(NO) 로
        // 8. sign_out 은 2(NO) 로
        // 9. last_login_date, change_password_date, c_date, u_date 는 현재 시간으로
        MemberEntity resultVo1 = memberService.getMemberInfo(memberEntity1.getUser_id());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.getNick_name(), is(memberEntity1.getUser_name()));
        assertThat(resultVo1.getMobile_phone_number(), is("01053728336"));
        assertThat(resultVo1.getAllow_mailing(), is(MDV.YES));
        assertThat(resultVo1.getAllow_message(), is(MDV.YES));
        assertThat(resultVo1.getDescription(), is(""));
        assertThat(resultVo1.getLast_login_date() > 0, is(true));
        assertThat(resultVo1.getChange_password_date() > 0, is(true));
        assertThat(resultVo1.getEnabled(), is(MDV.YES));
        assertThat(resultVo1.getEmail_confirm(), is(MDV.NO));
        assertThat(resultVo1.getSign_out(), is(MDV.NO));
        assertThat(resultVo1.getC_date() > 0, is(true));
        assertThat(resultVo1.getU_date() > 0, is(true));
        assertThat(resultVo1.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo1.equals(memberEntity1), is(true));
        assertThat(resultVo1.getMemberExtraEntity().getSocial_id(), is(""));
        assertThat(resultVo1.getMemberExtraEntity().getSocial_type(),
                is(MDV.SOCIAL_NONE));

        MemberEntity resultVo1_1 = memberService.getMemberInfo(memberEntity1.getMember_srl());
        assertThat(resultVo1_1.equals(resultVo1), is(true));

        List<GroupEntity> memberGroupList1 = memberService.getMemberGroup(memberEntity1.getMember_srl());
        assertThat(memberGroupList1.size() > 0, is(true));

        // 기본 값이 아닐때 가입 테스트
        memberService.signUp(memberEntity2, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        MemberEntity resultVo2 = memberService.getMemberInfo(memberEntity2.getUser_id());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.getNick_name(), is("others nicks"));
        assertThat(resultVo2.getMobile_phone_number(), is("01053728337"));
        assertThat(resultVo2.getAllow_mailing(), is(MDV.NO));
        assertThat(resultVo2.getAllow_message(), is(MDV.NO));
        assertThat(resultVo2.getDescription(), is("user description"));
        assertThat(resultVo2.getLast_login_date() > 0, is(true));
        assertThat(resultVo2.getChange_password_date() > 0, is(true));
        assertThat(resultVo2.getEnabled(), is(MDV.NO));
        assertThat(resultVo2.getEmail_confirm(), is(MDV.YES));
        assertThat(resultVo2.getSign_out(), is(MDV.YES));
        assertThat(resultVo2.getC_date() > 0, is(true));
        assertThat(resultVo2.getU_date() > 0, is(true));
        assertThat(resultVo2.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo2.equals(memberEntity2), is(true));
        assertThat(resultVo2.getMemberExtraEntity().getSocial_id(), is(""));
        assertThat(resultVo2.getMemberExtraEntity().getSocial_type(),
                is(MDV.SOCIAL_NONE));

        MemberEntity result2_1 = memberService.getMemberInfo(memberEntity2.getMember_srl());
        assertThat(result2_1.equals(resultVo2), is(true));

        List<GroupEntity> memberGroupList2 = memberService.getMemberGroup(memberEntity2.getMember_srl());
        assertThat(memberGroupList2.size() > 0, is(true));

        // memberExtraEntity 가 포함되어 있을때 가입 테스트(가입 이력은 root user 가 남긴다)
        MemberEntity rootMemberEntity = memberService.getMemberInfo("dhkim@ckstack.com");
        assertThat(rootMemberEntity, is(notNullValue()));

        assertThat(memberEntity3.getMemberExtraEntity(), is(notNullValue()));
        memberService.signUp(memberEntity3, MDV.NUSE, null, MDV.NUSE, rootMemberEntity.getMember_srl(), null);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        MemberEntity resultVo3 = memberService.getMemberInfo(memberEntity3.getUser_id());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.getNick_name(), is("others nicks3"));
        assertThat(resultVo3.getMobile_phone_number(), is("01053728338"));
        assertThat(resultVo3.getAllow_mailing(), is(MDV.NO));
        assertThat(resultVo3.getAllow_message(), is(MDV.NO));
        assertThat(resultVo3.getDescription(), is("user description"));
        assertThat(resultVo3.getLast_login_date() > 0, is(true));
        assertThat(resultVo3.getChange_password_date() > 0, is(true));
        assertThat(resultVo3.getEnabled(), is(MDV.NO));
        assertThat(resultVo3.getEmail_confirm(), is(MDV.YES));
        assertThat(resultVo3.getSign_out(), is(MDV.YES));
        assertThat(resultVo3.getC_date() > 0, is(true));
        assertThat(resultVo3.getU_date() > 0, is(true));
        assertThat(resultVo3.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo3.equals(memberEntity3), is(true));
        assertThat(resultVo3.getMemberExtraEntity().getSocial_type(), is(memberExtraEntity3.getSocial_type()));
        assertThat(resultVo3.getMemberExtraEntity().getSocial_id(), is(memberExtraEntity3.getSocial_id()));

        MemberEntity resultVo3_1 = memberService.getMemberInfo(memberEntity3.getMember_srl());
        assertThat(resultVo3_1.equals(resultVo3), is(true));

        List<GroupEntity> memberGroupList3 = memberService.getMemberGroup(memberEntity3.getMember_srl());
        assertThat(memberGroupList3.size() > 0, is(true));

        // 프로필 이미지를 가지면서 가입 테스트
        // TODO file domain 추가 되면 변경 해야 함.
        String tableName = confTblFile.getProperty("tbl_name_profile_file");
        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        memberService.signUp(memberEntity4, fileEntity4.getFile_srl(), null, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity4.getMember_srl() > 0, is(true));

        MemberEntity resultVo4 = memberService.getMemberInfo(memberEntity4.getUser_id());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.getNick_name(), is(memberEntity4.getUser_name()));
        assertThat(resultVo4.getMobile_phone_number(), is("01053728336"));
        assertThat(resultVo4.getAllow_mailing(), is(MDV.YES));
        assertThat(resultVo4.getAllow_message(), is(MDV.YES));
        assertThat(resultVo4.getDescription(), is(""));
        assertThat(resultVo4.getLast_login_date() > 0, is(true));
        assertThat(resultVo4.getChange_password_date() > 0, is(true));
        assertThat(resultVo4.getEnabled(), is(MDV.YES));
        assertThat(resultVo4.getEmail_confirm(), is(MDV.NO));
        assertThat(resultVo4.getSign_out(), is(MDV.NO));
        assertThat(resultVo4.getC_date() > 0, is(true));
        assertThat(resultVo4.getU_date() > 0, is(true));
        assertThat(resultVo4.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo4.equals(memberEntity4), is(true));
        assertThat(resultVo4.getMemberExtraEntity().getSocial_id(), is(""));
        assertThat(resultVo4.getMemberExtraEntity().getSocial_type(),
                is(MDV.SOCIAL_NONE));

        MemberEntity resultVo4_1 = memberService.getMemberInfo(memberEntity4.getMember_srl());
        assertThat(resultVo4_1.equals(resultVo4), is(true));

        List<GroupEntity> memberGroupList4 = memberService.getMemberGroup(memberEntity4.getMember_srl());
        assertThat(memberGroupList4.size() > 0, is(true));

        // 현재 가입 되어 있는 사용자 아이디
        // junit1@ckstack.com
        // junit_test_member2
        // junit_test_member3
        // junit4@ckstack.com

        memberEntity1 = memberService.getMemberInfo(memberEntity1.getUser_id());
        memberEntity2 = memberService.getMemberInfo(memberEntity2.getUser_id());
        memberEntity3 = memberService.getMemberInfo(memberEntity3.getUser_id());
        memberEntity4 = memberService.getMemberInfo(memberEntity4.getUser_id());

        // user_id asc 된 리스트를 가져온다.
        long memberCount1 = memberService.countMemberInfo("junit", null, MDV.NUSE, MDV.NUSE);
        assertThat(memberCount1, is(4L));
        List<MemberEntity> memberList1 = memberService.getMemberList("junit", null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)memberList1.size(), is(memberCount1));
        assertThat(memberList1.get(0).equals(memberEntity1), is(true));
        assertThat(memberList1.get(1).equals(memberEntity4), is(true));
        assertThat(memberList1.get(2).equals(memberEntity2), is(true));
        assertThat(memberList1.get(3).equals(memberEntity3), is(true));

        long memberCount2 = memberService.countMemberInfo("junit1", null, MDV.NUSE, MDV.NUSE);
        assertThat(memberCount2, is(1L));
        List<MemberEntity> memberList2 = memberService.getMemberList("junit1", null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)memberList2.size(), is(memberCount2));
        assertThat(memberList2.get(0).equals(memberEntity1), is(true));

        // under bar가 들어 있어서 역슬래쉬 두개를 언더바 앞에 적어야 함
        long memberCount3 = memberService.countMemberInfo("junit\\_", null, MDV.NUSE, MDV.NUSE);
        assertThat(memberCount3, is(2L));
        List<MemberEntity> memberList3 = memberService.getMemberList("junit\\_", null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)memberList3.size(), is(memberCount3));
        assertThat(memberList3.get(0).equals(memberEntity2), is(true));
        assertThat(memberList3.get(1).equals(memberEntity3), is(true));
    }

    /**
     * 가입할 회원값이 없을때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void noValueSignUpTest() {
        memberEntity1.init();
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * 가입할 회원값이 null 일때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void nullSignUpTest() {
        memberService.signUp(null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * user_id 가 없을때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void notUserIdSignUpTest() {
        memberEntity1.setUser_id("  ");
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * user_id 가 null 일때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void nullUserIdSignUpTest() {
        memberEntity1.setUser_id(null);
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * user_password 가 없을때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void notUserPasswordSignUpTest() {
        memberEntity1.setUser_password("  ");
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * user_password 가 null 일때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void nullUserPasswordSignUpTest() {
        memberEntity1.setUser_password(null);
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * user_name 이 없을때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void notUserNameSignUpTest() {
        memberEntity1.setUser_name("    ");
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * user_name 이 null 일때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void nullUserNameSignUpTest() {
        memberEntity1.setUser_name(null);
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * 지원하지 않는 social_type 일때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void notSupportSocialTypeSignUpTest() {
        memberEntity3.getMemberExtraEntity().setSocial_type(100);
        memberService.signUp(memberEntity3, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * social_id 가 없을때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void noSocialIdSignUpTest() {
        memberEntity3.getMemberExtraEntity().setSocial_id("  ");
        memberService.signUp(memberEntity3, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * social_id 가 null 일때 가입 실패 테스트
     */
    @Test(expected = CustomException.class)
    @Rollback
    public void nullSocialIdSignUpTest() {
        memberEntity3.getMemberExtraEntity().setSocial_id(null);
        memberService.signUp(memberEntity3, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * 중복 아이디를 사용하면 중복 아이디로 인해 insert 불가인 DuplicateKeyException 발생 테스트
     */
    @Test(expected = DuplicateKeyException.class)
    @Rollback
    public void duplicateUserIdSignUpTest() {
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
        memberEntity2.setUser_id(memberEntity1.getUser_id());
        memberService.signUp(memberEntity2, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
    }

    /**
     * default 그룹으로 가입 하지 않는 것 테스트
     */
    @Test
    @Rollback
    public void nonDefaultGroupSignUpTest() {
        // TODO group insert/add 것 비지니스로직 만들면 domain 으로 변경 해야 한다. 그룹 추가하면서 테스트 진행 하자.

        List<GroupEntity> groupList = groupDao.get(null, MDV.NO, null, MDV.NUSE, MDV.NUSE, 10);
        assertThat(groupList.size() > 0, is(true));
        GroupEntity groupEntity = groupList.get(0);

        memberService.signUp(memberEntity1, MDV.NUSE, null, groupEntity.getGroup_srl(), MDV.NUSE, null);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        List<GroupEntity> memberGroupList = memberService.getMemberGroup(memberEntity1.getMember_srl());
        assertThat(memberGroupList.size() == 1, is(true));
        assertThat(memberGroupList.get(0).equals(groupEntity), is(true));
    }

    /**
     * 유저 정보 변경 테스트
     */
    @Test
    @Rollback
    public void modifyMemberInfoTest() {
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        MemberEntity resultVo1 = memberService.getMemberInfo(memberEntity1.getMember_srl());
        assertThat(resultVo1, is(notNullValue()));

        // disable로 변경 테스트
        assertThat(resultVo1.getEnabled(), is(MDV.YES));
        memberService.enabledMember(memberEntity1.getUser_id(), MDV.NO);

        MemberEntity resultVo2 = memberService.getMemberInfo(memberEntity1.getMember_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.getEnabled(), is(MDV.NO));

        // deny로 변경 테스트
        memberService.enabledMember(memberEntity1.getUser_id(), MDV.DENY);
        MemberEntity resultVo5 = memberService.getMemberInfo(memberEntity1.getMember_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.getEnabled(), is(MDV.DENY));

        // enable 로 변경
        memberService.enabledMember(memberEntity1.getUser_id(), MDV.YES);

        MemberEntity resultVo3 = memberService.getMemberInfo(memberEntity1.getMember_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.getEnabled(), is(MDV.YES));

        // TODO 추가 유저 정보 변경이 있으면 테스트 추가 하도록 하자.

        // sign_out 변경 테스트
        assertThat(resultVo1.getSign_out(), is(MDV.NO));
        memberService.signOutMember(memberEntity1.getUser_id());

        MemberEntity resultVo4 = memberService.getMemberInfo(memberEntity1.getMember_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.getSign_out(), is(MDV.YES));
    }
}

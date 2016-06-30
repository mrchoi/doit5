package com.ckstack.ckpush.dao.user;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.app.AppMemberDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.app.AppMemberEntity;
import com.ckstack.ckpush.domain.user.MemberAccessTokenEntity;
import com.ckstack.ckpush.domain.user.MemberExtraEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
//import org.apache.ibatis.exceptions.TooManyResultsException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by dhkim94 on 15. 3. 20..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class MemberDaoTest {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberExtraDao memberExtraDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private MemberAccessTokenDao memberAccessTokenDao;
    @Autowired
    private AppMemberDao appMemberDao;
    @Autowired
    private Properties confCommon;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberEntity memberEntity3;
    private MemberEntity memberEntity4;
    private MemberEntity memberEntity5;

    private MemberExtraEntity memberExtraEntity1;
    private MemberExtraEntity memberExtraEntity2;
    private MemberExtraEntity memberExtraEntity3;
    private MemberExtraEntity memberExtraEntity4;
    private MemberExtraEntity memberExtraEntity5;

    private AppEntity appEntity1;
    private AppEntity appEntity2;

    private MemberAccessTokenEntity memberAccessTokenEntity1;
    private MemberAccessTokenEntity memberAccessTokenEntity2;
    private MemberAccessTokenEntity memberAccessTokenEntity3;
    private MemberAccessTokenEntity memberAccessTokenEntity4;
    private MemberAccessTokenEntity memberAccessTokenEntity5;

    private AppMemberEntity appMemberEntity1;
    private AppMemberEntity appMemberEntity2;
    private AppMemberEntity appMemberEntity3;
    private AppMemberEntity appMemberEntity4;
    private AppMemberEntity appMemberEntity5;

    @Before
    public void setUp() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        memberEntity1 = new MemberEntity();
        memberEntity1.init();
        memberEntity1.setUser_id("junit-test-1");
        memberEntity1.setEmail_address("dhkim1@ckstack.com");
        memberEntity1.setUser_password("123456790");
        memberEntity1.setUser_name("임의 사용자1");
        memberEntity1.setNick_name("임의 사용자 별명1");
        memberEntity1.setMobile_phone_number("01011112222");
        memberEntity1.setAllow_mailing(MDV.YES);
        memberEntity1.setAllow_message(MDV.YES);
        memberEntity1.setDescription("임의 사용자 1 입니다.");
        memberEntity1.setLast_login_date(ltm);
        memberEntity1.setChange_password_date(ltm);
        memberEntity1.setEnabled(MDV.YES);
        memberEntity1.setEmail_confirm(MDV.NO);
        memberEntity1.setSign_out(MDV.NO);
        memberEntity1.setC_date(ltm);
        memberEntity1.setU_date(ltm);

        memberEntity2 = new MemberEntity();
        memberEntity2.init();
        memberEntity2.setUser_id("junit-test-2");
        memberEntity2.setEmail_address("dhkim2@ckstack.com");
        memberEntity2.setUser_password("123456790");
        memberEntity2.setUser_name("임의 사용자2");
        memberEntity2.setNick_name("임의 사용자 별명2");
        memberEntity2.setMobile_phone_number("01033334444");
        memberEntity2.setAllow_mailing(MDV.YES);
        memberEntity2.setAllow_message(MDV.YES);
        memberEntity2.setDescription("임의 사용자 2 입니다.");
        memberEntity2.setLast_login_date(ltm);
        memberEntity2.setChange_password_date(ltm);
        memberEntity2.setEnabled(MDV.YES);
        memberEntity2.setEmail_confirm(MDV.NO);
        memberEntity2.setSign_out(MDV.NO);
        memberEntity2.setC_date(ltm);
        memberEntity2.setU_date(ltm);

        memberEntity3 = new MemberEntity();
        memberEntity3.init();
        memberEntity3.setUser_id("junit-test-3");
        memberEntity3.setEmail_address("dhkim3@ckstack.com");
        memberEntity3.setUser_password("123456790");
        memberEntity3.setUser_name("임의 사용자3");
        memberEntity3.setNick_name("임의 사용자 별명3");
        memberEntity3.setMobile_phone_number("01055556666");
        memberEntity3.setAllow_mailing(MDV.YES);
        memberEntity3.setAllow_message(MDV.YES);
        memberEntity3.setDescription("임의 사용자 3 입니다.");
        memberEntity3.setLast_login_date(ltm);
        memberEntity3.setChange_password_date(ltm);
        memberEntity3.setEnabled(MDV.YES);
        memberEntity3.setEmail_confirm(MDV.NO);
        memberEntity3.setSign_out(MDV.NO);
        memberEntity3.setC_date(ltm);
        memberEntity3.setU_date(ltm);

        memberEntity4 = new MemberEntity();
        memberEntity4.init();
        memberEntity4.setUser_id("junit-test-4");
        memberEntity4.setEmail_address("dhkim4@ckstack.com");
        memberEntity4.setUser_password("123456790");
        memberEntity4.setUser_name("임의 사용자4");
        memberEntity4.setNick_name("임의 사용자 별명4");
        memberEntity4.setMobile_phone_number("01011115555");
        memberEntity4.setAllow_mailing(MDV.YES);
        memberEntity4.setAllow_message(MDV.YES);
        memberEntity4.setDescription("임의 사용자 4 입니다.");
        memberEntity4.setLast_login_date(ltm);
        memberEntity4.setChange_password_date(ltm);
        memberEntity4.setEnabled(MDV.YES);
        memberEntity4.setEmail_confirm(MDV.NO);
        memberEntity4.setSign_out(MDV.NO);
        memberEntity4.setC_date(ltm);
        memberEntity4.setU_date(ltm);

        memberEntity5 = new MemberEntity();
        memberEntity5.init();
        memberEntity5.setUser_id("junit-test-5");
        memberEntity5.setEmail_address("dhkim5@ckstack.com");
        memberEntity5.setUser_password("123456790");
        memberEntity5.setUser_name("임의 사용자5");
        memberEntity5.setNick_name("임의 사용자 별명5");
        memberEntity5.setMobile_phone_number("01077778888");
        memberEntity5.setAllow_mailing(MDV.YES);
        memberEntity5.setAllow_message(MDV.YES);
        memberEntity5.setDescription("임의 사용자 5 입니다.");
        memberEntity5.setLast_login_date(ltm);
        memberEntity5.setChange_password_date(ltm);
        memberEntity5.setEnabled(MDV.YES);
        memberEntity5.setEmail_confirm(MDV.NO);
        memberEntity5.setSign_out(MDV.NO);
        memberEntity5.setC_date(ltm);
        memberEntity5.setU_date(ltm);

        memberExtraEntity1 = new MemberExtraEntity();
        memberExtraEntity1.setU_date(ltm);
        memberExtraEntity1.setC_date(ltm);
        memberExtraEntity1.setLogin_count(1);
        memberExtraEntity1.setSerial_login_count(1);
        memberExtraEntity1.setSocial_type(MDV.SOCIAL_NONE);
        memberExtraEntity1.setSocial_id("");

        memberExtraEntity2 = new MemberExtraEntity();
        memberExtraEntity2.setU_date(ltm);
        memberExtraEntity2.setC_date(ltm);
        memberExtraEntity2.setLogin_count(1);
        memberExtraEntity2.setSerial_login_count(1);
        memberExtraEntity2.setSocial_type(MDV.SOCIAL_KAKAOTOC);
        memberExtraEntity2.setSocial_id("kakao123");

        memberExtraEntity3 = new MemberExtraEntity();
        memberExtraEntity3.setU_date(ltm);
        memberExtraEntity3.setC_date(ltm);
        memberExtraEntity3.setLogin_count(1);
        memberExtraEntity3.setSerial_login_count(1);
        memberExtraEntity3.setSocial_type(MDV.SOCIAL_FACEBOOK);
        memberExtraEntity3.setSocial_id("face123");

        memberExtraEntity4 = new MemberExtraEntity();
        memberExtraEntity4.setU_date(ltm);
        memberExtraEntity4.setC_date(ltm);
        memberExtraEntity4.setLogin_count(1);
        memberExtraEntity4.setSerial_login_count(1);
        memberExtraEntity4.setSocial_type(MDV.SOCIAL_TWITTER);
        memberExtraEntity4.setSocial_id("twitter123");

        memberExtraEntity5 = new MemberExtraEntity();
        memberExtraEntity5.setU_date(ltm);
        memberExtraEntity5.setC_date(ltm);
        memberExtraEntity5.setLogin_count(1);
        memberExtraEntity5.setSerial_login_count(1);
        memberExtraEntity5.setSocial_type(MDV.SOCIAL_GOOGLE);
        memberExtraEntity5.setSocial_id("google123");

        appEntity1 = new AppEntity();
        appEntity1.init();
        appEntity1.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApp_name("junit test app9000");
        appEntity1.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApp_version("1.0.0");
        appEntity1.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setEnabled(MDV.YES);
        appEntity1.setReg_terminal(0);
        appEntity1.setReg_push_terminal(0);
        appEntity1.setC_date(ltm);
        appEntity1.setU_date(ltm);

        appEntity2 = new AppEntity();
        appEntity2.init();
        appEntity2.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApp_name("junit test app2");
        appEntity2.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApp_version("1.0.1");
        appEntity2.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setEnabled(MDV.YES);
        appEntity2.setReg_terminal(0);
        appEntity2.setReg_push_terminal(0);
        appEntity2.setC_date(ltm);
        appEntity2.setU_date(ltm);

        memberAccessTokenEntity1 = new MemberAccessTokenEntity();
        memberAccessTokenEntity1.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        memberAccessTokenEntity1.setToken_expire(ltm + 60);
        memberAccessTokenEntity1.setUser_data("");
        memberAccessTokenEntity1.setC_date(ltm);
        memberAccessTokenEntity1.setU_date(ltm);

        memberAccessTokenEntity2 = new MemberAccessTokenEntity();
        memberAccessTokenEntity2.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        memberAccessTokenEntity2.setToken_expire(ltm + 60);
        memberAccessTokenEntity2.setUser_data("");
        memberAccessTokenEntity2.setC_date(ltm);
        memberAccessTokenEntity2.setU_date(ltm);

        memberAccessTokenEntity3 = new MemberAccessTokenEntity();
        memberAccessTokenEntity3.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        memberAccessTokenEntity3.setToken_expire(ltm + 60);
        memberAccessTokenEntity3.setUser_data("");
        memberAccessTokenEntity3.setC_date(ltm);
        memberAccessTokenEntity3.setU_date(ltm);

        memberAccessTokenEntity4 = new MemberAccessTokenEntity();
        memberAccessTokenEntity4.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        memberAccessTokenEntity4.setToken_expire(ltm + 60);
        memberAccessTokenEntity4.setUser_data("");
        memberAccessTokenEntity4.setC_date(ltm);
        memberAccessTokenEntity4.setU_date(ltm);

        memberAccessTokenEntity5 = new MemberAccessTokenEntity();
        memberAccessTokenEntity5.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        memberAccessTokenEntity5.setToken_expire(ltm + 60);
        memberAccessTokenEntity5.setUser_data("");
        memberAccessTokenEntity5.setC_date(ltm);
        memberAccessTokenEntity5.setU_date(ltm);

        appMemberEntity1 = new AppMemberEntity();
        appMemberEntity1.setNick_name("nick1");
        appMemberEntity1.setAllow_mailing(MDV.YES);
        appMemberEntity1.setAllow_message(MDV.YES);
        appMemberEntity1.setLast_login_date(ltm);
        appMemberEntity1.setEnabled(MDV.YES);
        appMemberEntity1.setC_date(ltm);
        appMemberEntity1.setU_date(ltm);

        appMemberEntity2 = new AppMemberEntity();
        appMemberEntity2.setNick_name("nick2");
        appMemberEntity2.setAllow_mailing(MDV.YES);
        appMemberEntity2.setAllow_message(MDV.YES);
        appMemberEntity2.setLast_login_date(ltm);
        appMemberEntity2.setEnabled(MDV.YES);
        appMemberEntity2.setC_date(ltm);
        appMemberEntity2.setU_date(ltm);

        appMemberEntity3 = new AppMemberEntity();
        appMemberEntity3.setNick_name("nick3");
        appMemberEntity3.setAllow_mailing(MDV.YES);
        appMemberEntity3.setAllow_message(MDV.YES);
        appMemberEntity3.setLast_login_date(ltm);
        appMemberEntity3.setEnabled(MDV.YES);
        appMemberEntity3.setC_date(ltm);
        appMemberEntity3.setU_date(ltm);

        appMemberEntity4 = new AppMemberEntity();
        appMemberEntity4.setNick_name("nick4");
        appMemberEntity4.setAllow_mailing(MDV.YES);
        appMemberEntity4.setAllow_message(MDV.YES);
        appMemberEntity4.setLast_login_date(ltm);
        appMemberEntity4.setEnabled(MDV.YES);
        appMemberEntity4.setC_date(ltm);
        appMemberEntity4.setU_date(ltm);

        appMemberEntity5 = new AppMemberEntity();
        appMemberEntity5.setNick_name("nick5");
        appMemberEntity5.setAllow_mailing(MDV.YES);
        appMemberEntity5.setAllow_message(MDV.YES);
        appMemberEntity5.setLast_login_date(ltm);
        appMemberEntity5.setEnabled(MDV.YES);
        appMemberEntity5.setC_date(ltm);
        appMemberEntity5.setU_date(ltm);

    }

    /**
     * tbl_member 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void memberBasicTest() {
        // pre select list
        long count1 = memberDao.count(null, null, null, MDV.NUSE);
        List<MemberEntity> list1 = memberDao.get(null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        assertThat(memberEntity1.getMember_srl(), is((long) MDV.NUSE));
        memberDao.add(memberEntity1);
        assertThat((memberEntity1.getMember_srl() > 0), is(true));

        assertThat(memberEntity2.getMember_srl(), is((long) MDV.NUSE));
        memberDao.add(memberEntity2);
        assertThat((memberEntity2.getMember_srl() > 0), is(true));

        assertThat(memberEntity3.getMember_srl(), is((long) MDV.NUSE));
        memberDao.add(memberEntity3);
        assertThat((memberEntity3.getMember_srl() > 0), is(true));

        // select one
        MemberEntity resultVo1 = memberDao.get(memberEntity1.getMember_srl(), null);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(memberEntity1), is(true));

        MemberEntity resultVo2 = memberDao.get(memberEntity2.getMember_srl(), null);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(memberEntity2), is(true));

        MemberEntity resultVo3 = memberDao.get(memberEntity3.getMember_srl(), null);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(memberEntity3), is(true));

        // select list
        long count2 = memberDao.count(null, null, null, MDV.NUSE);
        assertThat(count1+3, is(count2));
        List<MemberEntity> list2 = memberDao.get(null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        // modify
        String next_user_password = "kkkdddhhh";
        String next_email_address = "kkddhh@ckstack.com";
        int next_email_confirm = MDV.YES;
        MemberEntity updateVo = new MemberEntity();
        updateVo.init();
        updateVo.setUser_password(next_user_password);
        updateVo.setEmail_address(next_email_address);
        updateVo.setEmail_confirm(next_email_confirm);
        updateVo.setMember_srl(memberEntity1.getMember_srl());
        memberDao.modify(updateVo, memberEntity1.getMember_srl(), null);

        // select one
        MemberEntity resultVo4 = memberDao.get(memberEntity1.getMember_srl(), null);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(memberEntity1), is(false));
        assertThat(resultVo4.getMember_srl(), is(memberEntity1.getMember_srl()));
        assertThat(resultVo4.getUser_password(), is(next_user_password));
        assertThat(resultVo4.getEmail_address(), is(next_email_address));
        assertThat(resultVo4.getEmail_confirm(), is(next_email_confirm));

        // delete
        memberDao.delete(memberEntity1.getMember_srl(), null, null);

        // select one
        MemberEntity resultVo5 = memberDao.get(memberEntity1.getMember_srl(), null);
        assertThat(resultVo5, is(nullValue()));
    }

    /**
     * tbl_member 에서 select one 했는데 select list 가 나왔을때 예외 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneMemberTest() {
        // insert
        assertThat(memberEntity1.getMember_srl(), is((long) MDV.NUSE));
        memberDao.add(memberEntity1);
        assertThat((memberEntity1.getMember_srl() > 0), is(true));

        assertThat(memberEntity2.getMember_srl(), is((long) MDV.NUSE));
        memberDao.add(memberEntity2);
        assertThat((memberEntity2.getMember_srl() > 0), is(true));

        memberDao.get(MDV.NUSE, null);
    }

    /**
     * tbl_member 에서 조건 없이 업데이트 할때 에러 나는지 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionUpdateMemberTest() {
        // insert
        assertThat(memberEntity1.getMember_srl(), is((long) MDV.NUSE));
        memberDao.add(memberEntity1);
        assertThat((memberEntity1.getMember_srl() > 0), is(true));

        // modify
        String next_user_password = "kkkdddhhh";
        String next_email_address = "kkddhh@ckstack.com";
        int next_email_confirm = MDV.YES;
        MemberEntity updateVo = new MemberEntity();
        updateVo.init();
        updateVo.setUser_password(next_user_password);
        updateVo.setEmail_address(next_email_address);
        updateVo.setEmail_confirm(next_email_confirm);
        updateVo.setMember_srl(memberEntity1.getMember_srl());
        memberDao.modify(updateVo, MDV.NUSE, null);
    }

    /**
     * tbl_member 에서 조건 없이 삭제 할때 에러 나는지 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteMemberTest() {
        // insert
        assertThat(memberEntity1.getMember_srl(), is((long) MDV.NUSE));
        memberDao.add(memberEntity1);
        assertThat((memberEntity1.getMember_srl() > 0), is(true));

        // delete
        memberDao.delete(MDV.NUSE, null, null);
    }

    /**
     * tbl_member 에 unique key 테스트
     */
    @Test(expected = DuplicateKeyException.class)
    @Rollback
    public void duplicateUserIdTest() {
        memberEntity2.setUser_id(memberEntity1.getUser_id());
        memberDao.add(memberEntity1);
        memberDao.add(memberEntity2);
    }

    /**
     * tbl_member 의 row listing 테스트
     */
    @Test
    @Rollback
    public void memberListTest() {
        memberDao.add(memberEntity1);
        memberDao.add(memberEntity2);
        memberDao.add(memberEntity3);
        memberDao.add(memberEntity4);
        memberDao.add(memberEntity5);

        List<MemberEntity> list1 = memberDao.get(null, memberEntity1.getUser_name(), null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list1.size(), is(1));
        assertThat(memberEntity1.equals(list1.get(0)), is(true));

        List<MemberEntity> list2 = memberDao.get(null, null, memberEntity2.getNick_name(), null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list2.size(), is(1));
        assertThat(memberEntity2.equals(list2.get(0)), is(true));

        List<MemberEntity> list3 = memberDao.get(null, null, null, memberEntity3.getMobile_phone_number(),
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list3.size(), is(1));
        assertThat(memberEntity3.equals(list3.get(0)), is(true));

        // select using index indicator
        List<MemberEntity> list4 = memberDao.get(null, null, null, null, MDV.NUSE, 0, 3);
        assertThat(list4.size(), is(3));

        long lastMemberSrl = list4.get(2).getMember_srl();

        List<MemberEntity> list5 = memberDao.get(null, null, null, null, lastMemberSrl, 0, 3);
        assertThat((list5.size() >= 2), is(true));

        List<MemberEntity> list6 = memberDao.get(null, null, null, null, MDV.NUSE, 0, 5);
        assertThat(list6.size(), is(5));
        assertThat(list6.get(3).equals(list5.get(0)), is(true));
        assertThat(list6.get(4).equals(list5.get(1)), is(true));
    }

    /**
     * tbl_member_extra 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void memberExtraBasicTest() {
        // insert prepare data
        memberDao.add(memberEntity1);
        memberDao.add(memberEntity2);
        memberDao.add(memberEntity3);

        List<Long> member_srls = new ArrayList<>();

        // select empty member_srls
        List<MemberExtraEntity> list1 = memberExtraDao.get(member_srls, MDV.NUSE, null);
        assertThat(list1.size() >= 0, is(true));

        // insert
        memberExtraEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberExtraEntity2.setMember_srl(memberEntity2.getMember_srl());
        memberExtraEntity3.setMember_srl(memberEntity3.getMember_srl());

        memberExtraDao.add(memberExtraEntity1);
        memberExtraDao.add(memberExtraEntity2);
        memberExtraDao.add(memberExtraEntity3);

        // select one
        MemberExtraEntity resultVo1 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(memberExtraEntity1), is(true));

        MemberExtraEntity resultVo2 = memberExtraDao.get(memberExtraEntity2.getMember_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(memberExtraEntity2), is(true));

        MemberExtraEntity resultVo3 = memberExtraDao.get(memberExtraEntity3.getMember_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(memberExtraEntity3), is(true));

        // select list
        member_srls.add(memberEntity1.getMember_srl());
        member_srls.add(memberEntity2.getMember_srl());
        member_srls.add(memberEntity3.getMember_srl());

        List<MemberExtraEntity> list2 = memberExtraDao.get(member_srls, MDV.NUSE, null);
        assertThat(list2.size(), is(3));

        // update
        String next_social_id = "kakao333";
        int next_u_date = (int)(DateTime.now().getMillis() / 1000);
        MemberExtraEntity updateVo = new MemberExtraEntity();
        updateVo.init();
        updateVo.setSocial_id(next_social_id);
        updateVo.setU_date(next_u_date);
        memberExtraDao.modify(updateVo, memberExtraEntity2.getMember_srl());

        // select one
        MemberExtraEntity resultVo4 = memberExtraDao.get(memberExtraEntity2.getMember_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(memberExtraEntity2), is(false));
        assertThat(resultVo4.getSocial_id(), is(next_social_id));
        assertThat(resultVo4.getU_date(), is(next_u_date));

        // delete
        memberExtraDao.delete(memberExtraEntity2.getMember_srl());

        // select one
        MemberExtraEntity resultVo5 = memberExtraDao.get(memberExtraEntity2.getMember_srl());
        assertThat(resultVo5, is(nullValue()));

        MemberEntity resultVo6 = memberDao.get(memberExtraEntity2.getMember_srl(), null);
        assertThat(resultVo6, is(notNullValue()));

        // delete member
        memberDao.delete(memberEntity3.getMember_srl(), null, null);

        MemberEntity resultVo7 = memberDao.get(memberEntity3.getMember_srl(), null);
        assertThat(resultVo7, is(nullValue()));
        MemberExtraEntity resultVo8 = memberExtraDao.get(memberExtraEntity3.getMember_srl());
        assertThat(resultVo8, is(nullValue()));
    }

    /**
     * tbl_member_extra 에서 select one 했는데 select list 가 나왔을때 예외 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneMemberExtraTest() {
        // insert prepare data
        memberDao.add(memberEntity1);
        memberExtraEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberExtraDao.add(memberExtraEntity1);

        memberExtraDao.get(MDV.NUSE);
    }

    /**
     * tbl_member_extra 에서 조건 없이 update 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionUpdateMemberExtraTest() {
        // insert prepare data
        memberDao.add(memberEntity1);
        memberExtraEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberExtraDao.add(memberExtraEntity1);

        // select one
        MemberExtraEntity resultVo1 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(memberExtraEntity1), is(true));

        // update
        String next_social_id = "kakao333";
        int next_u_date = (int)(DateTime.now().getMillis() / 1000);
        MemberExtraEntity updateVo = new MemberExtraEntity();
        updateVo.init();
        updateVo.setSocial_id(next_social_id);
        updateVo.setU_date(next_u_date);
        memberExtraDao.modify(updateVo, MDV.NUSE);
    }

    /**
     * tbl_member_extra 에서 조건 없이 delete 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteMemberExtraTest() {
        // insert prepare data
        memberDao.add(memberEntity1);
        memberExtraEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberExtraDao.add(memberExtraEntity1);

        // select one
        MemberExtraEntity resultVo1 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(memberExtraEntity1), is(true));

        // delete
        memberExtraDao.delete(MDV.NUSE);
    }

    /**
     * tbl_member_extra 특정 카운트들 increase 테스트
     */
    @Test
    @Rollback
    public void increaseLoginCountTest() {
        // insert prepare data
        memberDao.add(memberEntity1);
        memberDao.add(memberEntity2);

        // insert
        memberExtraEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberExtraEntity2.setMember_srl(memberEntity2.getMember_srl());

        memberExtraDao.add(memberExtraEntity1);
        memberExtraDao.add(memberExtraEntity2);

        long loginCount1 = memberExtraEntity1.getLogin_count();
        int serialLoginCount1 = memberExtraEntity1.getSerial_login_count();
        long loginCount2 = memberExtraEntity2.getLogin_count();
        int serialLoginCount2 = memberExtraEntity2.getSerial_login_count();

        memberExtraDao.increase(true, false, memberExtraEntity1.getMember_srl());
        MemberExtraEntity resultVo1 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(loginCount1+1, is(resultVo1.getLogin_count()));
        assertThat(serialLoginCount1, is(resultVo1.getSerial_login_count()));

        memberExtraDao.increase(true, false, memberExtraEntity1.getMember_srl());
        MemberExtraEntity resultVo2 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(loginCount1+2, is(resultVo2.getLogin_count()));
        assertThat(serialLoginCount1, is(resultVo2.getSerial_login_count()));

        memberExtraDao.increase(true, false, memberExtraEntity1.getMember_srl());
        MemberExtraEntity resultVo3 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(loginCount1+3, is(resultVo3.getLogin_count()));
        assertThat(serialLoginCount1, is(resultVo3.getSerial_login_count()));

        memberExtraDao.increase(true, true, memberExtraEntity1.getMember_srl());
        MemberExtraEntity resultVo4 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(loginCount1+4, is(resultVo4.getLogin_count()));
        assertThat(serialLoginCount1+1, is(resultVo4.getSerial_login_count()));

        memberExtraDao.increase(false, true, memberExtraEntity1.getMember_srl());
        MemberExtraEntity resultVo5 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(loginCount1+4, is(resultVo5.getLogin_count()));
        assertThat(serialLoginCount1+2, is(resultVo5.getSerial_login_count()));

        MemberExtraEntity resultVo6 = memberExtraDao.get(memberExtraEntity2.getMember_srl());
        assertThat(resultVo6.getLogin_count(), is(loginCount2));
        assertThat(resultVo6.getSerial_login_count(), is(serialLoginCount2));
    }

    /**
     * tbl_member_extra 의 특정 카운트 increase 할때 조건 없으면 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionIncreaseTest() {
        // insert prepare data
        memberDao.add(memberEntity1);
        memberExtraEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberExtraDao.add(memberExtraEntity1);

        // select one
        MemberExtraEntity resultVo1 = memberExtraDao.get(memberExtraEntity1.getMember_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(memberExtraEntity1), is(true));

        memberExtraDao.increase(true, false, MDV.NUSE);
    }

    /**
     * tbl_member, tbl_member_extra 를 join 테스트
     */
    @Test
    @Rollback
    public void memberFullInfoTest() {
        // select list
        long count1 = memberDao.countFullInfo(null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, false);
        List<MemberEntity> list1 = memberDao.getFullInfo(null, null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE, false);
        assertThat((long)list1.size(), is(count1));

        // insert member data
        memberDao.add(memberEntity1);
        memberDao.add(memberEntity2);
        memberDao.add(memberEntity3);
        memberDao.add(memberEntity4);
        memberDao.add(memberEntity5);

        // select list
        // tbl_member 테이블에만 값이 들어 있기 때문에 full info 로 select list 하면 없어야 함
        long count2 = memberDao.countFullInfo(null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, false);
        assertThat(count1, is(count2));
        List<MemberEntity> list2 = memberDao.getFullInfo(null, null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE, false);
        assertThat((long)list2.size(), is(count2));

        // select one
        // tbl_member 테이블에만 값이 들어 있기 때문에 full info 로 select list 하면 없어야 함
        MemberEntity resultVo1 = memberDao.getFullInfo(memberEntity1.getMember_srl(), null);
        assertThat(resultVo1, is(nullValue()));

        MemberEntity resultVo2 = memberDao.getFullInfo(memberEntity2.getMember_srl(), null);
        assertThat(resultVo2, is(nullValue()));

        MemberEntity resultVo3 = memberDao.getFullInfo(memberEntity3.getMember_srl(), null);
        assertThat(resultVo3, is(nullValue()));

        MemberEntity resultVo4 = memberDao.getFullInfo(memberEntity4.getMember_srl(), null);
        assertThat(resultVo4, is(nullValue()));

        MemberEntity resultVo5 = memberDao.getFullInfo(memberEntity5.getMember_srl(), null);
        assertThat(resultVo5, is(nullValue()));

        // insert member_extra data
        memberExtraEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberExtraEntity2.setMember_srl(memberEntity2.getMember_srl());
        memberExtraEntity3.setMember_srl(memberEntity3.getMember_srl());
        memberExtraEntity4.setMember_srl(memberEntity4.getMember_srl());
        memberExtraEntity5.setMember_srl(memberEntity5.getMember_srl());

        memberExtraDao.add(memberExtraEntity1);
        memberExtraDao.add(memberExtraEntity2);
        memberExtraDao.add(memberExtraEntity3);
        memberExtraDao.add(memberExtraEntity4);
        memberExtraDao.add(memberExtraEntity5);

        // select list
        long count3 = memberDao.countFullInfo(null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, false);
        assertThat(count1 + 5, is(count3));
        List<MemberEntity> list3 = memberDao.getFullInfo(null, null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE, false);
        assertThat((long)list3.size(), is(count3));

        // select list
        List<Long> member_srls = new ArrayList<>();
        List<MemberEntity> list3_2 = memberDao.getFullInfo(member_srls, null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE, false);
        assertThat(list3.size(), is(list3_2.size()));

        member_srls.add(memberEntity1.getMember_srl());
        member_srls.add(memberEntity3.getMember_srl());
        List<MemberEntity> list3_3 = memberDao.getFullInfo(member_srls, null, null, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE, false);
        assertThat(list3_3.size(), is(2));

        memberEntity3.setMemberExtraEntity(memberExtraEntity3);
        memberEntity1.setMemberExtraEntity(memberExtraEntity1);
        assertThat(list3_3.get(0).equals(memberEntity3), is(true));
        assertThat(list3_3.get(1).equals(memberEntity1), is(true));

        memberEntity3.setMemberExtraEntity(null);
        memberEntity1.setMemberExtraEntity(null);

        // select one
        MemberEntity resultVo6 = memberDao.getFullInfo(memberEntity1.getMember_srl(), null);
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo6.getMemberExtraEntity().equals(memberExtraEntity1), is(true));

        MemberEntity resultVo7 = memberDao.getFullInfo(memberEntity2.getMember_srl(), null);
        assertThat(resultVo7, is(notNullValue()));
        assertThat(resultVo7.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo7.getMemberExtraEntity().equals(memberExtraEntity2), is(true));

        MemberEntity resultVo8 = memberDao.getFullInfo(memberEntity3.getMember_srl(), null);
        assertThat(resultVo8, is(notNullValue()));
        assertThat(resultVo8.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo8.getMemberExtraEntity().equals(memberExtraEntity3), is(true));

        MemberEntity resultVo9 = memberDao.getFullInfo(memberEntity4.getMember_srl(), null);
        assertThat(resultVo9, is(notNullValue()));
        assertThat(resultVo9.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo9.getMemberExtraEntity().equals(memberExtraEntity4), is(true));

        MemberEntity resultVo10 = memberDao.getFullInfo(memberEntity5.getMember_srl(), null);
        assertThat(resultVo10, is(notNullValue()));
        assertThat(resultVo10.getMemberExtraEntity(), is(notNullValue()));
        assertThat(resultVo10.getMemberExtraEntity().equals(memberExtraEntity5), is(true));

        List<MemberEntity> list4 = memberDao.getFullInfo(null, null, null, null, null,
                MDV.NUSE, MDV.NUSE, memberExtraEntity2.getSocial_type(), memberExtraEntity2.getSocial_id(),
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE, false);
        assertThat(list4.size(), is(1));
        assertThat(list4.get(0).getMember_srl(), is(memberEntity2.getMember_srl()));
        assertThat(list4.get(0).getUser_id(), is(memberEntity2.getUser_id()));
        assertThat(list4.get(0).getMemberExtraEntity().equals(memberExtraEntity2), is(true));
    }

    /**
     * tbl_member, tbl_member_extra join 에서 select one 했는데 select list 가 나왔을때 예외 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneMemberJoinMemberExtraTest() {
        // insert prepare data
        memberDao.add(memberEntity1);
        memberExtraEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberExtraDao.add(memberExtraEntity1);

        memberDao.getFullInfo(MDV.NUSE, null);
    }

    /**
     * member access token 기본 테스트
     */
    @Test
    @Rollback
    public void memberAccessTokenBasicTest() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        // insert prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        // select one
        List<MemberAccessTokenEntity> list1 = memberAccessTokenDao.get(memberEntity1.getMember_srl(),
                appEntity1.getApp_srl(), null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list1.size(), is(0));

        List<MemberAccessTokenEntity> list2 = memberAccessTokenDao.get(memberEntity1.getMember_srl(),
                appEntity2.getApp_srl(), null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list2.size(), is(0));

        List<MemberAccessTokenEntity> list3 = memberAccessTokenDao.get(memberEntity2.getMember_srl(),
                appEntity1.getApp_srl(), null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list3.size(), is(0));

        List<MemberAccessTokenEntity> list4 = memberAccessTokenDao.get(memberEntity2.getMember_srl(),
                appEntity2.getApp_srl(), null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list4.size(), is(0));

        // insert
        // member1 : app1
        memberAccessTokenEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberAccessTokenEntity1.setApp_srl(appEntity1.getApp_srl());
        memberAccessTokenEntity1.setToken_expire(ltm + 60);
        memberAccessTokenDao.add(memberAccessTokenEntity1);
        assertThat(memberAccessTokenEntity1.getToken_srl() > 0, is(true));

        // member1 : app1
        memberAccessTokenEntity2.setMember_srl(memberEntity1.getMember_srl());
        memberAccessTokenEntity2.setApp_srl(appEntity1.getApp_srl());
        memberAccessTokenEntity2.setToken_expire(ltm + 61);
        memberAccessTokenDao.add(memberAccessTokenEntity2);
        assertThat(memberAccessTokenEntity2.getToken_srl() > 0, is(true));

        // member1 : app2
        memberAccessTokenEntity3.setMember_srl(memberEntity1.getMember_srl());
        memberAccessTokenEntity3.setApp_srl(appEntity2.getApp_srl());
        memberAccessTokenEntity3.setToken_expire(ltm + 62);
        memberAccessTokenDao.add(memberAccessTokenEntity3);
        assertThat(memberAccessTokenEntity3.getToken_srl() > 0, is(true));

        // member2 : app1
        memberAccessTokenEntity4.setMember_srl(memberEntity2.getMember_srl());
        memberAccessTokenEntity4.setApp_srl(appEntity1.getApp_srl());
        memberAccessTokenEntity4.setToken_expire(ltm + 63);
        memberAccessTokenDao.add(memberAccessTokenEntity4);
        assertThat(memberAccessTokenEntity4.getToken_srl() > 0, is(true));

        // member2 : app2
        memberAccessTokenEntity5.setMember_srl(memberEntity2.getMember_srl());
        memberAccessTokenEntity5.setApp_srl(appEntity2.getApp_srl());
        memberAccessTokenEntity5.setToken_expire(ltm + 64);
        memberAccessTokenDao.add(memberAccessTokenEntity5);
        assertThat(memberAccessTokenEntity5.getToken_srl() > 0, is(true));

        // select one
        MemberAccessTokenEntity resultVo1 = memberAccessTokenDao.get(MDV.NUSE, memberAccessTokenEntity1.getAccess_token());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(memberAccessTokenEntity1), is(true));

        MemberAccessTokenEntity resultVo2 = memberAccessTokenDao.get(MDV.NUSE, memberAccessTokenEntity2.getAccess_token());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(memberAccessTokenEntity2), is(true));

        MemberAccessTokenEntity resultVo3 = memberAccessTokenDao.get(MDV.NUSE, memberAccessTokenEntity3.getAccess_token());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(memberAccessTokenEntity3), is(true));

        MemberAccessTokenEntity resultVo4 = memberAccessTokenDao.get(MDV.NUSE, memberAccessTokenEntity4.getAccess_token());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(memberAccessTokenEntity4), is(true));

        MemberAccessTokenEntity resultVo5 = memberAccessTokenDao.get(MDV.NUSE, memberAccessTokenEntity5.getAccess_token());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(memberAccessTokenEntity5), is(true));

        // modify
        int next_expire = ltm + 1000;
        MemberAccessTokenEntity updateVo1 = new MemberAccessTokenEntity();
        updateVo1.setToken_expire(next_expire);
        memberAccessTokenDao.modify(updateVo1, MDV.NUSE, memberAccessTokenEntity1.getAccess_token(), MDV.NUSE);

        // select one
        MemberAccessTokenEntity resultVo6 = memberAccessTokenDao.get(MDV.NUSE, memberAccessTokenEntity1.getAccess_token());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(memberAccessTokenEntity1), is(false));
        assertThat(resultVo6.getToken_expire(), is(next_expire));

        // delete
        memberAccessTokenDao.delete(MDV.NUSE, memberAccessTokenEntity1.getAccess_token(), MDV.NUSE);

        // select one
        MemberAccessTokenEntity resultVo7 = memberAccessTokenDao.get(MDV.NUSE, memberAccessTokenEntity1.getAccess_token());
        assertThat(resultVo7, is(nullValue()));
    }

    /**
     * 앱에 가입자 매핑 테스트
     */
    @Test
    @Rollback
    public void appMemberBasicTest() {
        // insert prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        memberDao.add(memberEntity4);
        assertThat(memberEntity4.getMember_srl() > 0, is(true));

        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        // select list
        long count1 = appMemberDao.count(appEntity1.getApp_srl(), MDV.NUSE);
        assertThat(count1, is(0L));
        List<AppMemberEntity> list1 = appMemberDao.get(appEntity1.getApp_srl(), null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        // app1 : member1
        appMemberEntity1.setApp_srl(appEntity1.getApp_srl());
        appMemberEntity1.setMember_srl(memberEntity1.getMember_srl());
        appMemberDao.add(appMemberEntity1);

        // app1 : member2
        appMemberEntity2.setApp_srl(appEntity1.getApp_srl());
        appMemberEntity2.setMember_srl(memberEntity2.getMember_srl());
        appMemberDao.add(appMemberEntity2);

        // app1 : member3
        appMemberEntity3.setApp_srl(appEntity1.getApp_srl());
        appMemberEntity3.setMember_srl(memberEntity3.getMember_srl());
        appMemberDao.add(appMemberEntity3);

        // app2 : member1
        appMemberEntity4.setApp_srl(appEntity2.getApp_srl());
        appMemberEntity4.setMember_srl(memberEntity1.getMember_srl());
        appMemberDao.add(appMemberEntity4);

        // app2 : member4
        appMemberEntity5.setApp_srl(appEntity2.getApp_srl());
        appMemberEntity5.setMember_srl(memberEntity4.getMember_srl());
        appMemberDao.add(appMemberEntity5);

        // select one
        AppMemberEntity resultVo1 = appMemberDao.get(appEntity1.getApp_srl(), memberEntity1.getMember_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(appMemberEntity1), is(true));

        AppMemberEntity resultVo2 = appMemberDao.get(appEntity1.getApp_srl(), memberEntity2.getMember_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(appMemberEntity2), is(true));

        AppMemberEntity resultVo3 = appMemberDao.get(appEntity1.getApp_srl(), memberEntity3.getMember_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(appMemberEntity3), is(true));

        AppMemberEntity resultVo4 = appMemberDao.get(appEntity2.getApp_srl(), memberEntity1.getMember_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(appMemberEntity4), is(true));

        AppMemberEntity resultVo5 = appMemberDao.get(appEntity2.getApp_srl(), memberEntity4.getMember_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(appMemberEntity5), is(true));

        AppMemberEntity resultVo6 = appMemberDao.get(appEntity2.getApp_srl(), memberEntity2.getMember_srl());
        assertThat(resultVo6, is(nullValue()));

        // select list
        long count2 = appMemberDao.count(appEntity1.getApp_srl(), MDV.NUSE);
        assertThat(count2, is(3L));
        List<AppMemberEntity> list2 = appMemberDao.get(appEntity1.getApp_srl(), null, MDV.NUSE, null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        long count3 = appMemberDao.count(appEntity2.getApp_srl(), MDV.NUSE);
        assertThat(count3, is(2L));
        List<AppMemberEntity> list3 = appMemberDao.get(appEntity2.getApp_srl(), null, MDV.NUSE, null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list3.size(), is(count3));

        // modify
        AppMemberEntity updateVo = new AppMemberEntity();
        updateVo.setEnabled(MDV.NO);
        appMemberDao.modify(updateVo, appMemberEntity1.getApp_srl(), appMemberEntity1.getMember_srl());

        AppMemberEntity resultVo7 = appMemberDao.get(appMemberEntity1.getApp_srl(), appMemberEntity1.getMember_srl());
        assertThat(resultVo7, is(notNullValue()));
        assertThat(resultVo7.equals(appMemberEntity1), is(false));
        assertThat(resultVo7.getEnabled(), is(MDV.NO));

        // delete
        appMemberDao.delete(appMemberEntity1.getApp_srl(), appMemberEntity1.getMember_srl());

        // select one
        AppMemberEntity resultVo8 = appMemberDao.get(appMemberEntity1.getApp_srl(), appMemberEntity1.getMember_srl());
        assertThat(resultVo8, is(nullValue()));
    }
}

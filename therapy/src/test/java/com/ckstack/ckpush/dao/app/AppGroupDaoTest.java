package com.ckstack.ckpush.dao.app;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.app.AppGroupEntity;
import com.ckstack.ckpush.domain.app.AppGroupMemberEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by dhkim94 on 15. 7. 15..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class AppGroupDaoTest {
    @Autowired
    private AppDao appDao;
    @Autowired
    private AppGroupDao appGroupDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AppGroupMemberDao appGroupMemberDao;
    @Autowired
    private Properties confCommon;

    private AppEntity appEntity1;
    private AppEntity appEntity2;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberEntity memberEntity3;

    private AppGroupEntity appGroupEntity1;
    private AppGroupEntity appGroupEntity2;
    private AppGroupEntity appGroupEntity3;
    private AppGroupEntity appGroupEntity4;
    private AppGroupEntity appGroupEntity5;

    private AppGroupMemberEntity appGroupMemberEntity1;
    private AppGroupMemberEntity appGroupMemberEntity2;
    private AppGroupMemberEntity appGroupMemberEntity3;

    @Before
    public void setUp() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        String api_key1 = UUID.randomUUID().toString().replaceAll("-", "");
        String api_secret1 = UUID.randomUUID().toString().replaceAll("-", "");

        String api_key2 = UUID.randomUUID().toString().replaceAll("-", "");
        String api_secret2 = UUID.randomUUID().toString().replaceAll("-", "");

        appEntity1 = new AppEntity();
        appEntity1.init();
        appEntity1.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApp_name("junit test app9000");
        appEntity1.setApi_key(api_key1);
        appEntity1.setApp_version("1.0.0");
        appEntity1.setApi_secret(api_secret1);
        appEntity1.setEnabled(MDV.YES);
        appEntity1.setReg_terminal(0);
        appEntity1.setReg_push_terminal(0);
        appEntity1.setC_date(ltm);
        appEntity1.setU_date(ltm);

        appEntity2 = new AppEntity();
        appEntity2.init();
        appEntity2.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApp_name("junit test app2");
        appEntity2.setApi_key(api_key2);
        appEntity2.setApp_version("1.0.1");
        appEntity2.setApi_secret(api_secret2);
        appEntity2.setEnabled(MDV.YES);
        appEntity2.setReg_terminal(0);
        appEntity2.setReg_push_terminal(0);
        appEntity2.setC_date(ltm);
        appEntity2.setU_date(ltm);

        appGroupEntity1 = new AppGroupEntity();
        appGroupEntity1.setGroup_name("app group1");
        appGroupEntity1.setGroup_description("test group1");
        appGroupEntity1.setGroup_type(Integer.parseInt(confCommon.getProperty("system_app_group"), 10));
        appGroupEntity1.setAuthority(confCommon.getProperty("group_role_root"));
        appGroupEntity1.setEnabled(MDV.YES);
        appGroupEntity1.setAllow_default(MDV.NO);
        appGroupEntity1.setC_date(ltm);
        appGroupEntity1.setU_date(ltm);

        appGroupEntity2 = new AppGroupEntity();
        appGroupEntity2.setGroup_name("app group2");
        appGroupEntity2.setGroup_description("test group2");
        appGroupEntity2.setGroup_type(Integer.parseInt(confCommon.getProperty("system_app_group"), 10));
        appGroupEntity2.setAuthority(confCommon.getProperty("group_role_user"));
        appGroupEntity2.setEnabled(MDV.YES);
        appGroupEntity2.setAllow_default(MDV.NO);
        appGroupEntity2.setC_date(ltm);
        appGroupEntity2.setU_date(ltm);

        appGroupEntity3 = new AppGroupEntity();
        appGroupEntity3.setGroup_name("app group3");
        appGroupEntity3.setGroup_description("test group3");
        appGroupEntity3.setGroup_type(Integer.parseInt(confCommon.getProperty("custom_app_group"), 10));
        appGroupEntity3.setAuthority(confCommon.getProperty("group_role_visitor"));
        appGroupEntity3.setEnabled(MDV.YES);
        appGroupEntity3.setAllow_default(MDV.YES);
        appGroupEntity3.setC_date(ltm);
        appGroupEntity3.setU_date(ltm);

        appGroupEntity4 = new AppGroupEntity();
        appGroupEntity4.setGroup_name("app group4");
        appGroupEntity4.setGroup_description("test group4");
        appGroupEntity4.setGroup_type(Integer.parseInt(confCommon.getProperty("system_app_group"), 10));
        appGroupEntity4.setAuthority(confCommon.getProperty("group_role_root"));
        appGroupEntity4.setEnabled(MDV.YES);
        appGroupEntity4.setAllow_default(MDV.NO);
        appGroupEntity4.setC_date(ltm);
        appGroupEntity4.setU_date(ltm);

        appGroupEntity5 = new AppGroupEntity();
        appGroupEntity5.setGroup_name("app group5");
        appGroupEntity5.setGroup_description("test group5");
        appGroupEntity5.setGroup_type(Integer.parseInt(confCommon.getProperty("system_app_group"), 10));
        appGroupEntity5.setAuthority(confCommon.getProperty("group_role_user"));
        appGroupEntity5.setEnabled(MDV.YES);
        appGroupEntity5.setAllow_default(MDV.YES);
        appGroupEntity5.setC_date(ltm);
        appGroupEntity5.setU_date(ltm);

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

        appGroupMemberEntity1 = new AppGroupMemberEntity();
        appGroupMemberEntity1.setC_date(ltm);

        appGroupMemberEntity2 = new AppGroupMemberEntity();
        appGroupMemberEntity2.setC_date(ltm);

        appGroupMemberEntity3 = new AppGroupMemberEntity();
        appGroupMemberEntity3.setC_date(ltm);
    }

    /**
     * appGroupDao CRUD test
     */
    @Test
    @Rollback
    public void appGroupBasicTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        // prepare list
        long count1 = appGroupDao.count(appEntity1.getApp_srl(), null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        List<AppGroupEntity> list1 = appGroupDao.get(appEntity1.getApp_srl(), null, null, MDV.NUSE, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        long count2 = appGroupDao.count(appEntity2.getApp_srl(), null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        List<AppGroupEntity> list2 = appGroupDao.get(appEntity2.getApp_srl(), null, null, MDV.NUSE, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // insert
        appGroupEntity1.setApp_srl(appEntity1.getApp_srl());
        appGroupDao.add(appGroupEntity1);
        assertThat(appGroupEntity1.getApp_group_srl() > 0, is(true));

        appGroupEntity2.setApp_srl(appEntity1.getApp_srl());
        appGroupDao.add(appGroupEntity2);
        assertThat(appGroupEntity2.getApp_group_srl() > 0, is(true));

        appGroupEntity3.setApp_srl(appEntity1.getApp_srl());
        appGroupDao.add(appGroupEntity3);
        assertThat(appGroupEntity3.getApp_group_srl() > 0, is(true));

        appGroupEntity4.setApp_srl(appEntity2.getApp_srl());
        appGroupDao.add(appGroupEntity4);
        assertThat(appGroupEntity4.getApp_group_srl() > 0, is(true));

        appGroupEntity5.setApp_srl(appEntity2.getApp_srl());
        appGroupDao.add(appGroupEntity5);
        assertThat(appGroupEntity5.getApp_group_srl() > 0, is(true));

        // select one
        AppGroupEntity resultVo1 = appGroupDao.get(appGroupEntity1.getApp_group_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(appGroupEntity1), is(true));

        AppGroupEntity resultVo2 = appGroupDao.get(appGroupEntity2.getApp_group_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(appGroupEntity2), is(true));

        AppGroupEntity resultVo3 = appGroupDao.get(appGroupEntity3.getApp_group_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(appGroupEntity3), is(true));

        AppGroupEntity resultVo4 = appGroupDao.get(appGroupEntity4.getApp_group_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(appGroupEntity4), is(true));

        AppGroupEntity resultVo5 = appGroupDao.get(appGroupEntity5.getApp_group_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(appGroupEntity5), is(true));

        // select list
        long count3 = appGroupDao.count(appEntity1.getApp_srl(), null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat(count3, is(count1+3));
        List<AppGroupEntity> list3 = appGroupDao.get(appEntity1.getApp_srl(), null, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list3.size(), is(count3));

        long count4 = appGroupDao.count(appEntity2.getApp_srl(), null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat(count4, is(count2+2));
        List<AppGroupEntity> list4 = appGroupDao.get(appEntity2.getApp_srl(), null, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list4.size(), is(count4));

        // modify one
        String next_group_name = "tttt1112";
        AppGroupEntity updateVo = new AppGroupEntity();
        updateVo.init();
        updateVo.setGroup_name(next_group_name);

        appGroupDao.modify(updateVo, appGroupEntity1.getApp_group_srl(), MDV.NUSE);

        // select one
        AppGroupEntity resultVo6 = appGroupDao.get(appGroupEntity1.getApp_group_srl());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(appGroupEntity1), is(false));
        assertThat(resultVo6.getGroup_name(), is(next_group_name));

        // delete
        appGroupDao.delete(appGroupEntity1.getApp_group_srl(), MDV.NUSE);

        // select one
        AppGroupEntity resultVo7 = appGroupDao.get(appGroupEntity1.getApp_group_srl());
        assertThat(resultVo7, is(nullValue()));
    }

    /**
     * app group member basic test
     */
    @Test
    @Rollback
    public void appGroupMemberBasicTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        appGroupEntity1.setApp_srl(appEntity1.getApp_srl());
        appGroupDao.add(appGroupEntity1);
        assertThat(appGroupEntity1.getApp_group_srl() > 0, is(true));

        appGroupEntity2.setApp_srl(appEntity1.getApp_srl());
        appGroupDao.add(appGroupEntity2);
        assertThat(appGroupEntity2.getApp_group_srl() > 0, is(true));

        // prepare select list
        long count1 = appGroupMemberDao.count(appGroupEntity1.getApp_group_srl(), MDV.NUSE);
        List<AppGroupMemberEntity> list1 = appGroupMemberDao.get(appGroupEntity1.getApp_group_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        long count2 = appGroupMemberDao.count(appGroupEntity2.getApp_group_srl(), MDV.NUSE);
        List<AppGroupMemberEntity> list2 = appGroupMemberDao.get(appGroupEntity2.getApp_group_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        // insert
        // app_group1, member1
        appGroupMemberEntity1.setApp_group_srl(appGroupEntity1.getApp_group_srl());
        appGroupMemberEntity1.setMember_srl(memberEntity1.getMember_srl());
        appGroupMemberDao.add(appGroupMemberEntity1);

        // app_group1, member2
        appGroupMemberEntity2.setApp_group_srl(appGroupEntity1.getApp_group_srl());
        appGroupMemberEntity2.setMember_srl(memberEntity2.getMember_srl());
        appGroupMemberDao.add(appGroupMemberEntity2);

        // app_group2, member3
        appGroupMemberEntity3.setApp_group_srl(appGroupEntity2.getApp_group_srl());
        appGroupMemberEntity3.setMember_srl(memberEntity3.getMember_srl());
        appGroupMemberDao.add(appGroupMemberEntity3);

        // select one
        AppGroupMemberEntity resultVo1 = appGroupMemberDao.get(appGroupMemberEntity1.getApp_group_srl(),
                memberEntity1.getMember_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(appGroupMemberEntity1), is(true));

        AppGroupMemberEntity resultVo2 = appGroupMemberDao.get(appGroupMemberEntity2.getApp_group_srl(),
                memberEntity2.getMember_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(appGroupMemberEntity2), is(true));

        AppGroupMemberEntity resultVo3 = appGroupMemberDao.get(appGroupMemberEntity3.getApp_group_srl(),
                memberEntity3.getMember_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(appGroupMemberEntity3), is(true));

        // select list
        long count3 = appGroupMemberDao.count(appGroupEntity1.getApp_group_srl(), MDV.NUSE);
        assertThat(count3, is(2L));
        List<AppGroupMemberEntity> list3 = appGroupMemberDao.get(appGroupEntity1.getApp_group_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list3.size(), is(count3));

        long count4 = appGroupMemberDao.count(appGroupEntity2.getApp_group_srl(), MDV.NUSE);
        assertThat(count4, is(1L));
        List<AppGroupMemberEntity> list4 = appGroupMemberDao.get(appGroupEntity2.getApp_group_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list4.size(), is(count4));

        // delete
        appGroupMemberDao.delete(appGroupEntity1.getApp_group_srl(), null, null, memberEntity1.getMember_srl());

        // select one
        AppGroupMemberEntity resultVo4 = appGroupMemberDao.get(appGroupMemberEntity1.getApp_group_srl(),
                memberEntity1.getMember_srl());
        assertThat(resultVo4, is(nullValue()));
    }

}

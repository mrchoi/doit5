package com.ckstack.ckpush.dao.user;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.accessory.DeviceDao;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.user.MemberDeviceEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class MemberDeviceDaoTest {
    @Autowired
    private MemberDeviceDao memberDeviceDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private Properties confAccessory;
    @Autowired
    protected Properties confCommon;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;

    private DeviceEntity deviceEntity1;
    private DeviceEntity deviceEntity2;
    private DeviceEntity deviceEntity3;
    private DeviceEntity deviceEntity4;
    private DeviceEntity deviceEntity5;

    private AppEntity appEntity1;
    private AppEntity appEntity2;
    private AppEntity appEntity3;

    private MemberDeviceEntity memberDeviceEntity1;
    private MemberDeviceEntity memberDeviceEntity2;
    private MemberDeviceEntity memberDeviceEntity3;
    private MemberDeviceEntity memberDeviceEntity4;
    private MemberDeviceEntity memberDeviceEntity5;
    private MemberDeviceEntity memberDeviceEntity6;

    @Before
    public void setUp() {
        // 유저는 2명
        // 단말기는 5개
        // 앱의 종류는 2종. 1종은 버전이 2개임.

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        int androidDevice = Integer.parseInt(confCommon.getProperty("user_terminal_android"), 10);

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

        deviceEntity1 = new DeviceEntity();
        deviceEntity1.init();
        deviceEntity1.setOs_version("4.0.1");
        deviceEntity1.setC_date(ltm);
        deviceEntity1.setDevice_id("junit-android-id1");
        deviceEntity1.setDevice_type("LG G-Pro2");
        deviceEntity1.setDevice_class(androidDevice);
        deviceEntity1.setMobile_phone_number("01012341234");
        deviceEntity1.setU_date(ltm);

        deviceEntity2 = new DeviceEntity();
        deviceEntity2.init();
        deviceEntity2.setOs_version("3.1.10");
        deviceEntity2.setC_date(ltm);
        deviceEntity2.setDevice_id("junit-android-id2");
        deviceEntity2.setDevice_type("Samsung Galagy");
        deviceEntity2.setDevice_class(androidDevice);
        deviceEntity2.setMobile_phone_number("01011112222");
        deviceEntity2.setU_date(ltm);

        deviceEntity3 = new DeviceEntity();
        deviceEntity3.init();
        deviceEntity3.setOs_version("5.1.10");
        deviceEntity3.setC_date(ltm);
        deviceEntity3.setDevice_id("junit-android-id3");
        deviceEntity3.setDevice_type("Xiaomi1");
        deviceEntity3.setDevice_class(androidDevice);
        deviceEntity3.setMobile_phone_number("01022223333");
        deviceEntity3.setU_date(ltm);

        deviceEntity4 = new DeviceEntity();
        deviceEntity4.init();
        deviceEntity4.setOs_version("4.1.10");
        deviceEntity4.setC_date(ltm);
        deviceEntity4.setDevice_id("junit-android-id4");
        deviceEntity4.setDevice_type("LG G-Pro3");
        deviceEntity4.setDevice_class(androidDevice);
        deviceEntity4.setMobile_phone_number("01033334444");
        deviceEntity4.setU_date(ltm);

        deviceEntity5 = new DeviceEntity();
        deviceEntity5.init();
        deviceEntity5.setOs_version("4.4.10");
        deviceEntity5.setC_date(ltm);
        deviceEntity5.setDevice_id("junit-android-id5");
        deviceEntity5.setDevice_class(androidDevice);
        deviceEntity5.setDevice_type("G-Pra4");
        deviceEntity5.setU_date(ltm);

        String api_key1 = UUID.randomUUID().toString().replaceAll("-", "");
        String api_secret1 = UUID.randomUUID().toString().replaceAll("-", "");

        appEntity1 = new AppEntity();
        appEntity1.init();
        appEntity1.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApp_name("junit test app1");
        appEntity1.setApi_key(api_key1);
        appEntity1.setApp_version("1.0.0");
        appEntity1.setApi_secret(api_secret1);
        appEntity1.setC_date(ltm);
        appEntity1.setU_date(ltm);

        appEntity2 = new AppEntity();
        appEntity2.init();
        appEntity2.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApp_name("junit test app1");
        appEntity2.setApi_key(api_key1);
        appEntity2.setApp_version("1.0.1");
        appEntity2.setApi_secret(api_secret1);
        appEntity2.setC_date(ltm);
        appEntity2.setU_date(ltm);

        appEntity3 = new AppEntity();
        appEntity3.init();
        appEntity3.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity3.setApp_name("junit test app1");
        appEntity3.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity3.setApp_version("2.0.0");
        appEntity3.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity3.setC_date(ltm);
        appEntity3.setU_date(ltm);

        int gcmEffectType = MDV.PUSH_NOTI_SOUND;

        memberDeviceEntity1 = new MemberDeviceEntity();
        memberDeviceEntity1.setMobile_phone_number("01011112222");
        memberDeviceEntity1.setAllow_push_noti(MDV.YES);
        memberDeviceEntity1.setPush_noti_type(gcmEffectType);
        memberDeviceEntity1.setC_date(ltm);
        memberDeviceEntity1.setU_date(ltm);

        memberDeviceEntity2 = new MemberDeviceEntity();
        memberDeviceEntity2.setMobile_phone_number("01022223333");
        memberDeviceEntity2.setAllow_push_noti(MDV.YES);
        memberDeviceEntity2.setPush_noti_type(gcmEffectType);
        memberDeviceEntity2.setC_date(ltm);
        memberDeviceEntity2.setU_date(ltm);

        memberDeviceEntity3 = new MemberDeviceEntity();
        memberDeviceEntity3.setMobile_phone_number("01033334444");
        memberDeviceEntity3.setAllow_push_noti(MDV.YES);
        memberDeviceEntity3.setPush_noti_type(gcmEffectType);
        memberDeviceEntity3.setC_date(ltm);
        memberDeviceEntity3.setU_date(ltm);

        memberDeviceEntity4 = new MemberDeviceEntity();
        memberDeviceEntity4.setMobile_phone_number("01044445555");
        memberDeviceEntity4.setAllow_push_noti(MDV.YES);
        memberDeviceEntity4.setPush_noti_type(gcmEffectType);
        memberDeviceEntity4.setC_date(ltm);
        memberDeviceEntity4.setU_date(ltm);

        memberDeviceEntity5 = new MemberDeviceEntity();
        memberDeviceEntity5.setMobile_phone_number("01055556666");
        memberDeviceEntity5.setAllow_push_noti(MDV.YES);
        memberDeviceEntity5.setPush_noti_type(gcmEffectType);
        memberDeviceEntity5.setC_date(ltm);
        memberDeviceEntity5.setU_date(ltm);

        memberDeviceEntity6 = new MemberDeviceEntity();
        memberDeviceEntity6.setMobile_phone_number("01066667777");
        memberDeviceEntity6.setAllow_push_noti(MDV.YES);
        memberDeviceEntity6.setPush_noti_type(gcmEffectType);
        memberDeviceEntity6.setC_date(ltm);
        memberDeviceEntity6.setU_date(ltm);
    }

    /**
     * tbl_member_device 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void memberDeviceBasicTest() {
        // prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        deviceDao.add(deviceEntity1);
        assertThat(deviceEntity1.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity2);
        assertThat(deviceEntity2.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity3);
        assertThat(deviceEntity3.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity4);
        assertThat(deviceEntity4.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity5);
        assertThat(deviceEntity5.getDevice_srl() > 0, is(true));

        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));
        appDao.add(appEntity3);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        // pre select list
        long count1 = memberDeviceDao.count(MDV.NUSE, MDV.NUSE, MDV.NUSE);
        List<MemberDeviceEntity> list1 = memberDeviceDao.get(MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        // app1 과 app2 는 동일한 app 이지만 버전이 틀림
        // member1 : device1 : app1(version 1.0.0) - 1
        // member1 : device1 : app2(version 1.0.1) - 2
        // memeer1 : device2 : app1(version 1.0.1) - 3
        // member1 : device3 : app3(version 2.0.0) - 4
        // member2 : device4 : app2(version 1.0.1) - 5
        // member2 : device5 : app3(version 2.0.0) - 6

        memberDeviceEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity1.setDevice_srl(deviceEntity1.getDevice_srl());
        memberDeviceEntity1.setApp_srl(appEntity1.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity1);

        memberDeviceEntity2.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity2.setDevice_srl(deviceEntity1.getDevice_srl());
        memberDeviceEntity2.setApp_srl(appEntity2.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity2);

        memberDeviceEntity3.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity3.setDevice_srl(deviceEntity2.getDevice_srl());
        memberDeviceEntity3.setApp_srl(appEntity1.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity3);

        memberDeviceEntity4.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity4.setDevice_srl(deviceEntity3.getDevice_srl());
        memberDeviceEntity4.setApp_srl(appEntity3.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity4);

        memberDeviceEntity5.setMember_srl(memberEntity2.getMember_srl());
        memberDeviceEntity5.setDevice_srl(deviceEntity4.getDevice_srl());
        memberDeviceEntity5.setApp_srl(appEntity2.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity5);

        memberDeviceEntity6.setMember_srl(memberEntity2.getMember_srl());
        memberDeviceEntity6.setDevice_srl(deviceEntity5.getDevice_srl());
        memberDeviceEntity6.setApp_srl(appEntity3.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity6);

        // select list
        long count2 = memberDeviceDao.count(MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count1 + 6, is(count2));
        List<MemberDeviceEntity> list2 = memberDeviceDao.get(MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // select one
        MemberDeviceEntity resultVo1 = memberDeviceDao.get(memberDeviceEntity1.getMember_srl(),
                memberDeviceEntity1.getApp_srl(), memberDeviceEntity1.getDevice_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(memberDeviceEntity1), is(true));

        MemberDeviceEntity resultVo2 = memberDeviceDao.get(memberDeviceEntity2.getMember_srl(),
                memberDeviceEntity2.getApp_srl(), memberDeviceEntity2.getDevice_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(memberDeviceEntity2), is(true));

        MemberDeviceEntity resultVo3 = memberDeviceDao.get(memberDeviceEntity3.getMember_srl(),
                memberDeviceEntity3.getApp_srl(), memberDeviceEntity3.getDevice_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(memberDeviceEntity3), is(true));

        MemberDeviceEntity resultVo4 = memberDeviceDao.get(memberDeviceEntity4.getMember_srl(),
                memberDeviceEntity4.getApp_srl(), memberDeviceEntity4.getDevice_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(memberDeviceEntity4), is(true));

        MemberDeviceEntity resultVo5 = memberDeviceDao.get(memberDeviceEntity5.getMember_srl(),
                memberDeviceEntity5.getApp_srl(), memberDeviceEntity5.getDevice_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(memberDeviceEntity5), is(true));

        MemberDeviceEntity resultVo6 = memberDeviceDao.get(memberDeviceEntity6.getMember_srl(),
                memberDeviceEntity6.getApp_srl(), memberDeviceEntity6.getDevice_srl());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(memberDeviceEntity6), is(true));

        // modify
        int next_allow_push_noti = MDV.NO;
        int next_u_date = (int)(DateTime.now().getMillis() / 1000);
        MemberDeviceEntity updateVo = new MemberDeviceEntity();
        updateVo.init();
        updateVo.setAllow_push_noti(next_allow_push_noti);
        updateVo.setU_date(next_u_date);
        memberDeviceDao.modify(updateVo, memberDeviceEntity1.getMember_srl(), memberDeviceEntity1.getApp_srl(),
                memberDeviceEntity1.getDevice_srl());

        // select one
        MemberDeviceEntity resultVo7 = memberDeviceDao.get(memberDeviceEntity1.getMember_srl(),
                memberDeviceEntity1.getApp_srl(), memberDeviceEntity1.getDevice_srl());
        assertThat(resultVo7, is(notNullValue()));
        assertThat(resultVo7.equals(memberDeviceEntity1), is(false));
        assertThat(resultVo7.getAllow_push_noti(), is(next_allow_push_noti));
        assertThat(resultVo7.getU_date(), is(next_u_date));

        // delete
        memberDeviceDao.delete(memberDeviceEntity1.getMember_srl(),
                memberDeviceEntity1.getApp_srl(), memberDeviceEntity1.getDevice_srl());

        // select one
        MemberDeviceEntity resultVo8 = memberDeviceDao.get(memberDeviceEntity1.getMember_srl(),
                memberDeviceEntity1.getApp_srl(), memberDeviceEntity1.getDevice_srl());
        assertThat(resultVo8, is(nullValue()));

        // device_srl 은 FK 가 걸려 있지 않기 때문에 device 삭제 가능 하다.
        deviceDao.delete(memberDeviceEntity6.getDevice_srl(), null, null);

        MemberDeviceEntity resultVo9 = memberDeviceDao.get(memberDeviceEntity6.getMember_srl(),
                memberDeviceEntity6.getApp_srl(), memberDeviceEntity6.getDevice_srl());
        assertThat(resultVo9, is(notNullValue()));

        DeviceEntity resultVo10 = deviceDao.get(memberDeviceEntity6.getDevice_srl(), null);
        assertThat(resultVo10, is(nullValue()));
    }

    /**
     * tbl_member_device select one 조건이 유효하지 않을때 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneMemberDeviceTest() {
        // prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        deviceDao.add(deviceEntity1);
        assertThat(deviceEntity1.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity2);
        assertThat(deviceEntity2.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity3);
        assertThat(deviceEntity3.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity4);
        assertThat(deviceEntity4.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity5);
        assertThat(deviceEntity5.getDevice_srl() > 0, is(true));

        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));
        appDao.add(appEntity3);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        // insert
        memberDeviceEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity1.setDevice_srl(deviceEntity1.getDevice_srl());
        memberDeviceEntity1.setApp_srl(appEntity1.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity1);

        memberDeviceEntity2.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity2.setDevice_srl(deviceEntity1.getDevice_srl());
        memberDeviceEntity2.setApp_srl(appEntity2.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity2);

        memberDeviceEntity3.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity3.setDevice_srl(deviceEntity2.getDevice_srl());
        memberDeviceEntity3.setApp_srl(appEntity1.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity3);

        memberDeviceEntity4.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity4.setDevice_srl(deviceEntity3.getDevice_srl());
        memberDeviceEntity4.setApp_srl(appEntity3.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity4);

        memberDeviceEntity5.setMember_srl(memberEntity2.getMember_srl());
        memberDeviceEntity5.setDevice_srl(deviceEntity4.getDevice_srl());
        memberDeviceEntity5.setApp_srl(appEntity2.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity5);

        memberDeviceEntity6.setMember_srl(memberEntity2.getMember_srl());
        memberDeviceEntity6.setDevice_srl(deviceEntity5.getDevice_srl());
        memberDeviceEntity6.setApp_srl(appEntity3.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity6);

        memberDeviceDao.get(MDV.NUSE, MDV.NUSE, MDV.NUSE);
    }

    /**
     * tbl_member_device update 조건이 유효하지 않을때 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionUpdateMemberDeviceTest() {
        // prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        deviceDao.add(deviceEntity1);
        assertThat(deviceEntity1.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity2);
        assertThat(deviceEntity2.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity3);
        assertThat(deviceEntity3.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity4);
        assertThat(deviceEntity4.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity5);
        assertThat(deviceEntity5.getDevice_srl() > 0, is(true));

        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));
        appDao.add(appEntity3);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        // insert
        memberDeviceEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity1.setDevice_srl(deviceEntity1.getDevice_srl());
        memberDeviceEntity1.setApp_srl(appEntity1.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity1);

        memberDeviceEntity2.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity2.setDevice_srl(deviceEntity1.getDevice_srl());
        memberDeviceEntity2.setApp_srl(appEntity2.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity2);

        memberDeviceEntity3.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity3.setDevice_srl(deviceEntity2.getDevice_srl());
        memberDeviceEntity3.setApp_srl(appEntity1.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity3);

        memberDeviceEntity4.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity4.setDevice_srl(deviceEntity3.getDevice_srl());
        memberDeviceEntity4.setApp_srl(appEntity3.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity4);

        memberDeviceEntity5.setMember_srl(memberEntity2.getMember_srl());
        memberDeviceEntity5.setDevice_srl(deviceEntity4.getDevice_srl());
        memberDeviceEntity5.setApp_srl(appEntity2.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity5);

        memberDeviceEntity6.setMember_srl(memberEntity2.getMember_srl());
        memberDeviceEntity6.setDevice_srl(deviceEntity5.getDevice_srl());
        memberDeviceEntity6.setApp_srl(appEntity3.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity6);

        // modify
        int next_allow_push_noti = MDV.NO;
        int next_u_date = (int)(DateTime.now().getMillis() / 1000);
        MemberDeviceEntity updateVo = new MemberDeviceEntity();
        updateVo.init();
        updateVo.setAllow_push_noti(next_allow_push_noti);
        updateVo.setU_date(next_u_date);
        memberDeviceDao.modify(updateVo, MDV.NUSE, MDV.NUSE, MDV.NUSE);
    }

    /**
     * tbl_member_device delete 조건이 유효하지 않을때 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteMemberDeviceTest() {
        // prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        deviceDao.add(deviceEntity1);
        assertThat(deviceEntity1.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity2);
        assertThat(deviceEntity2.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity3);
        assertThat(deviceEntity3.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity4);
        assertThat(deviceEntity4.getDevice_srl() > 0, is(true));
        deviceDao.add(deviceEntity5);
        assertThat(deviceEntity5.getDevice_srl() > 0, is(true));

        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));
        appDao.add(appEntity3);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        // insert
        memberDeviceEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity1.setDevice_srl(deviceEntity1.getDevice_srl());
        memberDeviceEntity1.setApp_srl(appEntity1.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity1);

        memberDeviceEntity2.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity2.setDevice_srl(deviceEntity1.getDevice_srl());
        memberDeviceEntity2.setApp_srl(appEntity2.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity2);

        memberDeviceEntity3.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity3.setDevice_srl(deviceEntity2.getDevice_srl());
        memberDeviceEntity3.setApp_srl(appEntity1.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity3);

        memberDeviceEntity4.setMember_srl(memberEntity1.getMember_srl());
        memberDeviceEntity4.setDevice_srl(deviceEntity3.getDevice_srl());
        memberDeviceEntity4.setApp_srl(appEntity3.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity4);

        memberDeviceEntity5.setMember_srl(memberEntity2.getMember_srl());
        memberDeviceEntity5.setDevice_srl(deviceEntity4.getDevice_srl());
        memberDeviceEntity5.setApp_srl(appEntity2.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity5);

        memberDeviceEntity6.setMember_srl(memberEntity2.getMember_srl());
        memberDeviceEntity6.setDevice_srl(deviceEntity5.getDevice_srl());
        memberDeviceEntity6.setApp_srl(appEntity3.getApp_srl());
        memberDeviceDao.add(memberDeviceEntity6);

        memberDeviceDao.delete(MDV.NUSE, MDV.NUSE, MDV.NUSE);
    }
}

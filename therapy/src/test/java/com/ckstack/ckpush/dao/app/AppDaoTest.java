package com.ckstack.ckpush.dao.app;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.accessory.DeviceDao;
import com.ckstack.ckpush.domain.app.AppDeviceEntity;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
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

import java.util.*;

/**
 * Created by dhkim94 on 15. 3. 25..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class AppDaoTest {
    @Autowired
    private AppDao appDao;
    @Autowired
    private AppDeviceDao appDeviceDao;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    protected Properties confCommon;

    private AppEntity appEntity1;
    private AppEntity appEntity2;
    private AppEntity appEntity3;
    private AppEntity appEntity4;
    private AppEntity appEntity5;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;

    private AppDeviceEntity appDeviceEntity1;
    private AppDeviceEntity appDeviceEntity2;
    private AppDeviceEntity appDeviceEntity3;
    private AppDeviceEntity appDeviceEntity4;
    private AppDeviceEntity appDeviceEntity5;

    private DeviceEntity deviceEntity1;
    private DeviceEntity deviceEntity2;
    private DeviceEntity deviceEntity3;
    private DeviceEntity deviceEntity4;
    private DeviceEntity deviceEntity5;

    @Before
    public void setUp() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        String api_key1 = UUID.randomUUID().toString().replaceAll("-", "");
        String api_secret1 = UUID.randomUUID().toString().replaceAll("-", "");

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
        appEntity2.setApi_key(api_key1);
        appEntity2.setApp_version("1.0.1");
        appEntity2.setApi_secret(api_secret1);
        appEntity2.setEnabled(MDV.YES);
        appEntity2.setReg_terminal(0);
        appEntity2.setReg_push_terminal(0);
        appEntity2.setC_date(ltm);
        appEntity2.setU_date(ltm);

        appEntity3 = new AppEntity();
        appEntity3.init();
        appEntity3.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity3.setApp_name("junit test app3");
        appEntity3.setApi_key(api_key1);
        appEntity3.setApp_version("1.0.2");
        appEntity3.setApi_secret(api_secret1);
        appEntity3.setEnabled(MDV.YES);
        appEntity3.setReg_terminal(0);
        appEntity3.setReg_push_terminal(0);
        appEntity3.setC_date(ltm);
        appEntity3.setU_date(ltm);

        appEntity4 = new AppEntity();
        appEntity4.init();
        appEntity4.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity4.setApp_name("junit test app4");
        appEntity4.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity4.setApp_version("1.0.0");
        appEntity4.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity4.setEnabled(MDV.NO);
        appEntity4.setReg_terminal(0);
        appEntity4.setReg_push_terminal(0);
        appEntity4.setC_date(ltm);
        appEntity4.setU_date(ltm);

        appEntity5 = new AppEntity();
        appEntity5.init();
        appEntity5.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity5.setApp_name("junit test app5");
        appEntity5.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity5.setApp_version("2.1.2");
        appEntity5.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity5.setEnabled(MDV.DENY);
        appEntity5.setReg_terminal(0);
        appEntity5.setReg_push_terminal(0);
        appEntity5.setC_date(ltm);
        appEntity5.setU_date(ltm);

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

        appDeviceEntity1 = new AppDeviceEntity();
        appDeviceEntity1.setPush_id("rid 1");
        appDeviceEntity1.setAllow_push(MDV.YES);
        appDeviceEntity1.setReg_push_id(MDV.YES);
        appDeviceEntity1.setEnabled(MDV.YES);
        appDeviceEntity1.setC_date(ltm);
        appDeviceEntity1.setU_date(ltm);

        appDeviceEntity2 = new AppDeviceEntity();
        appDeviceEntity2.setPush_id("rid 2");
        appDeviceEntity2.setAllow_push(MDV.YES);
        appDeviceEntity2.setReg_push_id(MDV.YES);
        appDeviceEntity2.setEnabled(MDV.YES);
        appDeviceEntity2.setC_date(ltm);
        appDeviceEntity2.setU_date(ltm);

        appDeviceEntity3 = new AppDeviceEntity();
        appDeviceEntity3.setPush_id("rid 3");
        appDeviceEntity3.setAllow_push(MDV.YES);
        appDeviceEntity3.setReg_push_id(MDV.YES);
        appDeviceEntity3.setEnabled(MDV.YES);
        appDeviceEntity3.setC_date(ltm);
        appDeviceEntity3.setU_date(ltm);

        appDeviceEntity4 = new AppDeviceEntity();
        appDeviceEntity4.setPush_id("rid 4");
        appDeviceEntity4.setAllow_push(MDV.YES);
        appDeviceEntity4.setReg_push_id(MDV.YES);
        appDeviceEntity4.setEnabled(MDV.YES);
        appDeviceEntity4.setC_date(ltm);
        appDeviceEntity4.setU_date(ltm);

        appDeviceEntity5 = new AppDeviceEntity();
        appDeviceEntity5.setPush_id("rid 5");
        appDeviceEntity5.setAllow_push(MDV.YES);
        appDeviceEntity5.setReg_push_id(MDV.YES);
        appDeviceEntity5.setEnabled(MDV.YES);
        appDeviceEntity5.setC_date(ltm);
        appDeviceEntity5.setU_date(ltm);

        int androidDevice = Integer.parseInt(confCommon.getProperty("user_terminal_android"), 10);

        deviceEntity1 = new DeviceEntity();
        deviceEntity1.init();
        deviceEntity1.setOs_version("4.0.1");
        deviceEntity1.setC_date(ltm);
        deviceEntity1.setDevice_id("junit-android-id1");
        deviceEntity1.setDevice_type("LG G-Pro2");
        deviceEntity1.setDevice_class(androidDevice);
        deviceEntity1.setMobile_phone_number("01011112222");
        deviceEntity1.setU_date(ltm);

        deviceEntity2 = new DeviceEntity();
        deviceEntity2.init();
        deviceEntity2.setOs_version("3.1.10");
        deviceEntity2.setC_date(ltm);
        deviceEntity2.setDevice_id("junit-android-id2");
        deviceEntity2.setDevice_type("Samsung Galagy");
        deviceEntity2.setDevice_class(androidDevice);
        deviceEntity2.setMobile_phone_number("01022223333");
        deviceEntity2.setU_date(ltm);

        deviceEntity3 = new DeviceEntity();
        deviceEntity3.init();
        deviceEntity3.setOs_version("5.1.10");
        deviceEntity3.setC_date(ltm);
        deviceEntity3.setDevice_id("junit-android-id3");
        deviceEntity3.setDevice_type("Xiaomi1");
        deviceEntity3.setDevice_class(androidDevice);
        deviceEntity3.setMobile_phone_number("01033334444");
        deviceEntity3.setU_date(ltm);

        deviceEntity4 = new DeviceEntity();
        deviceEntity4.init();
        deviceEntity4.setOs_version("4.1.10");
        deviceEntity4.setC_date(ltm);
        deviceEntity4.setDevice_id("junit-android-id4");
        deviceEntity4.setDevice_type("LG G-Pro3");
        deviceEntity4.setDevice_class(androidDevice);
        deviceEntity4.setMobile_phone_number("01044445555");
        deviceEntity4.setU_date(ltm);

        deviceEntity5 = new DeviceEntity();
        deviceEntity5.init();
        deviceEntity5.setOs_version("4.4.10");
        deviceEntity5.setC_date(ltm);
        deviceEntity5.setDevice_id("junit-android-id5");
        deviceEntity5.setDevice_class(androidDevice);
        deviceEntity5.setDevice_type("G-Pra4");
        deviceEntity5.setU_date(ltm);
    }

    /**
     * tbl_app 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void appBasicTest() {
        // pre select list
        long count1 = appDao.count(null, null, null, null, MDV.NUSE, MDV.NUSE);
        List<AppEntity> list1 = appDao.get(null, null, null, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        List<Integer> appSrls = new ArrayList<>();
        List<AppEntity> list1_2 = appDao.get(null, null, null, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat(list1_2.size(), is(list1.size()));

        // insert
        assertThat(appEntity1.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity1);
        System.out.println(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        assertThat(appEntity2.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        assertThat(appEntity3.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity3);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        assertThat(appEntity4.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity4);
        assertThat(appEntity4.getApp_srl() > 0, is(true));

        assertThat(appEntity5.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity5);
        assertThat(appEntity5.getApp_srl() > 0, is(true));

        // select one
        AppEntity resultVo1 = appDao.get(appEntity1.getApp_srl(), null, null);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(appEntity1), is(true));

        AppEntity resultVo2 = appDao.get(appEntity2.getApp_srl(), null, null);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(appEntity2), is(true));

        AppEntity resultVo3 = appDao.get(appEntity3.getApp_srl(), null, null);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(appEntity3), is(true));

        AppEntity resultVo4 = appDao.get(appEntity4.getApp_srl(), null, null);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(appEntity4), is(true));

        AppEntity resultVo5 = appDao.get(appEntity5.getApp_srl(), null, null);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(appEntity5), is(true));

        // select list
        long count2 = appDao.count(null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat(count1 + 5, is(count2));
        List<AppEntity> list2 = appDao.get(null, null, null, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // modify one
        // app_srl 로 업데이트 하면 지정한 하나에만 반영 된다.
        String next_app_name = "junit modify app name1";
        String next_api_secret = UUID.randomUUID().toString().replaceAll("-", "");
        AppEntity updateVo = new AppEntity();
        updateVo.init();
        updateVo.setApp_name(next_app_name);
        updateVo.setApi_secret(next_api_secret);

        appDao.modify(updateVo, appEntity1.getApp_srl(), null);

        // select one
        AppEntity result6 = appDao.get(appEntity1.getApp_srl(), null, null);
        assertThat(result6, is(notNullValue()));
        assertThat(result6.getApp_name(), is(next_app_name));
        assertThat(result6.getApi_secret(), is(next_api_secret));

        AppEntity result7 = appDao.get(appEntity2.getApp_srl(), null, null);
        assertThat(result7, is(notNullValue()));
        assertThat(result7.getApp_name(), not(next_app_name));
        assertThat(result7.getApi_secret(), not(next_api_secret));

        // modify multiple
        // api_key 로 업데이트 하면 동일한 api_key 에 모두 반영 된다.
        appDao.modify(updateVo, MDV.NUSE, appEntity1.getApi_key());

        // select one
        AppEntity result8 = appDao.get(appEntity1.getApp_srl(), null, null);
        assertThat(result8, is(notNullValue()));
        assertThat(result8.getApp_name(), is(next_app_name));
        assertThat(result8.getApi_secret(), is(next_api_secret));

        AppEntity result9 = appDao.get(appEntity2.getApp_srl(), null, null);
        assertThat(result9, is(notNullValue()));
        assertThat(result9.getApp_name(), is(next_app_name));
        assertThat(result9.getApi_secret(), is(next_api_secret));

        AppEntity result10 = appDao.get(appEntity3.getApp_srl(), null, null);
        assertThat(result10, is(notNullValue()));
        assertThat(result10.getApp_name(), is(next_app_name));
        assertThat(result10.getApi_secret(), is(next_api_secret));

        // increase
        AppEntity result10_2 = appDao.get(appEntity1.getApp_srl(), null, null);
        appDao.increase(appEntity1.getApp_srl(), null, true, false, false);
        AppEntity result10_3 = appDao.get(appEntity1.getApp_srl(), null, null);
        assertThat(result10_2.getReg_terminal()+1, is(result10_3.getReg_terminal()));

        appDao.increase(appEntity1.getApp_srl(), null, true, false, false);
        AppEntity result10_4 = appDao.get(appEntity1.getApp_srl(), null, null);
        assertThat(result10_2.getReg_terminal() + 2, is(result10_4.getReg_terminal()));

        appDao.increase(appEntity1.getApp_srl(), null, true, true, false);
        appDao.increase(appEntity1.getApp_srl(), null, true, true, false);

        AppEntity result10_5 = appDao.get(appEntity1.getApp_srl(), null, null);
        assertThat(result10_2.getReg_terminal() + 4, is(result10_5.getReg_terminal()));
        assertThat(result10_2.getReg_push_terminal() + 2, is(result10_5.getReg_push_terminal()));


        // delete
        appDao.delete(appEntity4.getApp_srl(), null);

        // select one
        AppEntity result11 = appDao.get(appEntity4.getApp_srl(), null, null);
        assertThat(result11, is(nullValue()));

        AppEntity result12 = appDao.get(appEntity5.getApp_srl(), null, null);
        assertThat(result12, is(notNullValue()));

        appSrls.clear();
        appSrls.add(appEntity1.getApp_srl());
        appSrls.add(appEntity2.getApp_srl());

        // delete
        appDao.delete(MDV.NUSE, appSrls);

        AppEntity result13 = appDao.get(appEntity1.getApp_srl(), null, null);
        assertThat(result13, is(nullValue()));

        AppEntity result14 = appDao.get(appEntity2.getApp_srl(), null, null);
        assertThat(result14, is(nullValue()));
    }

    /**
     * tbl_app select one 조건이 유효하지 않을때 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneAppTest() {
        // insert
        assertThat(appEntity1.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        assertThat(appEntity2.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        assertThat(appEntity3.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity3);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        assertThat(appEntity4.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity4);
        assertThat(appEntity4.getApp_srl() > 0, is(true));

        assertThat(appEntity5.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity5);
        assertThat(appEntity5.getApp_srl() > 0, is(true));

        appDao.get(MDV.NUSE, null, null);
    }

    /**
     * tbl_app update row 조건이 유효하지 않을때 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionUpdateAppTest() {
        // insert
        assertThat(appEntity1.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        assertThat(appEntity2.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        assertThat(appEntity3.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity3);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        assertThat(appEntity4.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity4);
        assertThat(appEntity4.getApp_srl() > 0, is(true));

        assertThat(appEntity5.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity5);
        assertThat(appEntity5.getApp_srl() > 0, is(true));

        String next_app_name = "junit modify app name1";
        String next_api_secret = UUID.randomUUID().toString().replaceAll("-", "");
        AppEntity updateVo = new AppEntity();
        updateVo.init();
        updateVo.setApp_name(next_app_name);
        updateVo.setApi_secret(next_api_secret);

        appDao.modify(updateVo, MDV.NUSE, null);
    }

    /**
     * tbl_app row delete 조건이 유효하지 않을때 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteAppTest() {
        // insert
        assertThat(appEntity1.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        assertThat(appEntity2.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        assertThat(appEntity3.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity3);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        assertThat(appEntity4.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity4);
        assertThat(appEntity4.getApp_srl() > 0, is(true));

        assertThat(appEntity5.getApp_srl(), is(MDV.NUSE));
        appDao.add(appEntity5);
        assertThat(appEntity5.getApp_srl() > 0, is(true));

        appDao.delete(MDV.NUSE, null);
    }

    /**
     * api_key, app_version 은 복합 해서 unique 한 값이다.
     */
    @Test(expected = DuplicateKeyException.class)
    @Rollback
    public void duplicatedAppTest() {
        appEntity2.setApi_key(appEntity1.getApi_key());
        appEntity2.setApp_version(appEntity1.getApp_version());

        appDao.add(appEntity1);
        appDao.add(appEntity2);
    }

    @Test
    @Rollback
    public void appListTest() {
        // prepare data
        appDao.add(appEntity1);
        appDao.add(appEntity2);
        appDao.add(appEntity3);
        appDao.add(appEntity4);
        appDao.add(appEntity5);

        // select list by api_key
        List<AppEntity> list1 = appDao.get(null, null, appEntity1.getApi_key(), null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat(list1.size(), is(3));

        // select list by device_class
        List<AppEntity> list2 = appDao.get(null, null, null, null, MDV.NUSE, MDV.NUSE, null,
                MDV.NUSE, MDV.NUSE);
        assertThat(list2.size() >= 4, is(true));

        // select using index indicator
        List<AppEntity> list3 = appDao.get(null, null, null, null, MDV.NUSE, MDV.NUSE, null,
                0, 3);
        assertThat(list3.size(), is(3));

        int lastAppSrl = list3.get(2).getApp_srl();

        List<AppEntity> list4 = appDao.get(null, null, null, null, MDV.NUSE, lastAppSrl, null,
                0, 3);
        assertThat(list4.size() >= 2, is(true));

        List<AppEntity> list5 = appDao.get(null, null, null, null, MDV.NUSE, MDV.NUSE, null,
                0, 5);
        assertThat(list5.size(), is(5));
        assertThat(list5.get(3).equals(list4.get(0)), is(true));
        assertThat(list5.get(4).equals(list4.get(1)), is(true));

        // like 검색
        long count1 = appDao.count(null, "junit", null, null, MDV.NUSE, MDV.NUSE);
        assertThat(count1 > 0, is(true));
        List<AppEntity> list6 = appDao.get(null, "junit", null, null, MDV.NUSE, MDV.NUSE, null,
                MDV.NUSE, MDV.NUSE);
        assertThat(list6, is(notNullValue()));
        assertThat((long)list6.size(), is(count1));

        Map<String, String> sort = new LinkedHashMap<>();
        sort.put("app_name", "asc");
        sort.put("app_srl", "desc");
        List<AppEntity> list7 = appDao.get(null, "junit", null, null, MDV.NUSE, MDV.NUSE, sort,
                MDV.NUSE, MDV.NUSE);
        assertThat(list7, is(notNullValue()));
        assertThat((long) list7.size(), is(count1));
        // ORDER BY app_name asc, app_srl desc 로 정렬 했으므로 appEntity2 가 가장 먼저 나와야 한다.
        assertThat(list7.get(0).equals(appEntity2), is(true));

        // appSrls 로 검색
        List<Integer> appSrls = new ArrayList<>();
        appSrls.add(appEntity1.getApp_srl());
        appSrls.add(appEntity2.getApp_srl());
        appSrls.add(appEntity3.getApp_srl());

        List<AppEntity> list8 = appDao.get(appSrls, null, null, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat(list8.size(), is(3));
        assertThat(list1.get(0).equals(appEntity3), is(true));
        assertThat(list1.get(1).equals(appEntity2), is(true));
        assertThat(list1.get(2).equals(appEntity1), is(true));


    }

    /**
     * app 과 device 간의 mapping 테이블 기본 테스트
     */
    @Test
    @Rollback
    public void appDeviceBasicTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

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

        // prepare select list
        long count1 = appDeviceDao.count(appEntity1.getApp_srl(), MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count1, is(0L));
        List<AppDeviceEntity> list1 = appDeviceDao.get(appEntity1.getApp_srl(), MDV.NUSE,
                MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        // insert
        appDeviceEntity1.setApp_srl(appEntity1.getApp_srl());
        appDeviceEntity1.setDevice_srl(deviceEntity1.getDevice_srl());
        appDeviceDao.add(appDeviceEntity1);

        appDeviceEntity2.setApp_srl(appEntity1.getApp_srl());
        appDeviceEntity2.setDevice_srl(deviceEntity2.getDevice_srl());
        appDeviceDao.add(appDeviceEntity2);

        appDeviceEntity3.setApp_srl(appEntity1.getApp_srl());
        appDeviceEntity3.setDevice_srl(deviceEntity3.getDevice_srl());
        appDeviceDao.add(appDeviceEntity3);

        appDeviceEntity4.setApp_srl(appEntity1.getApp_srl());
        appDeviceEntity4.setDevice_srl(deviceEntity4.getDevice_srl());
        appDeviceDao.add(appDeviceEntity4);

        appDeviceEntity5.setApp_srl(appEntity1.getApp_srl());
        appDeviceEntity5.setDevice_srl(deviceEntity5.getDevice_srl());
        appDeviceDao.add(appDeviceEntity5);

        // select one
        AppDeviceEntity resultEntity1 = appDeviceDao.get(appDeviceEntity1.getApp_srl(),
                appDeviceEntity1.getDevice_srl(), MDV.NUSE);
        assertThat(resultEntity1, is(notNullValue()));
        assertThat(resultEntity1.equals(appDeviceEntity1), is(true));

        AppDeviceEntity resultEntity2 = appDeviceDao.get(appDeviceEntity2.getApp_srl(),
                appDeviceEntity2.getDevice_srl(), MDV.NUSE);
        assertThat(resultEntity2, is(notNullValue()));
        assertThat(resultEntity2.equals(appDeviceEntity2), is(true));

        AppDeviceEntity resultEntity3 = appDeviceDao.get(appDeviceEntity3.getApp_srl(),
                appDeviceEntity3.getDevice_srl(), MDV.NUSE);
        assertThat(resultEntity3, is(notNullValue()));
        assertThat(resultEntity3.equals(appDeviceEntity3), is(true));

        AppDeviceEntity resultEntity4 = appDeviceDao.get(appDeviceEntity4.getApp_srl(),
                appDeviceEntity4.getDevice_srl(), MDV.NUSE);
        assertThat(resultEntity4, is(notNullValue()));
        assertThat(resultEntity4.equals(appDeviceEntity4), is(true));

        AppDeviceEntity resultEntity5 = appDeviceDao.get(appDeviceEntity5.getApp_srl(),
                appDeviceEntity5.getDevice_srl(), MDV.NUSE);
        assertThat(resultEntity5, is(notNullValue()));
        assertThat(resultEntity5.equals(appDeviceEntity5), is(true));

        // select list
        long count2 = appDeviceDao.count(appEntity1.getApp_srl(), MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count2, is(5L));
        List<AppDeviceEntity> list2 = appDeviceDao.get(appEntity1.getApp_srl(), MDV.NUSE,
                MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // modify
        AppDeviceEntity updateEntity = new AppDeviceEntity();
        updateEntity.setEnabled(MDV.DENY);
        appDeviceDao.modify(updateEntity, appDeviceEntity1.getApp_srl(), appDeviceEntity1.getDevice_srl());

        // select one
        AppDeviceEntity resultEntity6 = appDeviceDao.get(appDeviceEntity1.getApp_srl(),
                appDeviceEntity1.getDevice_srl(), MDV.NUSE);
        assertThat(resultEntity6, is(notNullValue()));
        assertThat(resultEntity6.equals(appDeviceEntity1), is(false));
        assertThat(resultEntity6.getEnabled(), is(MDV.DENY));

        AppDeviceEntity resultEntity7 = appDeviceDao.get(appDeviceEntity1.getApp_srl(),
                appDeviceEntity1.getDevice_srl(), MDV.YES);
        assertThat(resultEntity7, is(nullValue()));

        // delete
        appDeviceDao.delete(appDeviceEntity2.getApp_srl(), appDeviceEntity2.getDevice_srl());

        // select one
        AppDeviceEntity resultEntity8 = appDeviceDao.get(appDeviceEntity2.getApp_srl(),
                appDeviceEntity2.getDevice_srl(), MDV.NUSE);
        assertThat(resultEntity8, is(nullValue()));
    }
}

package com.ckstack.ckpush.dao.user;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.accessory.DeviceDao;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.app.AppDeviceDao;
import com.ckstack.ckpush.domain.app.AppDeviceEntity;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by dhkim94 on 15. 3. 16..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class DeviceDaoTest {
    @Autowired
    private AppDao appDao;
    @Autowired
    private AppDeviceDao appDeviceDao;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private Properties confAccessory;
    @Autowired
    protected Properties confCommon;

    private DeviceEntity deviceEntity1;
    private DeviceEntity deviceEntity2;
    private DeviceEntity deviceEntity3;
    private DeviceEntity deviceEntity4;
    private DeviceEntity deviceEntity5;

    private AppEntity appEntity1;
    private AppEntity appEntity2;

    private AppDeviceEntity appDeviceEntity1;
    private AppDeviceEntity appDeviceEntity2;
    private AppDeviceEntity appDeviceEntity3;
    private AppDeviceEntity appDeviceEntity4;
    private AppDeviceEntity appDeviceEntity5;

    @Before
    public void setUp() {
        int ltm = (int) (DateTime.now().getMillis() / 1000);

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
        deviceEntity5.setDevice_type("G-Pra4");
        deviceEntity5.setDevice_class(androidDevice);
        deviceEntity5.setU_date(ltm);

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
    }

    /**
     * tbl_android_device 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void androidDeviceBasicTest() {
        // pre select list
        List<DeviceEntity> list1 = deviceDao.get(null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        long count1 = deviceDao.count(null, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // select list where empty in test
        List<Long> device_srls = new ArrayList<>();
        List<DeviceEntity> list_no_data = deviceDao.get(device_srls, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list_no_data, is(notNullValue()));

        // insert
        assertThat(deviceEntity1.getDevice_srl(), is((long)MDV.NUSE));
        deviceDao.add(deviceEntity1);
        assertThat((deviceEntity1.getDevice_srl() > 0), is(true));

        assertThat(deviceEntity2.getDevice_srl(), is((long)MDV.NUSE));
        deviceDao.add(deviceEntity2);
        assertThat((deviceEntity2.getDevice_srl() > 0), is(true));

        // select one
        DeviceEntity resultVo1 = deviceDao.get(deviceEntity1.getDevice_srl(), null);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(deviceEntity1), is(true));

        DeviceEntity resultVo2 = deviceDao.get(deviceEntity2.getDevice_srl(), null);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(deviceEntity2), is(true));

        // select list
        long count2 = deviceDao.count(null, MDV.NUSE);
        assertThat(count1+2, is(count2));
        List<DeviceEntity> list2 = deviceDao.get(null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        // modify
        DeviceEntity updateVo1 = new DeviceEntity();
        String nextMobilePhoneNumber = "01067890011";
        updateVo1.init();
        updateVo1.setMobile_phone_number(nextMobilePhoneNumber);
        updateVo1.setU_date((int)(DateTime.now().getMillis() / 1000));
        deviceDao.modify(updateVo1, deviceEntity1.getDevice_srl(), null);

        // select one
        DeviceEntity resultVo3 = deviceDao.get(deviceEntity1.getDevice_srl(), null);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(deviceEntity1), is(false));

        deviceEntity1.setMobile_phone_number(updateVo1.getMobile_phone_number());
        deviceEntity1.setU_date(updateVo1.getU_date());

        assertThat(resultVo3.equals(deviceEntity1), is(true));

        // delete one
        // 단일 대상을 삭제 한다.
        deviceDao.delete(deviceEntity1.getDevice_srl(), null, null);
        DeviceEntity result4 = deviceDao.get(deviceEntity1.getDevice_srl(), null);
        assertThat(result4, is(nullValue()));

        deviceDao.delete(MDV.NUSE, null, deviceEntity2.getDevice_id());
        DeviceEntity result5 = deviceDao.get(deviceEntity2.getDevice_srl(), null);
        assertThat(result5, is(nullValue()));

        deviceDao.add(deviceEntity1);
        deviceDao.add(deviceEntity2);
        deviceDao.add(deviceEntity3);

        device_srls.clear();
        device_srls.add(deviceEntity1.getDevice_srl());
        device_srls.add(deviceEntity3.getDevice_srl());

        // delete multiple
        // 여러개를 한번에 지운다.
        deviceDao.delete(MDV.NUSE, device_srls, null);

        DeviceEntity result6 = deviceDao.get(deviceEntity1.getDevice_srl(), null);
        assertThat(result6, is(nullValue()));

        DeviceEntity result7 = deviceDao.get(deviceEntity3.getDevice_srl(), null);
        assertThat(result7, is(nullValue()));

        DeviceEntity result8 = deviceDao.get(deviceEntity2.getDevice_srl(), null);
        assertThat(result8, is(notNullValue()));
        assertThat(result8.equals(deviceEntity2), is(true));
    }

    /**
     * tbl_android_device select one 조건이 유효하지 않은 경우 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneAndroidDeviceTest() {
        // insert
        assertThat(deviceEntity1.getDevice_srl(), is((long)MDV.NUSE));
        deviceDao.add(deviceEntity1);
        assertThat((deviceEntity1.getDevice_srl() > 0), is(true));

        assertThat(deviceEntity2.getDevice_srl(), is((long) MDV.NUSE));
        deviceDao.add(deviceEntity2);
        assertThat((deviceEntity2.getDevice_srl() > 0), is(true));

        deviceDao.get(MDV.NUSE, null);
    }

    /**
     * tbl_android_device update 조건이 유효하지 않은 경우 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionUpdateAndroidDeviceTest() {
        // insert
        assertThat(deviceEntity1.getDevice_srl(), is((long)MDV.NUSE));
        deviceDao.add(deviceEntity1);
        assertThat((deviceEntity1.getDevice_srl() > 0), is(true));

        assertThat(deviceEntity2.getDevice_srl(), is((long)MDV.NUSE));
        deviceDao.add(deviceEntity2);
        assertThat((deviceEntity2.getDevice_srl() > 0), is(true));

        // modify
        DeviceEntity updateVo1 = new DeviceEntity();
        String nextMobilePhoneNumber = "01034588765";
        updateVo1.init();
        updateVo1.setMobile_phone_number(nextMobilePhoneNumber);
        updateVo1.setU_date((int) (DateTime.now().getMillis() / 1000));
        deviceDao.modify(updateVo1, MDV.NUSE, null);
    }

    /**
     * tbl_android_device delete 시 조건이 유효하지 않은 경우 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteAndroidDeviceTest1() {
        // insert
        assertThat(deviceEntity1.getDevice_srl(), is((long)MDV.NUSE));
        deviceDao.add(deviceEntity1);
        assertThat((deviceEntity1.getDevice_srl() > 0), is(true));

        assertThat(deviceEntity2.getDevice_srl(), is((long) MDV.NUSE));
        deviceDao.add(deviceEntity2);
        assertThat((deviceEntity2.getDevice_srl() > 0), is(true));

        deviceDao.delete(MDV.NUSE, null, null);
    }

    /**
     * tbl_android_device delete 시 조건이 유효하지 않은 경우 테스트
     * device_srls 가 null 이 아닌데 size 가 zero 임.
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteAndroidDeviceTest2() {
        // insert
        assertThat(deviceEntity1.getDevice_srl(), is((long)MDV.NUSE));
        deviceDao.add(deviceEntity1);
        assertThat((deviceEntity1.getDevice_srl() > 0), is(true));

        assertThat(deviceEntity2.getDevice_srl(), is((long)MDV.NUSE));
        deviceDao.add(deviceEntity2);
        assertThat((deviceEntity2.getDevice_srl() > 0), is(true));

        List<Long> device_srls = new ArrayList<Long>();
        deviceDao.delete(MDV.NUSE, device_srls, null);
    }

    /**
     * tbl_android_device list 테스트
     */
    @Test
    @Rollback
    public void androidDeviceListTest() {
        // prepare data
        deviceDao.add(deviceEntity1);
        deviceDao.add(deviceEntity2);
        deviceDao.add(deviceEntity3);
        deviceDao.add(deviceEntity4);
        deviceDao.add(deviceEntity5);

        // select list using null device_srls
        List<DeviceEntity> list = deviceDao.get(null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list, is(notNullValue()));
        assertThat((list.size() >= 5), is(true));

        long count = deviceDao.count(null, MDV.NUSE);
        assertThat((long)list.size(), is(count));

        // select list using empty device_srls
        List<Long> device_srls = new ArrayList<Long>();
        list = deviceDao.get(device_srls, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list, is(notNullValue()));
        assertThat((list.size() >= 5), is(true));

        count = deviceDao.count(device_srls, MDV.NUSE);
        assertThat((long)list.size(), is(count));

        // select list using non-empty device_srls
        device_srls.add(deviceEntity2.getDevice_srl());
        device_srls.add(deviceEntity1.getDevice_srl());
        device_srls.add(deviceEntity5.getDevice_srl());

        count = deviceDao.count(device_srls, MDV.NUSE);
        assertThat(count, is(3L));
        list = deviceDao.get(device_srls, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list.size(), is(count));
        assertThat(list.get(0).getDevice_srl(), is(deviceEntity5.getDevice_srl()));
        assertThat(list.get(1).getDevice_srl(), is(deviceEntity2.getDevice_srl()));
        assertThat(list.get(2).getDevice_srl(), is(deviceEntity1.getDevice_srl()));

        // select using index indicator
        List<DeviceEntity> list1 = deviceDao.get(null, MDV.NUSE, 0, 3);
        assertThat(list1, is(notNullValue()));
        assertThat(list1.size(), is(3));

        long lastDeviceSrl = list1.get(2).getDevice_srl();

        List<DeviceEntity> list2 = deviceDao.get(null, lastDeviceSrl, 0, 3);
        assertThat(list2, is(notNullValue()));
        assertThat((list2.size() >= 2), is(true));

        List<DeviceEntity> list3 = deviceDao.get(null, MDV.NUSE, 0, 5);
        assertThat(list3, is(notNullValue()));
        assertThat(list3.size(), is(5));
        assertThat(list3.get(3).equals(list2.get(0)), is(true));
        assertThat(list3.get(4).equals(list2.get(1)), is(true));
    }

    /**
     * app 에 등록되어 있는 device 테스트
     */
    @Test
    @Rollback
    public void mappingAppAndroidDeviceTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));
        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

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

        // insert
        // 1, 2, 3 단말기는 app1 에 매핑
        // 4, 5 단말기는 app2 에 매핑
        appDeviceEntity1.setApp_srl(appEntity1.getApp_srl());
        appDeviceEntity1.setDevice_srl(deviceEntity1.getDevice_srl());
        appDeviceDao.add(appDeviceEntity1);

        appDeviceEntity2.setApp_srl(appEntity1.getApp_srl());
        appDeviceEntity2.setDevice_srl(deviceEntity2.getDevice_srl());
        appDeviceDao.add(appDeviceEntity2);

        appDeviceEntity3.setApp_srl(appEntity1.getApp_srl());
        appDeviceEntity3.setDevice_srl(deviceEntity3.getDevice_srl());
        appDeviceDao.add(appDeviceEntity3);

        appDeviceEntity4.setApp_srl(appEntity2.getApp_srl());
        appDeviceEntity4.setDevice_srl(deviceEntity4.getDevice_srl());
        appDeviceDao.add(appDeviceEntity4);

        appDeviceEntity5.setApp_srl(appEntity2.getApp_srl());
        appDeviceEntity5.setDevice_srl(deviceEntity5.getDevice_srl());
        appDeviceDao.add(appDeviceEntity5);

        // select list
        long count1 = deviceDao.countInApp(appEntity1.getApp_srl(), null, MDV.NUSE, null,
                null, MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);

        assertThat(count1, is(3L));
        List<DeviceEntity> list1 = deviceDao.getInApp(appEntity1.getApp_srl(), null, MDV.NUSE, null,
                null, MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        assertThat(list1.get(0).getAppDeviceEntity().equals(appDeviceEntity3), is(true));
        assertThat(list1.get(1).getAppDeviceEntity().equals(appDeviceEntity2), is(true));
        assertThat(list1.get(2).getAppDeviceEntity().equals(appDeviceEntity1), is(true));

        long count2 = deviceDao.countInApp(appEntity2.getApp_srl(), null, MDV.NUSE, null,
                null, MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count2, is(2L));
        List<DeviceEntity> list2 = deviceDao.getInApp(appEntity2.getApp_srl(), null, MDV.NUSE,
                null, null, MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        assertThat(list2.get(0).getAppDeviceEntity().equals(appDeviceEntity5), is(true));
        assertThat(list2.get(1).getAppDeviceEntity().equals(appDeviceEntity4), is(true));
    }
}

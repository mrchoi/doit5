package com.ckstack.ckpush.dao.accessory;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;


import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.accessory.DeviceAccessTokenEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
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
 * Created by dhkim94 on 15. 7. 13..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class DeviceAccessTokenDaoTest {
    @Autowired
    private DeviceAccessTokenDao deviceAccessTokenDao;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    protected Properties confCommon;
    @Autowired
    private Properties confAccessory;

    private AppEntity appEntity1;

    private DeviceEntity deviceEntity1;
    private DeviceEntity deviceEntity2;

    private DeviceAccessTokenEntity deviceAccessTokenEntity1;
    private DeviceAccessTokenEntity deviceAccessTokenEntity2;
    private DeviceAccessTokenEntity deviceAccessTokenEntity3;
    private DeviceAccessTokenEntity deviceAccessTokenEntity4;
    private DeviceAccessTokenEntity deviceAccessTokenEntity5;

    @Before
    public void setUp() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);
        int androidDevice = Integer.parseInt(confCommon.getProperty("user_terminal_android"), 10);

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

        int ONE_HOUR = 60 * 60;

        deviceAccessTokenEntity1 = new DeviceAccessTokenEntity();
        deviceAccessTokenEntity1.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        deviceAccessTokenEntity1.setToken_expire(ltm + ONE_HOUR);
        deviceAccessTokenEntity1.setC_date(ltm);
        deviceAccessTokenEntity1.setU_date(ltm);

        deviceAccessTokenEntity2 = new DeviceAccessTokenEntity();
        deviceAccessTokenEntity2.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        deviceAccessTokenEntity2.setToken_expire(ltm + ONE_HOUR);
        deviceAccessTokenEntity2.setC_date(ltm);
        deviceAccessTokenEntity2.setU_date(ltm);

        deviceAccessTokenEntity3 = new DeviceAccessTokenEntity();
        deviceAccessTokenEntity3.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        deviceAccessTokenEntity3.setToken_expire(ltm + ONE_HOUR);
        deviceAccessTokenEntity3.setC_date(ltm);
        deviceAccessTokenEntity3.setU_date(ltm);

        deviceAccessTokenEntity4 = new DeviceAccessTokenEntity();
        deviceAccessTokenEntity4.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        deviceAccessTokenEntity4.setToken_expire(ltm + ONE_HOUR);
        deviceAccessTokenEntity4.setC_date(ltm);
        deviceAccessTokenEntity4.setU_date(ltm);

        deviceAccessTokenEntity5 = new DeviceAccessTokenEntity();
        deviceAccessTokenEntity5.setAccess_token(UUID.randomUUID().toString().replaceAll("-", ""));
        deviceAccessTokenEntity5.setToken_expire(ltm + ONE_HOUR);
        deviceAccessTokenEntity5.setC_date(ltm);
        deviceAccessTokenEntity5.setU_date(ltm);
    }

    /**
     * tbl_device_access_token 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void deviceAccessTokenBasicTest() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        deviceDao.add(deviceEntity1);
        assertThat(deviceEntity1.getDevice_srl() > 0, is(true));

        deviceDao.add(deviceEntity2);
        assertThat(deviceEntity2.getDevice_srl() > 0, is(true));

        // prepare select list
        long count1 = deviceAccessTokenDao.count(MDV.NUSE, appEntity1.getApp_srl(), null, MDV.NUSE);
        assertThat(count1, is(0L));
        List<DeviceAccessTokenEntity> list1 = deviceAccessTokenDao.get(MDV.NUSE,
                appEntity1.getApp_srl(), null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        // insert
        deviceAccessTokenEntity1.setDevice_srl(deviceEntity1.getDevice_srl());
        deviceAccessTokenEntity1.setApp_srl(appEntity1.getApp_srl());
        deviceAccessTokenDao.add(deviceAccessTokenEntity1);

        deviceAccessTokenEntity2.setDevice_srl(deviceEntity1.getDevice_srl());
        deviceAccessTokenEntity2.setApp_srl(appEntity1.getApp_srl());
        deviceAccessTokenDao.add(deviceAccessTokenEntity2);

        deviceAccessTokenEntity3.setDevice_srl(deviceEntity1.getDevice_srl());
        deviceAccessTokenEntity3.setApp_srl(appEntity1.getApp_srl());
        deviceAccessTokenDao.add(deviceAccessTokenEntity3);

        deviceAccessTokenEntity4.setDevice_srl(deviceEntity2.getDevice_srl());
        deviceAccessTokenEntity4.setApp_srl(appEntity1.getApp_srl());
        deviceAccessTokenDao.add(deviceAccessTokenEntity4);

        deviceAccessTokenEntity5.setDevice_srl(deviceEntity2.getDevice_srl());
        deviceAccessTokenEntity5.setApp_srl(appEntity1.getApp_srl());
        deviceAccessTokenDao.add(deviceAccessTokenEntity5);

        // select one
        DeviceAccessTokenEntity resultVo1 = deviceAccessTokenDao.get(MDV.NUSE,
                deviceAccessTokenEntity1.getAccess_token());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(deviceAccessTokenEntity1), is(true));

        DeviceAccessTokenEntity resultVo2 = deviceAccessTokenDao.get(MDV.NUSE,
                deviceAccessTokenEntity2.getAccess_token());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(deviceAccessTokenEntity2), is(true));

        DeviceAccessTokenEntity resultVo3 = deviceAccessTokenDao.get(MDV.NUSE,
                deviceAccessTokenEntity3.getAccess_token());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(deviceAccessTokenEntity3), is(true));

        DeviceAccessTokenEntity resultVo4 = deviceAccessTokenDao.get(MDV.NUSE,
                deviceAccessTokenEntity4.getAccess_token());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(deviceAccessTokenEntity4), is(true));

        DeviceAccessTokenEntity resultVo5 = deviceAccessTokenDao.get(MDV.NUSE,
                deviceAccessTokenEntity5.getAccess_token());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(deviceAccessTokenEntity5), is(true));

        // select list
        long count2 = deviceAccessTokenDao.count(deviceEntity1.getDevice_srl(), appEntity1.getApp_srl(), null, MDV.NUSE);
        assertThat(count2, is(3L));
        List<DeviceAccessTokenEntity> list2 = deviceAccessTokenDao.get(deviceEntity1.getDevice_srl(),
                appEntity1.getApp_srl(), null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        long count3 = deviceAccessTokenDao.count(deviceEntity2.getDevice_srl(), appEntity1.getApp_srl(), null, MDV.NUSE);
        assertThat(count3, is(2L));
        List<DeviceAccessTokenEntity> list3 = deviceAccessTokenDao.get(deviceEntity2.getDevice_srl(),
                appEntity1.getApp_srl(), null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list3.size(), is(count3));

        // modify
        DeviceAccessTokenEntity updateVo = new DeviceAccessTokenEntity();
        updateVo.init();
        updateVo.setToken_expire(deviceAccessTokenEntity1.getToken_expire() - 70 * 60);
        updateVo.setU_date(ltm);

        deviceAccessTokenDao.modify(updateVo, deviceAccessTokenEntity1.getToken_srl(), null, MDV.NUSE);

        // select one
        DeviceAccessTokenEntity resultVo6 = deviceAccessTokenDao.get(MDV.NUSE,
                deviceAccessTokenEntity1.getAccess_token());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.getToken_expire() < ltm, is(true));

        // delete
        deviceAccessTokenDao.delete(deviceAccessTokenEntity1.getToken_srl(), null, MDV.NUSE);

        // select
        DeviceAccessTokenEntity resultVo8 = deviceAccessTokenDao.get(MDV.NUSE,
                deviceAccessTokenEntity1.getAccess_token());
        assertThat(resultVo8, is(nullValue()));
    }

}

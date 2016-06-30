package com.ckstack.ckpush.dao.push;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.push.PushMessageEntity;
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
 * Created by dhkim94 on 15. 4. 29..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class PushMessageDaoTest {
    @Autowired
    private PushMessageDao pushMessageDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private Properties confCommon;

    private AppEntity appEntity1;
    private AppEntity appEntity2;

    private PushMessageEntity pushMessageEntity1;
    private PushMessageEntity pushMessageEntity2;
    private PushMessageEntity pushMessageEntity3;
    private PushMessageEntity pushMessageEntity4;
    private PushMessageEntity pushMessageEntity5;

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

        pushMessageEntity1 = new PushMessageEntity();
        pushMessageEntity1.setUser_id("robot");
        pushMessageEntity1.setPush_title("junit test push1");
        pushMessageEntity1.setPush_text("junit test push body1");
        pushMessageEntity1.setCallback_url("pushapp://document/1");
        pushMessageEntity1.setPush_target(confCommon.getProperty("push_target_all_in_app"));

        pushMessageEntity2 = new PushMessageEntity();
        pushMessageEntity2.setUser_id("robot");
        pushMessageEntity2.setPush_title("junit test push2");
        pushMessageEntity2.setPush_text("junit test push body2");
        pushMessageEntity2.setCallback_url("pushapp://document/2");
        pushMessageEntity2.setPush_target(confCommon.getProperty("push_target_all_in_app"));

        pushMessageEntity3 = new PushMessageEntity();
        pushMessageEntity3.setUser_id("robot");
        pushMessageEntity3.setPush_title("junit test push3");
        pushMessageEntity3.setPush_text("junit test push body3");
        pushMessageEntity3.setCallback_url("pushapp://document/3");
        pushMessageEntity3.setPush_target(confCommon.getProperty("push_target_all_in_app"));

        pushMessageEntity4 = new PushMessageEntity();
        pushMessageEntity4.setUser_id("robot");
        pushMessageEntity4.setPush_title("junit test push4");
        pushMessageEntity4.setPush_text("junit test push body4");
        pushMessageEntity4.setCallback_url("pushapp://document/4");
        pushMessageEntity4.setPush_target(confCommon.getProperty("push_target_all_in_app"));

        pushMessageEntity5 = new PushMessageEntity();
        pushMessageEntity5.setUser_id("robot");
        pushMessageEntity5.setPush_title("junit test push5");
        pushMessageEntity5.setPush_text("junit test push body5");
        pushMessageEntity5.setCallback_url("pushapp://document/5");
        pushMessageEntity5.setPush_target(confCommon.getProperty("push_target_all_in_app"));
    }

    /**
     * push message 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void pushMessageBasicTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        // pre select list
        long count1 = pushMessageDao.count(appEntity1.getApp_srl(), null, MDV.NUSE);
        assertThat(count1, is(0L));
        List<PushMessageEntity> list1 = pushMessageDao.get(appEntity1.getApp_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        // app1 : message1, message2, message3
        // app2 : message4, message5
        pushMessageEntity1.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity1);
        assertThat(pushMessageEntity1.getPush_srl() > 0, is(true));

        pushMessageEntity2.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity2);
        assertThat(pushMessageEntity2.getPush_srl() > 0, is(true));

        pushMessageEntity3.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity3);
        assertThat(pushMessageEntity3.getPush_srl() > 0, is(true));

        pushMessageEntity4.setApp_srl(appEntity2.getApp_srl());
        pushMessageDao.add(pushMessageEntity4);
        assertThat(pushMessageEntity4.getPush_srl() > 0, is(true));

        pushMessageEntity5.setApp_srl(appEntity2.getApp_srl());
        pushMessageDao.add(pushMessageEntity5);
        assertThat(pushMessageEntity5.getPush_srl() > 0, is(true));

        // select one
        PushMessageEntity resultEntity1 = pushMessageDao.get(pushMessageEntity1.getPush_srl());
        assertThat(resultEntity1, is(notNullValue()));
        assertThat(resultEntity1.equals(pushMessageEntity1), is(true));

        PushMessageEntity resultEntity2 = pushMessageDao.get(pushMessageEntity2.getPush_srl());
        assertThat(resultEntity2, is(notNullValue()));
        assertThat(resultEntity2.equals(pushMessageEntity2), is(true));

        PushMessageEntity resultEntity3 = pushMessageDao.get(pushMessageEntity3.getPush_srl());
        assertThat(resultEntity3, is(notNullValue()));
        assertThat(resultEntity3.equals(pushMessageEntity3), is(true));

        PushMessageEntity resultEntity4 = pushMessageDao.get(pushMessageEntity4.getPush_srl());
        assertThat(resultEntity4, is(notNullValue()));
        assertThat(resultEntity4.equals(pushMessageEntity4), is(true));

        PushMessageEntity resultEntity5 = pushMessageDao.get(pushMessageEntity5.getPush_srl());
        assertThat(resultEntity5, is(notNullValue()));
        assertThat(resultEntity5.equals(pushMessageEntity5), is(true));

        // select list
        long count2 = pushMessageDao.count(appEntity1.getApp_srl(), null, MDV.NUSE);
        assertThat(count2, is(3L));
        List<PushMessageEntity> list2 = pushMessageDao.get(appEntity1.getApp_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));
        assertThat(list2.get(0).equals(pushMessageEntity3), is(true));
        assertThat(list2.get(1).equals(pushMessageEntity2), is(true));
        assertThat(list2.get(2).equals(pushMessageEntity1), is(true));

        long count3 = pushMessageDao.count(appEntity2.getApp_srl(), null, MDV.NUSE);
        assertThat(count3, is(2L));
        List<PushMessageEntity> list3 = pushMessageDao.get(appEntity2.getApp_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list3.size(), is(count3));
        assertThat(list3.get(0).equals(pushMessageEntity5), is(true));
        assertThat(list3.get(1).equals(pushMessageEntity4), is(true));

        // delete
        pushMessageDao.delete(pushMessageEntity1.getPush_srl());

        // select one
        PushMessageEntity resultEntity6 = pushMessageDao.get(pushMessageEntity1.getPush_srl());
        assertThat(resultEntity6, is(nullValue()));
    }

    /**
     * push message list 테스트
     */
    @Test
    @Rollback
    public void pushMessageListTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        pushMessageEntity1.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity1);
        assertThat(pushMessageEntity1.getPush_srl() > 0, is(true));

        pushMessageEntity2.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity2);
        assertThat(pushMessageEntity2.getPush_srl() > 0, is(true));

        pushMessageEntity3.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity3);
        assertThat(pushMessageEntity3.getPush_srl() > 0, is(true));

        pushMessageEntity4.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity4);
        assertThat(pushMessageEntity4.getPush_srl() > 0, is(true));

        pushMessageEntity5.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity5);
        assertThat(pushMessageEntity5.getPush_srl() > 0, is(true));

        // select using index indicator
        List<PushMessageEntity> list1 = pushMessageDao.get(appEntity1.getApp_srl(), null, null, MDV.NUSE, null, 0, 3);
        assertThat(list1.size(), is(3));

        long lastPushSrl = list1.get(2).getPush_srl();

        List<PushMessageEntity> list2 = pushMessageDao.get(appEntity1.getApp_srl(), null, null, lastPushSrl, null, 0, 3);
        assertThat(list2.size(), is(2));

        List<PushMessageEntity> list3 = pushMessageDao.get(appEntity1.getApp_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat(list3.size(), is(5));
        assertThat(list3.get(3).equals(list2.get(0)), is(true));
        assertThat(list3.get(4).equals(list2.get(1)), is(true));
    }
}

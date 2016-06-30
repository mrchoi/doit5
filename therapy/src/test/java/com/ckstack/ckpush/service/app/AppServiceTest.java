package com.ckstack.ckpush.service.app;

//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.service.user.MemberService;
import com.ckstack.ckpush.domain.user.MemberEntity;
//import org.joda.time.DateTime;
//import org.joda.time.Period;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by dhkim94 on 15. 4. 11..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class AppServiceTest {
    @Autowired
    protected AppService appService;
    @Autowired
    protected Properties confAccessory;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private AppEntity appEntity1;
    private AppEntity appEntity2;
    private AppEntity appEntity3;
    private AppEntity appEntity4;
    private AppEntity appEntity5;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;

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
        memberEntity2.setUser_id("junit2@ckstack.com");
        memberEntity2.setEmail_address("junit_test_member2@ckstack.com");
        memberEntity2.setUser_password(password);
        memberEntity2.setUser_name("junit test2");
        memberEntity2.setMobile_phone_number("010-5373-8336");

        appEntity1 = new AppEntity();
        appEntity1.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApi_secret("junit_test_ap_secret_123");
        appEntity1.setApi_key("junit_test_api_key_123");
        appEntity1.setApp_version("1.0.0");
        appEntity1.setApp_name("junit test 123");

        appEntity2 = new AppEntity();
        appEntity2.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApi_secret("junit_test2_ap_secret_123");
        appEntity2.setApi_key("junit_test2_api_key_123");
        appEntity2.setApp_version("1.0.0");
        appEntity2.setApp_name("junit test2 123");

        appEntity3 = new AppEntity();
        appEntity3.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity3.setApi_secret("junit_test3_ap_secret_123");
        appEntity3.setApi_key("junit_test3_api_key_123");
        appEntity3.setApp_version("1.1.0");
        appEntity3.setApp_name("junit test3 123");

        appEntity4 = new AppEntity();
        appEntity4.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity4.setApi_secret("junit_test4_ap_secret_123");
        appEntity4.setApi_key("junit_test4_api_key_123");
        appEntity4.setApp_version("2.1.0");
        appEntity4.setApp_name("junit test4 123");

        appEntity5 = new AppEntity();
        appEntity5.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity5.setApi_secret("junit_test5_ap_secret_123");
        appEntity5.setApi_key("junit_test5_api_key_123");
        appEntity5.setApp_version("3.1.10");
        appEntity5.setApp_name("junit test5 123");
    }

    /**
     * 중복 체크 테스트
     */
    @Test
    @Rollback
    public void isDuplicateTest() {
        // prepare data
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity1, null, null);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appEntity2.setApi_key(appEntity1.getApi_key());
        assertThat(appService.isDuplicate(appEntity2.getApi_key()), is(true));
    }

    /**
     * 앱 생성, 확인, 카운트 테스트
     */
    @Test
    @Rollback
    public void createAppTest() {
        // prepare data
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        memberService.signUp(memberEntity2, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

//        String strStartMonth = "201503";
//        DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");
//        DateTime dtStartTime = fmtYYYYMM.parseDateTime(strStartMonth);
//        DateTime dtCurrTime = DateTime.now();
//        Period period = new Period(dtStartTime, dtCurrTime);
//        int iPeriod = period.getMonths() + 1;

        appService.createApp(memberEntity1.getMember_srl(), appEntity1, null, null);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity2, null, null);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity3, null, null);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity4, null, null);
        assertThat(appEntity4.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity5, null, null);
        assertThat(appEntity5.getApp_srl() > 0, is(true));

        // 저장된 앱 가져와서 비교
        AppEntity resultVo1 = appService.getAppInfo(appEntity1.getApp_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(appEntity1), is(true));

        AppEntity resultVo2 = appService.getAppInfo(appEntity1.getApi_key());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(appEntity1), is(true));

        // app manager 도 추가 되었는지 확인
        List<MemberEntity> managerList1 = appService.getAppManager(appEntity1.getApp_srl());
        assertThat(managerList1.size(), is(1));

        AppEntity resultVo4 = appService.getAppInfo(appEntity2.getApp_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(appEntity2), is(true));

        AppEntity resultVo5 = appService.getAppInfo(appEntity2.getApi_key());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(appEntity2), is(true));

        // app manager 도 추가 되었는지 확인
        List<MemberEntity> managerList2 = appService.getAppManager(appEntity2.getApp_srl());
        assertThat(managerList2.size(), is(1));

        // app manager 변경
        List<String> nextAppManagerId = new ArrayList<>();
        nextAppManagerId.add(memberEntity2.getUser_id());
        // memberEntity1 을 삭제하고 memberEntity2 를 추가 함
        appService.modifyAppManager(appEntity2.getApp_srl(), nextAppManagerId);

        List<MemberEntity> managerList3 = appService.getAppManager(appEntity2.getApp_srl());
        assertThat(managerList3.size(), is(1));
        assertThat(managerList3.get(0).getMember_srl(), is(memberEntity2.getMember_srl()));

        // app manager 변경. 실제로는 manager 추가 임
        nextAppManagerId.add(memberEntity1.getUser_id());
        // memberEntity2 는 그대로 유지하고, memberEntity1 을 추가 함
        appService.modifyAppManager(appEntity2.getApp_srl(), nextAppManagerId);

        // app manager 는 2명이고, listing 되는 순서는 member_srl desc 임.
        List<MemberEntity> managerList4 = appService.getAppManager(appEntity2.getApp_srl());
        assertThat(managerList4.size(), is(2));
        assertThat(managerList4.get(0).getMember_srl(), is(memberEntity2.getMember_srl()));
        assertThat(managerList4.get(1).getMember_srl(), is(memberEntity1.getMember_srl()));

        // 카운트 해 본다.
        Map<String, String> searchMap = new HashMap<>();
        long count1 = appService.getAppCount(null, MDV.NUSE);
        long count2 = appService.getAppCount(null);
        assertThat(count1, is(count2));
        long count3 = appService.getAppCount(searchMap);
        assertThat(count1, is(count3));

        long count4 = appService.getAppCount("junit test4 12", MDV.NUSE);
        assertThat(count4, is(1L));
        searchMap.put("app_name", "junit test4 12");
        long count5 = appService.getAppCount(searchMap);
        assertThat(count4, is(count5));

        // 수정(resultVo1 - appEntity1)
        Map<String, String> updateMap = new HashMap<>();
        updateMap.put("app_name", "junit test changed app name123");
        updateMap.put("app_version", "3.141.592");
        updateMap.put("api_key", "123456789");  // 이건 변경되지 않는다.
        appService.modifyApp(resultVo1.getApp_srl(), updateMap);

        AppEntity resultVo1_2 = appService.getAppInfo(resultVo1.getApp_srl());
        assertThat(resultVo1_2, is(notNullValue()));
        assertThat(resultVo1_2.getApp_name(), is(updateMap.get("app_name")));
        assertThat(resultVo1_2.getApp_version(), is(updateMap.get("app_version")));
        assertThat(resultVo1_2.getApi_key(), not(updateMap.get("api_key")));

        // 삭제
        List<Integer> appSrs = new ArrayList<>();
        appSrs.add(appEntity1.getApp_srl());
        appService.deleteApp(appSrs, memberEntity1.getUser_id(), memberEntity1.getMember_srl(), "127.0.0.1");

        AppEntity resultVo7 = appService.getAppInfo(appEntity1.getApp_srl());
        assertThat(resultVo7, is(nullValue()));

        appSrs.add(appEntity2.getApp_srl());
        appSrs.add(appEntity3.getApp_srl());
        appService.deleteApp(appSrs, memberEntity1.getUser_id(), memberEntity1.getMember_srl(), "127.0.0.1");

        AppEntity resultVo8 = appService.getAppInfo(appEntity2.getApp_srl());
        assertThat(resultVo8, is(nullValue()));

        AppEntity resultVo9 = appService.getAppInfo(appEntity3.getApp_srl());
        assertThat(resultVo9, is(nullValue()));
    }

    /**
     * 앱 리스팅 테스트
     */
    @Test
    @Rollback
    public void listAppTest() {
        // prepare data
        memberService.signUp(memberEntity1, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity1, null, null);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity2, null, null);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity3, null, null);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity4, null, null);
        assertThat(appEntity4.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity5, null, null);
        assertThat(appEntity5.getApp_srl() > 0, is(true));

        Map<String, String> searchMap = new HashMap<>();

        List<AppEntity> list1 = appService.getAppInfo(null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        List<AppEntity> list2 = appService.getAppInfo(null, null, MDV.NUSE, MDV.NUSE);
        assertThat(list1, is(list2));
        List<AppEntity> list3 = appService.getAppInfo(searchMap, null, MDV.NUSE, MDV.NUSE);
        assertThat(list1, is(list3));

        List<AppEntity> list4 = appService.getAppInfo("junit test2", MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        searchMap.put("app_name", "junit test2");
        List<AppEntity> list5 = appService.getAppInfo(searchMap, null, MDV.NUSE, MDV.NUSE);
        assertThat(list4.size(), is(list5.size()));

        searchMap.put("app_name", "junit test");
        List<AppEntity> list6 = appService.getAppInfo(searchMap, null, MDV.NUSE, MDV.NUSE);
        assertThat(list6.size(), is(5));

        Map<String, String> sort = new LinkedHashMap<>();
        sort.put("app_name", "asc");
        List<AppEntity> list7 = appService.getAppInfo(searchMap, sort, MDV.NUSE, MDV.NUSE);
        assertThat(list7.size(), is(5));
        assertThat(list7.get(0).equals(appEntity1), is(true));

        sort.put("app_name", "desc");
        List<AppEntity> list8 = appService.getAppInfo(searchMap, sort, MDV.NUSE, MDV.NUSE);
        assertThat(list8.size(), is(5));
        assertThat(list8.get(0).equals(appEntity5), is(true));
    }

    @Test
    @Rollback
    public void appManagerTest() {

        /*
        // prepare data
        memberService.signUp(memberEntity1, MDV.NUSE, MDV.NUSE, MDV.NUSE, null);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

//        String strStartMonth = "201503";
//        DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");
//        DateTime dtStartTime = fmtYYYYMM.parseDateTime(strStartMonth);
//        DateTime dtCurrTime = DateTime.now();
//        Period period = new Period(dtStartTime, dtCurrTime);
//        int iPeriod = period.getMonths() + 1;

        appService.createApp(memberEntity1.getMember_srl(), appEntity1, null, null);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity2, null, null);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity3, null, null);
        assertThat(appEntity3.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity4, null, null);
        assertThat(appEntity4.getApp_srl() > 0, is(true));

        appService.createApp(memberEntity1.getMember_srl(), appEntity5, null, null);
        assertThat(appEntity5.getApp_srl() > 0, is(true));
        */
    }

}

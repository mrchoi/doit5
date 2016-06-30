package com.ckstack.ckpush.service.accessory;

//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dhkim94 on 15. 3. 19..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class ServiceHistoryServiceTest {
/*
    @Autowired
    private ServiceHistoryService serviceHistoryService;
    @Autowired
    private Properties confAccessory;
*/

    @Before
    public void setUp() {

    }

    // 이력은 웹에서 넣지 않고 프로세스를 띄어서 넣을 예정임.
    // 때문에 현재 이력 테스트는 의미 없음.


    @Test
    @Rollback
    public void noerror() {
        assertThat(1, is(1));
    }

    /**
     * 계정 추가 서비스 이력 테스트
     */
    /*
    @Test
    @Rollback
    public void createAccountHistoryTest() {
        String strStartMonth = "201503";
        DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");
        DateTime dtStartTime = fmtYYYYMM.parseDateTime(strStartMonth);
        DateTime dtCurrTime = DateTime.now();
        Period period = new Period(dtStartTime, dtCurrTime);
        int iPeriod = period.getMonths() + 1;

        // pre select
        long count1 = serviceHistoryService.count(strStartMonth, iPeriod,
                Integer.parseInt(confAccessory.getProperty("code_history_account"), 10),
                MDV.NUSE, MDV.NUSE);

        serviceHistoryService.addCreateAccountHistory(1, 2, "junit_test1", "junit_test2", null);

        long count2 = serviceHistoryService.count(strStartMonth, iPeriod,
                Integer.parseInt(confAccessory.getProperty("code_history_account"), 10),
                MDV.NUSE, MDV.NUSE);
        assertThat(count1+1, is(count2));
    }
    */

    /**
     * 계정 추가 이력 메시지 테스트. private 으로 바꿈
     */
    /*
    @Test
    @Rollback
    public void getCreateAccountContentTest() {
        String msg = serviceHistoryService.getCreateAccountContent(null, null);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "unknwon user 님이 unknwon user 계정을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 추가 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateAccountContent("root", null);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 unknwon user 계정을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 추가 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateAccountContent("root", "dhkim");
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 dhkim 계정을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 추가 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateAccountContent("root", "dhkim");
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 dhkim 계정을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 추가 하였습니다."), is(true));
    }
    */

    /**
     * 계정 추가 추가 이력 URL 테스트. private 으로 바꿈
     */
    /*
    @Test
    @Rollback
    public void getCreateAccountURLTest() {
        String url = serviceHistoryService.getCreateAccountURL(MDV.NUSE, MDV.NUSE);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("account://add/-1/-1"));

        url = serviceHistoryService.getCreateAccountURL(1, 2);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("account://add/1/2"));

        url = serviceHistoryService.getCreateAccountURL(1002, 1002);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("account://add/1002/1002"));
    }
    */

    /**
     * 앱 추가 서비스 이력 테스트
     */
    /*
    @Test
    @Rollback
    public void addCreateOrDeleteAppHistoryTest() {
        String strStartMonth = "201503";
        DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");
        DateTime dtStartTime = fmtYYYYMM.parseDateTime(strStartMonth);
        DateTime dtCurrTime = DateTime.now();
        Period period = new Period(dtStartTime, dtCurrTime);
        int iPeriod = period.getMonths() + 1;

        // pre select
        long count1 = serviceHistoryService.count(strStartMonth, iPeriod,
                Integer.parseInt(confAccessory.getProperty("code_history_app"), 10),
                MDV.NUSE, MDV.NUSE);

        Map<Integer, String> app = new HashMap<>();
        app.put(3, "junit test app");

        // 앱 추가
        serviceHistoryService.addCreateOrDeleteAppHistory(1, "junit_test1", app, null, true);

        // 앱 삭제
        serviceHistoryService.addCreateOrDeleteAppHistory(1, "junit_test1", app, null, false);

        long count2 = serviceHistoryService.count(strStartMonth, iPeriod,
                Integer.parseInt(confAccessory.getProperty("code_history_app"), 10),
                MDV.NUSE, MDV.NUSE);
        assertThat(count1+2, is(count2));
    }
    */

    /**
     * 앱 추가, 삭제 이력 메시지 테스트
     */
    /*
    @Test
    @Rollback
    public void getCreateOrDeleteAppContentTest() {
        // 추가
        String msg = serviceHistoryService.getCreateOrDeleteAppContent(null, null, true);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "unknwon user 님이 unknwon 앱을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 추가 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateOrDeleteAppContent("root", null, true);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 unknwon 앱을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 추가 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateOrDeleteAppContent("root", "Hello World", true);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 Hello World 앱을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 추가 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateOrDeleteAppContent("root", "Nice Day", true);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 Nice Day 앱을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 추가 하였습니다."), is(true));

        // 삭제
        msg = serviceHistoryService.getCreateOrDeleteAppContent(null, null, false);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "unknwon user 님이 unknwon 앱을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 삭제 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateOrDeleteAppContent("root", null, false);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 unknwon 앱을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 삭제 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateOrDeleteAppContent("root", "Hello World", false);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 Hello World 앱을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 삭제 하였습니다."), is(true));

        msg = serviceHistoryService.getCreateOrDeleteAppContent("root", "Nice Day", false);
        assertThat(msg, is(notNullValue()));
        assertThat(StringUtils.equals(msg, ""), is(false));
        assertThat(StringUtils.startsWith(msg, "root 님이 Nice Day 앱을"), is(true));
        assertThat(StringUtils.endsWith(msg, "에 삭제 하였습니다."), is(true));
    }
    */

    /**
     * 앱 추가 추가, 삭제 이력 URL 테스트
     */
    /*
    @Test
    @Rollback
    public void getCreateOrDeleteAppURLTest() {
        // 앱 추가
        String url = serviceHistoryService.getCreateOrDeleteAppURL(MDV.NUSE, MDV.NUSE, true);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("app://add/-1/-1"));

        url = serviceHistoryService.getCreateOrDeleteAppURL(1, 2, true);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("app://add/1/2"));

        url = serviceHistoryService.getCreateOrDeleteAppURL(1102, 1002, true);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("app://add/1102/1002"));

        // 앱 삭제
        url = serviceHistoryService.getCreateOrDeleteAppURL(MDV.NUSE, MDV.NUSE, false);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("app://delete/-1/-1"));

        url = serviceHistoryService.getCreateOrDeleteAppURL(1, 2, false);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("app://delete/1/2"));

        url = serviceHistoryService.getCreateOrDeleteAppURL(1102, 1002, false);
        assertThat(url, is(notNullValue()));
        assertThat(url, is("app://delete/1102/1002"));
    }
    */

}

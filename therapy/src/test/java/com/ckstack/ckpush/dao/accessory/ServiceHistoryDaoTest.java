package com.ckstack.ckpush.dao.accessory;

//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

//import com.ckstack.ckpush.common.MDV;
//import com.ckstack.ckpush.service.accessory.MyDatabaseUtilService;
//import com.ckstack.ckpush.service.accessory.ServiceHistoryService;
//import com.ckstack.ckpush.domain.accessory.ServiceHistoryEntity;
//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

//import java.util.List;
//import java.util.Properties;

/**
 * Created by dhkim94 on 15. 3. 17..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class ServiceHistoryDaoTest {
/*
    @Autowired
    private ServiceHistoryDao serviceHistoryDao;
    @Autowired
    private Properties confAccessory;
    @Autowired
    private ServiceHistoryService serviceHistoryService;
    @Autowired
    private MyDatabaseUtilService myDatabaseUtilService;

    private final DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");

    private ServiceHistoryEntity serviceHistoryEntity1;
    private ServiceHistoryEntity serviceHistoryEntity2;
    private ServiceHistoryEntity serviceHistoryEntity3;
    private ServiceHistoryEntity serviceHistoryEntity4;
    private ServiceHistoryEntity serviceHistoryEntity5;
*/

    @Before
    public void setUp() {
        /*
        assertThat(confAccessory.containsKey("code_history_account"), is(true));

        int ltm = (int) (DateTime.now().getMillis() / 1000);

        serviceHistoryEntity1 = new ServiceHistoryEntity();
        serviceHistoryEntity1.init();
        serviceHistoryEntity1.setC_date(ltm);
        serviceHistoryEntity1.setHistory_content(serviceHistoryService.getCreateAccountContent("junit", "new junit"));
        serviceHistoryEntity1.setHistory_type(Integer.parseInt(confAccessory.getProperty("code_history_account"), 10));
        serviceHistoryEntity1.setHistory_url(serviceHistoryService.getCreateAccountURL(1, 2));
        serviceHistoryEntity1.setIpaddress("127.0.0.1");
        serviceHistoryEntity1.setMember_srl(MDV.NONE);

        serviceHistoryEntity2 = new ServiceHistoryEntity();
        serviceHistoryEntity2.init();
        serviceHistoryEntity2.setC_date(ltm);
        serviceHistoryEntity2.setHistory_content(serviceHistoryService.getCreateAccountContent("junit", "new junit2"));
        serviceHistoryEntity2.setHistory_type(Integer.parseInt(confAccessory.getProperty("code_history_account"), 10));
        serviceHistoryEntity2.setHistory_url("");
        serviceHistoryEntity2.setIpaddress("127.0.0.1");
        serviceHistoryEntity2.setMember_srl(MDV.NONE);

        serviceHistoryEntity3 = new ServiceHistoryEntity();
        serviceHistoryEntity3.init();
        serviceHistoryEntity3.setC_date(ltm);
        serviceHistoryEntity3.setHistory_content(serviceHistoryService.getCreateAccountContent("junit", "new junit3"));
        serviceHistoryEntity3.setHistory_type(Integer.parseInt(confAccessory.getProperty("code_history_account"), 10));
        serviceHistoryEntity1.setHistory_url(serviceHistoryService.getCreateAccountURL(2, 3));
        serviceHistoryEntity3.setIpaddress("127.0.0.1");
        serviceHistoryEntity3.setMember_srl(MDV.NONE);

        serviceHistoryEntity4 = new ServiceHistoryEntity();
        serviceHistoryEntity4.init();
        serviceHistoryEntity4.setC_date(ltm);
        serviceHistoryEntity4.setHistory_content(serviceHistoryService.getCreateAccountContent("junit", "new junit4"));
        serviceHistoryEntity4.setHistory_type(Integer.parseInt(confAccessory.getProperty("code_history_account"), 10));
        serviceHistoryEntity1.setHistory_url(serviceHistoryService.getCreateAccountURL(4, 5));
        serviceHistoryEntity4.setIpaddress("127.0.0.1");
        serviceHistoryEntity4.setMember_srl(MDV.NONE);

        serviceHistoryEntity5 = new ServiceHistoryEntity();
        serviceHistoryEntity5.init();
        serviceHistoryEntity5.setC_date(ltm);
        serviceHistoryEntity5.setHistory_content(serviceHistoryService.getCreateAccountContent("junit", "new junit5"));
        serviceHistoryEntity5.setHistory_type(Integer.parseInt(confAccessory.getProperty("code_history_account"), 10));
        serviceHistoryEntity1.setHistory_url("");
        serviceHistoryEntity5.setIpaddress("127.0.0.1");
        serviceHistoryEntity5.setMember_srl(MDV.NONE);
        */
    }

    @Test
    @Rollback
    public void noerror() {
        assertThat(1, is(1));
    }

    // 이력은 웹에서 넣지 않고 프로세스를 띄어서 넣을 예정임.
    // 때문에 현재 이력 테스트는 의미 없음.

    /**
     * tbl_service_history 기본 CRUD 테스트
     */
    /*
    @Test
    @Rollback
    public void serviceHistoryBasicTest() {
        assertThat(confAccessory.containsKey("code_history_account"), is(true));
        assertThat(confAccessory.containsKey("sequence_history"), is(true));

        int historySeqType = Integer.parseInt(confAccessory.getProperty("sequence_history"), 10);
        int historyAccountType = Integer.parseInt(confAccessory.getProperty("code_history_account"), 10);
        int currTime = (int) (DateTime.now().getMillis() / 1000);
        int endTime = (int) (fmtYYYYMM.parseDateTime("202004").getMillis() / 1000);

        // pre select list
        long count1 = serviceHistoryDao.count(currTime, endTime, historyAccountType, MDV.NUSE, MDV.NUSE);
        List<ServiceHistoryEntity> list1 = serviceHistoryDao.get(currTime, endTime, historyAccountType, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        boolean bCheck = false;

        assertThat(serviceHistoryEntity1.getHistory_srl(), is((long)MDV.NUSE));
        System.out.println(serviceHistoryEntity1.getHistory_content());
        serviceHistoryEntity1.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity1);
        if (serviceHistoryEntity1.getHistory_srl() > 0) bCheck = true;
        assertThat(bCheck, is(true));

        bCheck = false;
        assertThat(serviceHistoryEntity2.getHistory_srl(), is((long)MDV.NUSE));
        System.out.println(serviceHistoryEntity2.getHistory_content());
        serviceHistoryEntity2.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity2);
        if (serviceHistoryEntity2.getHistory_srl() > 0) bCheck = true;
        assertThat(bCheck, is(true));

        bCheck = false;
        assertThat(serviceHistoryEntity3.getHistory_srl(), is((long)MDV.NUSE));
        System.out.println(serviceHistoryEntity3.getHistory_content());
        serviceHistoryEntity3.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity3);
        if (serviceHistoryEntity3.getHistory_srl() > 0) bCheck = true;
        assertThat(bCheck, is(true));

        bCheck = false;
        assertThat(serviceHistoryEntity4.getHistory_srl(), is((long)MDV.NUSE));
        System.out.println(serviceHistoryEntity4.getHistory_content());
        serviceHistoryEntity4.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity4);
        if (serviceHistoryEntity4.getHistory_srl() > 0) bCheck = true;
        assertThat(bCheck, is(true));

        // select one
        ServiceHistoryEntity resultVo1 = serviceHistoryDao.get(serviceHistoryEntity1.getHistory_srl(),
                serviceHistoryEntity1.getC_date());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(serviceHistoryEntity1), is(true));

        ServiceHistoryEntity resultVo2 = serviceHistoryDao.get(serviceHistoryEntity2.getHistory_srl(),
                serviceHistoryEntity2.getC_date());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(serviceHistoryEntity2), is(true));

        ServiceHistoryEntity resultVo3 = serviceHistoryDao.get(serviceHistoryEntity3.getHistory_srl(),
                serviceHistoryEntity3.getC_date());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(serviceHistoryEntity3), is(true));

        ServiceHistoryEntity resultVo4 = serviceHistoryDao.get(serviceHistoryEntity4.getHistory_srl(),
                serviceHistoryEntity4.getC_date());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(serviceHistoryEntity4), is(true));

        // select list
        long count2 = serviceHistoryDao.count(currTime, endTime, historyAccountType, MDV.NUSE, MDV.NUSE);
        assertThat(count1+4, is(count2));
        List<ServiceHistoryEntity> list2 = serviceHistoryDao.get(currTime, endTime, historyAccountType, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));
    }
    */

    /**
     * tbl_service_history list 테스트
     */
    /*
    @Test
    @Rollback
    public void serviceHistoryListTest() {
        // set prepare data
        assertThat(confAccessory.containsKey("code_history_account"), is(true));
        assertThat(confAccessory.containsKey("sequence_history"), is(true));
        int historySeqType = Integer.parseInt(confAccessory.getProperty("sequence_history"), 10);
        int historyAccountType = Integer.parseInt(confAccessory.getProperty("code_history_account"), 10);

        serviceHistoryEntity1.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity1);

        serviceHistoryEntity2.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity2);

        serviceHistoryEntity3.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity3);

        serviceHistoryEntity4.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity4);

        serviceHistoryEntity5.setHistory_srl(myDatabaseUtilService.getNextSequence(historySeqType));
        serviceHistoryDao.add(serviceHistoryEntity5);

        int currTime = (int) (DateTime.now().getMillis() / 1000) - 10;
        int endTime = (int) (fmtYYYYMM.parseDateTime("202004").getMillis() / 1000);

        // select list(index 를 타지 않는다.)
        long count1 = serviceHistoryDao.count(MDV.NUSE, endTime, historyAccountType, MDV.NUSE, MDV.NUSE);
        List<ServiceHistoryEntity> list1 = serviceHistoryDao.get(MDV.NUSE, endTime, historyAccountType,
                MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list1, is(notNullValue()));
        assertThat((long)list1.size(), is(count1));

        // select list(index 를 타지 않는다.)
        long count2 = serviceHistoryDao.count(currTime, MDV.NUSE, historyAccountType, MDV.NUSE, MDV.NUSE);
        List<ServiceHistoryEntity> list2 = serviceHistoryDao.get(currTime, MDV.NUSE, historyAccountType, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list2, is(notNullValue()));
        assertThat((long)list2.size(), is(count2));

        // select list no history type
        long countZero = serviceHistoryDao.count(currTime, endTime, 999999, MDV.NUSE, MDV.NUSE);
        assertThat(countZero, is(0L));
        List<ServiceHistoryEntity> listZero = serviceHistoryDao.get(currTime, endTime, 999999, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(listZero, is(notNullValue()));
        assertThat(listZero.size(), is(0));

        // select list no member
        countZero = serviceHistoryDao.count(currTime, endTime, MDV.NUSE, 9999999999L, MDV.NUSE);
        assertThat(countZero, is(0L));
        listZero = serviceHistoryDao.get(currTime, endTime, MDV.NUSE, 9999999999L, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(listZero, is(notNullValue()));
        assertThat(listZero.size(), is(0));

        // select using index indicator
        List<ServiceHistoryEntity> list3 = serviceHistoryDao.get(currTime, endTime, historyAccountType,
                MDV.NUSE, MDV.NUSE, 0, 3);
        assertThat(list3, is(notNullValue()));
        assertThat(list3.size(), is(3));

        long lastHistorySrl = list1.get(2).getHistory_srl();

        List<ServiceHistoryEntity> list4 = serviceHistoryDao.get(currTime, endTime, historyAccountType,
                MDV.NUSE, lastHistorySrl, 0, 3);
        assertThat(list4, is(notNullValue()));
        boolean bCheck = false;
        if(list4.size() >= 2) bCheck = true;
        assertThat(bCheck, is(true));

        List<ServiceHistoryEntity> list5 = serviceHistoryDao.get(currTime, endTime, historyAccountType,
                MDV.NUSE, MDV.NUSE, 0, 5);
        assertThat(list5, is(notNullValue()));
        assertThat(list5.size(), is(5));
        assertThat(list5.get(3).getHistory_srl(), is(list4.get(0).getHistory_srl()));
        assertThat(list5.get(4).getHistory_srl(), is(list4.get(1).getHistory_srl()));
    }
    */
}

package com.ckstack.ckpush.service.accessory;

//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Properties;

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
public class MyDatabaseUtilServiceTest {
    @Autowired
    private MyDatabaseUtilService myDatabaseUtilService;
    @Autowired
    private Properties confAccessory;

    @Before
    public void setUp() {

    }

    @Test
    @Rollback
    public void getSequenceBasicTest() {
        assertThat(confAccessory.containsKey("sequence_history"), is(true));

        boolean bCheck = false;
        int historySeqType = Integer.parseInt(confAccessory.getProperty("sequence_history"), 10);
        long seqNo1 = myDatabaseUtilService.getNextSequence(historySeqType);
        if(seqNo1 > 0) bCheck = true;
        assertThat(bCheck, is(true));

        long seqNo2 = myDatabaseUtilService.getNextSequence(historySeqType);
        bCheck = (seqNo2 > 0);
        assertThat(bCheck, is(true));
        assertThat(seqNo1+1, is(seqNo2));

        long seqNo3 = myDatabaseUtilService.getNextSequence(historySeqType);
        bCheck = (seqNo3 > 0);
        assertThat(bCheck, is(true));
        assertThat(seqNo2+1, is(seqNo3));
    }

    @Test(expected = CustomException.class)
    @Rollback
    public void notExistSequenceTest() {
        myDatabaseUtilService.getNextSequence(9999);
    }

    @Test
    @Rollback
    public void notExistSequenceExceptionValueTest() {
        String tid = null;
        String code = null;
        Map<String, String> reason = null;
        int httpCode = 500;

        try {
            myDatabaseUtilService.getNextSequence(9999);
        } catch (CustomException e) {
            code = e.getCode();
            reason = e.getReason();
            httpCode = e.getHttpStatus();
        }

        assertThat(tid, is(nullValue()));
        assertThat(code, is("custom_sequence_nextval_error"));
        assertThat(reason, is(notNullValue()));
        assertThat(httpCode, is(MDV.NONE));
    }
}

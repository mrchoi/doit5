package com.ckstack.ckpush.dao.accessory;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.domain.accessory.SequenceEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class SequenceDaoTest {
    @Autowired
    private SequenceDao sequenceDao;

    private final int SEQ_TYPE_JUNIT1 = 1000;
    private final int SEQ_TYPE_JUNIT2 = 2000;

    private SequenceEntity sequenceEntity1;
    private SequenceEntity sequenceEntity2;

    @Before
    public void setUp() {
        sequenceEntity1 = new SequenceEntity();
        sequenceEntity1.setSeq_no(0);
        sequenceEntity1.setSeq_type(SEQ_TYPE_JUNIT1);

        sequenceEntity2 = new SequenceEntity();
        sequenceEntity2.setSeq_no(0);
        sequenceEntity2.setSeq_type(SEQ_TYPE_JUNIT2);
    }

    /**
     * custom sequence 기본 테스트
     */
    @Test
    @Rollback
    public void sequenceBasicTest() {
        // pre select
        SequenceEntity resultVo1 = sequenceDao.get(SEQ_TYPE_JUNIT1);
        assertThat(resultVo1, is(nullValue()));

        // insert
        sequenceDao.add(sequenceEntity1);
        sequenceDao.add(sequenceEntity2);

        // select
        SequenceEntity resultVo2 = sequenceDao.get(SEQ_TYPE_JUNIT1);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.getSeq_no(), is(0L));

        SequenceEntity resultVo3 = sequenceDao.get(SEQ_TYPE_JUNIT2);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.getSeq_no(), is(0L));

        // modify
        sequenceDao.modify(SEQ_TYPE_JUNIT1);
        SequenceEntity resultVo4 = sequenceDao.get(SEQ_TYPE_JUNIT1);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.getSeq_no(), is(1L));

        sequenceDao.modify(SEQ_TYPE_JUNIT1);
        SequenceEntity resultVo5 = sequenceDao.get(SEQ_TYPE_JUNIT1);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.getSeq_no(), is(2L));

        sequenceDao.modify(SEQ_TYPE_JUNIT2);
        SequenceEntity resultVo6 = sequenceDao.get(SEQ_TYPE_JUNIT2);
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.getSeq_no(), is(1L));

        // delete
        sequenceDao.delete(SEQ_TYPE_JUNIT1);

        SequenceEntity resultVo7 = sequenceDao.get(SEQ_TYPE_JUNIT1);
        assertThat(resultVo7, is(nullValue()));
    }


}

package com.ckstack.ckpush.service.accessory.impl;

import com.ckstack.ckpush.service.accessory.MyDatabaseUtilService;
import com.ckstack.ckpush.dao.accessory.SequenceDao;
import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.domain.accessory.SequenceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
@Service
@Transactional(value = "transactionManager")
public class MyDatabaseUtilServiceImpl implements MyDatabaseUtilService {
    private final static Logger LOG = LoggerFactory.getLogger(MyDatabaseUtilServiceImpl.class);

    @Autowired
    private SequenceDao sequenceDao;

    /**
     * 종류에 맞는 customized sequence 시퀀스 값을 구한다.
     * @param seq_type sequence 종류
     * @return 다음 sequence 값을 리턴한다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public long getNextSequence(int seq_type) {
        sequenceDao.modify(seq_type);
        SequenceEntity sequenceEntity = sequenceDao.get(seq_type);

        if(sequenceEntity == null) {
            LOG.error("can't get custom sequence. seq_type["+seq_type+"]");
            HashMap<String, String> reason = new HashMap<>();
            reason.put("sequenceEntity", "sequenceEntity is null. failed get next sequence");
            throw new CustomException("custom_sequence_nextval_error", reason);
        }

        return sequenceEntity.getSeq_no();
    }
}

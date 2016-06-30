package com.ckstack.ckpush.dao.accessory;

import com.ckstack.ckpush.domain.accessory.SequenceEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
@Repository
@Transactional(value = "transactionManager")
public interface SequenceDao {
    /**
     * tbl_sequence insert row
     * @param sequenceEntity insert data
     * @return inserted row count
     */
    int add(SequenceEntity sequenceEntity);

    /**
     * tbl_sequence select one row
     * @param seq_type seq_type condition
     * @return row one. select row 가 없으면 null 을 리턴 한다.
     */
    @Transactional(readOnly = true)
    SequenceEntity get(int seq_type);

    /**
     * tbl_sequence update row
     * @param seq_type seq_type condition
     * @return updated row count
     */
    int modify(int seq_type);

    /**
     * tbl_sequence delete row
     * @param seq_type seq_type condition
     * @return deleted row count
     */
    int delete(int seq_type);
}

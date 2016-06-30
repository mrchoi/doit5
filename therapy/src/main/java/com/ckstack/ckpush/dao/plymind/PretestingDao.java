package com.ckstack.ckpush.dao.plymind;

import com.ckstack.ckpush.domain.plymind.PretestingEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wypark on 16. 1. 19.
 */
@Repository
@Transactional(value = "transactionManager")
public interface PretestingDao {
    /**
     * 사전검사를 등록한다.
     * @param pretestingEntity insert data
     * @return group by count
     */
    int add(PretestingEntity pretestingEntity);

    /**
     * 사전검사 정보를 조회한다.
     *
     * @param member_srl document_srl condition
     * @param select_date document_srls condition
     * @return deleted row count
     */
    List<PretestingEntity> info(@Param("member_srl") long member_srl,
                                @Param("c_date") int c_date);

    /**
     * 사전검사를 위한 항목 조회
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<PretestingEntity> get();

    /**
     * 사전검사를 위한 항목 조회
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<PretestingEntity> getQuestion();

    /**
     * 사전검사를 위한 항목 조회
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<PretestingEntity> getKind();

    /**
     * 사전검사를 위한 항목 조회
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<PretestingEntity> getItem();

    /**
     * 사전검사를 삭제한다.
     *
     * @param member_srl document_srl condition
     * @param c_date document_srls condition
     * @return deleted row count
     */
    int delete(@Param("member_srl") long member_srl,
               @Param("c_date") int c_date);
}

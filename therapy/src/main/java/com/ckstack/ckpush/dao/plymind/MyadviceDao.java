package com.ckstack.ckpush.dao.plymind;

import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.plymind.AddressEntity;
import com.ckstack.ckpush.domain.plymind.PlymindDocumentEntity;
import com.ckstack.ckpush.domain.plymind.DiaryEntity;
import com.ckstack.ckpush.domain.plymind.PushEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
@Repository
@Transactional(value = "transactionManager")
public interface MyadviceDao {
    /**
     * 마음일기을 등록한다.
     *
     * @param diaryEntity 마음일기 등록 정보
     * @return insert row count
     */
    int addDiary(DiaryEntity diaryEntity);

    /**
     * 상담사 코멘트를 등록한다.
     *
     * @param plymindDocumentEntity 상담사 코멘트 등록 정보
     * @return insert row count
     */
    int addPlymindDocument(PlymindDocumentEntity plymindDocumentEntity);

    /**
     * 상담사 코멘트를 수정한다.
     *
     * @param plymindDocumentEntity 상담사 코멘트 등록 정보
     * @return insert row count
     */
    void modifyPlymindDocument(@Param("plymindDocumentEntity") PlymindDocumentEntity plymindDocumentEntity,
                               @Param("document_srl") long document_srl);

    /**
     * 수령지 정보를 조회한다.
     *
     * @param application_group 상담 그룹 번호

     * @return select application_srl row
     */
    @Transactional(readOnly = true)
    AddressEntity getAddressInfo(@Param("application_group") int application_group);

    /**
     * 상담에 대한 마음일기 목록을 조회한다.
     *
     * @param member_srl 사용자 시리얼 번호
     * @param application_srl 상담 시리얼 번호
     * @param application_group 상담 그룹 번호

     * @return select application_srl row
     */
    @Transactional(readOnly = true)
    List<DiaryEntity> getDiary(@Param("member_srl") long member_srl,
                               @Param("application_srl") long application_srl,
                               @Param("application_group") int application_group);

    /**
     * 인문 컨텐츠 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    long countPush(@Param("application_srl") long application_srl,
                   @Param("application_group") int application_group,
                   @Param("application_statues") List<Integer> application_statues);

    /**
     * 인문 컨텐츠 목록을 조회한다.
     *
     * @param application_group 상담 그룹 번호
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<PushEntity> getPushList(@Param("application_srl") long application_srl,
                                 @Param("application_group") int application_group,
                                 @Param("application_statues") List<Integer> application_statues,
                                 @Param("sortValue") Map<String, String> sortValue,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    /**
     * 등록을 위한 인문 컨텐츠 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    long countAddPush(@Param("application_srl") long application_srl,
                      @Param("application_group") int application_group,
                      @Param("application_statues") List<Integer> application_statues);

    /**
     * 등록을 위한 인문 컨텐츠 목록을 조회한다.
     *
     * @param application_group 상담 그룹 번호
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<PushEntity> getAddPushList(@Param("application_srl") long application_srl,
                                    @Param("application_group") int application_group,
                                    @Param("application_statues") List<Integer> application_statues,
                                    @Param("sortValue") Map<String, String> sortValue,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);

    /**
     * 인문 컨텐츠 정보를 조회한다.
     *
     * @param application_srl 상담 그룹 번호
     * @return select multi row
     */
    @Transactional(readOnly = true)
    PushEntity getPushInfo(@Param("application_srl") long application_srl);

    /**
     * 인문 컨텐츠 등록을 위한 정보를 조회한다.
     *
     * @param application_group 상담 그룹 번호
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<PushEntity> getMessageList(@Param("application_srl") long application_srl,
                                    @Param("application_group") int application_group,
                                    @Param("push_status") int push_status);

    /**
     * 인문 컨텐츠 목록을 조회한다.
     *
     * @param application_group 상담 그룹 번호
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<PlymindDocumentEntity> getPlymindDocument(@Param("application_srl") long application_srl,
                                                   @Param("application_group") int application_group);

    @Transactional(readOnly = true)
    PlymindDocumentEntity getPlymindDocumentD(@Param("document_srl") long document_srl);
}

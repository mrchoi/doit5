package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.plymind.AddressEntity;
import com.ckstack.ckpush.domain.plymind.PlymindDocumentEntity;
import com.ckstack.ckpush.domain.plymind.DiaryEntity;
import com.ckstack.ckpush.domain.plymind.PushEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface MyadviceService {
    /**
     * 마음일기을 등록한다.
     * @param diaryEntity 마음일기 등록 정보
     *
     * @return insert row count
     */
    int addDiary(DiaryEntity diaryEntity);

    /**
     * 인문컨텐츠를 등록한다.
     * @param pushEntity 인문 컨텐츠 등록 정보
     *
     * @return boolean
     */
    boolean addPush(PushEntity pushEntity);

    /**
     * 메세를 발송한다.
     * @param pushEntity 인문 컨텐츠 등록 정보
     *
     * @return boolean
     */
    boolean addMessage(PushEntity pushEntity);

    /**
     * 마음일기을 등록한다.
     * @param plymindDocumentEntity 상담사 코멘트 등록 정보
     *
     * @return insert row count
     */
    int addPlymindDocument(PlymindDocumentEntity plymindDocumentEntity);

    /**
     * 상담사 코멘트를 수정한다.
     * @param plymindDocumentEntity
     *
     * @return insert row count
     */
    void modifyPlymindDocument(PlymindDocumentEntity plymindDocumentEntity, long documentSrl);

    /**
     * 수령지 정보를 조회한다.
     * @param application_group 상담 그룹 번호
     *
     * @return 조회된 총 갯수
     */
    AddressEntity getAddressInfo(int application_group);

    /**
     * 상담에 대한 마음일기 목록을 조회한다.
     * @param member_srl 사용자 시리얼 번호
     * @param application_group 상담 그룹 번호
     * @param application_srl 상담 시리얼 번호
     *
     * @return 조회된 총 갯수
     */
    List<DiaryEntity> getDiary(long member_srl, long application_srl, int application_group);

    /**
     * 인문컨텐츠 갯수를 조회한다.
     * @param application_group 심리상담 그룹
     * @param application_statues 심리상담 진행상태
     *
     * @return 조회된 총 갯수
     */
    long countPush(long application_srl,
                   int application_group,
                   List<Integer> application_statues);

    /**
     * 인문컨텐츠 정보를 조회한다.
     * @param application_srl 상담 시리얼번호
     * @param application_group 상담 그룹 번호
     * @param application_statues 상담 진행 상태
     * @param sortValue 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 데이터
     */
    List<PushEntity> getPushList(long application_srl,
                                 int application_group,
                                 List<Integer> application_statues,
                                 Map<String, String> sortValue,
                                 int offset,
                                 int limit);

    /**
     * 인문컨텐츠 갯수를 조회한다.
     * @param application_group 심리상담 그룹
     * @param application_statues 심리상담 진행상태
     *
     * @return 조회된 총 갯수
     */
    long countAddPush(long application_srl,
                      int application_group,
                      List<Integer> application_statues);

    /**
     * 인문컨텐츠 정보를 조회한다.
     * @param application_srl 상담 시리얼번호
     * @param application_group 상담 그룹 번호
     * @param application_statues 상담 진행 상태
     * @param sortValue 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 데이터
     */
    List<PushEntity> getAddPushList(long application_srl,
                                    int application_group,
                                    List<Integer> application_statues,
                                    Map<String, String> sortValue,
                                    int offset,
                                    int limit);

    /**
     * 인문컨텐츠 정보를 조회한다.
     * @param application_srl 상담 시리얼 번호
     *
     * @return 조회된 데이터
     */
    PushEntity getPushInfo(long application_srl);

    /**
     * 메시지 정보를 조회한다.
     * @param application_srl 상담 시리얼번호
     * @param application_group 상담 그룹 번호
     * @param push_status sms 발송 상태
     *
     * @return 조회된 데이터
     */
    List<PushEntity> getMessageList(long application_srl,
                                    int application_group,
                                    int push_status);

    /**
     * 상담사 코멘트  등록을 위한 정보를 조회한다.
     * @param application_group 상담 그룹 번호
     * @param application_group 상담 그룹 번호
     *
     * @return 조회된 데이터
     */
    List<PlymindDocumentEntity> getPlymindDocument(long application_srl, int application_group);

    /**
     * 상담사 코멘트  등록을 위한 정보를 조회한다.
     * @param member_srl 사용자 시리얼 번호
     *
     * @return 조회된 데이터
     */
    Map<String, Object> getPresent(long member_srl);

    PlymindDocumentEntity getPlymindDocumentD(long documentSrl);
}


package com.ckstack.ckpush.service.push;

import com.ckstack.ckpush.domain.push.NotificationEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by kodaji on 16. 2. 22.
 */
public interface PlymindMessageService {

    /**
     * insert document row
     *
     * @param notificationEntity insert data
     * @return insert row count
     */
    int add(NotificationEntity notificationEntity);
    /**
     * select row count
     *
     * @param applicationGroup 상담 그룹
     * @param applicationSrl   상담 시리어 넘버
     * @param notiType         메시지 타입
     * @param memberSrl        member_srl
     * @param userId           user_id
     * @param pushStatus       메시지 상태
     * @param bookTime         예약 시간 (발송 요청 시간)
     * @param notiTime         발송 시간
     * @return 메시지 count
     */
    long count(long applicationGroup, long applicationSrl, int notiType,
               long memberSrl, String userId, int pushStatus, int bookTime, int notiTime);
    /**
     * 메시지 목록을 가지고 온다.
     *
     * @param applicationGroup 상담 그룹
     * @param applicationSrl   상담 시리어 넘버
     * @param notiType         메시지 타입
     * @param memberSrl        member_srl
     * @param userId           user_id
     * @param pushStatus       메시지 상태
     * @param bookTime         예약 시간 (발송 요청 시간)
     * @param notiTime         발송 요청 시간
     * @param sort             list sort
     * @param offset           list offset
     * @param limit            list limit
     * @return 메시지 목록
     */
    List<NotificationEntity> get(long applicationGroup, long applicationSrl, int notiType,
                                 long memberSrl, String userId, int pushStatus, int bookTime, int notiTime,
                                 Map<String, String> sort, int offset, int limit);
    /**
     * 전송할 메시지 row count
     *
     * @param notiType         메시지 타입
     * @param memberSrl        member_srl
     * @param userId           user_id
     * @param pushStatus       메시지 상태
     * @param bookTime         예약시간, 발송 요청 시간
     * @param notiTime         발송시간
     */
    long notificationCount(int notiType, long memberSrl, String userId, int pushStatus, int bookTime, int notiTime);
    /**
     * 전송할 메시지 목록
     *
     * @param notiType         메시지 타입
     * @param memberSrl        member_srl
     * @param userId           user_id
     * @param pushStatus       메시지 상태
     * @param bookTime         예약시간, 발송 요청 시간
     * @param notiTime         발송시간
     * @Return 전송할 메시지 목록
     */
    List<NotificationEntity> notificationList(int notiType, long memberSrl, String userId, int pushStatus, int bookTime, int notiTime);

    /**
     * 메시지 변환 NotificationEntity -> Map
     *
     * @param object 변환할 메시지
     * @return map object
     */

    Map<String, Object> getContent(Object object);
    /**
     * 메시지 전송
     * @param notificationEntity 전송할 메시지
     */
    void pushMessage(NotificationEntity notificationEntity);

    /**
     * 전송 메시지 수정
     * @param notiSrl  noti_srl
     * @param pushText 전송 메시지 내용
     * @return update row count
     */
    int updatePushText(long notiSrl, String pushText);
}

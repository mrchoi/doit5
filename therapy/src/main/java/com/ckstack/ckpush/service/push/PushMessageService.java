package com.ckstack.ckpush.service.push;

import com.ckstack.ckpush.domain.push.PushMessageEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 5. 11..
 */
public interface PushMessageService {
    /**
     * APNs, GCM Push 메시지를 발송 한다.
     *
     * @param pushMessageEntity 발송할 Push 메시지 데이터
     * @param requestIP 발송 요청한 client 의 ipaddress
     */
    void pushGcmApns(PushMessageEntity pushMessageEntity, String requestIP);

    /**
     * APNs, GCM Push 메시지 발송 이력을 카운트 한다.
     *
     * @param appSrl 메시지 발송 대상 앱 시리얼 넘버
     * @param pushTitle 메시지 제목
     * @return 발송 이력 개수
     */
    long getPushMessageHistoryCount(int appSrl, String pushTitle);

    /**
     * APNs, GCM Push 메시지 발송 이력을 카운트 한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, push_title 에 대해서는 지원 한다.
     * @return 발송 이력 개수
     */
    long getPushMessageHistoryCount(Map<String, String> searchFilter);

    /**
     * APNs, GCM Push 메시지 발송 이력 리스트를 구한다.
     *
     * @param appSrl 메시지 발송 대상 앱 시리얼 넘버
     * @param pushTitle 메시지 제목
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 메시지 발송 이력
     */
    List<PushMessageEntity> getPushMessageHistory(int appSrl, String pushTitle, Map<String, String> sort,
                                                  int offset, int limit);

    /**
     * APNs, GCM Push 메시지 발송 이력 리스트를 구한다.
     *
     * @param searchFilter 검색 칼럼과 값이 포함된 map. app_srl, push_title 에 대해서는 지원 한다.
     * @param sort 리스트 order 에 대한 정보. {column:value, dir:value} 형태의 hashmap list
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     * @return 조건에 맞는 메시지 발송 이력
     */
    List<PushMessageEntity> getPushMessageHistory(Map<String, String> searchFilter, Map<String, String> sort,
                                                  int offset, int limit);

    /**
     * 앱에 등록된 단말기의 GCM RID 를 변경 한다.
     *
     * @param appSrl 앱 시리얼 넘버
     * @param deviceSrl 단말기 시리얼 넘버
     * @param rid gcm rid
     */
    void modifyGCMRid(int appSrl, long deviceSrl, String rid);
}

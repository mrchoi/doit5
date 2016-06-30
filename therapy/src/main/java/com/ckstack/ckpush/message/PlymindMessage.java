package com.ckstack.ckpush.message;

/**
 * Created by root on 16. 2. 22.
 */
public interface PlymindMessage {
    /**
     * plymind 서비스에서 app push 를 보내기 위해 사용한다.
     * scheduled 사용.
     * 1초 마다 실행
     * 현재 시간 이전의 전송 요청 (push_status = 1)인 메시지를 가져와서 메시지를 전송한다.
     * tbl_plymind_noti
     * push_status = 1 and noti_time1 <= System.currentTimeMillis()
     */
    void sendList();

    /**
     * plymind 상담 상태 처리를 한다.
     * scheduled 사용.
     * 하루에 1회 실행
     */
    void applicationStatusChange();
}

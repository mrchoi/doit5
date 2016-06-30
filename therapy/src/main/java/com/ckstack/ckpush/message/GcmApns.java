package com.ckstack.ckpush.message;

import com.ckstack.ckpush.data.ras.RasEventBean;

/**
 * Created by dhkim94 on 15. 6. 12..
 */
public interface GcmApns {
    /**
     * gcm 이나 apns 메시지 발송을 위해서 redis에 event 를 날린다.
     *
     * @param rasEventBean redis 에 날릴 이벤트 내용
     */
    void send(RasEventBean rasEventBean);
}

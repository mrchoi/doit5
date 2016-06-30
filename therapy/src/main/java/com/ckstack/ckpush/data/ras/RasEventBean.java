package com.ckstack.ckpush.data.ras;

import lombok.Data;

import java.util.Map;

/**
 * Created by dhkim94 on 15. 6. 12..
 */
@Data
public class RasEventBean {
    private String app_id;
    private String pid;
    private String et;
    private String tid;
    private long ctime;
    private Map<String, Object> ud;

    // ud 는 다음 형태로 구성 된다.
    // app_srl      : 앱 시리얼 넘버
    // app_id       : 서비스에서 사용되는 app_id
    // user_id      : push 메시지 발송하는 사용자 아이디
    // push_title   : push 메시지 타이틀
    // push_text    : push 메시지 본문
    // callback_url : push 메시지 callback url
    // image_url    : push 메시지에 포함될 이미지 url
    // push_type    : push 타입. 전체는 '__all', 개발 대상은 '__unit'
    // single_target: push 대상의 단말 시리얼 넘버. __unit 일때만 유효함. __all 일때는 -1 로 고정됨.
    // c_date       : push 메시지 발송 시간
}

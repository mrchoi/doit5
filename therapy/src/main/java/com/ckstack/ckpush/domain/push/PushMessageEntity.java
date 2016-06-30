package com.ckstack.ckpush.domain.push;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.validator.ByteSize;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dhkim94 on 15. 4. 29..
 */
@Data
public class PushMessageEntity {
    private long push_srl;
    @Min(value = 1, message = "{pushmessageentity.app_srl.min}")
    private int app_srl;

    private String user_id;

    @NotNull(message = "{pushmessageentity.push_title.notNull}")
    @Size(min = 1, max = 128, message = "{pushmessageentity.push_title.size}")
    @ByteSize(min = 1, max = 128, message = "{pushmessageentity.push_title.byteSize}")
    private String push_title;

    @NotNull(message = "{pushmessageentity.push_text.notNull}")
    @Size(min = 1, message = "{pushmessageentity.push_text.size}")
    private String push_text;

    private String callback_url;

    @NotNull(message = "{pushmessageentity.push_target.notNull}")
    @Size(min = 1, message = "{pushmessageentity.push_target.size}")
    private String push_target;

    private long total_count;
    private long total_real_count;
    private long success_count;
    private long fail_count;
    private long fail_real_count;
    private long received_count;
    private long confirm_count;

    private int c_date;
    private int u_date;

    // 어드민에서 사용하기 위해서 추가한 변수
    private int file_srl;               // 메시지 발송시 첨부 파일의 file_srl. 외부 이미지 이면 MDV.NONE 임.
    private String image_url;           // 메시지 발송시 첨부 파일의 url
    private String single_push_target;  // 메시지 발송시 대상이 단일 일때, 대상의 device_id

    public void init() {
        this.push_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.user_id = null;
        this.push_title = null;
        this.push_text = null;
        this.callback_url = null;
        this.push_target = null;
        this.total_count = MDV.NUSE;
        this.total_real_count = MDV.NUSE;
        this.success_count = MDV.NUSE;
        this.fail_count = MDV.NUSE;
        this.fail_real_count = MDV.NUSE;
        this.received_count = MDV.NUSE;
        this.confirm_count = MDV.NUSE;
        this.c_date = MDV.NUSE;
    }
}

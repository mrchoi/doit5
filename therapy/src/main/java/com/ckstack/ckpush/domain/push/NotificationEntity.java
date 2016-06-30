package com.ckstack.ckpush.domain.push;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by kodaji on 16. 2. 21.
 */
@Data
public class NotificationEntity {
    private long noti_srl;
    private long application_group;
    private long application_srl;
    private int noti_type;
    private long member_srl;
    private String user_id;
    private String push_title;
    private String push_text;
    private String callback_url;
    private int push_status;
    private int book_time;
    private int noti_time1;
    private String noti_time2;
    private int c_date;

    public void init()
    {
        noti_srl = MDV.NUSE;
        application_group = MDV.NUSE;
        application_srl = MDV.NUSE;
        noti_type = MDV.NUSE;
        member_srl = MDV.NUSE;
        user_id = null;
        push_title = null;
        push_text = null;
        callback_url = null;
        push_status = MDV.NUSE;
        book_time = MDV.NUSE;
        noti_time1 = MDV.NUSE;
        noti_time2 = null;
        c_date = MDV.NUSE;
    }
}

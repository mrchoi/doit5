package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class PushEntity {
    private long noti_srl;
    private long application_srl;
    private int application_group;
    private String push_text;
    private int book_time;
    private int noti_time1;
    private String noti_time2;

    private int application_number;
    private int application_status;
    private String push_date;
    private String push_time;

    private long member_srl;

    public void init() {
        this.noti_srl = MDV.NONE;
        this.application_srl = MDV.NONE;
        this.application_group = MDV.NONE;
        this.push_text = null;
        this.book_time = MDV.NONE;
        this.noti_time1 = MDV.NONE;
        this.noti_time2 = null;

        this.application_number = MDV.NONE;
        this.application_status = MDV.NONE;
        this.push_date = null;
        this.push_time = null;

        this.member_srl = MDV.NONE;
    }
}
package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class HolidayEntity {
    private long holiday_srl;
    private long member_srl;
    private String holiday_title;
    private String holiday_date;
    private String holiday_time;
    private int c_date;

    private String user_name;
    private String nick_name;

    public void init() {
        this.holiday_srl = MDV.NONE;
        this.member_srl = MDV.NONE;
        this.holiday_title = null;
        this.holiday_date = null;
        this.holiday_time = null;
        this.c_date = MDV.NONE;

        this.user_name = null;
        this.nick_name = null;
    }
}

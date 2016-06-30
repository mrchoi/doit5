package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class ApplicationEntity {
    private long application_srl;
    private int application_group;
    private int application_number;
    private int application_status;
    private long member_srl;
    private long product_srl;
    private long advisor_srl;
    private String start_date;
    private String end_date;
    private String push_date;
    private String push_time;
    private int c_date;
    private int u_date;

    private long contents_srl;
    private int kind;
    private String title;

    private int advice_type;
    private int advice_period;
    private int advice_number;
    private int advice_price;

    private String appointment_date;
    private String appointment_time;

    private String receive_name;
    private String receive_address;
    private String receive_phone;

    private String user_name;
    private String advisor_name;

    private long[] member_srls;

    public void init() {
        this.application_srl = MDV.NONE;
        this.application_group = MDV.NONE;
        this.application_number = MDV.NONE;
        this.application_status = MDV.NONE;
        this.member_srl = MDV.NONE;
        this.product_srl = MDV.NONE;
        this.advisor_srl = MDV.NONE;
        this.start_date = null;
        this.end_date = null;
        this.push_date = null;
        this.push_time = null;
        this.c_date = MDV.NONE;
        this.u_date = MDV.NONE;

        this.contents_srl = MDV.NUSE;
        this.kind = MDV.NUSE;
        this.title = null;

        this.advice_type = MDV.NUSE;
        this.advice_period = MDV.NUSE;
        this.advice_number = MDV.NUSE;
        this.advice_price = MDV.NUSE;

        this.appointment_date = null;
        this.appointment_time = null;

        this.receive_name = null;
        this.receive_address = null;
        this.receive_phone = null;

        this.user_name = null;
        this.advisor_name = null;

        this.member_srls = null;
    }
}

package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class AppointmentEntity {
    private long appointment_srl;
    private long member_srl;
    private long advisor_srl;
    private long application_srl;
    private String appointment_date;
    private String appointment_time;
    private int appointment_status;
    private long c_date;
    private long u_date;

    private long application_group;
    private long application_number;
    private long product_srl;

    private int kind;
    private String title;

    private int advice_type;
    private int advice_period;
    private int advice_number;
    private int advice_price;

    private String user_name;
    private String advisor_name;

    public void init() {
        this.appointment_srl = MDV.NONE;
        this.member_srl = MDV.NONE;
        this.advisor_srl = MDV.NONE;
        this.application_srl = MDV.NONE;
        this.appointment_date = null;
        this.appointment_time = null;
        this.appointment_status = MDV.NONE;
        this.c_date = MDV.NONE;
        this.u_date = MDV.NONE;

        this.application_group = MDV.NONE;
        this.application_number = MDV.NONE;
        this.product_srl = MDV.NONE;

        this.kind = MDV.NONE;
        this.title = null;

        this.advice_type = MDV.NONE;
        this.advice_period = MDV.NONE;
        this.advice_number = MDV.NONE;
        this.advice_price = MDV.NONE;

        this.user_name = null;
        this.advisor_name = null;
    }
}

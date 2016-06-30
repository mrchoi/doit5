package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class PaymentEntity {
    private long payment_srl;
    private long member_srl;
    private long product_srl;
    private int application_group;
    private String payment_name;
    private String payment_phone;
    private long price;
    private long receipt_kind;
    private String receipt_phone;
    private long payment_status;
    private String refund_bank;
    private String refund_account;
    private String refund_name;
    private long reporting_date;
    private long cancel_date;
    private long payment_date;
    private long refund_req_date;
    private long refund_date;

    private long contents_srl;
    private int kind;
    private String title;

    private long advice_type;
    private long advice_period;
    private long advice_number;

    private long appointment_srl;
    private String appointment_date;
    private String appointment_time;

    private String user_name;
    private String advisor_name;

    public void init() {
        this.payment_srl = MDV.NONE;
        this.member_srl = MDV.NONE;
        this.product_srl = MDV.NONE;
        this.application_group = MDV.NONE;
        this.payment_name = null;
        this.payment_phone = null;
        this.price = MDV.NONE;
        this.receipt_kind = MDV.NONE;
        this.receipt_phone = null;
        this.payment_status = MDV.NONE;
        this.refund_bank = null;
        this.refund_account = null;
        this.refund_name = null;
        this.reporting_date = MDV.NONE;
        this.cancel_date = MDV.NONE;
        this.payment_date = MDV.NONE;
        this.refund_req_date = MDV.NONE;
        this.refund_date = MDV.NONE;

        this.contents_srl = MDV.NONE;
        this.kind = MDV.NONE;
        this.title = null;

        this.advice_type = MDV.NONE;
        this.advice_period = MDV.NONE;
        this.advice_number = MDV.NONE;

        this.appointment_srl = MDV.NONE;
        this.appointment_date = null;
        this.appointment_time = null;

        this.user_name = null;
        this.advisor_name = null;
    }
}

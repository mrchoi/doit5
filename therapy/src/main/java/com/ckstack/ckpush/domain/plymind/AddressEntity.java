package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class AddressEntity {
    private long address_srl;
    private int application_group;
    private String receive_name;
    private String receive_address;
    private String receive_phone;
    private int c_date;
    private int u_date;

    public void init() {
        this.address_srl = MDV.NONE;
        this.application_group = MDV.NONE;
        this.receive_name = null;
        this.receive_address = null;
        this.receive_phone = null;
        this.c_date = MDV.NONE;
        this.u_date = MDV.NONE;
    }
}

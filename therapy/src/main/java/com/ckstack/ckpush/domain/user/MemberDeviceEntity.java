package com.ckstack.ckpush.domain.user;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 3. 25..
 */
@Data
public class MemberDeviceEntity {
    private long member_srl;
    private int app_srl;
    private long device_srl;
    private String mobile_phone_number;
    private int allow_push_noti;
    private int push_noti_type;
    private int c_date;
    private int u_date;

    public void init() {
        this.member_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.device_srl = MDV.NUSE;
        this.mobile_phone_number = null;
        this.allow_push_noti = MDV.NUSE;
        this.push_noti_type = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

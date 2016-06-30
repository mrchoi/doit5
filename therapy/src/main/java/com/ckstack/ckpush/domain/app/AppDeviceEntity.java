package com.ckstack.ckpush.domain.app;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 4. 20..
 */
@Data
public class AppDeviceEntity {
    private int app_srl;
    private long device_srl;
    private String push_id;
    private int reg_push_id;
    private int allow_push;
    private int enabled;
    private int c_date;
    private int u_date;

    public void init() {
        this.app_srl = MDV.NUSE;
        this.device_srl = MDV.NUSE;
        this.push_id = null;
        this.reg_push_id = MDV.NUSE;
        this.allow_push = MDV.NUSE;
        this.enabled = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

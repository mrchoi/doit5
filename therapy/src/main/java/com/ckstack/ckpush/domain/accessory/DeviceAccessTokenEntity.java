package com.ckstack.ckpush.domain.accessory;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 7. 10..
 */
@Data
public class DeviceAccessTokenEntity {
    private long token_srl;
    private long device_srl;
    private int app_srl;
    private String access_token;
    private int token_expire;
    private int c_date;
    private int u_date;

    public void init() {
        this.token_srl = MDV.NUSE;
        this.device_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.access_token = null;
        this.token_expire = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

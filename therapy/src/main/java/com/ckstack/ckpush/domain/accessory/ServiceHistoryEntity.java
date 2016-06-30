package com.ckstack.ckpush.domain.accessory;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 3. 17..
 */
@Data
public class ServiceHistoryEntity {
    private long history_srl;
    private int history_type;
    private long member_srl;
    private String history_content;
    private String history_url;
    private String ipaddress;
    private int c_date;

    public void init() {
        this.history_srl = MDV.NUSE;
        this.history_type = MDV.NUSE;
        this.member_srl = MDV.NUSE;
        this.history_content = null;
        this.history_url = null;
        this.ipaddress = null;
        this.c_date = MDV.NUSE;
    }
}

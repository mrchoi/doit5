package com.ckstack.ckpush.domain.app;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 7. 14..
 */
@Data
public class AppGroupEntity {
    private long app_group_srl;
    private int app_srl;
    private String group_name;
    private String group_description;
    private int group_type;
    private String authority;
    private int enabled;
    private int allow_default;
    private int c_date;
    private int u_date;

    public void init() {
        this.app_group_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.group_name = null;
        this.group_description = null;
        this.group_type = MDV.NUSE;
        this.authority = null;
        this.enabled = MDV.NUSE;
        this.allow_default = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

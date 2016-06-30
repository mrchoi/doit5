package com.ckstack.ckpush.domain.user;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
@Data
public class GroupEntity {
    private int group_srl;
    private String group_name;
    private int allow_default;
    private String authority;
    private String description;
    private int c_date;
    private int u_date;

    public void init() {
        this.group_srl = MDV.NUSE;
        this.group_name = null;
        this.allow_default = MDV.NUSE;
        this.authority = null;
        this.description = null;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

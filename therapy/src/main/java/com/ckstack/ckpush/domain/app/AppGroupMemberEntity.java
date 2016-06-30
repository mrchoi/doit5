package com.ckstack.ckpush.domain.app;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 7. 14..
 */
@Data
public class AppGroupMemberEntity {
    private long app_group_srl;
    private long member_srl;
    private int c_date;

    public void init() {
        this.app_group_srl = MDV.NUSE;
        this.member_srl = MDV.NUSE;
        this.c_date = MDV.NUSE;
    }
}

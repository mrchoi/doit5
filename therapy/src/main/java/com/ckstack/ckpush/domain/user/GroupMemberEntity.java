package com.ckstack.ckpush.domain.user;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 3. 27..
 */
@Data
public class GroupMemberEntity {
    private int group_srl;
    private long member_srl;
    private int c_date;

    public void init() {
        this.group_srl = MDV.NUSE;
        this.member_srl = MDV.NUSE;
        this.c_date = MDV.NUSE;
    }
}

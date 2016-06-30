package com.ckstack.ckpush.domain.app;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 8. 20..
 */
@Data
public class AppMemberEntity {
    private int app_srl;
    private long member_srl;
    private String nick_name;
    private int allow_mailing;
    private int allow_message;
    private int last_login_date;
    private int enabled;
    private int c_date;
    private int u_date;

    public void init() {
        this.app_srl = MDV.NUSE;
        this.member_srl = MDV.NUSE;
        this.nick_name = null;
        this.allow_mailing = MDV.NUSE;
        this.allow_message = MDV.NUSE;
        this.last_login_date = MDV.NUSE;
        this.enabled = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

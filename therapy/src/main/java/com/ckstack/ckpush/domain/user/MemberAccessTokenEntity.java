package com.ckstack.ckpush.domain.user;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 8. 19..
 */
@Data
public class MemberAccessTokenEntity {
    private long token_srl;
    private long member_srl;
    private int app_srl;
    private String access_token;
    private int token_expire;
    private String user_data;
    private int c_date;
    private int u_date;

    public void init() {
        this.token_srl = MDV.NUSE;
        this.member_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.access_token = null;
        this.token_expire = MDV.NUSE;
        this.user_data = null;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

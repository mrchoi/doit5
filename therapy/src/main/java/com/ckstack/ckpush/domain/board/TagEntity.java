package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 8. 7..
 */
@Data
public class TagEntity {
    private long tag_srl;
    private int app_srl;
    private long member_srl;
    private String tag_name;
    private int admin_tag;
    private int c_date;
    private int u_date;

    public void init() {
        this.tag_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.member_srl = MDV.NUSE;
        this.tag_name = null;
        this.admin_tag = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

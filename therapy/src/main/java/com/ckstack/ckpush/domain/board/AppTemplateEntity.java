package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 7. 23..
 */
@Data
public class AppTemplateEntity {
    private int app_srl;
    private int template_srl;
    private int c_date;

    public void init() {
        this.app_srl = MDV.NUSE;
        this.template_srl = MDV.NUSE;
        this.c_date = MDV.NUSE;
    }
}

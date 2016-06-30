package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class DiaryEntity {
    private long diary_srl;
    private long member_srl;
    private int application_group;
    private long application_srl;
    private String mind_diary;
    private int c_date;
    private int u_date;

    public void init() {
        this.diary_srl = MDV.NONE;
        this.member_srl = MDV.NONE;
        this.application_group = MDV.NONE;
        this.application_srl = MDV.NONE;
        this.mind_diary = null;
        this.c_date = MDV.NONE;
        this.u_date = MDV.NONE;
    }
}
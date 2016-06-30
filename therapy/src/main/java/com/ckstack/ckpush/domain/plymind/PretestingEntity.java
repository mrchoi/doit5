package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class PretestingEntity {
    private long application_srl;
    private long member_srl;

    private long question_srl;
    private String question;

    private long kind_srl;
    private String kind;

    private long item_srl;
    private String item;

    private String contents;

    private int c_date;

    public void init() {
        this.application_srl = MDV.NONE;
        this.member_srl = MDV.NONE;
        this.question_srl = MDV.NONE;
        this.question = null;
        this.kind_srl = MDV.NONE;
        this.kind = null;
        this.item_srl = MDV.NONE;
        this.item = null;
        this.contents = null;
        this.c_date = MDV.NONE;
    }
}

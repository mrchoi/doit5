package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class PlymindDocumentEntity {
    private long document_srl;
    private long application_srl;
    private int application_group;
    private String advisor_comment;
    private long checkup_file_srl;
    private long reply_file_srl;
    private long result_file_srl;
    private int checkup_date;
    private int reply_date;
    private int result_date;
    private int c_date;
    private String checkup_file_name;
    private String reply_file_name;
    private String result_file_name;
    private String checkup_file_url;
    private String reply_file_url;
    private String result_file_url;
    private long member_srl;

    public void init() {
        this.document_srl = MDV.NONE;
        this.application_srl = MDV.NONE;
        this.application_group = MDV.NONE;
        this.advisor_comment = null;
        this.checkup_file_srl = MDV.NONE;
        this.reply_file_srl = MDV.NONE;
        this.result_file_srl = MDV.NONE;
        this.checkup_date = MDV.NONE;
        this.reply_date = MDV.NONE;
        this.result_date = MDV.NONE;
        this.c_date = MDV.NONE;
        this.checkup_file_name = null;
        this.reply_file_name = null;
        this.result_file_name = null;
        this.checkup_file_url = null;
        this.reply_file_url = null;
        this.result_file_url = null;
        this.member_srl = MDV.NONE;
    }
}

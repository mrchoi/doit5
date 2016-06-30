package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by kodaji on 2016. 1. 29..
 */
@Data
public class DocumentCommentEntity {
    private long comment_srl;
    private long app_srl;
    private long board_srl;
    private long category_srl;
    private long document_srl;
    private long head_comment_srl;
    private long parent_comment_srl;
    private long comment_depth;
    private String comment_content;
    private long like_count;
    private long blame_count;
    private long child_comment_count;
    private long file_count;
    private int block;
    private long member_srl;
    private String user_id;
    private String user_name;
    private String nick_name;
    private String email_address;
    private String ipaddress;
    private int c_date;
    private int u_date;

    public void init() {
        comment_srl = MDV.NUSE;
        app_srl = MDV.NUSE;
        board_srl = MDV.NUSE;
        category_srl = MDV.NUSE;
        document_srl = MDV.NUSE;
        head_comment_srl = MDV.NUSE;
        parent_comment_srl = MDV.NUSE;
        comment_depth = MDV.NUSE;
        comment_content = null;
        like_count = MDV.NUSE;
        blame_count = MDV.NUSE;
        child_comment_count = MDV.NUSE;
        file_count = MDV.NUSE;
        block = MDV.NUSE;
        member_srl = MDV.NUSE;
        user_id = null;
        user_name = null;
        nick_name = null;
        email_address = null;
        ipaddress = null;
        c_date = MDV.NUSE;
        u_date = MDV.NUSE;
    }
}

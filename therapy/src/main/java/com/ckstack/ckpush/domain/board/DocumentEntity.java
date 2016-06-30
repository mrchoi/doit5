package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.validator.ByteSize;
import com.ckstack.ckpush.domain.mineral.DocumentAttachEntity;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 8. 5..
 */
@Data
public class DocumentEntity {
    private long document_srl;

    @NotNull(message = "{documententity.app_srl.notNull}")
    @Min(value = 1, message = "{documententity.app_srl.min}")
    private int app_srl;

    @NotNull(message = "{documententity.board_srl.notNull}")
    @Min(value = 1, message = "{documententity.board_srl.min}")
    private long board_srl;

    @NotNull(message = "{documententity.category_srl.notNull}")
    @Min(value = 1, message = "{documententity.category_srl.min}")
    private long category_srl;

    @NotNull(message = "{documententity.document_title.notNull}")
    @ByteSize(min = 1, max = 128, message = "{documententity.document_title.byteSize}")
    private String document_title;

    private String document_content;
    private long read_count;
    private long like_count;
    private long blame_count;
    private int comment_count;
    private int file_count;
    private String outer_link;

    @Min(value = -2, message = "{documententity.secret.min}")
    private int secret;

    @Min(value = 1, message = "{documententity.block.min}")
    @Max(value = 2, message = "{documententity.block.max}")
    private int block;

    @Min(value = 1, message = "{documententity.allow_comment.min}")
    @Max(value = 2, message = "{documententity.allow_comment.max}")
    private int allow_comment;

    @Min(value = 1, message = "{documententity.allow_notice.min}")
    @Max(value = 2, message = "{documententity.allow_notice.max}")
    private int allow_notice;

    private long list_order;
    private int template_srl;
    private String template_extra;
    private long member_srl;
    private String user_id;
    private String user_name;
    private String nick_name;
    private String email_address;
    private String document_password;
    private String ipaddress;
    private int c_date;
    private int u_date;

    // admin 을 위한 값
    private List<Map<String, Object>> admin_tags;
    private List<Map<String, Object>> user_tags;
    private List<Long> attach_file_srls;
    private List<DocumentAttachEntity> documentAttachEntities;


    public void init() {
        this.document_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.board_srl = MDV.NUSE;
        this.category_srl = MDV.NUSE;
        this.document_title = null;
        this.document_content = null;
        this.read_count = MDV.NUSE;
        this.like_count = MDV.NUSE;
        this.blame_count = MDV.NUSE;
        this.comment_count = MDV.NUSE;
        this.file_count = MDV.NUSE;
        this.outer_link = null;
        this.secret = MDV.NUSE;
        this.block = MDV.NUSE;
        this.allow_comment = MDV.NUSE;
        this.allow_notice = MDV.NUSE;
        this.list_order = MDV.NUSE;
        this.template_srl = MDV.NUSE;
        this.template_extra = null;
        this.member_srl = MDV.NUSE;
        this.user_id = null;
        this.user_name = null;
        this.nick_name = null;
        this.email_address = null;
        this.document_password = null;
        this.ipaddress = null;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

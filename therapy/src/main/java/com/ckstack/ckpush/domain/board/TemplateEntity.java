package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.validator.ByteSize;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by dhkim94 on 15. 7. 22..
 */
@Data
public class TemplateEntity {
    private int template_srl;

    @NotNull(message = "{templateentity.template_title.notNull}")
    @ByteSize(min = 1, max = 64, message = "{templateentity.template_title.byteSize}")
    private String template_title;

    @NotNull(message = "{templateentity.template_content.notNull}")
    @Size(min = 0, message = "{templateentity.template_content.size}")
    private String template_content;

    private String template_description;
    private int c_date;
    private int u_date;

    // admin 을 위한 변수
    private List<Integer> app_srls;
    private long app_count;

    public void init() {
        this.template_srl = MDV.NUSE;
        this.template_title = null;
        this.template_content = null;
        this.template_description = null;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

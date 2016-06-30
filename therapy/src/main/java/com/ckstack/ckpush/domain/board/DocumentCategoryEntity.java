package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.validator.ByteSize;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dhkim94 on 15. 7. 31..
 */
@Data
public class DocumentCategoryEntity {
    private long category_srl;

    @NotNull(message = "{documentcategoryentity.app_srl.notNull}")
    @Min(value = 1, message = "{documentcategoryentity.app_srl.min}")
    private int app_srl;

    @NotNull(message = "{documentcategoryentity.board_srl.notNull}")
    @Min(value = 1, message = "{documentcategoryentity.board_srl.min}")
    private long board_srl;

    @NotNull(message = "{documentcategoryentity.category_name.notNull}")
    @Size(min = 2, max = 64, message = "{documentcategoryentity.category_name.size}")
    @ByteSize(min = 2, max = 64, message = "{documentcategoryentity.category_name.byteSize}")
    private String category_name;

    @Size(min = 0, max = 256, message = "{documentcategoryentity.category_description.size}")
    @ByteSize(min = 0, max = 256, message = "{documentcategoryentity.category_description.byteSize}")
    private String category_description;

    @Min(value = 1, message = "{documentcategoryentity.category_type.min}")
    @Max(value = 2, message = "{documentcategoryentity.category_type.max}")
    private int category_type;

    @Min(value = 1, message = "{documentcategoryentity.enabled.min}")
    @Max(value = 2, message = "{documentcategoryentity.enabled.max}")
    private int enabled;

    @Min(value = 1, message = "{documentcategoryentity.open_type.min}")
    @Max(value = 2, message = "{documentcategoryentity.open_type.max}")
    private int open_type;

    private int c_date;
    private int u_date;

    // admin을 위해서
    private long document_count;

    public void init() {
        this.category_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.board_srl = MDV.NUSE;
        this.category_name = null;
        this.category_description = null;
        this.category_type = MDV.NUSE;
        this.enabled = MDV.NUSE;
        this.open_type = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

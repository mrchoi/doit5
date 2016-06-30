package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.validator.ByteSize;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dhkim94 on 15. 7. 29..
 */
@Data
public class DocumentBoardEntity {
    private long board_srl;

    @NotNull(message = "{documentboardentity.app_srl.notNull}")
    @Min(value = 1, message = "{documentboardentity.app_srl.min}")
    private int app_srl;

    @NotNull(message = "{documentboardentity.board_name.notNull}")
    @Size(min = 2, max = 64, message = "{documentboardentity.board_name.size}")
    @ByteSize(min = 2, max = 64, message = "{documentboardentity.board_name.byteSize}")
    private String board_name;

    @Size(min = 0, max = 256, message = "{documentboardentity.board_description.size}")
    @ByteSize(min = 0, max = 256, message = "{documentboardentity.board_description.byteSize}")
    private String board_description;

    @Min(value = 1, message = "{documentboardentity.enabled.min}")
    @Max(value = 2, message = "{documentboardentity.enabled.max}")
    private int enabled;

    @Min(value = 1, message = "{documentboardentity.open_type.min}")
    @Max(value = 2, message = "{documentboardentity.open_type.max}")
    private int open_type;

    private int c_date;
    private int u_date;

    public void init() {
        this.board_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.board_name = null;
        this.board_description = null;
        this.enabled = MDV.NUSE;
        this.open_type = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

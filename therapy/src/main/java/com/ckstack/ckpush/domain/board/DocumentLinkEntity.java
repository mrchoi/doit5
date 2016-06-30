package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by dhkim94 on 15. 8. 24..
 */
@Data
public class DocumentLinkEntity {
    private long document_link_srl;

    // app_srl은 document_srl에서 구하기 때문에 값을 넣지 않아도 된다.
    private int app_srl;

    @NotNull(message = "{documentlinkentity.board_srl.notNull}")
    @Min(value = 1, message = "{documentlinkentity.board_srl.min}")
    private long board_srl;

    @NotNull(message = "{documentlinkentity.category_srl.notNull}")
    @Min(value = 1, message = "{documentlinkentity.category_srl.min}")
    private long category_srl;

    @NotNull(message = "{documentlinkentity.document_srl.notNull}")
    @Min(value = 1, message = "{documentlinkentity.document_srl.min}")
    private long document_srl;

    private long list_order;
    private int c_date;
    private DocumentEntity documentEntity;

    public void init() {
        this.document_link_srl = MDV.NUSE;
        this.app_srl = MDV.NUSE;
        this.board_srl = MDV.NUSE;
        this.category_srl = MDV.NUSE;
        this.document_srl = MDV.NUSE;
        this.list_order = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.documentEntity = null;
    }
}

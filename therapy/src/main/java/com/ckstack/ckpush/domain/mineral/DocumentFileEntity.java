package com.ckstack.ckpush.domain.mineral;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

import java.util.List;

/**
 * Created by dhkim94 on 15. 8. 10..
 */
@Data
public class DocumentFileEntity {
    private long document_srl;
    private long file_srl;
    private int c_date;
    private List<DocumentAttachEntity> documentAttachEntities;

    public void init() {
        this.document_srl = MDV.NUSE;
        this.file_srl = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.documentAttachEntities = null;
    }
}

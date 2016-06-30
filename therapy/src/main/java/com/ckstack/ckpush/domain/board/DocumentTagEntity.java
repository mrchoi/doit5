package com.ckstack.ckpush.domain.board;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

import java.util.List;

/**
 * Created by dhkim94 on 15. 8. 7..
 */
@Data
public class DocumentTagEntity {
    private long document_srl;
    private long tag_srl;
    private int c_date;
    private DocumentEntity documentEntity;
    private DocumentLinkEntity documentLinkEntity;
    private long document_link_srl;

    public void init() {
        this.document_srl = MDV.NUSE;
        this.document_link_srl = MDV.NUSE;
        this.tag_srl = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.documentEntity = null;
        this.documentLinkEntity = null;
    }
}

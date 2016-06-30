package com.ckstack.ckpush.domain.mineral;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

import java.util.List;

/**
 * Created by dhkim94 on 15. 4. 29..
 */
@Data
public class PushMessagePicEntity {
    private long push_srl;
    private long file_srl;
    private int c_date;
    private List<FileEntity> fileEntities;

    public void init() {
        this.push_srl = MDV.NUSE;
        this.file_srl = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.fileEntities = null;
    }
}

package com.ckstack.ckpush.domain.mineral;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

import java.util.List;

/**
 * Created by dhkim94 on 15. 4. 3..
 */
@Data
public class MemberPicEntity {
    private long member_srl;
    private long file_srl;
    private int c_date;
    private List<FileEntity> fileEntities;

    public void init() {
        this.member_srl = MDV.NUSE;
        this.file_srl = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.fileEntities = null;
    }
}

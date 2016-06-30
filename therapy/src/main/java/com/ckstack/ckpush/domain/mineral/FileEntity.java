package com.ckstack.ckpush.domain.mineral;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 4. 1..
 */
@Data
public class FileEntity {
    private long file_srl;
    private String orig_name;
    private long file_size;
    private String file_path;
    private String file_url;
    private int width;
    private int height;
    private String thumb_path;
    private String thumb_url;
    private int thumb_width;
    private int thumb_height;
    private String ipaddress;
    private int deleted;
    private int c_date;
    private int u_date;

    public void init() {
        this.file_srl = MDV.NUSE;
        this.orig_name = null;
        this.file_size = MDV.NUSE;
        this.file_path = null;
        this.file_url = null;
        this.width = MDV.NUSE;
        this.height = MDV.NUSE;
        this.thumb_path = null;
        this.thumb_url = null;
        this.thumb_width = MDV.NUSE;
        this.thumb_height = MDV.NUSE;
        this.ipaddress = null;
        this.deleted = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

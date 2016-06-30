package com.ckstack.ckpush.domain.app;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.validator.ByteSize;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * Created by dhkim94 on 15. 3. 24..
 */
@Data
public class AppEntity {
    private int app_srl;

    @NotNull(message = "{appentity.app_id.notNull}")
    @Pattern(regexp = "^[0-9a-zA-Z]+(\\.[0-9a-zA-Z]+)*", message = "{appentity.app_id.pattern}")
    @Size(min = 1, max = 128, message = "{appentity.app_id.size}")
    private String app_id;

    @NotNull(message = "{appentity.app_name.notNull}")
    @Size(min = 1, max = 64, message = "{appentity.app_name.size}")
    @ByteSize(min = 1, max = 64, message = "{appentity.app_name.byteSize}")
    private String app_name;

    @NotNull(message = "{appentity.app_version.notNull}")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+){0,2}", message = "{appentity.app_version.pattern}")
    @Size(min = 1, max = 16, message = "{appentity.app_version.size}")
    private String app_version;

    @NotNull(message = "{appentity.api_key.notNull}")
    @Pattern(regexp = "[a-zA-Z0-9]{1,32}", message = "{appentity.api_key.pattern}")
    private String api_key;

    @NotNull(message = "{appentity.api_secret.notNull}")
    @Pattern(regexp = "[a-zA-Z0-9]{1,32}", message = "{appentity.api_secret.pattern}")
    private String api_secret;

    @Min(value = 1, message = "{appentity.enabled.min}")
    @Max(value = 3, message = "{appentity.enabled.max}")
    private int enabled;

    private long reg_terminal;
    private long reg_push_terminal;
    private long push_count;

    private int c_date;
    private int u_date;

    public void init() {
        this.app_srl = MDV.NUSE;
        this.app_id = null;
        this.app_name = null;
        this.app_version = null;
        this.api_key = null;
        this.api_secret = null;
        this.enabled = MDV.NUSE;
        this.reg_terminal = MDV.NUSE;
        this.reg_push_terminal = MDV.NUSE;
        this.push_count = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
    }
}

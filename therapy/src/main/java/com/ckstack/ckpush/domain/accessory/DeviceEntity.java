package com.ckstack.ckpush.domain.accessory;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.validator.ByteSize;
import com.ckstack.ckpush.domain.app.AppDeviceEntity;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dhkim94 on 15. 3. 16..
 */
@Data
public class DeviceEntity {
    private long device_srl;

    @NotNull(message = "{deviceentity.device_id.notNull}")
    @Size(min = 1, max = 64, message = "{deviceentity.device_id.size}")
    @ByteSize(min = 1, max = 64, message = "{deviceentity.device_id.byteSize}")
    private String device_id;

    @ByteSize(min = 0, max = 128, message = "{deviceentity.device_type.byteSize}")
    private String device_type;

    @Min(value = 1, message = "{deviceentity.device_class.min}")
    @Max(value = 2, message = "{deviceentity.device_class.max}")
    private int device_class;

    @ByteSize(min = 0, max = 32, message = "{deviceentity.os_version.byteSize}")
    private String os_version;

    @Size(min = 0, max = 16, message = "{deviceentity.mobile_phone_number.size}")
    private String mobile_phone_number;

    private int c_date;
    private int u_date;
    private AppDeviceEntity appDeviceEntity;

    // admin 사용 용도
    private int app_srl;
    private String push_id;

    public void init() {
        this.device_srl = MDV.NUSE;
        this.device_id = null;
        this.device_type = null;
        this.os_version = null;
        this.mobile_phone_number = null;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
        this.appDeviceEntity = null;

        // admin 사용 용도
        //this.app_srl = MDV.NUSE;
        //this.push_id = null;
    }

}

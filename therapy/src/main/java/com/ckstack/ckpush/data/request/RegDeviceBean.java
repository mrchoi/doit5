package com.ckstack.ckpush.data.request;

import com.ckstack.ckpush.common.validator.ByteSize;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dhkim94 on 15. 7. 10..
 */
@Data
public class RegDeviceBean {
    @NotNull(message = "{regdevicebean.device_id.notNull}")
    @Size(min = 32, max = 64, message = "{regdevicebean.device_id.size}")
    @ByteSize(min = 32, max = 64, message = "{regdevicebean.device_id.byteSize}")
    private String device_id;

    @ByteSize(min = 0, max = 128, message = "{regdevicebean.model.byteSize}")
    private String model;

    @ByteSize(min = 0, max = 32, message = "{regdevicebean.os_version.byteSize}")
    private String os_version;

    @Size(min = 0, max = 16, message = "{regdevicebean.mobile_phone_number.size}")
    private String mobile_phone_number;

    private String push_id;

    @Min(value = 1, message = "{regdevicebean.type.min}")
    @Max(value = 2, message = "{regdevicebean.type.max}")
    private int type;
}

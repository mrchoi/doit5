package com.ckstack.ckpush.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
@Getter
@Setter
@ToString
public class CustomException extends RuntimeException {
    private String code;
    private Map<String, String> reason;
    private int httpStatus;

    //public CustomException() {}

    public CustomException(String code) {
        this.code = code;
        this.reason = null;
        this.httpStatus = MDV.NONE;
    }

    public CustomException(String code, Map<String, String> reason) {
        this.code = code;
        this.reason = reason;
        this.httpStatus = MDV.NONE;
    }

    public CustomException(String code, int httpStatus, Map<String, String> reason) {
        this.code = code;
        this.reason = reason;
        this.httpStatus = httpStatus;
    }

    public CustomException(String code, int httpStatus) {
        this.code = code;
        this.reason = null;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return this.toString();
    }
}

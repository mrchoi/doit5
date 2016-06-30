package com.ckstack.ckpush.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by dhkim94 on 15. 4. 9..
 */
public class ByteSizeValidator implements ConstraintValidator<ByteSize, String> {
    private int min;
    private int max;

    @Override
    public void initialize(ByteSize constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            if(min == 0) return true;
            return false;
        }

        // URLEncoding 되어 들어 올 수 있으므로 decoding 시켜서 byte size를 구한다.
        String dec = "";
        try {
            dec = URLDecoder.decode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] bytes = dec.getBytes();

        return !(bytes.length > max || bytes.length < min);
    }
}

package com.ckstack.ckpush.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.UnsupportedEncodingException;

/**
 * Created by dhkim94 on 15. 8. 19..
 */
public class OneByteCharactorValidator implements ConstraintValidator<OneByteCharactor, String> {
    @Override
    public void initialize(OneByteCharactor constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null 체크가 아니기 때문에 null 인 경우는 성공으로 처리 한다.
        if(value == null) return true;

        int len = value.length();
        byte[] bytes = new byte[0];
        try {
            bytes = value.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            // 그냥 콘솔에 찍자
            e.printStackTrace();
        }

        return len == bytes.length;
    }
}

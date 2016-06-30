package com.ckstack.ckpush.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dhkim94 on 15. 4. 9..
 * Size annotation 은 글자 길이만 체크 하기 때문에 byte 길이 체크 하는
 * ByteSize annotation 을 추가 한다.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=ByteSizeValidator.class)
public @interface ByteSize {
    String message() default "{com.ckstack.ckpush.common.validator.ByteSize.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int min() default 0;
    int max() default Integer.MAX_VALUE;
}

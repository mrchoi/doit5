package com.ckstack.ckpush.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dhkim94 on 15. 8. 19..
 * user_id 등 one byte charactor만 사용되어야 하는 곳에 one byte charactor 인지 아닌지 체크
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=OneByteCharactorValidator.class)
public @interface OneByteCharactor {
    String message() default "{com.ckstack.ckpush.common.validator.OneByteCharactor.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.ckstack.ckpush.data.request;

import com.ckstack.ckpush.common.validator.OneByteCharactor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dhkim94 on 15. 8. 21..
 */
@Data
public class SignInBean {
    @NotNull(message = "{signinbean.user_id.notNull}")
    @Size(min = 4, max = 128, message = "{signinbean.user_id.size}")
    private String user_id;

    @NotNull(message = "{signinbean.user_password.notNull}")
    @Size(min = 4, max = 128, message = "{signinbean.user_password.size}")
    private String user_password;
}

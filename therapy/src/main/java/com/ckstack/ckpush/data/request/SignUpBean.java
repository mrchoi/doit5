package com.ckstack.ckpush.data.request;

import com.ckstack.ckpush.common.validator.ByteSize;
import com.ckstack.ckpush.common.validator.OneByteCharactor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by dhkim94 on 15. 8. 18..
 * api를 통한 가입시 사용되는 bean.
 * 가입시 request header 에 X-Api-Key, X-Api-Secret, X-Social-Type 세 개를 허용 해야 한다.
 *
 */
@Data
public class SignUpBean {
    @NotNull(message = "{signupbean.user_id.notNull}")
    @Size(min = 4, max = 128, message = "{signupbean.user_id.size}")
    @OneByteCharactor(message = "{signupbean.user_id.oneByteCharactor}")
    private String user_id;

    @NotNull(message = "{signupbean.user_password.notNull}")
    @Size(min = 4, max = 128, message = "{signupbean.user_password.size}")
    @OneByteCharactor(message = "{signupbean.user_password.oneByteCharactor}")
    private String user_password;

    @NotNull(message = "{signupbean.user_name.notNull}")
    @Size(min = 2, message = "{signupbean.user_name.size}")
    @ByteSize(min = 2, max = 64, message = "{signupbean.user_name.byteSize}")
    private String user_name;

    @ByteSize(min = 0, max = 64, message = "{signupbean.nick_name.byteSize}")
    private String nick_name;

    @Pattern(regexp = "^(|((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))$", message = "{signupbean.email_address.pattern}")
    private String email_address;

    private String mobile_phone_number;
    private long profile_file_srl;
    private String profile_file_url;

    @ByteSize(min = 0, max = 64, message = "{signupbean.device_id.byteSize}")
    private String device_id;

    @ByteSize(min = 0, max = 128, message = "{signupbean.model.byteSize}")
    private String model;

    @ByteSize(min = 0, max = 32, message = "{signupbean.os_version.byteSize}")
    private String os_version;

    private String push_id;
    private int type;
}

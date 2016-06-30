package com.ckstack.ckpush.domain.user;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.validator.ByteSize;
import com.ckstack.ckpush.domain.mineral.FileEntity;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 11..
 */
@Data
public class MemberEntity {
    private long member_srl;

    @NotNull(message = "{memberentity.user_id.notNull}")
    @Size(min = 3, max = 128, message = "{memberentity.user_id.size}")
    //@Pattern(regexp = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))$", message = "{memberentity.user_id.pattern}")
    private String user_id;

    // empty string 을 허용하는 email 주소 형태
    @Pattern(regexp = "^(|((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))$", message = "{memberentity.email_address.pattern}")
    private String email_address;

    @NotNull(message = "{memberentity.user_password.notNull}")
    @Size(min = 3, max = 64, message = "{memberentity.user_password.size}")
    private String user_password;

    private String old_password;

    @NotNull(message = "{memberentity.user_name.notNull}")
    @ByteSize(min = 1, max = 64, message = "{memberentity.user_name.byteSize}")
    private String user_name;

    @ByteSize(min = 0, max = 64, message = "{memberentity.nick_name.byteSize}")
    private String nick_name;

    @Size(min = 0, max = 16, message = "{memberentity.mobile_phone_number.size}")
    private String mobile_phone_number;

    @Min(value = 1, message = "{memberentity.allow_mailing.min}")
    @Max(value = 2, message = "{memberentity.allow_mailing.max}")
    private int allow_mailing;

    @Min(value = 1, message = "{memberentity.allow_message.min}")
    @Max(value = 2, message = "{memberentity.allow_message.max}")
    private int allow_message;

    @ByteSize(min = 0, max = 256, message = "{memberentity.description.byteSize}")
    private String description;

    private int last_login_date;
    private int change_password_date;

    @Min(value = 1, message = "{memberentity.enabled.min}")
    @Max(value = 3, message = "{memberentity.enabled.max}")
    private int enabled;

    @Min(value = 1, message = "{memberentity.email_confirm.min}")
    @Max(value = 2, message = "{memberentity.email_confirm.max}")
    private int email_confirm;

    private int sign_out;
    private int c_date;
    private int u_date;
    private MemberExtraEntity memberExtraEntity;
    // 유저 프로필 이미지는 하나만 들어가는 경우가 많지만 구조상으로 여러장을 설정 할 수 있기 때문에
    // 기능은 오픈 해 둔다.
    private List<FileEntity> fileEntities;

    // admin 사용 용도
    private int account_type;
    private int app_account_type;
    private String app_srl;

    // 상담사 사용용도
    private String certificate;
    private String kakao_id;
    private int class_srl;
    private int domain_srl;
    private String self_introduction;

    //사용자 회원가입 시 단체 등록용도
    private int group_gubun;
    private long user_group_srl;

    public void init() {
        this.member_srl = MDV.NUSE;
        this.user_id = null;
        this.email_address = null;
        this.user_password = null;
        this.old_password = null;
        this.user_name = null;
        this.nick_name = null;
        this.mobile_phone_number = null;
        this.allow_mailing = MDV.NUSE;
        this.allow_message = MDV.NUSE;
        this.description = null;
        this.last_login_date = MDV.NUSE;
        this.change_password_date = MDV.NUSE;
        this.enabled = MDV.NUSE;
        this.email_confirm = MDV.NUSE;
        this.sign_out = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;
        this.memberExtraEntity = null;
        this.fileEntities = null;

        this.certificate = null;
        this.kakao_id = null;
        this.class_srl = MDV.NUSE;
        this.domain_srl = MDV.NUSE;
        this.self_introduction = null;

        this.group_gubun = MDV.NUSE;
        this.user_group_srl = MDV.NUSE;
    }
}

package com.ckstack.ckpush.data.cache;

import lombok.Data;

/**
 * Created by dhkim94 on 15. 8. 20..
 */
@Data
public class AccessTokenExtra {
    long member_srl;
    int app_srl;
    String access_token;
    String user_id;
    String email_address;
    String user_name;
    String nick_name;
    int allow_mailing;
    int allow_message;
    int last_login_date;
    int token_expire;
    int enabled;
}

package com.ckstack.ckpush.domain.user;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 3. 24..
 */
@Data
public class MemberExtraEntity {
    private long member_srl;
    private int social_type;
    private String social_id;
    private long login_count;
    private int serial_login_count;
    private int allow_temperature;
    private int allow_distance;
    private int allow_battery;
    private int allow_call;
    private int c_date;
    private int u_date;

    private int group_gubun;
    private String birthday;
    private String job;
    private int marriage;
    private String children;
    private int religion;
    private String religion_name;
    private int disability;
    private String disability_type;
    private String disability_rate;
    private int gender;
    private String certificate;
    private String kakao_id;
    private String self_introduction;
    private int advisor_class;
    private long user_group_srl;
    private long academic_srl;
    private long class_srl;
    private int domain_srl;


    public void init() {
        this.member_srl = MDV.NUSE;
        this.social_type = MDV.NUSE;
        this.social_id = null;
        this.login_count = MDV.NUSE;
        this.serial_login_count = MDV.NUSE;
        this.allow_temperature = MDV.NUSE;
        this.allow_distance = MDV.NUSE;
        this.allow_battery = MDV.NUSE;
        this.allow_call = MDV.NUSE;
        this.c_date = MDV.NUSE;
        this.u_date = MDV.NUSE;

        this.group_gubun = MDV.NUSE;
        this.birthday = null;
        this.job = null;
        this.marriage = MDV.NUSE;
        this.children = null;
        this.religion = MDV.NUSE;
        this.religion_name = null;
        this.disability = MDV.NUSE;
        this.disability_type = null;
        this.disability_rate = null;
        this.gender = MDV.NUSE;
        this.certificate = null;
        this.kakao_id = null;
        this.self_introduction = null;
        this.advisor_class = MDV.NUSE;
        this.user_group_srl = MDV.NUSE;
        this.academic_srl = MDV.NUSE;
        this.class_srl = MDV.NUSE;
        this.domain_srl = MDV.NUSE;
    }
}

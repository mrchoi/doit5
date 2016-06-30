package com.ckstack.ckpush.domain.user;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
@Data
public class ZipCodeEntity {
    private int zipcode_srl;
    private String zipcode;
    private String sido;
    private String sigungu;
    private String dong;
    private String doromyung;
    private String konmulbunho_bonbun;
    private String konmulbunho_bubun;
    private String konmul_myung;
    private String bubjungdong;
    private String ri;
    private String hangjungdong;
    private String jibunbonbun;
    private String jibunbubun;

    private String full;

    public void init() {
        this.zipcode_srl = MDV.NUSE;
        this.zipcode = null;
        this.sido = null;
        this.sigungu = null;
        this.dong = null;
        this.doromyung = null;
        this.konmulbunho_bonbun = null;
        this.konmulbunho_bubun = null;
        this.konmul_myung = null;
        this.bubjungdong = null;
        this.ri = null;
        this.hangjungdong = null;
        this.jibunbonbun = null;
        this.jibunbubun = null;
        this.full = null;
    }
}

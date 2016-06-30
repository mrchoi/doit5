package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class AdvicedomainEntity {
    private long advicedoamin_srl;
    private int advicedoamin_type;
    private long member_srl;
    private int advice_domain;

    public void init() {
        this.advicedoamin_srl = MDV.NONE;
        this.advicedoamin_type = MDV.NONE;
        this.member_srl = MDV.NONE;
        this.advice_domain = MDV.NONE;
    }
}

package com.ckstack.ckpush.domain.plymind;

import com.ckstack.ckpush.common.MDV;
import lombok.Data;

/**
 * Created by wypark on 16. 1. 19.
 */
@Data
public class ProductEntity {

    private long contents_srl;
    private int kind;
    private String title;
    private String description;
    private String contents;
    private String advice_time;
    private String method;

    private long product_srl;
    private int advice_type;
    private int advice_period;
    private int advice_number;
    private int advice_price;
    private String benefit;

    public void init() {
        this.contents_srl = MDV.NONE;
        this.kind = MDV.NONE;
        this.title = null;
        this.description = null;
        this.contents = null;
        this.advice_time = null;
        this.method = null;

        this.product_srl = MDV.NONE;
        this.advice_type = MDV.NONE;
        this.advice_period = MDV.NONE;
        this.advice_number = MDV.NONE;
        this.advice_price = MDV.NONE;
    }
}

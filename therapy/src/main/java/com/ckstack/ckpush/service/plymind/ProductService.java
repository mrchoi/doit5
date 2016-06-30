package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.plymind.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface ProductService {
    List<ProductEntity> getContentsList(int kind_srl);

    List<ProductEntity> getProductList(int kind_srl);

    ProductEntity getProductInfoBySrl(long product_srl);

    int add(long member_srl, ApplicationEntity applicationEntity);

    boolean userInfoCheck(long member_srl);

    boolean pretestingCheck(long member_srl);

    List<AdvicedomainEntity> getAdvicedomainList(long member_srl, int advicedomain_type, int advice_domain);
}


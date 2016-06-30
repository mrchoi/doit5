package com.ckstack.ckpush.dao.plymind;

import com.ckstack.ckpush.domain.plymind.AdvicedomainEntity;
import com.ckstack.ckpush.domain.plymind.PretestingEntity;
import com.ckstack.ckpush.domain.plymind.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wypark on 16. 1. 19.
 */
@Repository
@Transactional(value = "transactionManager")
public interface ProductDao {
    /**
     * 상품 리스트를 가져온다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<ProductEntity> getContentsList(@Param("kind_srl") int kind_srl);

    /**
     * 상품 리스트를 가져온다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<ProductEntity> getProductList(@Param("kind_srl") int kind_srl,
                                       @Param("title") String title);

    /**
     * 상품 정보를 가져온다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    ProductEntity getProductInfoBySrl(@Param("product_srl") long product_srl);

    /**
     * 상담사의 상담 영역을 조회한다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<AdvicedomainEntity> getAdvicedomainList(@Param("member_srl") long member_srl,
                                                 @Param("advicedomain_type") int advicedomain_type,
                                                 @Param("advice_domain") int advice_domain);
}

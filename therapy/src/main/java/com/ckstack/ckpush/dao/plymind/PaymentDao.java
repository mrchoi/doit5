package com.ckstack.ckpush.dao.plymind;

import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
import com.ckstack.ckpush.domain.plymind.PaymentEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
@Repository
@Transactional(value = "transactionManager")
public interface PaymentDao {
    /**
     * 결제 정보를 저장한다.
     *
     * @param paymentEntity insert data
     * @return insert row count
     */
    int add(PaymentEntity paymentEntity);

    /**
     * 해당 고객의 결제내역 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    long countPayment(@Param("member_srl") long member_srl,
                      @Param("application_group") int application_group,
                      @Param("payment_statues") List<Integer> payment_statues,
                      @Param("payment_name") String payment_name,
                      @Param("advisor_srls") List<Long> advisor_srls,
                      @Param("product_srls") List<Long> product_srls);

    /**
     * 해당 고객의 결제내역 리스트를 가져온다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<PaymentEntity> getPaymentList(@Param("member_srl") long member_srl,
                                       @Param("application_group") int application_group,
                                       @Param("payment_statues") List<Integer> payment_statues,
                                       @Param("payment_name") String payment_name,
                                       @Param("advisor_srls") List<Long> advisor_srls,
                                       @Param("product_srls") List<Long> product_srls,
                                       @Param("sort") Map<String, String> sort,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    /**
     * 관리자 사이트
     * 입금완료 처리를 한다.
     *
     * @param payment_srl update data
     * @return update row count
     */
    int payment(@Param("payment_srl") long payment_srl);

    /**
     * 환불요청을 한다.
     *
     * @param paymentEntity update data
     * @return insert row count
     */
    int refundReq(@Param("paymentEntity") PaymentEntity paymentEntity);

    /**
     * 관리자 사이트
     * 환불완료 처리를 한다.
     *
     * @param paymentEntity update data
     * @return insert row count
     */
    int refund(@Param("paymentEntity") PaymentEntity paymentEntity);

    /**
     * 결제 취소한다.
     *
     * @param payment_srl update data
     * @return insert row count
     */
    int cancel(@Param("payment_srl") long payment_srl);

    /**
     * 결제 정보를 가져온다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    PaymentEntity getPaymentInfoBySrl(@Param("payment_srl") int payment_srl);
}

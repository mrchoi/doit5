package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.plymind.PaymentEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface PaymentService {
    Map<String, Object> paymentAdd(long member_srl, PaymentEntity paymentEntity); // payment_status=0

    int paymentDeposit(PaymentEntity paymentEntity); // payment_status=1

    int paymentRefundReq(PaymentEntity paymentEntity); // payment_status=2

    int paymentRefund(PaymentEntity paymentEntity); // payment_status=3

    int paymentCancel(PaymentEntity paymentEntity); // payment_status=4

    PaymentEntity getPaymentInfoBySrl(int payment_srl);

    long countPayment(long member_srl, int application_group, List<Integer> payment_statues);

    long countSearchPayment(long member_srl, int application_group, List<Integer> payment_statues, Map<String, String> searchFilter);

    List<PaymentEntity> getPaymentList(long member_srl, int application_group, List<Integer> payment_statues, Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit);
}


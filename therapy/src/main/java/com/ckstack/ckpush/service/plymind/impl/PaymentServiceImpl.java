package com.ckstack.ckpush.service.plymind.impl;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.plymind.ApplicationDao;
import com.ckstack.ckpush.dao.plymind.AppointmentDao;
import com.ckstack.ckpush.dao.plymind.PaymentDao;
import com.ckstack.ckpush.dao.plymind.ProductDao;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
import com.ckstack.ckpush.domain.plymind.AppointmentEntity;
import com.ckstack.ckpush.domain.plymind.PaymentEntity;
import com.ckstack.ckpush.domain.plymind.ProductEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.plymind.PaymentService;
import com.ckstack.ckpush.service.plymind.ProductService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
@Service
@Transactional(value = "transactionManager")
public class PaymentServiceImpl implements PaymentService {
    private final static Logger LOG = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Override
    public Map<String, Object> paymentAdd(long member_srl, PaymentEntity paymentEntity) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            /* 이미 저장된 결제 정보인지 확인해서 중복 결제 요청을 차단한다. START */
            List<PaymentEntity> paymentEntities = paymentDao.getPaymentList(member_srl, paymentEntity.getApplication_group(), null, null, null, null, null, MDV.NONE, MDV.NUSE);
            if(paymentEntities.size() > 0) {
                for (PaymentEntity getPaymentEntity : paymentEntities) {
                    paymentEntity = getPaymentEntity;
                    break;
                }

                resultMap.put("result_code", "2");
                resultMap.put("payment_srl", paymentEntity.getPayment_srl());

                return resultMap;
            }
            /* 이미 저장된 결제 정보인지 확인해서 중복 결제 요청을 차단한다. END */

            int ltm = (int)(DateTime.now().getMillis() / 1000);

            paymentEntity.setMember_srl(member_srl);
            paymentEntity.setPayment_name(URLDecoder.decode(paymentEntity.getPayment_name(), "utf-8"));
            paymentEntity.setRefund_bank(URLDecoder.decode(paymentEntity.getRefund_bank(), "utf-8"));
            paymentEntity.setRefund_name(URLDecoder.decode(paymentEntity.getRefund_name(), "utf-8"));
            paymentEntity.setReporting_date(ltm);
            paymentEntity.setPayment_date(MDV.NUSE);
            paymentEntity.setRefund_req_date(MDV.NUSE);
            paymentEntity.setRefund_date(MDV.NUSE);

            int result = paymentDao.add(paymentEntity);
        } catch (Exception e) {
            LOG.debug(e.getMessage());

            resultMap.put("result_code", "0");
            resultMap.put("payment_srl", paymentEntity.getPayment_srl());

            return resultMap;
        }

        resultMap.put("result_code", "1");
        resultMap.put("payment_srl", paymentEntity.getPayment_srl());

        return resultMap;
    }

    /**
     * 관리자 사이트
     * 입금완료 처리를 한다.
     */
    @Override
    public int paymentDeposit(PaymentEntity paymentEntity) {
        int result = 0;

        try {
            result = paymentDao.payment(paymentEntity.getPayment_srl());

            result = applicationDao.statusReady(MDV.NUSE, paymentEntity.getApplication_group());

            //if(paymentEntity.getAppointment_srl() > 0) {
            //   result = appointmentDao.complete(paymentEntity.getAppointment_srl());
            //}
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return 0;
        }

        return result;
    }

    /**
     * 결제 내역에 대해 환불 요청을 한다..
     */
    @Override
    public int paymentRefundReq(PaymentEntity paymentEntity) {
        int result = 0;

        try {
            List<ApplicationEntity> applicationEntities = applicationDao.getApplicationListByGroup(paymentEntity.getMember_srl(), paymentEntity.getApplication_group(), null, MDV.NUSE, MDV.NUSE);

            List<Long> application_srls = new ArrayList<Long>();
            for(ApplicationEntity applicationEntity : applicationEntities) {
                application_srls.add(applicationEntity.getApplication_srl());
            }

            paymentEntity.setRefund_bank(URLDecoder.decode(paymentEntity.getRefund_bank(), "utf-8"));
            paymentEntity.setRefund_name(URLDecoder.decode(paymentEntity.getRefund_name(), "utf-8"));

            result = paymentDao.refundReq(paymentEntity);

            result = applicationDao.cancel(paymentEntity.getApplication_group());

            result = appointmentDao.cancel(0, application_srls);
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return 0;
        }

        return result;
    }

    /**
     * 결제 내역에 대해 환불 요청을 한다..
     */
    @Override
    public int paymentRefund(PaymentEntity paymentEntity) {
        int result = paymentDao.refund(paymentEntity);

        return result;
    }

    /**
     * 결제 내역에 대해 취소를 한다..
     */
    @Override
    public int paymentCancel(PaymentEntity paymentEntity) {
        int result = 0;

        try {
            List<ApplicationEntity> applicationEntities = applicationDao.getApplicationListByGroup(MDV.NUSE, paymentEntity.getApplication_group(), null, MDV.NUSE, MDV.NUSE);

            List<Long> application_srls = new ArrayList<Long>();
            for(ApplicationEntity applicationEntity : applicationEntities) {
                application_srls.add(applicationEntity.getApplication_srl());
            }

            result = paymentDao.cancel(paymentEntity.getPayment_srl());

            result = applicationDao.cancel(paymentEntity.getApplication_group());

            result = appointmentDao.cancel(0, application_srls);
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return 0;
        }

        return result;
    }

    /**
     * 상품 정보 가져온다.
     */
    @Transactional(readOnly = true)
    @Override
    public PaymentEntity getPaymentInfoBySrl(int payment_srl) {
        PaymentEntity paymentEntitity = paymentDao.getPaymentInfoBySrl(payment_srl);

        return paymentEntitity;
    }


    /**
     * 해당 고객의 결제내역 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countPayment(long member_srl, int application_group, List<Integer> payment_statues) {
        long paymentCount = paymentDao.countPayment(member_srl, application_group, payment_statues, null, null, null);

        return paymentCount;
    }

    /**
     * 해당 고객의 결제내역 중 검색 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countSearchPayment(long member_srl, int application_group, List<Integer> payment_statues, Map<String, String> searchFilter) {
        String title = null;
        String payment_name = null;
        String advisor_name = null;

        if(searchFilter.containsKey("payment_name")) payment_name = searchFilter.get("payment_name");
        if(searchFilter.containsKey("advisor_name")) advisor_name = searchFilter.get("advisor_name");
        if(searchFilter.containsKey("title")) title = searchFilter.get("title");

        List<Long> advisor_srls = new ArrayList<Long>();
        if(advisor_name != null && advisor_name != "") {
            List<MemberEntity> memberEntities = memberDao.getMemberListByGroup(MDV.NUSE, null, null, null, advisor_name, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            for(MemberEntity memberEntity : memberEntities) {
                advisor_srls.add(memberEntity.getMember_srl());
            }

            if(memberEntities.size() == 0) {
                advisor_srls.add((long)MDV.NUSE);
            }
        }

        List<Long> product_srls = new ArrayList<Long>();
        if(title != null && title != "") {
            List<ProductEntity> productEntities = productDao.getProductList(MDV.NUSE, title);

            for(ProductEntity productEntity : productEntities) {
                product_srls.add(productEntity.getProduct_srl());
            }

            if(productEntities.size() == 0) {
                product_srls.add((long)MDV.NUSE);
            }
        }

        long paymentSearchCount = paymentDao.countPayment(member_srl, application_group, payment_statues, payment_name, advisor_srls, product_srls);

        return paymentSearchCount;
    }

    /**
     * 해당 고객의 결제내역을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PaymentEntity> getPaymentList(long member_srl, int application_group, List<Integer> payment_statues, Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit) {
        if(searchFilter == null) {
            return paymentDao.getPaymentList(member_srl, application_group, payment_statues, null, null, null, sortValue, offset, limit);
        }

        String title = null;
        String payment_name = null;
        String advisor_name = null;

        if(searchFilter.containsKey("payment_name")) payment_name = searchFilter.get("payment_name");
        if(searchFilter.containsKey("advisor_name")) advisor_name = searchFilter.get("advisor_name");
        if(searchFilter.containsKey("title")) title = searchFilter.get("title");

        List<Long> advisor_srls = new ArrayList<Long>();
        if(advisor_name != null && advisor_name != "") {
            List<MemberEntity> memberEntities = memberDao.getMemberListByGroup(MDV.NUSE, null, null, null, advisor_name, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            for(MemberEntity memberEntity : memberEntities) {
                advisor_srls.add(memberEntity.getMember_srl());
            }

            if(memberEntities.size() == 0) {
                advisor_srls.add((long)MDV.NUSE);
            }
        }

        List<Long> product_srls = new ArrayList<Long>();
        if(title != null && title != "") {
            List<ProductEntity> productEntities = productDao.getProductList(MDV.NUSE, title);

            for(ProductEntity productEntity : productEntities) {
                product_srls.add(productEntity.getProduct_srl());
            }

            if(productEntities.size() == 0) {
                product_srls.add((long)MDV.NUSE);
            }
        }

        Map<String, String> sortMap = new HashMap<String, String>();
        if(sortValue.containsKey("title")) {
            sortMap.put("product_srl", sortValue.get("title"));
        } else if(sortValue.containsKey("advisor_name")) {
            sortMap.put("advisor_srl", sortValue.get("advisor_name"));
        } else {
            sortMap = sortValue;
        }

        List<PaymentEntity> paymentEntities = paymentDao.getPaymentList(member_srl, application_group, payment_statues, payment_name, advisor_srls, product_srls, sortMap, offset, limit);

        return paymentEntities;
    }
}

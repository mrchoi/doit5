package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.plymind.ApplicationEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface PresentService {
    /**
     * 상담사 상담현황을 조회한다.
     * @param advisor_srl 사용자 시리얼 번호
     *
     * @return 조회된 데이터
     */
    Map<String, Object> getAdvisorPresent(long advisor_srl);
}
package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.plymind.PretestingEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface PretestingService {
    List<PretestingEntity> getPretestingList();

    List<PretestingEntity> getQuestionList();

    List<PretestingEntity> getKindList();

    List<PretestingEntity> getItemList();

    int add(long member_srl, Map<String, Object> requestBody);

    List<PretestingEntity> getPretestingInfo(long member_srl);

    Map<String, Object> getPretestingResult(long member_srl);
}


package com.ckstack.ckpush.service.accessory;

import java.util.Map;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
public interface ServiceHistoryService {
    long count(String startMonth, int periodMonth, int historyType, long memberSrl,
               long maxHistorySrl);
    void addCreateAccountHistory(long tryMemberSrl, long resultMemberSrl,
                                 String tryUserId, String resultUserId, String ip);
    void addCreateOrDeleteAppHistory(long tryMemberSrl, String tryUserId,
                                     Map<Integer, String> app, String ip, boolean isCreate);

}

package com.ckstack.ckpush.dao.plymind;

import com.ckstack.ckpush.domain.plymind.PushEntity;
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
public interface SentenceDao {
    /**
     * 너나들이 총 갯수를 조회한다.
     *
     * @return 조회된 총 갯수
     */
    @Transactional(readOnly = true)
    long countSentence(@Param("push_text") String push_text);

    /**
     * 너나들이 목록을 조회한다.
     * @param push_text 너나들이 내용
     * @param sort 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 데이터
     */
    @Transactional(readOnly = true)
    List<PushEntity> getSentenceList(@Param("push_text") String push_text,
                                     @Param("sort") Map<String, String> sort,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);
}

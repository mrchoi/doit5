package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.plymind.PushEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface SentenceService {
    /**
     * 너마들이를 저장 한다.
     *
     * @param documentEntity 저장할 너나들이 정보
     */
    void addDocument(DocumentEntity documentEntity);

    /**
     * 너나들이 총 갯수를 조회한다.
     *
     * @return 조회된 총 갯수
     */
    long countSentence();

    /**
     * 검색된 너나들이 총 갯수를 조회한다.
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     *
     * @return 조회된 총 갯수
     */
    long countSearchSentence(Map<String, String> searchFilter);

    /**
     * 너나들이 목록을 조회한다.
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     * @param sortValue 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 데이터
     */
    List<PushEntity> getSentenceList(Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit);
}


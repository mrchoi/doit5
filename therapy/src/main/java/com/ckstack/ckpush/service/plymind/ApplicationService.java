package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.plymind.ApplicationEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface ApplicationService {
    /**
     * 상담완료 처리를 한다.
     * @param application_srl 상담 시리얼 번호
     *
     * @return update row count
     */
    int statusReady(long application_srl, int application_group);

    /**
     * 상담 진행중 처리를 한다.
     * @param application_group 상담 그룹 번호
     *
     * @return update row count
     */
    int statusProgress(long application_srl, int application_group);

    /**
     * 상담완료 처리를 한다.
     * @param application_srl 상담 시리얼 번호
     *
     * @return update row count
     */
    int statusComplete(long application_srl, int application_group);

    /**
     * 싸이케어테라피 / 커플케어테라피 / 패밀리케어테라피 스마트기기 수령확인 처리
     * @param application_group 상담 그룹 번호
     *
     * @return update row count
     */
    int receiveCheck(int application_group);

    /**
     * 상담신청 내역 총 갯수를 조회한다.
     * 고객 시리얼 번호/상담사 시리얼 번호가 0보다 작으면 전체 예약 내역 갯수 조회
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param contents_srls 컨텐츠 시리얼 번호 리스트
     *
     * @return 조회된 총 갯수
     */
    long countApplicationGroup(long member_srl, long advisor_srl, List<Integer> application_statues, List<Long> contents_srls);

    /**
     * 상담 신청 내역 중 검색되어진 총 갯수를 조회한다.
     * 고객 시리얼 번호/상담사 시리얼 번호가 0보다 작으면 검색되어진 전체 예약 내역 갯수 조회
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param contents_srls 컨텐츠 시리얼 번호 리스트
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     *
     * @return 조회된 총 갯수
     */
    long countSearchApplicationGroup(long member_srl, long advisor_srl, List<Integer> application_statues, List<Long> contents_srls, Map<String, String> searchFilter);

    /**
     * 상담 목록을 조회한다.
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param application_statues 상담 진행 상태
     * @param contents_srls 컨텐츠 시리얼 번호 리스트
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     * @param sortValue 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 데이터
     */
    List<ApplicationEntity> getApplicationGroupList(long member_srl,
                                                    long advisor_srl,
                                                    List<Integer> application_statues,
                                                    List<Long> contents_srls,
                                                    Map<String, String> searchFilter,
                                                    Map<String, String> sortValue,
                                                    int offset,
                                                    int limit);
    /**
     * 상담신청 내역 총 갯수를 조회한다.
     * 고객 시리얼 번호/상담사 시리얼 번호가 0보다 작으면 전체 예약 내역 갯수 조회
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param contents_srls 컨텐츠 시리얼 번호 리스트
     *
     * @return 조회된 총 갯수
     */
    long countApplicationGroupComplete(long member_srl, long advisor_srl, List<Integer> application_statues, List<Long> contents_srls);

    /**
     * 상담 신청 내역 중 검색되어진 총 갯수를 조회한다.
     * 고객 시리얼 번호/상담사 시리얼 번호가 0보다 작으면 검색되어진 전체 예약 내역 갯수 조회
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param contents_srls 컨텐츠 시리얼 번호 리스트
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     *
     * @return 조회된 총 갯수
     */
    long countSearchApplicationGroupComplete(long member_srl, long advisor_srl, List<Integer> application_statues, List<Long> contents_srls, Map<String, String> searchFilter);

    /**
     * 상담 시리얼 번호로 상담 정보를 조회한다.
     *
     * @return 조회된 데이터
     */
    ApplicationEntity getApplication(long application_srl);

    /**
     * 상태 값을 변경한다. 0: 접수, 1:준비중, 2:진행중, 3:완료, 4취소
     *
     * @return 조회된 데이터
     */
    void statusModify(long application_srl, int application_group, int status);

    /**
     * 상담 신청 내역 중 application_group 조건으로 총 갯수를 조회한다.
     * @param application_group 상담 그룹 번호
     *
     * @return 조회된 총 갯수
     */
    long countApplicationByGroup(int application_group);

    /**
     * 상담 신청 내역 중 application_group 조건으로 회차 정보를 조회한다.
     * @param member_srl 사용자 시리얼 번호
     * @param application_group 상담 그룹 번호
     * @param sortValue 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 총 갯수
     */
    List<ApplicationEntity> getApplicationListByGroup(long member_srl,
                                                      int application_group,
                                                      Map<String, String> sortValue,
                                                      int offset,
                                                      int limit);

    List<ApplicationEntity> getApplicationList(long member_srl,
                                               long advisor_srl,
                                               List<Integer> application_statues,
                                               List<Long> contents_srls,
                                               Map<String, String> searchFilter,
                                               Map<String, String> sortValue,
                                               int offset,
                                               int limit);

    List<ApplicationEntity> getApplicationMember(int application_group);
}


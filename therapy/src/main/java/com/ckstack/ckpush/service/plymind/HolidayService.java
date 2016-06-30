package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
import com.ckstack.ckpush.domain.plymind.HolidayEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface HolidayService {
    /**
     * 휴일을 등록한다.
     * @param holidayEntity 등록될 휴일 정보
     *
     * @return insert row count
     */
    int addHoliday(HolidayEntity holidayEntity);

    /**
     * 등록된 휴일 총 갯수를 조회한다.
     *
     * @return 조회된 총 갯수
     */
    int countHoliday(Map<String, String> searchFilter);

    /**
     * 등록된 휴일 총 갯수를 조회한다.
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     * @Param member_srls 상담사 srl.
     * @param sortValue 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 총 갯수
     */
    List<HolidayEntity> getHolidayList(Map<String, String> searchFilter,
                                       Map<String, String> sortValue,
                                       int offset,
                                       int limit);

    /**
     * 등록된 상담사 일정 갯수를 조회한다.
     * @param advisor_srl 상담사 시리얼 넘버
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     * @return 조회된 총 갯수
     */
    int countAdvisorHoliday(long advisor_srl,
                            Map<String, String> searchFilter);

    /**
     * 등록된 상담사 일정 목록을 조회한다.
     * @param advisor_srl 상담사 시리얼 넘버.
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     * @param sortValue 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 총 갯수
     */
    List<HolidayEntity> getAdvisorHolidayList(long advisor_srl,
                                              Map<String, String> searchFilter,
                                              Map<String, String> sortValue,
                                              int offset,
                                              int limit);

    /**
     * 휴일을 삭제 한다.
     *
     * @param holiday_srls 삭제할 휴일의 시리얼 넘버 리스트
     */
    void deleteHoliday(List<Long> holiday_srls);
}


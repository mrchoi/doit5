package com.ckstack.ckpush.dao.plymind;

import com.ckstack.ckpush.domain.plymind.AppointmentEntity;
import com.ckstack.ckpush.domain.plymind.HolidayEntity;
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
public interface HolidayDao {
    /**
     * 휴일 정보를 저장한다.
     *
     * @param holidayEntity insert data
     * @return insert row count
     */
    int addHoliday(HolidayEntity holidayEntity);

    /**
     * 휴일 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    int countHoliday(@Param("holiday_title") String holiday_title);

    /**
     * 휴일 목록을 가져온다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<HolidayEntity> getHolidayList(@Param("holiday_title") String holiday_title,
                                       @Param("sort") Map<String, String> sort,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    /**
     * 상담사 일정 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    int countAdvisorHoliday(@Param("advisor_srl") long advisor_srl,
                            @Param("advisor_srls") List<Long> advisor_srls,
                            @Param("holiday_title") String holiday_title);

    /**
     * 상담사 일정 목록을 가져온다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<HolidayEntity> getAdvisorHolidayList(@Param("advisor_srl") long advisor_srl,
                                              @Param("advisor_srls") List<Long> advisor_srls,
                                              @Param("holiday_title") String holiday_title,
                                              @Param("sort") Map<String, String> sort,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);

    int deleteHoliday(@Param("holiday_srls") List<Long> holiday_srls);
}

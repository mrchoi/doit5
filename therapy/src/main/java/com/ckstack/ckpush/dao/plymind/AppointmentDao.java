package com.ckstack.ckpush.dao.plymind;

import com.ckstack.ckpush.domain.plymind.AppointmentEntity;
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
public interface AppointmentDao {
    /**
     * 예약 정보를 저장한다.
     *
     * @param appointmententity insert data
     * @return insert row count
     */
    int add(AppointmentEntity appointmententity);

    /**
     * 해당 고객의 결제내역 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    long countAppointment(@Param("member_srl") long member_srl,
                          @Param("advisor_srl") long advisor_srl,
                          @Param("application_statues") List<Integer> application_statues,
                          @Param("user_name") String user_name,
                          @Param("advisor_srls") List<Long> advisor_srls,
                          @Param("product_srls") List<Long> product_srls);

    /**
     * 해당 고객의 결제내역 리스트를 가져온다.
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<AppointmentEntity> getAppointmentList(@Param("member_srl") long member_srl,
                                               @Param("advisor_srl") long advisor_srl,
                                               @Param("application_statues") List<Integer> application_statues,
                                               @Param("user_name") String user_name,
                                               @Param("advisor_srls") List<Long> advisor_srls,
                                               @Param("product_srls") List<Long> product_srls,
                                               @Param("sort") Map<String, String> sort,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);

    /**
     * 예약 정보를 변경한다.
     *
     * @param appointmentEntity
     * @return update row count
     */
    int change(@Param("appointmentEntity") AppointmentEntity appointmentEntity);

    /**
     * 예약 정보를 취소한다.
     *
     * @return update row count
     */
    int cancel(@Param("application_srl") long application_srl,
               @Param("application_srls") List<Long> application_srls);

    /**
     * 예약 상태를 변경한다.
     *
     * @param appointment_srl
     * @return update row count
     */
    int changeStatus(@Param("appointment_srl") long appointment_srl,
                     @Param("appointment_status") int appointment_status);

    /**
     * 고객에 대한 예약 정보를 조회한다.
     *
     * @param member_srl
     * @return 조회된 예약 정보 리스트
     */
    List<AppointmentEntity> getAppointmentList(@Param("member_srl") long member_srl);

    /**
     * 시리얼번호에 대한 예약 정보를 조회한다.
     *
     * @param appointment_srl
     * @return 조회된 예약 정보
     */
    AppointmentEntity getAppointmentBySrl(@Param("appointment_srl") long appointment_srl);

    /**
     * 월별 예약 정보를 조회한다.
     *
     * @param yearMonth
     * @return 조회된 예약 정보 리스트
     */
    List<AppointmentEntity> getAppointmentCheckList(@Param("advisor_srl") long advisor_srl,
                                                    @Param("yearMonth") String yearMonth);
}

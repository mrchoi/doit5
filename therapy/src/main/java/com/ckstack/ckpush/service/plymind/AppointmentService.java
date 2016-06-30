package com.ckstack.ckpush.service.plymind;

import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
import com.ckstack.ckpush.domain.plymind.AppointmentEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
public interface AppointmentService {
    /**
     * 예약 정보를 등록한다.
     * @param appointmentEntity 등록할 예약 정보
     *
     * @return insert row count
     */
    int add(AppointmentEntity appointmentEntity);

    /**
     * 예약 내역 총 갯수를 조회한다.
     * 고객 시리얼 번호/상담사 시리얼 번호가 0보다 작으면 전체 예약 내역 갯수 조회
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     *
     * @return 조회된 총 갯수
     */
    long countAppointment(long member_srl, long advisor_srl, List<Integer> application_statues);

    /**
     * 예약 내역 중 검색되어진 총 갯수를 조회한다.
     * 고객 시리얼 번호/상담사 시리얼 번호가 0보다 작으면 검색되어진 전체 예약 내역 갯수 조회
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     *
     * @return 조회된 총 갯수
     */
    long countSearchAppointment(long member_srl, long advisor_srl, List<Integer> application_statues, Map<String, String> searchFilter);

    /**
     * 예약 내역 중 검색되어진 총 갯수를 조회한다.
     * 고객 시리얼 번호/상담사 시리얼 번호가 0보다 작으면 검색되어진 전체 예약 내역 갯수 조회
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param searchFilter 검색 칼럼과 값이 포함된 map
     * @param sortValue 리스트 order 에 대한 정보.
     * @param offset 리스트를 가져올때 offset(mysql 의 offset 개념)
     * @param limit 리스트를 가져올때 limit(mysql 의 limit 개념)
     *
     * @return 조회된 예약 정보
     */
    List<AppointmentEntity> getAppointmentList(long member_srl, long advisor_srl, List<Integer> application_statues, Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit);

    /**
     * 예약 정보(날짜 및 시간)를 변경한다.
     * @param appointmentEntity 변경정보
     *
     * @return 조회된 총 갯수
     */
    int change(AppointmentEntity appointmentEntity);

    /**
     * 예약 상태를 변경한다.
     * @param appointment_srl 예약 시리얼 번호
     * @param appointment_status 예약 상태 값
     *
     * @return update row
     */
    int changeStatus(long appointment_srl, int appointment_status);

    List<AppointmentEntity> getAppointmentList(long member_srl);

    List<AppointmentEntity> getAppointmentCheckList(long advisor_srl, String yearMonth);

    AppointmentEntity getAppointmentBySrl(long appointment_srl);
}


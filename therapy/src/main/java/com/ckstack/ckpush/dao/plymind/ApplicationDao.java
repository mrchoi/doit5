package com.ckstack.ckpush.dao.plymind;

import com.ckstack.ckpush.domain.plymind.AddressEntity;
import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
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
public interface ApplicationDao {
    /**
     * 상담 정보를 저장한다.
     *
     * @param applicationEntity insert data
     * @return insert row count
     */
    int add(ApplicationEntity applicationEntity);

    /**
     * 주소 정보를 저장한다.
     *
     * @param addressEntity insert data
     * @return insert row count
     */
    int addAddress(AddressEntity addressEntity);

    /**
     * 관리자 사이트
     * 입금완료 처리시 상담가능 상태로 변경 한다.
     *
     * @param application_group 상담 그룹 번호
     * @return update row count
     */
    int statusReady(@Param("application_srl") long application_srl,
                    @Param("application_group") int application_group);

    /**
     * 관리자 사이트
     * 상담 진행중 상태로 변경 한다.
     *
     * @param application_group 상담 그룹 번호
     * @return update row count
     */
    int statusProgress(@Param("application_srl") long application_srl,
                       @Param("application_group") int application_group);

    /**
     * 관리자 사이트
     * 상담완료 상태로 변경 한다.
     *
     * @param application_srl 상담 시리얼 번호
     * @return update row count
     */
    int statusComplete(@Param("application_srl") long application_srl,
                       @Param("application_group") int application_group);

    /**
     * 상담취소 상태로 변경 한다.
     *
     * @param application_group 상담 그룹 번호
     * @return update row count
     */
    int cancel(@Param("application_group") int application_group);

    /**
     * 상태값을 변경한다.
     *
     * @param application_group insert data
     * @return update row count
     */
    int statusModify(@Param("application_srl") long application_srl,
                     @Param("application_group") int application_group,
                     @Param("application_status") int application_status);

    /**
     * 싸이케어테라피 / 커플케어테라피 / 패밀리케어테라피 스마트기기 수령확인 처리
     * @param applicationEntity update data
     * @return update row count
     */
    int receiveCheck(@Param("applicationEntity") ApplicationEntity applicationEntity);

    /**
     * 해당 고객의 결제내역 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    long countApplicationGroup(@Param("member_srl") long member_srl,
                               @Param("advisor_srl") long advisor_srl,
                               @Param("application_statues") List<Integer> application_statues,
                               @Param("contents_srls") List<Long> contents_srls,
                               @Param("user_name") String user_name,
                               @Param("advisor_srls") List<Long> advisor_srls,
                               @Param("product_srls") List<Long> product_srls);

    /**
     * 상담 정보를 조회 한다.
     *
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param contents_srls 컨텐츠 시리얼 번호 리스트
     * @param user_name 고객 이름
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<ApplicationEntity> getApplicationGroupList(@Param("member_srl") long member_srl,
                                               @Param("advisor_srl") long advisor_srl,
                                               @Param("application_statues") List<Integer>  application_statues,
                                               @Param("contents_srls") List<Long> contents_srls,
                                               @Param("user_name") String user_name,
                                               @Param("advisor_srls") List<Long> advisor_srls,
                                               @Param("product_srls") List<Long> product_srls,
                                               @Param("sort") Map<String, String> sort,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);

    /**
     * 해당 고객의 결제내역 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    long countApplicationGroupComplete(@Param("member_srl") long member_srl,
                                       @Param("advisor_srl") long advisor_srl,
                                       @Param("application_statues") List<Integer> application_statues,
                                       @Param("contents_srls") List<Long> contents_srls,
                                       @Param("user_name") String user_name,
                                       @Param("advisor_srls") List<Long> advisor_srls,
                                       @Param("product_srls") List<Long> product_srls);

    @Transactional(readOnly = true)
    List<ApplicationEntity> getApplicationGroupCompleteList(@Param("member_srl") long member_srl,
                                                    @Param("advisor_srl") long advisor_srl,
                                                    @Param("application_statues") List<Integer>  application_statues,
                                                    @Param("contents_srls") List<Long> contents_srls,
                                                    @Param("user_name") String user_name,
                                                    @Param("advisor_srls") List<Long> advisor_srls,
                                                    @Param("product_srls") List<Long> product_srls,
                                                    @Param("sort") Map<String, String> sort,
                                                    @Param("offset") int offset,
                                                    @Param("limit") int limit);

    @Transactional(readOnly = true)
    ApplicationEntity getApplication(@Param("application_srl") long application_srl);

    /**
     * 해당 고객의 결제내역 총 갯수를 조회한다..
     *
     * @return group by count
     */
    @Transactional(readOnly = true)
    long countApplicationByGroup(@Param("application_group") int application_group);

    /**
     * 상담 정보를 조회 한다.
     *
     * @param application_group insert data
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<ApplicationEntity> getApplicationListByGroup(@Param("member_srl") long member_srl,
                                                      @Param("application_group") int application_group,
                                                      @Param("sort") Map<String, String> sort,
                                                      @Param("offset") int offset,
                                                      @Param("limit") int limit);

    /**
     * 상담 정보를 조회 한다.
     *
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param contents_srls 컨텐츠 시리얼 번호 리스트
     * @param user_name 고객 이름
     * @return select multi row
     */
    @Transactional(readOnly = true)
    long countApplicationList(@Param("member_srl") long member_srl,
                              @Param("advisor_srl") long advisor_srl,
                              @Param("application_statues") List<Integer> application_statues,
                              @Param("contents_srls") List<Long> contents_srls,
                              @Param("user_name") String user_name);

    /**
     * 상담 정보를 조회 한다.
     *
     * @param member_srl 고객 시리얼 번호
     * @param advisor_srl 상담사 시리얼 번호
     * @param contents_srls 컨텐츠 시리얼 번호 리스트
     * @param user_name 고객 이름
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<ApplicationEntity> getApplicationList(@Param("member_srl") long member_srl,
                                               @Param("advisor_srl") long advisor_srl,
                                               @Param("application_statues") List<Integer> application_statues,
                                               @Param("contents_srls") List<Long> contents_srls,
                                               @Param("user_name") String user_name,
                                               @Param("sort") Map<String, String> sort,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);

    /**
     * 상담 그룹별 고객정보를 조회한다.
     *
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<ApplicationEntity> getApplicationMember(@Param("application_group") int application_group);
}

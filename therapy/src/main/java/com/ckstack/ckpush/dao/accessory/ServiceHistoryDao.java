package com.ckstack.ckpush.dao.accessory;

import com.ckstack.ckpush.domain.accessory.ServiceHistoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 17..
 */
@Repository
@Transactional(value = "transactionManager")
public interface ServiceHistoryDao {
    int add(ServiceHistoryEntity serviceHistoryEntity);

    /**
     * tbl_service_history row count
     *
     * @param start_c_date start_c_date condition
     * @param end_c_date end_c_date condition
     * @param history_type history_type condition
     * @param member_srl member_srl condition
     * @param max_history_srl max_history_srl condition
     * @return tbl_service_history row count
     */
    @Transactional(readOnly = true)
    long count(@Param("start_c_date") int start_c_date,
               @Param("end_c_date") int end_c_date,
               @Param("history_type") int history_type,
               @Param("member_srl") long member_srl,
               @Param("max_history_srl") long max_history_srl);

    /**
     * tbl_service_history row select one.
     * history_srl 이 0 보다 작으면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     * c_date 는 파티션 구분을 위해서 넣어야 한다.(c_date 로 파티션 구분됨)
     *
     * @param history_srl history_srl condition
     * @param c_date c_date condition
     * @return tbl_service_history row one
     */
    @Transactional(readOnly = true)
    ServiceHistoryEntity get(@Param("history_srl") long history_srl,
                         @Param("c_date") int c_date);

    /**
     * tbl_service_history row select list.
     * start_c_date, end_c_date 는 파티션 구분을 위해서 넣어야 한다.(c_date 로 파티션 구분됨)
     *
     * @param start_c_date start_c_date condition
     * @param end_c_date end_c_date condition
     * @param history_type history_type condition
     * @param member_srl member_srl condition
     * @param max_history_srl max_history_srl condition
     * @param offset list offset
     * @param limit list limit
     * @return tbl_service_history row list. select row 가 없으면 empty list 를 리턴 한다.
     */
    @Transactional(readOnly = true)
    List<ServiceHistoryEntity> get(@Param("start_c_date") int start_c_date,
                               @Param("end_c_date") int end_c_date,
                               @Param("history_type") int history_type,
                               @Param("member_srl") long member_srl,
                               @Param("max_history_srl") long max_history_srl,
                               @Param("offset") int offset,
                               @Param("limit") int limit);
}

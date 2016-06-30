package com.ckstack.ckpush.dao.push;

import com.ckstack.ckpush.domain.push.PushMessageEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 4. 29..
 */
@Repository
@Transactional(value = "transactionManager")
public interface PushMessageDao {
    /**
     * tbl_push_message insert
     * @param pushMessageEntity insert data
     * @return inserted row count
     */
    int add(PushMessageEntity pushMessageEntity);

    /**
     * tbl_push_message row count
     *
     * @param app_srl app_srl condition
     * @param push_title push_title condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("push_title") String push_title,
               @Param("max_push_srl") long max_push_srl);

    /**
     * tbl_push_message row select one
     *
     * @param push_srl push_srl condition
     * @return only one row
     */
    @Transactional(readOnly = true)
    PushMessageEntity get(@Param("push_srl") long push_srl);

    /**
     * tbl_push_message row select list
     *
     * @param app_srl app_srl condition
     * @param push_srls push_srls condition
     * @param push_title push_title condition
     * @param max_push_srl max_push_srl condition. 특정 위치의 push_srl 이하 row list 가져올때 사용
     * @param offset list offset
     * @param limit list limit
     * @return row list
     */
    @Transactional(readOnly = true)
    List<PushMessageEntity> get(@Param("app_srl") int app_srl,
                                @Param("push_srls") List<Long> push_srls,
                                @Param("push_title") String push_title,
                                @Param("max_push_srl") long max_push_srl,
                                @Param("sort") Map<String, String> sort,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    /**
     * tbl_push_message row delete
     *
     * @param push_srl push_srl condition
     * @return deleted row count
     */
    int delete(long push_srl);
}

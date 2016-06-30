package com.ckstack.ckpush.dao.mineral;

import com.ckstack.ckpush.domain.mineral.PushMessagePicEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 4. 29..
 */
@Repository
@Transactional(value = "transactionManager")
public interface PushMessagePicDao {
    /**
     * tbl_push_message_pic row insert
     *
     * @param pushMessagePicEntity insert data
     * @return inserted row count
     */
    int add(PushMessagePicEntity pushMessagePicEntity);

    /**
     * tbl_push_message_pic row count
     *
     * @param push_srl push_srl condition
     * @param file_srl file_srl condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("push_srl") long push_srl,
               @Param("file_srl") long file_srl);

    /**
     * tbl_push_message_pic row select one
     *
     * @param push_srl push_srl condition
     * @param file_srl file_srl condition
     * @return select row one
     */
    @Transactional(readOnly = true)
    PushMessagePicEntity get(@Param("push_srl") long push_srl,
                             @Param("file_srl") long file_srl);

    /**
     * tbl_push_message_pic row select list
     *
     * @param push_srl push_srl condition
     * @param push_srls push_srls condition. push_srl list
     * @param file_srl file_srl condition
     * @param file_srls file_srls condition. file_srl list
     * @param offset list offset
     * @param limit list limit
     * @return
     */
    @Transactional(readOnly = true)
    List<PushMessagePicEntity> get(@Param("push_srl") long push_srl,
                                   @Param("push_srls") List<Long> push_srls,
                                   @Param("file_srl") long file_srl,
                                   @Param("file_srls") List<Long> file_srls,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    /**
     * tbl_push_message_pic, tbl_push_pic join. select one row
     * push_srl 이 0 보다 작으면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param push_srl push_srl condition
     * @param deleted 삭제 여부.
     * @return tbl_push_message_pic, tbl_push_pic join. select one row
     */
    @Transactional(readOnly = true)
    PushMessagePicEntity getAndFile(@Param("push_srl") long push_srl,
                                    @Param("deleted") int deleted);

    /**
     * tbl_push_message_pic, tbl_push_pic join. select list row
     *
     * @param push_srls push_srl list
     * @param deleted 삭제 여부
     * @return message_pic, tbl_push_pic join. select list
     */
    @Transactional(readOnly = true)
    List<PushMessagePicEntity> getAndFile(@Param("push_srls") List<Long> push_srls,
                                          @Param("deleted") int deleted);

    /**
     * tbl_push_message_pic row delete
     *
     * @param push_srl push_srl condition
     * @param file_srl file_srl condition
     * @return delete row count
     */
    int delete(@Param("push_srl") long push_srl,
               @Param("file_srl") long file_srl);
}

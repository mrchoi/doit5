package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.DocumentBoardEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 7. 29..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DocumentBoardDao {
    /**
     * insert document board row
     *
     * @param documentBoardEntity insert data
     * @return inserted row count
     */
    int add(DocumentBoardEntity documentBoardEntity);

    /**
     * select document board row count
     *
     * @param app_srl app_srl condition
     * @param board_name board_name condition
     * @param enabled enabled condition
     * @param open_type open_type condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("board_name") String board_name,
               @Param("enabled") int enabled,
               @Param("open_type") int open_type);

    /**
     * select document board one row
     *
     * @param board_srl board_srl condition
     * @param enabled enabled condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    DocumentBoardEntity get(@Param("board_srl") long board_srl,
                            @Param("enabled") int enabled);

    /**
     * select document board multi row
     *
     * @param board_srls board_srls condition
     * @param board_name board_name condition
     * @param enabled enabled condition
     * @param open_type open_type condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<DocumentBoardEntity> get(@Param("board_srls") List<Long> board_srls,
                                  @Param("app_srl") int app_srl,
                                  @Param("board_name") String board_name,
                                  @Param("enabled") int enabled,
                                  @Param("open_type") int open_type,
                                  @Param("sort") Map<String, String> sort,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    /**
     * modify document board row
     *
     * @param documentBoardEntity modify data
     * @param target_board_srl target
     * @return update row count
     */
    int modify(@Param("documentBoardEntity") DocumentBoardEntity documentBoardEntity,
               @Param("target_board_srl") long target_board_srl);

    /**
     * delete document board row
     *
     * @param board_srl board_srl condition
     * @param board_srls board_srls condition
     * @return deleted row count
     */
    int delete(@Param("board_srl") long board_srl,
               @Param("board_srls") List<Long> board_srls);
}

package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.DocumentCategoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 7. 31..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DocumentCategoryDao {
    /**
     * insert document category row
     *
     * @param documentCategoryEntity insert data
     * @return insert row count
     */
    int add(DocumentCategoryEntity documentCategoryEntity);

    /**
     * select document category row count
     *
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param category_name category_name condition
     * @param category_type category_type condition
     * @param enabled enabled condition
     * @param open_type open_type condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("board_srl") long board_srl,
               @Param("category_name") String category_name,
               @Param("category_type") int category_type,
               @Param("enabled") int enabled,
               @Param("open_type") int open_type);

    /**
     * select document category one row
     *
     * @param category_srl category_srl condition
     * @param enabled enabled condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    DocumentCategoryEntity get(@Param("category_srl") long category_srl,
                               @Param("enabled") int enabled);

    /**
     * select document category multi row
     *
     * @param category_srls category_srls condition
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param board_srls board_srls condition
     * @param category_name category_name condition
     * @param category_type category_type condition
     * @param enabled enabled condition
     * @param open_type open_type condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<DocumentCategoryEntity> get(@Param("category_srls") List<Long> category_srls,
                                     @Param("app_srl") int app_srl,
                                     @Param("board_srl") long board_srl,
                                     @Param("board_srls") List<Long> board_srls,
                                     @Param("category_name") String category_name,
                                     @Param("category_type") int category_type,
                                     @Param("enabled") int enabled,
                                     @Param("open_type") int open_type,
                                     @Param("sort") Map<String, String> sort,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    /**
     * 카테고리별 게시물 카운트
     *
     * @param category_srl category_srl condition
     * @param category_srls category_srls condition
     * @param secret secret condition
     * @param secrets secret를 여러 조건으로 넣을때 값
     * @param block block condition
     * @return 카테고리별 게시물 카운트
     */
    @Transactional(readOnly = true)
    List<Map<String, Object>> countDocument(@Param("category_srl") long category_srl,
                                            @Param("category_srls") List<Long> category_srls,
                                            @Param("secret") int secret,
                                            @Param("secrets") List<Long> secrets,
                                            @Param("block") int block);

    /**
     * 카테고리별 링크 게시물 카운트.
     * 링크와 연결된 게시물의 secret, block을 본다
     *
     * @param category_srl category_srl condition
     * @param category_srls category_srls condition
     * @param secret secret condition
     * @param secrets secret를 여러 조건으로 넣을때 값
     * @param block block condition
     * @return 카테고리별 링크 게시물 카운트
     */
    @Transactional(readOnly = true)
    List<Map<String, Object>> countDocumentLink(@Param("category_srl") long category_srl,
                                                @Param("category_srls") List<Long> category_srls,
                                                @Param("secret") int secret,
                                                @Param("secrets") List<Long> secrets,
                                                @Param("block") int block);

    /**
     * update document category row
     *
     * @param documentCategoryEntity update data
     * @param target_category_srl category_srl target
     * @param target_board_srl board_srl target
     * @return update row count
     */
    int modify(@Param("documentCategoryEntity") DocumentCategoryEntity documentCategoryEntity,
               @Param("target_category_srl") long target_category_srl,
               @Param("target_board_srl") long target_board_srl);

    /**
     * delete document category row
     *
     * @param category_srl category_srl condition
     * @param category_srls category_srls condition
     * @return delete row count
     */
    int delete(@Param("category_srl") long category_srl,
               @Param("category_srls") List<Long> category_srls);
}

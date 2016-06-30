package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.DocumentCommentEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by kodaji on 2016. 1. 29..
 */
public interface DocumentCommentDao {

    /**
     * insert document row
     *
     * @param documentCommentEntity insert data
     * @return insert row count
     */
    int add(DocumentCommentEntity documentCommentEntity);

    /**
     * select document row count
     *
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param category_srl category_srl condition
     * @param document_srl document_srl condition
     * @return select row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("board_srl") long board_srl,
               @Param("category_srl") long category_srl,
               @Param("document_srl") long document_srl);

    /**
     * select document one row
     *
     * @param comment_srl document_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    DocumentCommentEntity get(@Param("comment_srl") long comment_srl);

    /**
     * select document multi row
     *
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param category_srl category_srl condition
     * @param document_srl document_srl condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<DocumentCommentEntity> get(@Param("app_srl") int app_srl,
                                     @Param("board_srl") long board_srl,
                                     @Param("category_srl") long category_srl,
                                     @Param("document_srl") long document_srl,
                                     @Param("sort") Map<String, String> sort,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    /**
     * modify document row
     *
     * @param documentCommentEntity update data
     * @param target_comment_srl target_comment_srl condition
     * @return update row count
     */
    int modify(@Param("documentCommentEntity") DocumentCommentEntity documentCommentEntity,
               @Param("target_comment_srl") long target_comment_srl);

    /**
     * increase document's count
     *
     * @param target_comment_srl target_comment_srl condition
     * @param increase_like_count true 이면 like_count를 +1 증가
     * @param increase_blame_count true 이면 blame_count를 +1 증가
     * @param increase_child_comment_count true 이면 child_comment_count를 +1 증가
     * @param increase_file_count true 이면 file_count를 +1 증가
     * @return updated row count
     */
    int increase(@Param("target_comment_srl") long target_comment_srl,
                 @Param("increase_like_count") boolean increase_like_count,
                 @Param("increase_blame_count") boolean increase_blame_count,
                 @Param("increase_child_comment_count") boolean increase_child_comment_count,
                 @Param("increase_file_count") boolean increase_file_count);

    /**
     * decrease document's count
     *
     * @param target_comment_srl comment_srl condition
     * @param decrease_like_count true 이면 like_count를 -1 감소
     * @param decrease_blame_count true 이면 blame_count를 -1 감소
     * @param decrease_child_comment_count true 이면 child_comment_count를 -1 감소
     * @param decrease_file_count true 이면 file_count를 -1 감소
     * @return updated row count
     */
    int decrease(@Param("target_comment_srl") long target_comment_srl,
                 @Param("decrease_like_count") boolean decrease_like_count,
                 @Param("decrease_blame_count") boolean decrease_blame_count,
                 @Param("decrease_child_comment_count") boolean decrease_child_comment_count,
                 @Param("decrease_file_count") boolean decrease_file_count);

    /**
     * delete document row
     *
     * @param comment_srl document_srl condition
     * @param comment_srls document_srls condition
     * @return deleted row count
     */
    int delete(@Param("comment_srl") long comment_srl,
               @Param("comment_srls") List<Long> comment_srls);

    /**
     * select document row count
     *
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param category_srl category_srl condition
     * @param member_srl document_srl condition
     * @return select row count
     */
    @Transactional(readOnly = true)
    long countAdvisorPresent(@Param("app_srl") int app_srl,
                             @Param("board_srl") long board_srl,
                             @Param("category_srl") long category_srl,
                             @Param("member_srl") long member_srl);
}

package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.DocumentEntity;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 8. 5..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DocumentDao {
    /**
     * insert document row
     *
     * @param documentEntity insert data
     * @return insert row count
     */
    int add(DocumentEntity documentEntity);

    /**
     * select document row count
     *
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param category_srl category_srl condition
     * @param document_title document_title condition
     * @param secret secret condition
     * @param secrets secrets condition
     * @param block block condition
     * @param allow_comment allow_comment condition
     * @param allow_notice allow_notice condition
     * @param member_srl member_srl condition
     * @param user_id user_id condition
     * @param user_name user_name condition
     * @param nick_name nick_name condition
     * @return select row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("board_srl") long board_srl,
               @Param("category_srl") long category_srl,
               @Param("document_title") String document_title,
               @Param("secret") long secret,
               @Param("secrets") List<Long> secrets,
               @Param("block") int block,
               @Param("allow_comment") int allow_comment,
               @Param("allow_notice") int allow_notice,
               @Param("member_srl") long member_srl,
               @Param("user_id") String user_id,
               @Param("user_name") String user_name,
               @Param("nick_name") String nick_name);

    /**
     * select document one row
     *
     * @param document_srl document_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    DocumentEntity get(@Param("document_srl") long document_srl);

    /**
     * select document multi row
     *
     * @param document_srls document_srls condition
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param board_srls board_srls condition
     * @param category_srl category_srl condition
     * @param category_srls category_srls condition
     * @param document_title document_title condition
     * @param secret secret condition
     * @param secrets secrets condition
     * @param block block condition
     * @param allow_comment allow_comment condition
     * @param allow_notice allow_notice condition
     * @param member_srl member_srl condition
     * @param user_id user_id condition
     * @param user_name user_name condition
     * @param nick_name nick_name condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<DocumentEntity> get(@Param("document_srls") List<Long> document_srls,
                             @Param("app_srl") int app_srl,
                             @Param("board_srl") long board_srl,
                             @Param("board_srls") List<Long> board_srls,
                             @Param("category_srl") long category_srl,
                             @Param("category_srls") List<Long> category_srls,
                             @Param("document_title") String document_title,
                             @Param("secret") long secret,
                             @Param("secrets") List<Long> secrets,
                             @Param("block") int block,
                             @Param("allow_comment") int allow_comment,
                             @Param("allow_notice") int allow_notice,
                             @Param("member_srl") long member_srl,
                             @Param("user_id") String user_id,
                             @Param("user_name") String user_name,
                             @Param("nick_name") String nick_name,
                             @Param("sort") Map<String, String> sort,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    /**
     * modify document row
     *
     * @param documentEntity update data
     * @param target_document_srl document_srl condition
     * @param target_category_srl category_srl condition
     * @param target_board_srl board_srl condition
     * @return update row count
     */
    int modify(@Param("documentEntity") DocumentEntity documentEntity,
               @Param("target_document_srl") long target_document_srl,
               @Param("target_category_srl") long target_category_srl,
               @Param("target_board_srl") long target_board_srl);

    /**
     * increase document's count
     *
     * @param target_document_srl document_srl condition
     * @param increase_read_count true 이면 read_count를 +1 증가
     * @param increase_like_count true 이면 like_count를 +1 증가
     * @param increase_blame_count true 이면 blame_count를 +1 증가
     * @param increase_comment_count true 이면 comment_count를 +1 증가
     * @param increase_file_count true 이면 file_count를 +1 증가
     * @return updated row count
     */
    int increase(@Param("target_document_srl") long target_document_srl,
                 @Param("increase_read_count") boolean increase_read_count,
                 @Param("increase_like_count") boolean increase_like_count,
                 @Param("increase_blame_count") boolean increase_blame_count,
                 @Param("increase_comment_count") boolean increase_comment_count,
                 @Param("increase_file_count") boolean increase_file_count);

    /**
     * decrease document's count
     *
     * @param target_document_srl document_srl condition
     * @param decrease_read_count true 이면 read_count를 -1 감소
     * @param decrease_like_count true 이면 like_count를 -1 감소
     * @param decrease_blame_count true 이면 blame_count를 -1 감소
     * @param decrease_comment_count true 이면 comment_count를 -1 감소
     * @param decrease_file_count true 이면 file_count를 -1 감소
     * @return updated row count
     */
    int decrease(@Param("target_document_srl") long target_document_srl,
                 @Param("decrease_read_count") boolean decrease_read_count,
                 @Param("decrease_like_count") boolean decrease_like_count,
                 @Param("decrease_blame_count") boolean decrease_blame_count,
                 @Param("decrease_comment_count") boolean decrease_comment_count,
                 @Param("decrease_file_count") boolean decrease_file_count);

    /**
     * delete document row
     *
     * @param document_srl document_srl condition
     * @param document_srls document_srls condition
     * @return deleted row count
     */
    int delete(@Param("document_srl") long document_srl,
               @Param("document_srls") List<Long> document_srls);

}

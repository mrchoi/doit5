package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.DocumentLinkEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 8. 24..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DocumentLinkDao {
    /**
     * insert document_link row
     *
     * @param documentLinkEntity insert data
     * @return inserted row count
     */
    int add(DocumentLinkEntity documentLinkEntity);

    /**
     * select row count
     *
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param category_srl category_srl condition
     * @param document_srl document_srl condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("board_srl") long board_srl,
               @Param("category_srl") long category_srl,
               @Param("document_srl") long document_srl);

    /**
     * select document link one row
     *
     * @param document_link_srl document_link_srl condition
     * @return one row. if not exist return null
     */
    @Transactional(readOnly = true)
    DocumentLinkEntity get(@Param("document_link_srl") long document_link_srl);

    /**
     * select document link multi row
     *
     * @param document_link_srls document_link_srls condition
     * @param app_srl app_srl condition
     * @param board_srl board_srl condition
     * @param board_srls board_srls condition
     * @param category_srl category_srl condition
     * @param category_srls category_srls condition
     * @param document_srl document_srl condition
     * @param document_srls document_srls condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return multi row
     */
    @Transactional(readOnly = true)
    List<DocumentLinkEntity> get(@Param("document_link_srls") List<Long> document_link_srls,
                                 @Param("app_srl") int app_srl,
                                 @Param("board_srl") long board_srl,
                                 @Param("board_srls") List<Long> board_srls,
                                 @Param("category_srl") long category_srl,
                                 @Param("category_srls") List<Long> category_srls,
                                 @Param("document_srl") long document_srl,
                                 @Param("document_srls") List<Long> document_srls,
                                 @Param("sort") Map<String, String> sort,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    /**
     * count document link full row
     *
     * @param document_srl document_srl condition
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
     * @return document link full info row count
     */
    @Transactional(readOnly = true)
    long countFullInfo(@Param("document_srl") long document_srl,
                       @Param("document_srls") List<Long> document_srls,
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
                       @Param("nick_name") String nick_name);

    /**
     * select document link full info one row
     *
     * @param document_link_srl document_link_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    DocumentLinkEntity getFullInfo(@Param("document_link_srl") long document_link_srl);

    /**
     * select document link full info multi row
     *
     * @param document_link_srls document_link_srls condition
     * @param document_srl document_srl condition
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
    List<DocumentLinkEntity> getFullInfo(@Param("document_link_srls") List<Long> document_link_srls,
                                         @Param("document_srl") long document_srl,
                                         @Param("document_srls") List<Long> document_srls,
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
     * update document link
     *
     * @param documentLinkEntity update date
     * @param target_document_link_srl document_link_srl target
     * @param target_board_srl board_srl target
     * @param target_category_srl category_srl target
     * @return update row count
     */
    int modify(@Param("documentLinkEntity") DocumentLinkEntity documentLinkEntity,
               @Param("target_document_link_srl") long target_document_link_srl,
               @Param("target_board_srl") long target_board_srl,
               @Param("target_category_srl") long target_category_srl);

    /**
     * delete document link
     *
     * @param document_link_srl document_link_srl condition
     * @param document_link_srls document_link_srls condition
     * @return deleted row count
     */
    int delete(@Param("document_link_srl") long document_link_srl,
               @Param("document_link_srls") List<Long> document_link_srls);
}

package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.DocumentTagEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 8. 7..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DocumentTagDao {
    /**
     * insert row
     *
     * @param documentTagEntity insert data
     * @return inserted row count
     */
    int add(DocumentTagEntity documentTagEntity);

    /**
     * select one row
     *
     * @param document_srl document_srl condition
     * @param tag_srl tag_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    DocumentTagEntity get(@Param("document_srl") long document_srl,
                          @Param("tag_srl") long tag_srl);

    /**
     * select multi row
     *
     * @param document_srl document_srl condition
     * @param document_srls document_srls condition
     * @param tag_srl tag_srl condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<DocumentTagEntity> get(@Param("document_srl") long document_srl,
                                @Param("document_srls") List<Long> document_srls,
                                @Param("tag_srl") long tag_srl,
                                @Param("sort") Map<String, String> sort,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    /**
     * 특정 태그가 달린 게시물 카운트를 구한다.
     *
     * @param tag_srl tag_srl condition
     * @param tag_srls tag_srls condition
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
     * @return 게시물 태그, 게시물을 join 해서 구한 태그가 달린 게시물 카운트
     */
    @Transactional(readOnly = true)
    long countFullInfo(@Param("tag_srl") long tag_srl,
                       @Param("tag_srls") List<Long> tag_srls,
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
                       @Param("nick_name") String nick_name);

    /**
     * 특정 태그가 달린 게시물 리스트를 구한다.
     *
     * @param tag_srl tag_srl condition
     * @param tag_srls tag_srls condition
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
     * @return 게시물 태그, 게시물 join 해서 구한 게시물 리스트
     */
    @Transactional(readOnly = true)
    List<DocumentTagEntity> getFullInfo(@Param("tag_srl") long tag_srl,
                                        @Param("tag_srls") List<Long> tag_srls,
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
     * 특정 태그가 달린 게시물을 링크하는 링크 게시물 카운트를 구한다.
     *
     * @param tag_srl tag_srl condition
     * @param tag_srls tag_srls condition
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
     * @return 게시물 태그, 게시물, 게시물 링크를 join 해서 특정 태그가 달린 게시물을 링크하는 링크 게시물 카운트를 구한다.
     */
    @Transactional(readOnly = true)
    long countFullLinkInfo(@Param("tag_srl") long tag_srl,
                           @Param("tag_srls") List<Long> tag_srls,
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
                           @Param("nick_name") String nick_name);

    /**
     * 특정 태그가 달린 게시물을 링크하는 링크 게시물 리스트를 구한다.
     *
     * @param tag_srl tag_srl condition
     * @param tag_srls tag_srls condition
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
     * @param sort sort condition
     * @param offset offset condition
     * @param limit limit condition
     * @return 게시물 태그, 게시물, 게시물 링크를 join 해서 특정 태그가 달린 게시물을 링크하는 링크 게시물 리스트를 구한다.
     */
    @Transactional(readOnly = true)
    List<DocumentTagEntity> getFullLinkInfo(@Param("tag_srl") long tag_srl,
                                            @Param("tag_srls") List<Long> tag_srls,
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
     * delete row
     *
     * @param document_srl document_srl condition
     * @param tag_srl tag_srl condition
     * @param tag_srls tag_srls condition
     * @return deleted row count
     */
    int delete(@Param("document_srl") long document_srl,
               @Param("tag_srl") long tag_srl,
               @Param("tag_srls") List<Long> tag_srls);
}

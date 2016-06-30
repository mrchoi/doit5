package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.TagEntity;
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
public interface TagDao {
    /**
     * insert tag row
     *
     * @param tagEntity insert data
     * @return inserted row count
     */
    int add(TagEntity tagEntity);

    /**
     * select row count
     *
     * @param app_srl app_srl condition
     * @param member_srl member_srl condition
     * @param tag_name tag_name condition
     * @param admin_tag admin_tag condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("member_srl") long member_srl,
               @Param("tag_name") String tag_name,
               @Param("admin_tag") int admin_tag);

    /**
     * select one row
     *
     * @param tag_srl tag_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    TagEntity get(@Param("tag_srl") long tag_srl);

    /**
     * select multi row
     *
     * @param tag_srls tag_srls condition
     * @param app_srl app_srl condition
     * @param member_srl member_srl condition
     * @param tag_name tag_name condition
     * @param admin_tag admin_tag condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<TagEntity> get(@Param("tag_srls") List<Long> tag_srls,
                        @Param("app_srl") int app_srl,
                        @Param("member_srl") long member_srl,
                        @Param("tag_name") String tag_name,
                        @Param("admin_tag") int admin_tag,
                        @Param("sort") Map<String, String> sort,
                        @Param("offset") int offset,
                        @Param("limit") int limit);

    /**
     * update row
     *
     * @param tagEntity updated data
     * @param target_tag_srl tag_srl condition
     * @param target_app_srl app_srl condition
     * @return update row count
     */
    int modify(@Param("tagEntity") TagEntity tagEntity,
               @Param("target_tag_srl") long target_tag_srl,
               @Param("target_app_srl") int target_app_srl);

    /**
     * delete row
     *
     * @param tag_srl tag_srl condition
     * @param tag_srls tag_srls condition
     * @return deleted row count
     */
    int delete(@Param("tag_srl") long tag_srl,
               @Param("tag_srls") List<Long> tag_srls);
}

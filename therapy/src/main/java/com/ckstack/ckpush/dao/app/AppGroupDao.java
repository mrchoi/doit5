package com.ckstack.ckpush.dao.app;

import com.ckstack.ckpush.domain.app.AppGroupEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 7. 14..
 */
@Repository
@Transactional(value = "transactionManager")
public interface AppGroupDao {
    /**
     * insert row in tbl_app_group
     *
     * @param appGroupEntity inserted data
     * @return inserted row count
     */
    int add(AppGroupEntity appGroupEntity);

    /**
     * tbl_app_group row count
     *
     * @param app_srl app_srl condition
     * @param group_name group_name condition
     * @param group_type group_type condition
     * @param enabled enabled condition
     * @param allow_default allow_default condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("group_name") String group_name,
               @Param("group_type") int group_type,
               @Param("authority") String authority,
               @Param("enabled") int enabled,
               @Param("allow_default") int allow_default);

    /**
     * tbl_app_group row select one
     *
     * @param app_group_srl app_group_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    AppGroupEntity get(@Param("app_group_srl") long app_group_srl);

    /**
     * tbl_app_group row select multi
     *
     * @param app_srl app_srl condition
     * @param group_name group_name condition
     * @param group_type group_type condition
     * @param enabled enabled condition
     * @param allow_default allow_default condition
     * @param sort sort 종류
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<AppGroupEntity> get(@Param("app_srl") int app_srl,
                             @Param("app_group_srls") List<Long> app_group_srls,
                             @Param("group_name") String group_name,
                             @Param("group_type") int group_type,
                             @Param("authority") String authority,
                             @Param("enabled") int enabled,
                             @Param("allow_default") int allow_default,
                             @Param("sort")Map<String, String> sort,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    /**
     * tbl_app_group row update
     *
     * @param appGroupEntity updated data
     * @param target_app_group_srl app_group_srl condition
     * @param target_app_srl app_srl condition
     * @return update row count
     */
    int modify(@Param("appGroupEntity") AppGroupEntity appGroupEntity,
               @Param("target_app_group_srl") long target_app_group_srl,
               @Param("target_app_srl") int target_app_srl);

    /**
     * tbl_app_group row delete
     *
     * @param app_group_srl app_group_srl condition
     * @param app_srl app_srl condition
     * @return deleted row count
     */
    int delete(@Param("app_group_srl") long app_group_srl,
               @Param("app_srl") int app_srl);

}

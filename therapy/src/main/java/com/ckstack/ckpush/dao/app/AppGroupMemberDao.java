package com.ckstack.ckpush.dao.app;

import com.ckstack.ckpush.domain.app.AppGroupMemberEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 7. 15..
 */
@Repository
@Transactional(value = "transactionManager")
public interface AppGroupMemberDao {
    /**
     * tbl_app_group_member insert row
     *
     * @param appGroupMemberEntity insert data
     * @return inserted row count
     */
    int add(AppGroupMemberEntity appGroupMemberEntity);

    /**
     * tbl_app_group_member row count
     *
     * @param app_group_srl app_group_srl condition
     * @param member_srl member_srl condition
     * @return select row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_group_srl") long app_group_srl,
               @Param("member_srl") long member_srl);

    /**
     * tbl_app_group_member select one row
     *
     * @param app_group_srl app_group_srl condition
     * @param member_srl member_srl condition
     * @return selected one row
     */
    @Transactional(readOnly = true)
    AppGroupMemberEntity get(@Param("app_group_srl") long app_group_srl,
                             @Param("member_srl") long member_srl);

    /**
     * tbl_app_group_member select multi row
     *
     * @param app_group_srl app_group_srl condition
     * @param app_group_srls app_group_srls condition
     * @param member_srl member_srl condition
     * @param limit list limit
     * @param offset list offset
     * @return selected multi row
     */
    @Transactional(readOnly = true)
    List<AppGroupMemberEntity> get(@Param("app_group_srl") long app_group_srl,
                                   @Param("app_group_srls") List<Long> app_group_srls,
                                   @Param("member_srl") long member_srl,
                                   @Param("limit") int limit,
                                   @Param("offset") int offset);

    /**
     * tbl_app_group_member delete row
     *
     * @param app_group_srl app_group_srl condition
     * @param member_srl member_srl condition
     * @return delete rows
     */
    int delete(@Param("app_group_srl") long app_group_srl,
               @Param("app_group_srls") List<Long> app_group_srls,
               @Param("member_srls") List<Long> member_srls,
               @Param("member_srl") long member_srl);
}

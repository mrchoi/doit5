package com.ckstack.ckpush.dao.app;

import com.ckstack.ckpush.domain.app.AppMemberEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 8. 20..
 */
@Repository
@Transactional(value = "transactionManager")
public interface AppMemberDao {
    /**
     * insert app member row
     *
     * @param appMemberEntity insert data
     * @return inserted row count
     */
    int add(AppMemberEntity appMemberEntity);

    /**
     * select app member count
     *
     * @param app_srl app_srl condition
     * @param member_srl member_srl condition
     * @return select row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("member_srl") long member_srl);

    /**
     * select one row
     *
     * @param app_srl app_srl condition
     * @param member_srl member_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    AppMemberEntity get(@Param("app_srl") int app_srl,
                        @Param("member_srl") long member_srl);

    /**
     * select multi row
     *
     * @param app_srl app_srl condition
     * @param app_srls app_srls condition
     * @param member_srl member_srl condition
     * @param member_srls member_srls condition
     * @param enabled enabled condition
     * @param limit list limit
     * @param offset list offset
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<AppMemberEntity> get(@Param("app_srl") int app_srl,
                              @Param("app_srls") List<Integer> app_srls,
                              @Param("member_srl") long member_srl,
                              @Param("member_srls") List<Long> member_srls,
                              @Param("enabled") int enabled,
                              @Param("limit") int limit,
                              @Param("offset") int offset);

    /**
     * update app member row
     *
     * @param appMemberEntity update data
     * @param target_app_srl target app_srl
     * @param target_member_srl target member_srl
     * @return updated row count
     */
    int modify(@Param("appMemberEntity") AppMemberEntity appMemberEntity,
               @Param("target_app_srl") int target_app_srl,
               @Param("target_member_srl") long target_member_srl);

    /**
     * delete app member row
     *
     * @param app_srl app_srl condition
     * @param member_srl member_srl condition
     * @return deleted row count
     */
    int delete(@Param("app_srl") int app_srl,
               @Param("member_srl") long member_srl);
}

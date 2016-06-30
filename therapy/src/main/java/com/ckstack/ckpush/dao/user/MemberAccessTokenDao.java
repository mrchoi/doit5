package com.ckstack.ckpush.dao.user;

import com.ckstack.ckpush.domain.user.MemberAccessTokenEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 8. 19..
 */
@Repository
@Transactional(value = "transactionManager")
public interface MemberAccessTokenDao {
    /**
     * insert data
     *
     * @param memberAccessTokenEntity inserted data
     * @return insert row count
     */
    int add(MemberAccessTokenEntity memberAccessTokenEntity);

    /**
     * count member's access token
     *
     * @param member_srl member_srl condition
     * @param app_srl app_srl condition
     * @param access_token access_token condition
     * @param token_expire token_expire condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("member_srl") long member_srl,
               @Param("app_srl") int app_srl,
               @Param("access_token") String access_token,
               @Param("token_expire") int token_expire);

    /**
     * select access_token one row
     *
     * @param token_srl token_srl condition
     * @param access_token access_token condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    MemberAccessTokenEntity get(@Param("token_srl") long token_srl,
                                @Param("access_token") String access_token);

    /**
     * select access_token multi row
     *
     * @param member_srl member_srl condition
     * @param app_srl app_srl condition
     * @param token_srls token_srls condition
     * @param token_expire token_expire condition
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<MemberAccessTokenEntity> get(@Param("member_srl") long member_srl,
                                      @Param("app_srl") int app_srl,
                                      @Param("token_srls") List<Long> token_srls,
                                      @Param("token_expire") int token_expire,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    /**
     * update access_token
     *
     * @param memberAccessTokenEntity update row data
     * @param target_token_srl target token_srl
     * @param target_access_token target access_token
     * @param target_app_srl target app_srl
     * @return updated row count
     */
    int modify(@Param("memberAccessTokenEntity") MemberAccessTokenEntity memberAccessTokenEntity,
               @Param("target_token_srl") long target_token_srl,
               @Param("target_access_token") String target_access_token,
               @Param("target_app_srl") int target_app_srl);

    /**
     * delete access_token
     *
     * @param token_srl token_srl condition
     * @param access_token access_token condition
     * @param app_srl app_srl condition
     * @return deleted row count
     */
    int delete(@Param("token_srl") long token_srl,
               @Param("access_token") String access_token,
               @Param("app_srl") int app_srl);
}

package com.ckstack.ckpush.dao.accessory;

import com.ckstack.ckpush.domain.accessory.DeviceAccessTokenEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 7. 10..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DeviceAccessTokenDao {
    /**
     * insert row to tbl_device_access_token
     *
     * @param deviceAccessTokenEntity insert data
     * @return inserted row data
     */
    int add(DeviceAccessTokenEntity deviceAccessTokenEntity);

    /**
     * row count of tbl_device_access_token
     *
     * @param device_srl device_srl condition
     * @param app_srl app_srl condition
     * @param access_token access_token condition
     * @param token_expire token expire condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("device_srl") long device_srl,
               @Param("app_srl") int app_srl,
               @Param("access_token") String access_token,
               @Param("token_expire") int token_expire);

    /**
     * select one row of tbl_device_access_token
     *
     * @param token_srl token_srl condition
     * @param access_token access_token condition
     * @return selected one row. if not found return null
     */
    @Transactional(readOnly = true)
    DeviceAccessTokenEntity get(@Param("token_srl") long token_srl,
                                @Param("access_token") String access_token);

    /**
     * select list row of tbl_device_access_token
     *
     * @param device_srl device_srl condition
     * @param app_srl app_srl condition
     * @param token_srls token_srls condition
     * @param token_expire token_expire condition
     * @param offset list offset
     * @param limit list limit
     * @return selected multi row. if not found return empty list
     */
    @Transactional(readOnly = true)
    List<DeviceAccessTokenEntity> get(@Param("device_srl") long device_srl,
                                      @Param("app_srl") int app_srl,
                                      @Param("token_srls") List<Long> token_srls,
                                      @Param("token_expire") int token_expire,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    /**
     * modify row of tbl_device_access_token.
     * it can modify device_srl, access_token, token_expire value.
     *
     * @param deviceAccessTokenEntity modify data
     * @param target_token_srl token_srl condition
     * @param target_access_token access_token condition
     * @return updated row count
     */
    int modify(@Param("deviceAccessTokenEntity") DeviceAccessTokenEntity deviceAccessTokenEntity,
               @Param("target_token_srl") long target_token_srl,
               @Param("target_access_token") String target_access_token,
               @Param("target_app_srl") int target_app_srl);

    /**
     * delete row
     *
     * @param token_srl token_srl condition
     * @param access_token access_token condition
     * @return deleted row count
     */
    int delete(@Param("token_srl") long token_srl,
               @Param("access_token") String access_token,
               @Param("app_srl") int app_srl);
}

package com.ckstack.ckpush.dao.app;

import com.ckstack.ckpush.domain.app.AppDeviceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 4. 20..
 */
@Repository
@Transactional(value = "transactionManager")
public interface AppDeviceDao {
    /**
     * tbl_app_device row insert
     *
     * @param appDeviceEntity insert data
     * @return inserted row count
     */
    int add(AppDeviceEntity appDeviceEntity);

    /**
     * tbl_app_device row count
     *
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @param allow_push allow_gcm condition
     * @param enabled enabled condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("device_srl") long device_srl,
               @Param("allow_push") int allow_push,
               @Param("enabled") int enabled);

    /**
     * tbl_app_device row
     *
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @param enabled enabled condition
     * @return one row or null
     */
    @Transactional(readOnly = true)
    AppDeviceEntity get(@Param("app_srl") int app_srl,
                        @Param("device_srl") long device_srl,
                        @Param("enabled") int enabled);

    /**
     * tbl_app_device row list
     *
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @param allow_push allow_gcm condition
     * @param enabled enabled condition
     * @param offset list offset
     * @param limit list limit
     * @return row list
     */
    @Transactional(readOnly = true)
    List<AppDeviceEntity> get(@Param("app_srl") int app_srl,
                              @Param("device_srl") long device_srl,
                              @Param("allow_push") int allow_push,
                              @Param("enabled") int enabled,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    /**
     * tbl_app_device row update
     *
     * @param appDeviceEntity update data
     * @param target_app_srl app_srl condition
     * @param target_device_srl device_srl condition
     * @return updated row count
     */
    int modify(@Param("appDeviceEntity") AppDeviceEntity appDeviceEntity,
               @Param("target_app_srl") int target_app_srl,
               @Param("target_device_srl") long target_device_srl);

    /**
     * tbl_app_device row delete
     *
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @return deleted row count
     */
    int delete(@Param("app_srl") int app_srl,
               @Param("device_srl") long device_srl);
}

package com.ckstack.ckpush.dao.accessory;

import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 3. 16..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DeviceDao {
    /**
     * tbl_android_device insert
     *
     * @param deviceEntity insert data
     * @return inserted row count
     */
    int add(DeviceEntity deviceEntity);

    /**
     * tbl_android_device row count
     * max_device_srl 을 이용하여 특정 device_srl 기준 이하로 카운트 할 수 있다.
     *
     * @param device_srls device_srls condition. device_srl list
     * @param max_device_srl max_device_srl condition
     * @return tbl_android_device row count
     */
    @Transactional(readOnly = true)
    long count(@Param("device_srls") List<Long> device_srls,
               @Param("max_device_srl") long max_device_srl);

    /**
     * tbl_android_device row select one
     * device_srl 이 0 보다 작고 device_id 이 null 이면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param device_srl device_srl condtion
     * @param device_id device_id condtion
     * @return tbl_android_device row one. select row 가 없으면 null 을 리턴 한다.
     */
    @Transactional(readOnly = true)
    DeviceEntity get(@Param("device_srl") long device_srl,
                     @Param("device_id") String device_id);

    /**
     * tbl_android_device row select list
     *
     * @param device_srls device_srls condition. device_srl list
     * @param max_device_srl max_device_srl condition
     * @param offset list offset
     * @param limit list limit
     * @return tbl_android_device row list. select row 가 없으면 empty list 를 리턴 한다.
     */
    @Transactional(readOnly = true)
    List<DeviceEntity> get(@Param("device_srls") List<Long> device_srls,
                           @Param("max_device_srl") long max_device_srl,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    /**
     * tbl_android_device, tbl_app join row count
     *
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @param device_id device_id condition. start prefix like condition
     * @param reg_push_id reg_push_id condition
     * @param allow_push allow_push condition
     * @param enabled enabled condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long countInApp(@Param("app_srl") int app_srl,
                    @Param("app_srls") List<Integer> app_srls,
                    @Param("device_srl") long device_srl,
                    @Param("device_id") String device_id,
                    @Param("device_type") String device_type,
                    @Param("device_class") int device_class,
                    @Param("os_version") String os_version,
                    @Param("mobile_phone_number") String mobile_phone_number,
                    @Param("reg_push_id") int reg_push_id,
                    @Param("allow_push") int allow_push,
                    @Param("enabled") int enabled);

    /**
     * tbl_android_device, tbl_app join row select list
     *
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @param device_id device_id condition. start prefix like condition
     * @param reg_push_id reg_push_id condition
     * @param allow_push allow_push condition
     * @param enabled enabled condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select row list
     */
    @Transactional(readOnly = true)
    List<DeviceEntity> getInApp(@Param("app_srl") int app_srl,
                                @Param("app_srls") List<Integer> app_srls,
                                @Param("device_srl") long device_srl,
                                @Param("device_id") String device_id,
                                @Param("device_type") String device_type,
                                @Param("device_class") int device_class,
                                @Param("os_version") String os_version,
                                @Param("mobile_phone_number") String mobile_phone_number,
                                @Param("reg_push_id") int reg_push_id,
                                @Param("allow_push") int allow_push,
                                @Param("enabled") int enabled,
                                @Param("sort") Map<String, String> sort,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    /**
     * tbl_android_device row update
     * target_device_srl 이 0 보다 작고 target_device_id 이 null 이면 BadSqlGrammarException exception 발생
     *
     * @param deviceEntity update data
     * @param target_device_srl device_srl condtion
     * @param target_device_id device_id condition
     * @return updated row count
     */
    int modify(@Param("deviceEntity") DeviceEntity deviceEntity,
               @Param("target_device_srl") long target_device_srl,
               @Param("target_device_id") String target_device_id);

    /**
     * tbl_android_device row delete
     * device_srl 이 0 보다 작고 device_srls 가 null 이나 size가 0이고 device_id 가 null 이면 BadSqlGrammarException exception 발생
     *
     * @param device_srl device_srl condtion
     * @param device_srls device_srls condition. device_srl list
     * @param device_id device_id condition
     * @return delete row count
     */
    int delete(@Param("device_srl") long device_srl,
               @Param("device_srls") List<Long> device_srls,
               @Param("device_id") String device_id);
}

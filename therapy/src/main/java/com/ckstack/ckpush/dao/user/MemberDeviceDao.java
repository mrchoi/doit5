package com.ckstack.ckpush.dao.user;

import com.ckstack.ckpush.domain.user.MemberDeviceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
@Repository
@Transactional(value = "transactionManager")
public interface MemberDeviceDao {
    /**
     * tbl_member_device row insert
     * @param memberDeviceEntity insert data
     * @return inserted row count
     */
    int add(MemberDeviceEntity memberDeviceEntity);

    /**
     * tbl_member_device row count
     *
     * @param member_srl member_srl condition
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @return tbl_member_device row count
     */
    @Transactional(readOnly = true)
    long count(@Param("member_srl") long member_srl,
               @Param("app_srl") int app_srl,
               @Param("device_srl") long device_srl);

    /**
     * tbl_member_device row select one
     * member_srl, app_srl, device_srl, device_class 중 하나가 모두 0 보다 작으면
     * MyBatisSystemException 이 발생 할 수 있다.(TooManyResultsException 상황임)
     *
     * @param member_srl member_srl condition
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @return tbl_member_device row one. select row 가 없으면 null 을 리턴 한다.
     */
    @Transactional(readOnly = true)
    MemberDeviceEntity get(@Param("member_srl") long member_srl,
                       @Param("app_srl") int app_srl,
                       @Param("device_srl") long device_srl);

    /**
     * tbl_member_device row select list
     *
     * @param member_srl member_srl condition
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @param offset offset condition
     * @param limit limit condition
     * @return tbl_member_device row select list. select row 가 없으면 empty list 를 리턴 한다.
     */
    @Transactional(readOnly = true)
    List<MemberDeviceEntity> get(@Param("member_srl") long member_srl,
                             @Param("app_srl") int app_srl,
                             @Param("device_srl") long device_srl,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    /**
     * tbl_member_device row update
     * target_member_srl, target_app_srl, target_device_srl, target_device_class 가 모두 0 보다
     * 작으면 BadSqlGrammarException exception 발생
     *
     * @param memberDeviceEntity update data
     * @param target_member_srl member_srl condition
     * @param target_app_srl app_srl condition
     * @param target_device_srl device_srl condition
     * @return updated row count
     */
    int modify(@Param("memberDeviceEntity") MemberDeviceEntity memberDeviceEntity,
               @Param("target_member_srl") long target_member_srl,
               @Param("target_app_srl") int target_app_srl,
               @Param("target_device_srl") long target_device_srl);

    /**
     * tbl_member_device row delete
     * target_member_srl, target_app_srl, target_device_srl, target_device_class 가 모두 0 보다
     * 작으면 BadSqlGrammarException exception 발생
     *
     * @param member_srl member_srl condition
     * @param app_srl app_srl condition
     * @param device_srl device_srl condition
     * @return delete row count
     */
    int delete(@Param("member_srl") long member_srl,
               @Param("app_srl") int app_srl,
               @Param("device_srl") long device_srl);
}

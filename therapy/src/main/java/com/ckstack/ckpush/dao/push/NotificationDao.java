package com.ckstack.ckpush.dao.push;

import com.ckstack.ckpush.domain.push.NotificationEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 16. 2. 21.
 */
public interface NotificationDao {
    /**
     * PUSH 메시지 정보를 저장한다.
     *
     * @param notificationEntity insert data
     * @return insert row count
     */
    int add(NotificationEntity notificationEntity);

    /**
     * select notifaction row count
     *
     * @param application_group application_group condition
     * @param application_srl application_srl condition
     * @param noti_type noti_type condition
     * @param member_srl member_srl condition
     * @param user_id user_id condition
     * @param push_status push_status condition
     * @param book_time book_time condition
     * @param noti_time1 noti_time1 condition
     * @return select row count
     */
    @Transactional(readOnly = true)
    long count(@Param("application_group") long application_group,
               @Param("application_srl") long application_srl,
               @Param("noti_type") int noti_type,
               @Param("member_srl") long member_srl,
               @Param("user_id") String user_id,
               @Param("push_status") int push_status,
               @Param("book_time") int book_time,
               @Param("noti_time1") int noti_time1);

    /**
     * select notification one row
     *
     * @param noti_srl noti_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    NotificationEntity get(@Param("noti_srl") long noti_srl);

    /**
     * select notification multi row
     *
     * @param application_group application_group condition
     * @param application_srl application_srl condition
     * @param noti_type noti_type condition
     * @param user_id user_id condition
     * @param push_status push_status condition
     * @param book_time book_time condition
     * @param noti_time1 noti_time1 condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<NotificationEntity> get(@Param("application_group") long application_group,
                                @Param("application_srl") long application_srl,
                                @Param("noti_type") int noti_type,
                                @Param("member_srl") long member_srl,
                                @Param("user_id") String user_id,
                                @Param("push_status") int push_status,
                                @Param("book_time") int book_time,
                                @Param("noti_time1") int noti_time1,
                                @Param("sort") Map<String, String> sort,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    /**
     * update notification row
     *
     * @param notificationEntity 업데이트 할 data
     * @param target_noti_srl 업데이트 할 noti_srl
     * @return updated row count
     */
    int modify(@Param("notificationEntity") NotificationEntity notificationEntity,
               @Param("target_noti_srl") long target_noti_srl);
}

package com.ckstack.ckpush.dao.user;

import com.ckstack.ckpush.domain.user.MemberExtraEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 24..
 */
@Repository
@Transactional(value = "transactionManager")
public interface MemberExtraDao {
    /**
     * tbl_member_extra 에 insert
     * @param memberExtraEntity insert 할 데이터
     * @return insert row count
     */
    int add(MemberExtraEntity memberExtraEntity);

    /**
     * tbl_member_extra 테이블에서 단일 row 를 select 한다.
     * member_srl 이 0 보다 작으면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param member_srl member_srl 조건
     * @return tbl_member_extra row 하나. 존재하지 않으면 null 을 리턴한다.
     */
    MemberExtraEntity get(@Param("member_srl") long member_srl);

    /**
     * tbl_member_extra 테이블에서 row list 를 select 한다.
     *
     * @param member_srls member_srls 조건. member_srl 리스트
     * @param social_type social_type 조건
     * @param social_id social_id 조건
     * @return tbl_member_extra row list. 존재하지 않으면 empty list 를 리턴한다.
     */
    List<MemberExtraEntity> get(@Param("member_srls") List<Long> member_srls,
                            @Param("social_type") int social_type,
                            @Param("social_id") String social_id);

    /**
     * tbl_member_extra 테이블 row 를 update 한다.
     * target_member_srl 이 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param memberExtraEntity update 할 데이터
     * @param target_member_srl update 대상의 member_srl
     * @return update row count
     */
    int modify(@Param("memberExtraEntity") MemberExtraEntity memberExtraEntity,
               @Param("target_member_srl") long target_member_srl);

    /**
     * tbl_member_extra 의 login_count 나 serial_login_count 를 +1 시킨다.
     *
     * @param increase_login_count true 이면 login_count 를 +1 시킨다.
     * @param increase_serial_login_count true 이면 serial_login_count 를 +1 시킨다.
     * @param target_member_srl member_srl 조건
     * @return update row count
     */
    int increase(@Param("increase_login_count") boolean increase_login_count,
                 @Param("increase_serial_login_count") boolean increase_serial_login_count,
                 @Param("target_member_srl") long target_member_srl);

    /**
     * tbl_member_extra 테이블 row 를 delete 한다.
     * member_srl 이 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param member_srl member_srl 조건
     * @return delete row count
     */
    int delete(long member_srl);
}

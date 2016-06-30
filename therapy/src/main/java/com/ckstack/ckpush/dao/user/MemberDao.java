package com.ckstack.ckpush.dao.user;

import com.ckstack.ckpush.domain.user.MemberEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 3. 23..
 */
@Repository
@Transactional(value = "transactionManager")
public interface MemberDao {
    /**
     * tbl_member 에 insert
     *
     * @param memberEntity insert 할 데이터
     * @return insert row count
     */
    int add(MemberEntity memberEntity);

    /**
     * tbl_member 테이블에서 row count 한다.
     * max_member_srl 을 이용하여 특정 member_srl 기준 이하로 카운트 할 수 있다.
     *
     * @param user_name user_name 조건
     * @param nick_name nick_name 조건
     * @param mobile_phone_number mobile_phone_number 조건
     * @param max_member_srl max_member_srl 조건
     * @return 특정 조건에 따른 tbl_member row 카운트
     */
    @Transactional(readOnly = true)
    long count(@Param("user_name") String user_name,
               @Param("nick_name") String nick_name,
               @Param("mobile_phone_number") String mobile_phone_number,
               @Param("max_member_srl") long max_member_srl);

    /**
     * tbl_member 테이블에서 단일 row 를 select 한다.
     * member_srl 이 0 보다 작고 user_id 가 null 이면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param member_srl select row 의 member_srl 조건
     * @param user_id select row 의 user_id 조건
     * @return tbl_member row 하나. 존재하지 않으면 null 을 리턴한다.
     */
    @Transactional(readOnly = true)
    MemberEntity get(@Param("member_srl") long member_srl,
                 @Param("user_id") String user_id);

    /**
     * tbl_member 테이블에서 row list 를 select 한다.
     * max_member_srl 을 이용하여 특정 member_srl 기준 이하로 select list 할 수 있다.
     *
     * @param user_name user_name 조건
     * @param nick_name nick_name 조건
     * @param mobile_phone_number mobile_phone_number 조건
     * @param max_member_srl max_member_srl 조건
     * @param offset list 의 select offset
     * @param limit list 의 select limit
     * @return tbl_member row list. 존재하지 않으면 empty list 를 리턴한다.
     */
    @Transactional(readOnly = true)
    List<MemberEntity> get(@Param("user_ids") List<String> user_ids,
                           @Param("user_name") String user_name,
                           @Param("nick_name") String nick_name,
                           @Param("mobile_phone_number") String mobile_phone_number,
                           @Param("max_member_srl") long max_member_srl,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    /**
     * tbl_member 테이블에서 email_address와 user_name으로 row list를 select한다.
     *
     * @param email_address email_address 조건
     * @param user_name user_name 조건
     * @return tbl_member row list. 존재하지 않으면 empty list 를 리턴한다.
     */
    @Transactional(readOnly = true)
    List<MemberEntity> get(@Param("email_address") String email_address,
                           @Param("user_name") String user_name);
    /**
     * tbl_member, tbl_member_extra join select row count
     * max_member_srl 을 이용하여 특정 member_srl 기준 이하로 select list 할 수 있다.
     *
     * @param user_id user_id condition
     * @param user_name user_name 조건
     * @param nick_name nick_name 조건
     * @param mobile_phone_number mobile_phone_number 조건
     * @param enabled enabled condition
     * @param sign_out sign_out condition
     * @param social_type social_type 조건
     * @param social_id social_id 조건
     * @param max_member_srl max_member_srl 조건
     * @param is_like user_id 로 like 검색 할지 말지 여부. true 이면 like 검색 한다.
     * @return row count
     */
    @Transactional(readOnly = true)
    long countFullInfo(@Param("user_id") String user_id,
                       @Param("user_name") String user_name,
                       @Param("nick_name") String nick_name,
                       @Param("mobile_phone_number") String mobile_phone_number,
                       @Param("enabled") int enabled,
                       @Param("sign_out") int sign_out,
                       @Param("social_type") int social_type,
                       @Param("social_id") String social_id,
                       @Param("max_member_srl") long max_member_srl,
                       @Param("is_like") boolean is_like);

    /**
     * tbl_member, tbl_member_extra join select one row
     *
     * @param member_srl member_srl 조건
     * @param user_id user_id 조건
     * @return select one row
     */
    @Transactional(readOnly = true)
    MemberEntity getFullInfo(@Param("member_srl") long member_srl,
                             @Param("user_id") String user_id);

    /**
     * tbl_member, tbl_member_extra join select list
     * max_member_srl 을 이용하여 특정 member_srl 기준 이하로 select list 할 수 있다.
     *
     * @param member_srls member_srl list condition
     * @param user_id user_id condition
     * @param user_name user_name 조건
     * @param nick_name nick_name 조건
     * @param mobile_phone_number mobile_phone_number 조건
     * @param enabled enabled condition
     * @param sign_out sign_out condition
     * @param social_type social_type 조건
     * @param social_id social_id 조건
     * @param max_member_srl max_member_srl 조건
     * @param offset list offset
     * @param limit list limit
     * @param is_like user_id 로 like 검색 할지 말지 여부. true 이면 like 검색 한다.
     * @return select row list
     */
    @Transactional(readOnly = true)
    List<MemberEntity> getFullInfo(@Param("member_srls") List<Long> member_srls,
                                   @Param("user_id") String user_id,
                                   @Param("user_name") String user_name,
                                   @Param("nick_name") String nick_name,
                                   @Param("mobile_phone_number") String mobile_phone_number,
                                   @Param("enabled") int enabled,
                                   @Param("sign_out") int sign_out,
                                   @Param("social_type") int social_type,
                                   @Param("social_id") String social_id,
                                   @Param("max_member_srl") long max_member_srl,
                                   @Param("sort") Map<String, String> sort,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit,
                                   @Param("is_like") boolean is_like);

    /**
     * tbl_member 테이블 row 를 update 한다.
     * target_member_srl 이 0 보다 작고 target_user_id 이 null 이면 BadSqlGrammarException exception 발생
     *
     * @param memberEntity update 할 데이터
     * @param target_member_srl update 대상의 member_srl
     * @param target_user_id update 대상의 user_id
     * @return update row count
     */
    int modify(@Param("memberEntity") MemberEntity memberEntity,
               @Param("target_member_srl") long target_member_srl,
               @Param("target_user_id") String target_user_id);

    /**
     * tbl_member 테이블 row 를 delete 한다.
     * member_srl 이 0 보다 작고 user_id 가 null 이면 BadSqlGrammarException exception 발생
     *
     * @param member_srl member_srl 조건
     * @param user_id user_id 조건
     * @return delete 된 row count
     */
    int delete(@Param("member_srl") long member_srl,
               @Param("user_id") String user_id,
               @Param("member_srls") List<Long> member_srls);

    @Transactional(readOnly = true)
    long countMemberByGroup(@Param("group_srl") long group_srl,
                            @Param("group_srls") List<Long> group_srls,
                            @Param("user_id") String user_id,
                            @Param("user_name") String user_name,
                            @Param("nick_name") String nick_name,
                            @Param("enabled") int enabled,
                            @Param("sign_out") int sign_out,
                            @Param("class_srl") int class_srl);

    /**
     * tbl_member 테이블에서 row list 를 select 한다.
     *
     * @param group_srl group_srl 조건
     */
    List<MemberEntity> getMemberListByGroup(@Param("group_srl") long group_srl,
                                            @Param("group_srls") List<Long> group_srls,
                                            @Param("user_id") String user_id,
                                            @Param("user_name") String user_name,
                                            @Param("nick_name") String nick_name,
                                            @Param("enabled") int enabled,
                                            @Param("sign_out") int sign_out,
                                            @Param("class_srl") int class_srl,
                                            @Param("sort") Map<String, String> sort,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);
    /**
     * ------------------------------------------------------------------------------
     * added by kodaji
     */

    /**
     * tbl_member, tbl_group_member join select row count
     * max_member_srl 을 이용하여 특정 member_srl 기준 이하로 select list 할 수 있다.
     *
     * @param user_id user_id condition
     * @param user_name user_name 조건
     * @param nick_name nick_name 조건
     * @param mobile_phone_number mobile_phone_number 조건
     * @param enabled enabled condition
     * @param sign_out sign_out condition
     * @param group_srl group_srl 조건
     * @param max_member_srl max_member_srl 조건
     * @param is_like user_id 로 like 검색 할지 말지 여부. true 이면 like 검색 한다.
     * @return row count
     */
    @Transactional(readOnly = true)
    long countGroupFullInfo(@Param("user_id") String user_id,
                           @Param("user_name") String user_name,
                           @Param("nick_name") String nick_name,
                           @Param("mobile_phone_number") String mobile_phone_number,
                           @Param("enabled") int enabled,
                           @Param("sign_out") int sign_out,
                           @Param("group_srl") int group_srl,
                           @Param("max_member_srl") long max_member_srl,
                           @Param("is_like") boolean is_like);

    /**
     * tbl_member, tbl_member_extra join select list
     * max_member_srl 을 이용하여 특정 member_srl 기준 이하로 select list 할 수 있다.
     *
     * @param member_srls member_srl list condition
     * @param user_id user_id condition
     * @param user_name user_name 조건
     * @param nick_name nick_name 조건
     * @param mobile_phone_number mobile_phone_number 조건
     * @param enabled enabled condition
     * @param sign_out sign_out condition
     * @param group_srl group_srl 조건
     * @param max_member_srl max_member_srl 조건
     * @param offset list offset
     * @param limit list limit
     * @param is_like user_id 로 like 검색 할지 말지 여부. true 이면 like 검색 한다.
     * @return select row list
     */
    @Transactional(readOnly = true)
    List<MemberEntity> getGroupFullInfo(@Param("member_srls") List<Long> member_srls,
                                       @Param("user_id") String user_id,
                                       @Param("user_name") String user_name,
                                       @Param("nick_name") String nick_name,
                                       @Param("mobile_phone_number") String mobile_phone_number,
                                       @Param("enabled") int enabled,
                                       @Param("sign_out") int sign_out,
                                       @Param("group_srl") int group_srl,
                                       @Param("max_member_srl") long max_member_srl,
                                       @Param("sort") Map<String, String> sort,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit,
                                       @Param("is_like") boolean is_like);

}

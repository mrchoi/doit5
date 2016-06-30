package com.ckstack.ckpush.dao.user;

import com.ckstack.ckpush.domain.user.GroupEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
@Repository
@Transactional(value = "transactionManager")
public interface GroupDao {
    /**
     * tbl_group row insert
     * @param groupEntity insert data
     * @return inserted row count
     */
    int add(GroupEntity groupEntity);

    /**
     * tbl_group row count
     * max_group_srl 을 이용하여 특정 group_srl 기준 이하로 카운트 할 수 있다.
     *
     * @param allow_default allow_default 조건
     * @param authority authority 조건
     * @param max_group_srl max_group_srl 조건
     * @return tbl_group row count
     */
    @Transactional(readOnly = true)
    long count(@Param("allow_default") int allow_default,
               @Param("authority") String authority,
               @Param("max_group_srl") int max_group_srl);

    /**
     * tbl_group row select one
     * group_srl 이 0 보다 작으면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param group_srl group_srl condition
     * @return tbl_group row only one. select row 가 없으면 null 을 리턴 한다.
     */
    @Transactional(readOnly = true)
    GroupEntity get(@Param("group_srl") int group_srl);

    /**
     * tbl_group row select list
     * max_group_srl 을 이용하여 특정 group_srl 기준 이하로 select list 할 수 있다.
     *
     * @param group_srls group_srls condition
     * @param allow_default allow_default condition
     * @param authority authority condition
     * @param max_group_srl max_group_srl condition
     * @param offset offset condition
     * @param limit limit condition
     * @return tbl_group row select list. select row 가 없으면 empty list 를 리턴 한다.
     */
    @Transactional(readOnly = true)
    List<GroupEntity> get(@Param("group_srls") List<Integer> group_srls,
                      @Param("allow_default") int allow_default,
                      @Param("authority") String authority,
                      @Param("max_group_srl") int max_group_srl,
                      @Param("offset") int offset,
                      @Param("limit") int limit);

    /**
     * tbl_group row update
     * target_group_srl 이 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param groupEntity update data
     * @param target_group_srl group_srl condition
     * @return updated row count
     */
    int modify(@Param("groupEntity") GroupEntity groupEntity,
               @Param("target_group_srl") int target_group_srl);

    /**
     * tbl_group row delete
     * group_srl 이 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param group_srl group_srl condition
     * @return deleted row count
     */
    int delete(int group_srl);
}

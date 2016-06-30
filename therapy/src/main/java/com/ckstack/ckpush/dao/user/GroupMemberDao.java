package com.ckstack.ckpush.dao.user;

import com.ckstack.ckpush.domain.user.GroupMemberEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 27..
 */
@Repository
@Transactional(value = "transactionManager")
public interface GroupMemberDao {
    /**
     * tbl_group_member insert row
     * @param groupMemberEntity insert data
     * @return inserted row count
     */
    int add(GroupMemberEntity groupMemberEntity);

    /**
     * tbl_group_member row count
     *
     * @param group_srl group_srl condition
     * @param member_srl member_srl condition
     * @return tbl_group_member row count.
     */
    @Transactional(readOnly = true)
    long count(@Param("group_srl") int group_srl,
               @Param("member_srl") long member_srl);

    /**
     * tbl_group_member row one.
     * group_srl 이 0 보다 작고 member_srl 이 0 보다 작으면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param group_srl group_srl condition
     * @param member_srl member_srl condition
     * @return tbl_group_member row one. select row 가 없으면 null 을 리턴 한다.
     */
    @Transactional(readOnly = true)
    GroupMemberEntity get(@Param("group_srl") int group_srl,
                          @Param("member_srl") long member_srl);

    /**
     * tbl_group_member row list
     *
     * @param group_srl group_srl condition
     * @param member_srl member_srl condition
     * @param offset offset condition
     * @param limit limit condition
     * @return tbl_group_member row list. select row 가 없으면 empty list 를 리턴 한다.
     */
    @Transactional(readOnly = true)
    List<GroupMemberEntity> get(@Param("group_srl") int group_srl,
                                @Param("member_srl") long member_srl,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    /**
     * tbl_group_member row delete.
     * group_srl 이 0 보다 작고 member_srl 이 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param group_srl group_srl condition
     * @param member_srl member_srl condition
     * @return deleted row count
     */
    int delete(@Param("group_srl") int group_srl,
               @Param("member_srl") long member_srl);
}

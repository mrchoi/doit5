package com.ckstack.ckpush.dao.mineral;

import com.ckstack.ckpush.domain.mineral.MemberPicEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 4. 3..
 */
@Repository
@Transactional(value = "transactionManager")
public interface MemberPicDao {
    /**
     * tbl_member_pic row insert
     *
     * @param memberPicEntity insert data
     * @return inserted row count
     */
    int add(MemberPicEntity memberPicEntity);

    /**
     * tbl_member_pic row count
     *
     * @param member_srl member_srl condition
     * @param file_srl file_srl condition
     * @return tbl_member_pic row count
     */
    @Transactional(readOnly = true)
    long count(@Param("member_srl") long member_srl,
               @Param("file_srl") long file_srl);

    /**
     * tbl_member_pic row select one.
     * member_srl, file_srl 이 모두 0 보다 작으면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param member_srl member_srl condition
     * @param file_srl file_srl condition
     * @return tbl_member_pic one row. select row 가 없으면 null 을 리턴 한다.
     */
    @Transactional(readOnly = true)
    MemberPicEntity get(@Param("member_srl") long member_srl,
                        @Param("file_srl") long file_srl);

    /**
     * tbl_member_pic row select list
     *
     * @param member_srl member_srl condition
     * @param member_srls member_srls condition. member_srl list
     * @param file_srl file_srl condition
     * @param file_srls file_srls condition. file_srl list
     * @param offset list offset
     * @param limit list limit
     * @return tbl_member_pic select list. list. select row 가 없으면 empty list 를 리턴 한다.
     */
    @Transactional(readOnly = true)
    List<MemberPicEntity> get(@Param("member_srl") long member_srl,
                              @Param("member_srls") List<Long> member_srls,
                              @Param("file_srl") long file_srl,
                              @Param("file_srls") List<Long> file_srls,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    /**
     * tbl_member_pic, tbl_profile_pic join. select one row
     * member_srl 이 0 보다 작으면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param member_srl member_srl condition
     * @param deleted deleted condition
     * @return tbl_member_pic, tbl_profile_pic join. select one row
     */
    @Transactional(readOnly = true)
    MemberPicEntity getAndFile(@Param("member_srl") long member_srl,
                               @Param("deleted") int deleted);

    /**
     * tbl_member_pic, tbl_profile_pic join. select list row
     *
     * @param member_srls member_srls condition. member_srl list
     * @param deleted deleted condition
     * @return tbl_member_pic, tbl_profile_pic join. select list
     */
    @Transactional(readOnly = true)
    List<MemberPicEntity> getAndFile(@Param("member_srls") List<Long> member_srls,
                                     @Param("deleted") int deleted);

    /**
     * tbl_member_pic row delete.
     * member_srl, file_srl 이 모두 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param member_srl member_srl condition
     * @param file_srl file_srl condition
     * @return delete row count
     */
    int delete(@Param("member_srl") long member_srl,
               @Param("file_srl") long file_srl);
}

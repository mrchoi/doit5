package com.ckstack.ckpush.dao.mineral;

import com.ckstack.ckpush.domain.mineral.FileEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 4. 1..
 */
@Repository
@Transactional(value = "transactionManager")
public interface FileDao {
    /**
     * 각 파일 관리 테이블에 row insert
     * @param table_name 파일 관리 테이블명. 'tbl_' 는 없이 사용
     * @param fileEntity insert data
     * @return inserted row count
     */
    int add(@Param("table_name") String table_name,
            @Param("fileEntity") FileEntity fileEntity);

    /**
     * 각 파일 관리 테이블의 row count
     * max_file_srl 을 이용하여 특정 file_srl 기준 이하로 카운트 할 수 있다.
     *
     * @param table_name 파일 관리 테이블명. 'tbl_' 는 없이 사용
     * @param file_srls file_srls condition. file_srl list
     * @param orig_name 원본 파일명. 첫글자 부터 like 검색
     * @param deleted deleted condition
     * @param max_file_srl max_file_srl condition
     * @return 각 파일 관리 테이블 row count
     */
    @Transactional(readOnly = true)
    long count(@Param("table_name") String table_name,
               @Param("file_srls") List<Long> file_srls,
               @Param("orig_name") String orig_name,
               @Param("deleted") int deleted,
               @Param("max_file_srl") long max_file_srl);

    /**
     * 각 파일 관리 테이블의 row select one.
     * file_srl 이 0 보다 작으면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param table_name 파일 관리 테이블명. 'tbl_' 는 없이 사용
     * @param file_srl file_srl condition
     * @param deleted deleted condition
     * @return 파일 관리 테이블 one row
     */
    @Transactional(readOnly = true)
    FileEntity get(@Param("table_name") String table_name,
                   @Param("file_srl") long file_srl,
                   @Param("deleted") int deleted);

    /**
     * 각 파일 관리 테이블 select list
     * max_file_srl 을 이용하여 특정 file_srl 기준 이하로 select 할 수 있다.
     *
     * @param table_name 파일 관리 테이블명. 'tbl_' 는 없이 사용
     * @param file_srls file_srls condition. file_srl list
     * @param orig_name 원본 파일명. 첫글자 부터 like 검색
     * @param deleted deleted condition
     * @param max_file_srl max_file_srl condition
     * @param offset list offset
     * @param limit list limit
     * @return 각 파일 관리 테이블 row select list
     */
    @Transactional(readOnly = true)
    List<FileEntity> get(@Param("table_name") String table_name,
                         @Param("file_srls") List<Long> file_srls,
                         @Param("deleted") int deleted,
                         @Param("orig_name") String orig_name,
                         @Param("max_file_srl") long max_file_srl,
                         @Param("sort") Map<String, String> sort,
                         @Param("offset") int offset,
                         @Param("limit") int limit);

    /**
     * 각 파일 관리 테이블 row update.
     * target_file_srl 이 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param table_name 파일 관리 테이블명. 'tbl_' 는 없이 사용
     * @param fileEntity update data
     * @param target_file_srl file_srl condition
     * @param target_file_srls file_srls condition
     * @return updated row count
     */
    int modify(@Param("table_name") String table_name,
               @Param("fileEntity") FileEntity fileEntity,
               @Param("target_file_srl") long target_file_srl,
               @Param("target_file_srls") List<Long> target_file_srls);

    /**
     * 각 파일 관리 테이블 row delete
     * file_srl 이 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param table_name 파일 관리 테이블명. 'tbl_' 는 없이 사용
     * @param file_srl file_srl condition
     * @return delete row count
     */
    int delete(@Param("table_name") String table_name,
               @Param("file_srl") long file_srl);
}

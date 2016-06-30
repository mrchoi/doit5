package com.ckstack.ckpush.dao.mineral;

import com.ckstack.ckpush.domain.mineral.FileRepositoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 6. 29..
 */
@Repository
@Transactional(value = "transactionManager")
public interface FileRepositoryDao {
    /**
     * 파일 저장용 테이블에 insert
     *
     * @param fileRepositoryEntity insert row data
     * @return inserted row count
     */
    int add(FileRepositoryEntity fileRepositoryEntity);

    /**
     * select count
     *
     * @param file_srls file_srls condition
     * @param orig_name orig_name condition
     * @param user_id user_id condition
     * @param deleted deleted condition
     * @param max_file_srl max_file_srl condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("file_srls") List<Long> file_srls,
               @Param("orig_name") String orig_name,
               @Param("user_id") String user_id,
               @Param("deleted") int deleted,
               @Param("max_file_srl") long max_file_srl);

    @Transactional(readOnly = true)
    long countPlymind(@Param("file_srls") List<Long> file_srls,
                      @Param("file_type") int file_type,
                      @Param("file_types") List<Integer> file_types,
               @Param("orig_name") String orig_name,
               @Param("user_id") String user_id,
               @Param("deleted") int deleted,
               @Param("max_file_srl") long max_file_srl);

    /**
     * select one row
     *
     * @param file_srl file_srl condition
     * @param deleted deleted condition
     * @return row one. 없으면 null
     */
    @Transactional(readOnly = true)
    FileRepositoryEntity get(@Param("file_srl") long file_srl,
                             @Param("deleted") int deleted);


    /**
     * select multiple row
     *
     * @param file_srls file_srls condition
     * @param deleted deleted condition
     * @param orig_name orig_name condition
     * @param user_id user_id condition
     * @param max_file_srl max_file_srl condition
     * @param sort sort 형태
     * @param offset list offset
     * @param limit list limit
     * @return multiple row. 없으면 empty list
     */
    @Transactional(readOnly = true)
    List<FileRepositoryEntity> get(@Param("file_srls") List<Long> file_srls,
                                   @Param("deleted") int deleted,
                                   @Param("orig_name") String orig_name,
                                   @Param("user_id") String user_id,
                                   @Param("max_file_srl") long max_file_srl,
                                   @Param("sort") Map<String, String> sort,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    @Transactional(readOnly = true)
    List<FileRepositoryEntity> getPlymind(@Param("file_srls") List<Long> file_srls,
                                   @Param("deleted") int deleted,
                                   @Param("orig_name") String orig_name,
                                   @Param("file_type") int file_type,
                                   @Param("file_types") List<Integer> file_types,
                                   @Param("user_id") String user_id,
                                   @Param("max_file_srl") long max_file_srl,
                                   @Param("sort") Map<String, String> sort,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    /**
     * modify row
     *
     * @param fileRepositoryEntity modify row data
     * @param target_file_srl modify condition
     * @return updated row data
     */
    int modify(@Param("fileRepositoryEntity") FileRepositoryEntity fileRepositoryEntity,
               @Param("target_file_srl") long target_file_srl);

    /**
     * delete row
     *
     * @param file_srl delete condition
     * @return deleted row count
     */
    int delete(@Param("file_srl") long file_srl);
}

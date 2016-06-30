package com.ckstack.ckpush.dao.mineral;

import com.ckstack.ckpush.domain.mineral.DocumentAttachEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 8. 10..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DocumentAttachDao {
    /**
     * insert data
     *
     * @param documentAttachEntity insert data
     * @return inserted row count
     */
    int add(DocumentAttachEntity documentAttachEntity);

    /**
     * select document's file count
     *
     * @param file_srls file_srls condition
     * @param orig_name orig_name condition
     * @param member_srl member_srl condition
     * @param deleted deleted condition
     * @param max_file_srl max_file_srl condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("file_srls") List<Long> file_srls,
               @Param("orig_name") String orig_name,
               @Param("member_srl") long member_srl,
               @Param("deleted") int deleted,
               @Param("max_file_srl") long max_file_srl);

    /**
     * select document's file one row
     *
     * @param file_srl file_srl condition
     * @param deleted deleted condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    DocumentAttachEntity get(@Param("file_srl") long file_srl,
                             @Param("deleted") int deleted);

    /**
     * select doument's file multi row
     *
     * @param file_srls file_srls condition
     * @param deleted deleted condition
     * @param orig_name orig_name condition
     * @param member_srl member_srl condition
     * @param max_file_srl max_file_srl condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<DocumentAttachEntity> get(@Param("file_srls") List<Long> file_srls,
                                   @Param("deleted") int deleted,
                                   @Param("orig_name") String orig_name,
                                   @Param("member_srl") long member_srl,
                                   @Param("max_file_srl") long max_file_srl,
                                   @Param("sort") Map<String, String> sort,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    /**
     * modify document's file row
     *
     * @param documentAttachEntity updated data
     * @param target_file_srl target file_srl
     * @return updated row count
     */
    int modify(@Param("documentAttachEntity") DocumentAttachEntity documentAttachEntity,
               @Param("target_file_srl") long target_file_srl,
               @Param("target_file_srls") List<Long> target_file_srls);

    /**
     * delete document's file row
     *
     * @param file_srl file_srl condition
     * @return deleted row count
     */
    int delete(@Param("file_srl") long file_srl);
}

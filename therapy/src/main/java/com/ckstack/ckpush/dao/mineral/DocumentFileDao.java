package com.ckstack.ckpush.dao.mineral;

import com.ckstack.ckpush.domain.mineral.DocumentFileEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dhkim94 on 15. 8. 10..
 */
@Repository
@Transactional(value = "transactionManager")
public interface DocumentFileDao {
    /**
     * tbl_document_file row insert
     *
     * @param documentFileEntity insert data
     * @return inserted row count
     */
    int add(DocumentFileEntity documentFileEntity);

    /**
     * tbl_document_file row count
     *
     * @param document_srl document_srl condition
     * @param file_srl file_srl condition
     * @return select row count
     */
    @Transactional(readOnly = true)
    long count(@Param("document_srl") long document_srl,
               @Param("file_srl") long file_srl);

    /**
     * tbl_document_file row select one.
     *
     * @param document_srl document_srl condition
     * @param file_srl file_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    DocumentFileEntity get(@Param("document_srl") long document_srl,
                           @Param("file_srl") long file_srl);

    /**
     * tbl_document_file row select list
     *
     * @param document_srl document_srl condition
     * @param document_srls document_srls condition
     * @param file_srl file_srl condition
     * @param file_srls file_srls condition
     * @param offset list offset
     * @param limit list limit
     * @return select multi row
     */
    @Transactional(readOnly = true)
    List<DocumentFileEntity> get(@Param("document_srl") long document_srl,
                                 @Param("document_srls") List<Long> document_srls,
                                 @Param("file_srl") long file_srl,
                                 @Param("file_srls") List<Long> file_srls,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    /**
     * select one row with file
     *
     * @param document_srl document_srl condition
     * @param deleted deleted condition
     * @return select row with file
     */
    @Transactional(readOnly = true)
    DocumentFileEntity getAndFile(@Param("document_srl") long document_srl,
                                  @Param("deleted") int deleted);

    /**
     * select multi row with file
     *
     * @param document_srls document_srls condition
     * @param deleted deleted condition
     * @return select multi row with file
     */
    @Transactional(readOnly = true)
    List<DocumentFileEntity> getAndFile(@Param("document_srls") List<Long> document_srls,
                                        @Param("deleted") int deleted);

    /**
     * delete row
     *
     * @param document_srl document_srl condition
     * @param file_srl file_srl condition
     * @return deleted row count
     */
    int delete(@Param("document_srl") long document_srl,
               @Param("file_srl") long file_srl,
               @Param("file_srls") List<Long> file_srls);
}

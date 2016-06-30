package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.TemplateEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 7. 22..
 */
@Repository
@Transactional(value = "transactionManager")
public interface TemplateDao {
    /**
     * insert template row
     *
     * @param templateEntity inserted data
     * @return insert row count
     */
    int add(TemplateEntity templateEntity);

    /**
     * template row count
     *
     * @param template_srl template_srl condition
     * @param template_title template_title condition
     * @return select row count
     */
    @Transactional(readOnly = true)
    long count(@Param("template_srl") int template_srl,
               @Param("template_title") String template_title);

    /**
     * template row select one
     *
     * @param template_srl template_srl condition
     * @return select row one
     */
    @Transactional(readOnly = true)
    TemplateEntity get(@Param("template_srl") int template_srl);

    /**
     * template row select multi
     *
     * @param template_srls template_srls condition
     * @param template_title template_title condition
     * @param sort list sort
     * @param offset list offset
     * @param limit list limit
     * @return select row multi
     */
    @Transactional(readOnly = true)
    List<TemplateEntity> get(@Param("template_srls") List<Integer> template_srls,
                             @Param("template_title") String template_title,
                             @Param("sort") Map<String, String> sort,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    /**
     * update template row
     *
     * @param templateEntity update data
     * @param target_template_srl target template_srl
     * @return updated row count
     */
    int modify(@Param("templateEntity") TemplateEntity templateEntity,
               @Param("target_template_srl") int target_template_srl);

    /**
     * delete template row
     *
     * @param template_srl template_srl condition
     * @param template_srls template_srls condition
     * @return deleted row count
     */
    int delete(@Param("template_srl") int template_srl,
               @Param("template_srls") List<Integer> template_srls);
}

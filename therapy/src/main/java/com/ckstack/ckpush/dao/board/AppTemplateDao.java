package com.ckstack.ckpush.dao.board;

import com.ckstack.ckpush.domain.board.AppTemplateEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 7. 23..
 */
@Repository
@Transactional(value = "transactionManager")
public interface AppTemplateDao {
    /**
     * insert app template data
     *
     * @param appTemplateEntity insert data
     * @return inserted row count
     */
    int add(AppTemplateEntity appTemplateEntity);

    /**
     * count app template row
     *
     * @param app_srl app_srl condition
     * @param template_srl template_srl condition
     * @return row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_srl") int app_srl,
               @Param("template_srl") int template_srl);

    /**
     * 템플릿별 앱에 할당된 카운트를 구한다.
     *
     * @param template_srls template_srls condition
     * @return group by count
     */
    @Transactional(readOnly = true)
    List<Map<String, Object>> countTemplateUsingApp(@Param("template_srls") List<Integer> template_srls);

    /**
     * select app template one row
     *
     * @param app_srl app_srl condition
     * @param template_srl template_srl condition
     * @return select one row
     */
    @Transactional(readOnly = true)
    AppTemplateEntity get(@Param("app_srl") int app_srl,
                          @Param("template_srl") int template_srl);

    /**
     * select app template list
     *
     * @param app_srl app_srl condition
     * @param template_srl template_srl condition
     * @param sort list sort map
     * @param offset list offset
     * @param limit list limit
     * @return select app template multi row
     */
    @Transactional(readOnly = true)
    List<AppTemplateEntity> get(@Param("app_srl") int app_srl,
                                @Param("template_srl") int template_srl,
                                @Param("sort") Map<String, String> sort,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    /**
     * delete app template row
     *
     * @param app_srl app_srl condition
     * @param app_srls app_srls condition
     * @param template_srl template_srl condition
     * @return deleted row count
     */
    int delete(@Param("app_srl") int app_srl,
               @Param("app_srls") List<Integer> app_srls,
               @Param("template_srl") int template_srl);
}

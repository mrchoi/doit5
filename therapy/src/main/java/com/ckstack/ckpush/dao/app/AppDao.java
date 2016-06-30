package com.ckstack.ckpush.dao.app;

import com.ckstack.ckpush.domain.app.AppEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 3. 25..
 */
@Repository
@Transactional(value = "transactionManager")
public interface AppDao {
    /**
     * tbl_app row insert
     * @param appEntity insert data
     * @return inserted row count
     */
    int add(AppEntity appEntity);

    /**
     * tbl_app row count
     * max_app_srl 을 이용하여 특정 app_srl 기준 이하로 select list 할 수 있다.
     *
     * @param app_id application id
     * @param app_name app_name like condition. 'app_name%' 로 like 된다.
     * @param api_key api_key condition
     * @param app_version app_version condition
     * @param enabled enabled condition
     * @param max_app_srl max_app_srl condition
     * @return tbl_app row count
     */
    @Transactional(readOnly = true)
    long count(@Param("app_id") String app_id,
               @Param("app_name") String app_name,
               @Param("api_key") String api_key,
               @Param("app_version") String app_version,
               @Param("enabled") int enabled,
               @Param("max_app_srl") int max_app_srl);

    /**
     * tbl_app row select one.
     * app_srl 가 0 보다 작고 app_key 가 null 이면 MyBatisSystemException 이 발생 한다.(TooManyResultsException 상황임)
     *
     * @param app_srl app_srl condition
     * @param app_id app_id condition
     * @param api_key api_key condition
     * @return tbl_app row one. select row 가 없으면 null 을 리턴 한다.
     */
    @Transactional(readOnly = true)
    AppEntity get(@Param("app_srl") int app_srl,
                  @Param("app_id") String app_id,
                  @Param("api_key") String api_key);

    /**
     * tbl_app row select list.
     * max_app_srl 을 이용하여 특정 app_srl 기준 이하로 select list 할 수 있다.
     *
     * @param app_srls app_srls condition. app_srl list
     * @param app_name app_name like condition. 'app_name%' 로 like 된다.
     * @param api_key api_key condition
     * @param app_version api_version condition
     * @param enabled enabled condition
     * @param max_app_srl max_app_srl condition
     * @param sort ordering 칼럼과 방향. {column:value, dir:value} 형태의 linked hashmap (순서가 있기 때문에 LinkedHashMap 임)
     *             - column : ordering 할 칼럼명
     *             - dir    : ordering 방향. desc, asc 중의 하나
     * @param offset list offset
     * @param limit list limit
     * @return tbl_app row select list
     */
    @Transactional(readOnly = true)
    List<AppEntity> get(@Param("app_srls") List<Integer> app_srls,
                        @Param("app_name") String app_name,
                        @Param("api_key") String api_key,
                        @Param("app_version") String app_version,
                        @Param("enabled") int enabled,
                        @Param("max_app_srl") int max_app_srl,
                        @Param("sort") Map<String, String> sort,
                        @Param("offset") int offset,
                        @Param("limit") int limit);

    /**
     * tbl_app row update
     * target_app_srl 이 0 보다 작고 target_api_key 가 null 이면 BadSqlGrammarException exception 발생
     *
     * @param appEntity update data
     * @param target_app_srl app_srl condition
     * @param target_api_key api_key condition
     * @return updated row count
     */
    int modify(@Param("appEntity") AppEntity appEntity,
               @Param("target_app_srl") int target_app_srl,
               @Param("target_api_key") String target_api_key);

    /**
     * app 의 등록된 단말이나 push id 가 등록된 단말의 카운트를 증가 시킨다.
     *
     * @param target_app_srl app_srl condition
     * @param target_api_key api_key condition
     * @param increase_reg_terminal reg_terminal condition. if true count plus 1
     * @param increase_reg_push_terminal reg_push_terminal condition. if true count plus 1
     * @param increase_push_count push_count condition. if true count plus 1
     * @return update row count
     */
    int increase(@Param("target_app_srl") int target_app_srl,
                 @Param("target_api_key") String target_api_key,
                 @Param("increase_reg_terminal") boolean increase_reg_terminal,
                 @Param("increase_reg_push_terminal") boolean increase_reg_push_terminal,
                 @Param("increase_push_count") boolean increase_push_count);

    /**
     * app 의 등록된 단말이나 push id 가 등록된 단말의 카운트를 감소 시킨다.
     *
     * @param target_app_srl app_srl condition
     * @param target_api_key api_key condition
     * @param decrease_reg_terminal reg_terminal condition. if true count minus 1
     * @param decrease_reg_push_terminal reg_push_terminal condition. if true count minus 1
     * @return update row count
     */
    int decrease(@Param("target_app_srl") int target_app_srl,
                 @Param("target_api_key") String target_api_key,
                 @Param("decrease_reg_terminal") boolean decrease_reg_terminal,
                 @Param("decrease_reg_push_terminal") boolean decrease_reg_push_terminal);

    /**
     * tbl_app row delete.
     * app_srl 이 0 보다 작으면 BadSqlGrammarException exception 발생
     *
     * @param app_srl app_srl condition
     * @param app_srls app_srls condition. app_srl list
     * @return delete row count
     */
    int delete(@Param("app_srl") int app_srl,
               @Param("app_srls") List<Integer> app_srls);
}

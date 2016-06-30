package com.ckstack.ckpush.dao.user;

import com.ckstack.ckpush.domain.user.ZipCodeEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
@Repository
@Transactional(value = "transactionManager")
public interface ZipCodeDao {

    @Transactional(readOnly = true)
    List<ZipCodeEntity> get(@Param("query") String query);

    @Transactional(readOnly = true)
    List<ZipCodeEntity> getjibun(@Param("query") String query);

}

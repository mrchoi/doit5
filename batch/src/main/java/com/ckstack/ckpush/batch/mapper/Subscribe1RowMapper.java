package com.ckstack.ckpush.batch.mapper;

import com.ckstack.ckpush.batch.domain.RowCountByDateEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dhkim94 on 15. 9. 21..
 */
public class Subscribe1RowMapper implements RowMapper<RowCountByDateEntity> {
    @Override
    public RowCountByDateEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        RowCountByDateEntity rowCountByDateEntity = new RowCountByDateEntity();

        rowCountByDateEntity.setCnt(rs.getLong("cnt"));
        rowCountByDateEntity.setTimestamp(rs.getInt("timestamp"));

        return rowCountByDateEntity;
    }
}

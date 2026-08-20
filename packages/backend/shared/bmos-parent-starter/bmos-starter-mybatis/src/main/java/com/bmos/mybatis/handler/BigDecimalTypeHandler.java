package com.bmos.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BigDecimalTypeHandler
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/5/30 23:04
 */
@MappedTypes({BigDecimal.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class BigDecimalTypeHandler extends BaseTypeHandler<BigDecimal> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BigDecimal parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toPlainString());
    }

    @Override
    public BigDecimal getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        return StrUtil.isBlank(str) ? null : new BigDecimal(str);
    }

    @Override
    public BigDecimal getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        return StrUtil.isBlank(str) ? null : new BigDecimal(str);
    }

    @Override
    public BigDecimal getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        return StrUtil.isBlank(str) ? null : new BigDecimal(str);
    }
}

package com.tuituidan.tresdin.mybatis.handler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.Assert;

/**
 * ArrayTypeHandler.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/11
 */
@Slf4j
@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes({String[].class, Integer[].class, Boolean[].class, Number[].class, Object[].class})
public class ArrayTypeHandler extends BaseTypeHandler<Object[]> {

    private static final Map<Class<?>, String> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put(String[].class, "varchar");
        TYPE_MAP.put(Integer[].class, "integer");
        TYPE_MAP.put(Boolean[].class, "boolean");
        TYPE_MAP.put(Number[].class, "numeric");
        TYPE_MAP.put(Object[].class, "object");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object[] parameter, JdbcType jdbcType) throws SQLException {
        String typeName = TYPE_MAP.get(parameter.getClass());
        Assert.hasText(typeName, "不支持的数组类型-" + parameter.getClass().getName());
        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf(TYPE_MAP.get(parameter.getClass()), parameter);
        ps.setArray(i, array);
    }

    /**
     * Gets the nullable result.
     *
     * @param rs         the rs
     * @param columnName Colunm name, when configuration <code>useColumnLabel</code> is <code>false</code>
     * @return the nullable result
     * @throws SQLException the SQL exception
     */
    @Override
    public Object[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getArray(rs.getArray(columnName));
    }

    @Override
    public Object[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getArray(rs.getArray(columnIndex));
    }

    @Override
    public Object[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getArray(cs.getArray(columnIndex));
    }

    private Object[] getArray(Array array) {
        if (array != null) {
            try {
                return (Object[]) array.getArray();
            } catch (Exception e) {
                log.error("获取数组出错！", e);
            }
        }
        return new Object[]{};
    }
}

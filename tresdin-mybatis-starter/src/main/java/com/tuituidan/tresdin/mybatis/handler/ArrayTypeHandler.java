package com.tuituidan.tresdin.mybatis.handler;

import com.tuituidan.tresdin.consts.Separator;
import com.tuituidan.tresdin.mybatis.util.DbTypeUtils;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public void setNonNullParameter(PreparedStatement ps, int i, Object[] parameter, JdbcType jdbcType)
            throws SQLException {
        if (DbTypeUtils.isPostgresql()) {
            String typeName = TYPE_MAP.get(parameter.getClass());
            Assert.hasText(typeName, "不支持的数组类型-" + parameter.getClass().getName());
            Connection conn = ps.getConnection();
            Array array = conn.createArrayOf(TYPE_MAP.get(parameter.getClass()), parameter);
            ps.setArray(i, array);
            return;
        }
        ps.setString(i, StringUtils.join(parameter, Separator.SEMICOLON));
    }

    /**
     * Gets the nullable result.
     *
     * @param rs the rs
     * @param columnName Colunm name, when configuration <code>useColumnLabel</code> is <code>false</code>
     * @return the nullable result
     * @throws SQLException the SQL exception
     */
    @Override
    public Object[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (DbTypeUtils.isPostgresql()) {
            return getArray(rs.getArray(columnName));
        }
        return getArray(rs.getString(columnName));

    }

    @Override
    public Object[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (DbTypeUtils.isPostgresql()) {
            return getArray(rs.getArray(columnIndex));
        }
        return getArray(rs.getString(columnIndex));
    }

    @Override
    public Object[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (DbTypeUtils.isPostgresql()) {
            return getArray(cs.getArray(columnIndex));
        }
        return getArray(cs.getString(columnIndex));
    }

    private Object[] getArray(String array) {
        return StringUtils.split(array, Separator.SEMICOLON);
    }

    private Object[] getArray(Array array) {
        if (array != null) {
            try {
                return (Object[]) array.getArray();
            } catch (Exception e) {
                log.error("获取数组出错！", e);
            }
        }
        return null;
    }

}

package com.tuituidan.tresdin.mybatis.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuituidan.tresdin.mybatis.util.DbTypeUtils;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;
import org.springframework.util.Assert;

/**
 * json类型，只验证了postgresql，mysql，其他数据库需要中setNonNullParameter中添加对应转换.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/11
 */
@Slf4j
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes({JSONArray.class, JSONObject.class})
public class JsonTypeHandler extends BaseTypeHandler<Object> {

    protected Class<?> cls;

    /**
     * JsonTypeHandler
     *
     * @param cls cls
     */
    public JsonTypeHandler(Class<?> cls) {
        Assert.notNull(cls, "Type argument cannot be null");
        this.cls = cls;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
            throws SQLException {
        if (DbTypeUtils.isPostgresql()) {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            jsonObject.setValue(JSON.toJSONString(parameter));
            ps.setObject(i, jsonObject);
            return;
        }
        // mysql
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toJavaObject(rs.getString(columnName));
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toJavaObject(rs.getString(columnIndex));
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toJavaObject(cs.getString(columnIndex));
    }

    private Object toJavaObject(String columnValue) {
        if (columnValue == null) {
            return null;
        }
        if (this.cls.isAssignableFrom(JSONArray.class)) {
            return JSON.toJavaObject(JSON.parseArray(columnValue), this.cls);
        }
        return JSON.toJavaObject(JSON.parseObject(columnValue), this.cls);
    }

}

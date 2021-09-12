package com.tuituidan.tresdin.mybatis.interceptor;

import com.tuituidan.tresdin.mybatis.bean.IEntity;
import com.tuituidan.tresdin.util.StringExtUtils;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

/**
 * 自动设置默认字段.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/12/11
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
@Component
public class EntitySetDefaultInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object obj = invocation.getArgs()[1];
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (obj instanceof IEntity) {
            handlerEntity(obj, mappedStatement.getSqlCommandType());
        }
        if (obj instanceof ParamMap) {
            handlerList(obj, mappedStatement.getSqlCommandType());
        }
        return invocation.proceed();
    }

    @SuppressWarnings({"unchecked"})
    private void handlerList(Object obj, SqlCommandType commandType) {
        ((ParamMap<Object>) obj).values().stream().filter(Collection.class::isInstance)
                .forEach(item -> ((Collection) item).forEach(entity -> {
                    if (entity instanceof IEntity) {
                        handlerEntity(entity, commandType);
                    }
                }));
    }

    private void handlerEntity(Object obj, SqlCommandType commandType) {
        IEntity<?> entity = (IEntity<?>) obj;
        switch (commandType) {
            case INSERT:
                if (StringUtils.isBlank(entity.getId())) {
                    entity.setId(StringExtUtils.getUuid());
                }
                if (entity.getCreateTime() == null) {
                    entity.setCreateTime(LocalDateTime.now());
                }
                entity.setUpdateTime(LocalDateTime.now());
                break;
            case UPDATE:
                entity.setUpdateTime(LocalDateTime.now());
                break;
            default:
                break;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 这里可以不用实现.
    }
}

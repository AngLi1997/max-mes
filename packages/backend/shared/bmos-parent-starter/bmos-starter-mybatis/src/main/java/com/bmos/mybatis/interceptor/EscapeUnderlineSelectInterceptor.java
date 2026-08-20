package com.bmos.mybatis.interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class EscapeUnderlineSelectInterceptor implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor,
                            MappedStatement mappedStatement,
                            Object parameter,
                            RowBounds rowBounds,
                            ResultHandler resultHandler,
                            BoundSql boundSql) throws SQLException {

        escapeLike(boundSql);
    }


    /**
     * like条件中若有关键字，则进行转义
     *
     * @param boundSql
     */
    public void escapeLike(BoundSql boundSql) {
        String sql = boundSql.getSql().toLowerCase();
        if (!sql.contains(" like ") || !sql.contains("?")) {
            return;
        }
        // mybatis-plus like会直接将%拼接到参数上
        boolean xml = StrUtil.containsIgnoreCase(sql, "concat(");
        MetaObject parameterObjectMetaObject = SystemMetaObject.forObject(boundSql.getParameterObject());
        String[] strList = sql.split("\\?");
        Set<String> keyNames = new HashSet<>();
        for (int i = 0; i < strList.length; i++) {
            if (strList[i].contains(" like ")) {
                String keyName = boundSql.getParameterMappings().get(i).getProperty();
                keyNames.add(keyName);
            }
        }
        for (String keyName : keyNames) {
            if (parameterObjectMetaObject.hasGetter(keyName)) {
                Object value = parameterObjectMetaObject.getValue(keyName);
                if (value instanceof String) {
                    String v = (String) value;
                    parameterObjectMetaObject.setValue(keyName, xml ? convertXmlParam(v) : convert(v));
                }
            } else if (boundSql.getParameterObject() instanceof String) {
                String v = (String) boundSql.getParameterObject();
                boundSql.setAdditionalParameter(keyName, xml ? convertXmlParam(v) : convert(v));
            }
        }
    }

    private String convert(String before) {
        char[] chars = before.toCharArray();
        int len = chars.length;
        int offset = 0;
        char[] result = new char[len * 2];
        for (int i = 0; i < len; i++) {
            char c = chars[i];
            // 特殊字符增加转义
            if (c == '_' || c == '%') {
                if (i != 0 && chars[i - 1] != '\\' && i != len - 1) {
                    result[offset++] = '\\';
                }
            }
            result[offset++] = c;
        }
        return new String(result, 0, offset);
    }

    private String convertXmlParam(String before) {
        char[] chars = before.toCharArray();
        int len = chars.length;
        int offset = 0;
        char[] result = new char[len * 2];
        for (int i = 0; i < len; i++) {
            char c = chars[i];
            // 特殊字符增加转义
            if (c == '_' || c == '%') {
                if (i == 0 || chars[i - 1] != '\\') {
                    result[offset++] = '\\';
                }
            }
            result[offset++] = c;
        }
        return new String(result, 0, offset);
    }
}

package com.bmos.mybatis.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.bmos.common.util.json.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;
import java.util.Map;

@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class MapListTypeHandler extends AbstractJsonTypeHandler<Object> {


    @Override
    protected Object parse(String json) {
        return JsonUtils.parseObject(json, new TypeReference<Map<String, List<String>>>() {
        });
    }

    @Override
    protected String toJson(Object obj) {
        return JsonUtils.toJsonString(obj);
    }
}

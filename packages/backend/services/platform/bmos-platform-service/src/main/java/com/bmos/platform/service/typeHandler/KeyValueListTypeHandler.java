package com.bmos.platform.service.typeHandler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.pojo.KeyValue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyValueListTypeHandler extends AbstractJsonTypeHandler<List<KeyValue>> {

    @Override
    protected List<KeyValue> parse(String json) {
        return JsonUtils.parseArray(json, KeyValue.class);
    }

    @Override
    protected String toJson(List<KeyValue> obj) {
        return JsonUtils.toJsonString(obj);
    }
}

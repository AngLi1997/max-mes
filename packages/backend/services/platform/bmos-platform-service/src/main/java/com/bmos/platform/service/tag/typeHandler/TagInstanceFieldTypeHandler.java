package com.bmos.platform.service.tag.typeHandler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.platform.service.tag.dto.TagInstanceField;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/5 15:11
 */
public class TagInstanceFieldTypeHandler extends AbstractJsonTypeHandler<List<TagInstanceField>> {
    @Override
    protected List<TagInstanceField> parse(String json) {
        return JsonUtils.parseArray(json, TagInstanceField.class);
    }

    @Override
    protected String toJson(List<TagInstanceField> obj) {
        return JsonUtils.toJsonString(obj);
    }
}

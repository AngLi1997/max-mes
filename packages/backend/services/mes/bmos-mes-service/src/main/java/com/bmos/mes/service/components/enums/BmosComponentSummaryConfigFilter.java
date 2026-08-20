package com.bmos.mes.service.components.enums;

import com.alibaba.fastjson.JSON;
import lombok.Getter;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/1 16:13
 */
@Getter
public enum BmosComponentSummaryConfigFilter {

    /**
     * 物料投入
     */
    MATERIAL_INPUT((config) -> Optional.ofNullable(config)
            .map(JSON::parseObject)
            .map(item -> item.getString("formulaMaterialId"))
            .map(item -> new String[]{item})
            .orElse(new String[0]),
            "formulaMaterialId"
    );


    /**
     * 组件配置查询function
     */
    private final Function<String, String[]> function;

    /**
     * 过滤字段
     */
    private final String[] fieldNames;


    BmosComponentSummaryConfigFilter(Function<String, String[]> function, String... fieldNames){
        this.function = function;
        this.fieldNames = fieldNames;
    }
}

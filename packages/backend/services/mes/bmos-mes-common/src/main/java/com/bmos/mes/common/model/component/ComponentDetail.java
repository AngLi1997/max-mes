package com.bmos.mes.common.model.component;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 组件详情 含自定义字段相关信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentDetail {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段数据
     */
    private String fieldData;

    /**
     * 数据来源
     */
    private String dataSources;

}

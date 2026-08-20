package com.bmos.mes.service.storage.manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义字段
 */
@Getter
@Setter
@ApiModel(value = "物料批次自定义字段")
public class MaterialBatchFieldVO {

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    /**
     * 字段类型名称
     */
    @ApiModelProperty(value = "字段类型名称")
    private String fieldTypeName;

    /**
     * 字段
     */
    @ApiModelProperty(value = "字段Code")
    private String field;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    /**
     * 物料批次字段值
     */
    @ApiModelProperty(value = "物料批次字段值值")
    private String fieldValue;


}

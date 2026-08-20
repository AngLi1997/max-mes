package com.bmos.mes.common.model.component;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义字段详细信息
 */
@Data
public class CustomFieldDetailInfo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty("物料批次id/物料id")
    private Long keyId;

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
     * 自定义字段code
     */
    @ApiModelProperty(value = "自定义字段code")
    private String field;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "自定义字段名称")
    private String fieldName;

    /**
     * 自定义字段值
     */
    @ApiModelProperty(value = "自定义字段值")
    private String fieldValue;

}

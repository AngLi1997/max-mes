package com.bmos.lims2.web.material.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 生产物料的自定字段信息
 */
@Getter
@Setter
@ApiModel("生产物料的自定字段信息")
public class MaterialFieldInfoVO {

    @ApiModelProperty(value = "id")
    private Long id;

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

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id")
    private Long materialId;

}

package com.bmos.lims2.web.material.vo.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("保存生产物料自定义字段VO")
public class MaterialFieldSaveVO {

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

package com.bmos.lims2.server.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("自定义字段VO")
public class MaterialFieldDTO {

    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    @ApiModelProperty(value = "字段类型名称")
    private String fieldTypeName;

    @ApiModelProperty(value = "自定义字段code")
    private String field;

    @ApiModelProperty(value = "自定义字段名称")
    private String fieldName;

    @ApiModelProperty(value = "自定义字段值")
    private String fieldValue;


}

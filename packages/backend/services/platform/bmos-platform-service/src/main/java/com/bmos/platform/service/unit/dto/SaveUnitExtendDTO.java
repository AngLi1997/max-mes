package com.bmos.platform.service.unit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "添加扩展单位DTO")
public class SaveUnitExtendDTO {

    @ApiModelProperty(value = "扩展单位名称")
    private String extendUnitName;

    @ApiModelProperty(value = "表达式值")
    private String expressionValue;

    @ApiModelProperty(value = "扩展单位精度")
    private Long extendPrecision;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "标准单位id")
    private Long unitId;
}

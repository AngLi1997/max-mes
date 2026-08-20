package com.bmos.mes.service.unit.vo;

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
@ApiModel(value = "查询扩展单位返回VO")
public class UnitExtendVO {

    @ApiModelProperty(value = "扩展单位id")
    private Long id;

    @ApiModelProperty(value = "扩展单位名称")
    private String extendUnitName;

    @ApiModelProperty(value = "表达式值")
    private String expressionValue;

    @ApiModelProperty(value = "表达式")
    private String expression;

    @ApiModelProperty(value = "是否启用；0：未启用；1：启用")
    private Boolean state;

    @ApiModelProperty(value = "扩展单位精度")
    private Long extendPrecision;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "标准单位id")
    private Long unitId;
}

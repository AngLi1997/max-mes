package com.bmos.platform.service.unit.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(value = "扩展单位下拉列表VO")
public class UnitExtendListVO {

    @ApiModelProperty(value = "扩展单位id")
    private Long id;

    @ApiModelProperty(value = "扩展单位名称")
    private String extendUnitName;

    @ApiModelProperty(value = "表达式值")
    @JsonIgnore
    private String expressionValue;

    @ApiModelProperty(value = "表达式")
    private String expression;

    @ApiModelProperty(value = "扩展单位精度")
    @JsonIgnore
    private Long extendPrecision;

    @ApiModelProperty(value = "标准单位id")
    @JsonIgnore
    private Long unitId;

    @ApiModelProperty("标准单位名称")
    @JsonIgnore
    private String unitName;
}

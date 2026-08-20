package com.bmos.lims2.server.eln.record.vo;

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
@ApiModel(value = "公式返回VO")
public class FormulaVO {

    @ApiModelProperty(value = "组件表id")
    private Long id;

    @ApiModelProperty(value = "精度")
    private Long formulaPrecision;

    @ApiModelProperty(value = "标记该组件是否是一个计算结果（0否1是，默认0）")
    private Integer isResult;

    @ApiModelProperty(value = "公式id")
    private Long formulaId;

    @ApiModelProperty(value = "公式实际参数字段JSON")
    private String formulaField;

    @ApiModelProperty(value = "公式表达式")
    private String formulaExpression;

    @ApiModelProperty(value = "公式类型")
    private String formulaType;

    @ApiModelProperty(value = "修约公式code")
    private String roundCode;
}

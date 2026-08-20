package com.bmos.lims2.server.eln.record.dto;

import com.bmos.lims2.server.eln.record.entity.formula.ComponentFormulaConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "添加公式配置DTO")
public class SaveFormulaDTO {

    @ApiModelProperty(value = "组件id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "精度")
    private Long formulaPrecision;

    @ApiModelProperty(value = "标记该组件是否是一个计算结果（0否1是，默认0）")
    private Integer isResult;

    @ApiModelProperty(value = "公式id")
    private Long formulaId;

    @ApiModelProperty(value = "组件具体空格id")
    private Long filedId;

    @ApiModelProperty(value = "公式实际参数字段JSON")
    private String formulaField;

    @ApiModelProperty(value = "公式表达式")
    private String formulaExpression;

    @ApiModelProperty(value = "公式类型")
    private String formulaType;

    @ApiModelProperty(value = "修约公式code")
    private String roundCode;

    @ApiModelProperty(value = "记录版本id")
    private Long recordVersionId;

    @ApiModelProperty(value = "时间类型")
    private String dateType;

    @ApiModelProperty(value = "公式参数详情")
    private List<FormulaParameterDTO> formulaDetailList;

    @ApiModelProperty("公式额外配置")
    private ComponentFormulaConfig formulaConfig;
}

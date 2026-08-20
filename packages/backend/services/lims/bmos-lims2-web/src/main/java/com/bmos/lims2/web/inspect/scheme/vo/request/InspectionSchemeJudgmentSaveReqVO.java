package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 检验方案判定配置保存请求VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案判定配置保存请求")
public class InspectionSchemeJudgmentSaveReqVO {

    @ApiModelProperty(value = "判定类型", required = true)
    @NotNull(message = "判定类型不能为空")
    private JudgmentTypeEnum judgmentType;


    @ApiModelProperty("默认测试结果")
    private Boolean defaultResult;

    @ApiModelProperty("最小值")
    private BigDecimal minValue;

    @ApiModelProperty("最大值")
    private BigDecimal maxValue;

    @ApiModelProperty("标准值")
    private String standardValue;

    @ApiModelProperty(value = "判定表达式", required = true)
    @NotBlank(message = "判定表达式不能为空")
    @Pattern(regexp = "^[A-Za-z0-9\\s()&|!]+$", message = "判定表达式只能包含字母、数字、空格、括号和运算符(&|!)")
    private String expression;
} 
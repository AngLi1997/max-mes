package com.bmos.lims2.web.inspect.parameter.vo.req;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 分析项趋势线请求参数
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@ApiModel("分析项趋势线请求参数")
public class InspectParameterTrendReqVO {

    @ApiModelProperty("趋势线id")
    private Long id;

    @ApiModelProperty(value = "范围名称", required = true)
    @NotBlank(message = "范围名称不能为空")
    private String rangeName;

    @ApiModelProperty(value = "最小值", required = true)
    private BigDecimal minValue;

    @ApiModelProperty(value = "最小值比较运算符", required = true)
    @ApiModelEnumProperty(value = "最小值比较运算符", enumClass = CompareOperatorEnum.class)
    private CompareOperatorEnum minOperator;

    @ApiModelProperty(value = "最大值", required = true)
    private BigDecimal maxValue;

    @ApiModelProperty(value = "最大值比较运算符", required = true)
    @ApiModelEnumProperty(value = "最大值比较运算符", enumClass = CompareOperatorEnum.class)
    private CompareOperatorEnum maxOperator;
} 
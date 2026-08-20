package com.bmos.lims2.web.inspect.parameter.vo.resp;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分析项趋势线响应参数
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@ApiModel("分析项趋势线响应参数")
public class InspectParameterTrendRespVO {

    @ApiModelProperty("趋势线id")
    private Long id;

    @ApiModelProperty("范围名称")
    private String rangeName;

    @ApiModelProperty("最小值")
    private Double minValue;

    @ApiModelProperty("最小值比较运算符")
    @ApiModelEnumProperty(value = "最小值比较运算符", enumClass = CompareOperatorEnum.class)
    private CompareOperatorEnum minOperator;

    @ApiModelProperty("最大值")
    private Double maxValue;

    @ApiModelProperty("最大值比较运算符")
    @ApiModelEnumProperty(value = "最大值比较运算符", enumClass = CompareOperatorEnum.class)
    private CompareOperatorEnum maxOperator;
} 
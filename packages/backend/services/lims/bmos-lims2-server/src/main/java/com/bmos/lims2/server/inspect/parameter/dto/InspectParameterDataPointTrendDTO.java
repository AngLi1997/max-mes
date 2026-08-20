package com.bmos.lims2.server.inspect.parameter.dto;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 分析项趋势线配置DTO
 */
@Getter
@Setter
@ApiModel("分析项趋势线配置DTO")
public class InspectParameterDataPointTrendDTO extends BaseDO {

    /**
     * 分析项id
     */
    @ApiModelProperty(value = "分析项id", required = true)
    @NotNull
    private Long parameterId;

    /**
     * 范围名称
     */
    @ApiModelProperty(value = "范围名称", required = true)
    @NotBlank
    private String rangeName;

    /**
     * 最小值
     */
    @ApiModelProperty(value = "最小值")
    private BigDecimal minValue;

    /**
     * 最小值运算符
     */
    @ApiModelEnumProperty(value = "最小值运算符", enumClass = CompareOperatorEnum.class)
    private CompareOperatorEnum minOperator;

    /**
     * 最大值
     */
    @ApiModelProperty(value = "最大值")
    private BigDecimal maxValue;

    /**
     * 最大值运算符
     */
    @ApiModelEnumProperty(value = "最大值运算符", enumClass = CompareOperatorEnum.class)
    private CompareOperatorEnum maxOperator;
} 
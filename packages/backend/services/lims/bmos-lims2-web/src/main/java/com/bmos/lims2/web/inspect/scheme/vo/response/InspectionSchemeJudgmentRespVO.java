package com.bmos.lims2.web.inspect.scheme.vo.response;

import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 检验方案判定配置响应VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案判定配置响应")
public class InspectionSchemeJudgmentRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("判定类型：RANGE-范围判定, EQUAL-相等判定")
    private JudgmentTypeEnum judgmentType;


    @ApiModelProperty("默认测试结果")
    private Boolean defaultResult;

    @ApiModelProperty("最小值")
    private BigDecimal minValue;

    @ApiModelProperty("最大值")
    private BigDecimal maxValue;

    @ApiModelProperty("标准值")
    private String standardValue;

    @ApiModelProperty("判定表达式")
    private String expression;
} 
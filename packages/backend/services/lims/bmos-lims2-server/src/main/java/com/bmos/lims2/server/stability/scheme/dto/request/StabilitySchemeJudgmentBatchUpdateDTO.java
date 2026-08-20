package com.bmos.lims2.server.stability.scheme.dto.request;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 稳定性方案判定配置批量更新DTO
 */
@Data
public class StabilitySchemeJudgmentBatchUpdateDTO {

    @ApiModelProperty("判定配置ID（修改时需要）")
    private Long judgmentConfigId;

    @ApiModelProperty("判定配置名称")
    private String judgementConfigName;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("检验项目配置ID")
    private Long itemConfigId;

    @ApiModelProperty("分析项配置ID")
    private Long parameterConfigId;

    @ApiModelProperty("数据点配置ID")
    private Long dataPointConfigId;

    @ApiModelProperty("原始分析项ID")
    private Long parameterId;

    @ApiModelProperty("原始数据点ID")
    private Long dataPointId;

    @ApiModelProperty("数据点类型")
    private DataPointTypeEnum pointType;

    @ApiModelProperty("判定类型")
    private JudgmentTypeEnum judgmentType;

    @ApiModelProperty("默认测试结果")
    private Boolean defaultResult;

    @ApiModelProperty("最小值")
    private BigDecimal minValue;

    @ApiModelProperty("最小值比较运算符")
    private CompareOperatorEnum minOperator;

    @ApiModelProperty("最大值")
    private BigDecimal maxValue;

    @ApiModelProperty("最大值比较运算符")
    private CompareOperatorEnum maxOperator;

    @ApiModelProperty("标准值")
    private String standardValue;

    @ApiModelProperty("判定表达式")
    private String expression;

    @ApiModelProperty("最小时间（时间类型区间判定）")
    private String minTime;

    @ApiModelProperty("最大时间（时间类型区间判定）")
    private String maxTime;
}

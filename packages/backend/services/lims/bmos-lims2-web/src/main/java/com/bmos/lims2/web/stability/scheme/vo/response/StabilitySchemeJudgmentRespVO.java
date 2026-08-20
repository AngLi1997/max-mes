package com.bmos.lims2.web.stability.scheme.vo.response;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 稳定性方案判定配置响应VO
 */
@Data
@ApiModel("稳定性方案判定配置响应")
public class StabilitySchemeJudgmentRespVO {

    @ApiModelProperty("判定配置ID")
    private Long judgmentConfigId;

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

    @ApiModelProperty("判定配置名称")
    private String judgementConfigName;

    @ApiModelProperty("数据点类型")
    private DataPointTypeEnum pointType;

    @ApiModelProperty("判定类型")
    private JudgmentTypeEnum judgmentType;

    @ApiModelProperty("默认测试结果")
    private Boolean defaultResult;

    @ApiModelProperty("最小值")
    private BigDecimal minValue;

    @ApiModelProperty("最小值运算符")
    private CompareOperatorEnum minOperator;

    @ApiModelProperty("最大值")
    private BigDecimal maxValue;

    @ApiModelProperty("最大值运算符")
    private CompareOperatorEnum maxOperator;

    @ApiModelProperty("标准值")
    private String standardValue;

    @ApiModelProperty("判定表达式")
    private String expression;

    @ApiModelProperty("最小时间")
    private String minTime;

    @ApiModelProperty("最大时间")
    private String maxTime;

    @ApiModelProperty("判定引用的数据点是否已删除")
    private Boolean dataPointDeleted;

    @ApiModelProperty("判定引用的数据点与记录组件绑定是否缺失")
    private Boolean dataPointBindingMissing;

    @ApiModelProperty("判定引用的数据点类型是否已变更")
    private Boolean dataPointTypeChanged;

    @ApiModelProperty("判定引用的选项是否未在组件中配置")
    private Boolean dataPointOptionInvalid;
}

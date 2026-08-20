package com.bmos.lims2.web.stability.scheme.vo.request;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 稳定性方案判定条件批量更新请求VO
 */
@Data
@ApiModel("稳定性方案判定条件批量更新请求")
public class StabilitySchemeJudgmentBatchUpdateReqVO {

    @ApiModelProperty(value = "分析项配置ID", required = true)
    @NotNull(message = "分析项配置ID不能为空")
    private Long parameterConfigId;

    @ApiModelProperty("最终判定表达式")
    private String finalExpression;

    @ApiModelProperty("判定条件列表")
    private List<JudgmentItemVO> updateJudgmentList;

    @Data
    @ApiModel("判定条件配置")
    public static class JudgmentItemVO {

        @ApiModelProperty("判定配置ID（修改时需要）")
        private Long judgmentConfigId;

        @ApiModelProperty("判定配置名称")
        private String judgementConfigName;

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

        @ApiModelProperty("最小时间")
        private String minTime;

        @ApiModelProperty("最大时间")
        private String maxTime;
    }
}

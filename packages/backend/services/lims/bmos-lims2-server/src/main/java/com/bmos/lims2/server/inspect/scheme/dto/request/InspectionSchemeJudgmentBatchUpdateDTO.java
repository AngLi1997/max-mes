package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 检验方案判定条件批量更新DTO
 *
 * @author yigaohui
 * @since 2025/01/29 21:00
 */
@Data
public class InspectionSchemeJudgmentBatchUpdateDTO {


    @ApiModelProperty("判定配置ID（修改时需要）")
    private Long judgmentConfigId;



    @ApiModelProperty("判定配置名称")
    private String judgementConfigName;

    /**
     * 关联的方案ID
     */
    @ApiModelProperty("关联的方案ID")
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    @ApiModelProperty("关联的版本ID")
    private Long versionId;


    @ApiModelProperty("关联的实验包ID")
    private Long packageId;

    @ApiModelProperty("关联的检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("分析项ID")
    private Long parameterId;
    /**
     * 关联的分析项配置ID
     */
    @ApiModelProperty("关联的分析项配置ID")
    private Long parameterConfigId;

    @ApiModelProperty("关联的判定配置ID")
    private Long dataPointConfigId;

    /**
     * 关联的数据点ID
     */
    @ApiModelProperty("关联的数据点ID")
    private Long dataPointId;

    @ApiModelProperty("数据点类型")
    private DataPointTypeEnum pointType;

    /**
     * 判定类型：RANGE-范围判定, EQUAL-相等判定
     */
    @ApiModelProperty("判定类型")
    private JudgmentTypeEnum judgmentType;


    @ApiModelProperty("默认测试结果")
    private Boolean defaultResult;

    /**
     * 最小值
     */
    @ApiModelProperty("最小值")
    private BigDecimal minValue;

    /**
     * 最小值比较运算符
     */
    @ApiModelProperty("最小值比较运算符")
    private CompareOperatorEnum minOperator;

    /**
     * 最大值
     */
    @ApiModelProperty("最大值")
    private BigDecimal maxValue;

    /**
     * 最大值比较运算符
     */
    @ApiModelProperty("最大值比较运算符")
    private CompareOperatorEnum maxOperator;

    /**
     * 标准值
     */
    @ApiModelProperty("标准值")
    private String standardValue;

    /**
     * 判定表达式
     */
    @ApiModelProperty("判定表达式")
    private String expression;



    @ApiModelProperty("最小时间;时间类型数据点区间判定时配置")
    private String minTime;


    @ApiModelProperty("最大时间;时间类型数据点区间判定时配置")
    private String maxTime;
}

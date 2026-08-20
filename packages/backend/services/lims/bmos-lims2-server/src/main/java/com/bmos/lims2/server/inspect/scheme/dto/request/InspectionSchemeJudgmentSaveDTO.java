package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 检验方案判定配置保存请求DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeJudgmentSaveDTO {


    /**
     * 检验项目id
     */
    private Long inspectItemId;
    /**
     * 分析项id
     */
    private Long inspectParameterId;


    private String judgementConfigName;

    private Long dataPointId;


    private Long dataPointConfigId;

    @ApiModelProperty("数据点类型")
    private DataPointTypeEnum pointType;

    /**
     * 判定类型：RANGE-范围判定, EQUAL-相等判定
     */

    private JudgmentTypeEnum judgmentType;


    @ApiModelProperty("默认测试结果")
    private Boolean defaultResult;

    /**
     * 最小值
     */
    private BigDecimal minValue;

    /**
     * 最大值
     */
    private BigDecimal maxValue;

    /**
     * 标准值
     */
    private String standardValue;

    /**
     * 判定表达式
     */
    private String expression;

    /**
     * 最小值比较运算符
     */
    private CompareOperatorEnum minOperator;

    /**
     * 最大值比较运算符
     */
    private CompareOperatorEnum maxOperator;



    private String minTime;


    private String maxTime;
} 
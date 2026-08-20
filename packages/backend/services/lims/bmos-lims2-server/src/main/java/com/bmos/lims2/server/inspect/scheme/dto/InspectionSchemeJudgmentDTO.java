package com.bmos.lims2.server.inspect.scheme.dto;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 检验方案判定配置DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeJudgmentDTO {

    /**
     * 主键ID
     */
    private Long id;



    private String judgementConfigName;

    /**
     * 关联的方案ID
     */
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    private Long versionId;


    private Long packageId;

    private Long inspectItemId;

    private Long parameterId;
    /**
     * 关联的分析项配置ID
     */
    private Long parameterConfigId;

    private Long dataPointConfigId;

    /**
     * 关联的数据点ID
     */
    private Long dataPointId;

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
     * 最小值比较运算符
     */
    private CompareOperatorEnum minOperator;

    /**
     * 最大值
     */
    private BigDecimal maxValue;

    /**
     * 最大值比较运算符
     */
    private CompareOperatorEnum maxOperator;

    /**
     * 标准值
     */
    private String standardValue;

    /**
     * 判定表达式
     */
    private String expression;

    /**
     * 数据点类型：NUMBER-数值, TEXT-文本, OPTION-选项, TIME-时间
     */
    private DataPointTypeEnum pointType;

    private String minTime;

    private String maxTime;

    /**
     * 判定引用的数据点是否已被删除
     */
    private Boolean dataPointDeleted;

    /**
     * 判定引用的数据点与组件绑定是否缺失
     */
    private Boolean dataPointBindingMissing;

    /**
     * 判定引用的数据点类型是否变更
     */
    private Boolean dataPointTypeChanged;

    /**
     * 判定引用的选项是否未在组件中配置
     */
    private Boolean dataPointOptionInvalid;
} 
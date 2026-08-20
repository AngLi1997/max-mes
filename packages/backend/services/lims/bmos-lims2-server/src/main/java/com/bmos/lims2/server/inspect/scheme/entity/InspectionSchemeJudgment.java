package com.bmos.lims2.server.inspect.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 检验方案判定配置实体类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Getter
@Setter
@TableName("lm_inspection_scheme_judgment")
public class InspectionSchemeJudgment extends BaseDO {

    /**
     * 关联的方案ID
     */
    private Long schemeId;


    /**
     * 判定配置名称
     */
    private String judgementConfigName;

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
     * 数据点类型：NUMBER-数值, TEXT-文本, OPTION-选项, TIME-时间
     */
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


    private String minTime;


    private String maxTime;
} 
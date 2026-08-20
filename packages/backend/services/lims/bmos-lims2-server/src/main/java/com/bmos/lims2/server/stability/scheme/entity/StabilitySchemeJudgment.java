package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 稳定性方案判定配置实体类
 */
@Getter
@Setter
@TableName("lm_stability_scheme_judgment")
public class StabilitySchemeJudgment extends BaseDO {

    private Long schemeId;

    private Long versionId;

    /**
     * 检验项目配置ID（lm_stability_scheme_item.id）
     */
    private Long itemConfigId;

    /**
     * 分析项配置ID（lm_stability_scheme_parameter.id）
     */
    private Long parameterConfigId;

    /**
     * 数据点配置ID（lm_stability_scheme_data_point.id）
     */
    private Long dataPointConfigId;

    /**
     * 原始分析项ID
     */
    private Long parameterId;

    /**
     * 原始数据点ID
     */
    private Long dataPointId;

    private String judgementConfigName;

    private DataPointTypeEnum pointType;

    private JudgmentTypeEnum judgmentType;

    private Boolean defaultResult;

    private BigDecimal minValue;

    private CompareOperatorEnum minOperator;

    private BigDecimal maxValue;

    private CompareOperatorEnum maxOperator;

    private String standardValue;

    private String expression;

    private String minTime;

    private String maxTime;
}

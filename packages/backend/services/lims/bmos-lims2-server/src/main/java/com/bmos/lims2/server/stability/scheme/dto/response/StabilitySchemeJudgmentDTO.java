package com.bmos.lims2.server.stability.scheme.dto.response;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 稳定性方案判定配置响应DTO
 */
@Data
public class StabilitySchemeJudgmentDTO {

    private Long id;

    private Long schemeId;

    private Long versionId;

    private Long itemConfigId;

    private Long parameterConfigId;

    private Long dataPointConfigId;

    private Long parameterId;

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

    private Boolean dataPointDeleted;

    private Boolean dataPointBindingMissing;

    private Boolean dataPointTypeChanged;

    private Boolean dataPointOptionInvalid;
}

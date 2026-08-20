package com.bmos.lims2.server.stability.scheme.dto.response;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import lombok.Data;

import java.util.List;

/**
 * 稳定性方案分析项配置响应DTO
 */
@Data
public class StabilitySchemeParameterDTO {

    private Long id;

    private Long schemeId;

    private Long versionId;

    private Long itemConfigId;

    private Long inspectItemId;

    private String inspectItemCode;

    private Long parameterId;

    private String parameterName;

    private String parameterCode;

    private String standardRule;

    private Boolean isExecutable;

    private Boolean isReportable;

    private ExecuteMethodEnum executeMethod;

    private String finalExpression;

    private Long recordId;

    private String recordCode;

    private Long recordVersionId;

    private Long recordItemId;

    private String recordName;

    private String recordVersion;

    private List<StabilitySchemeDataPointDTO> dataPoints;

    private List<StabilitySchemeJudgmentDTO> judgments;

    private Boolean judgmentConfigError;

    private Boolean judgmentDataPointDeleted;

    private Boolean judgmentDataPointBindingMissing;

    private Boolean judgmentDataPointTypeChanged;

    private Boolean judgmentDataPointOptionInvalid;
}

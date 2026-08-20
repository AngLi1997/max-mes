package com.bmos.lims2.server.stability.scheme.dto.response;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import lombok.Data;

/**
 * 稳定性方案数据点配置响应DTO
 */
@Data
public class StabilitySchemeDataPointDTO {

    private Long id;

    private Long schemeId;

    private Long versionId;

    private Long itemConfigId;

    private Long parameterConfigId;

    private Long parameterId;

    private Long dataPointId;

    private String name;

    private DataPointTypeEnum pointType;

    private String trendLineConfig;

    private String options;

    private String timeFormat;

    private String dateStyle;

    private Boolean roundingUp;

    private Boolean reportDisplay;

    private Long recordId;

    private Long recordVersionId;

    private Long componentId;

    private Long recordItemId;

    private Long fieldId;

    /**
     * 是否被判定引用
     */
    private Boolean referencedByJudgment;
}

package com.bmos.lims2.server.stability.scheme.dto.response;

import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import lombok.Data;

import java.util.List;

/**
 * 稳定性方案检验项目配置响应DTO
 */
@Data
public class StabilitySchemeItemDTO {

    private Long id;

    private Long schemeId;

    private Long versionId;

    private Long inspectItemId;

    private String inspectItemName;

    private String inspectItemCode;

    private Integer duration;

    private ItemDurationUnitEnum timeUnit;

    private List<Long> teams;

    private Boolean isRequired;

    private String remark;

    private List<StabilitySchemeParameterDTO> inspectionParameters;
}

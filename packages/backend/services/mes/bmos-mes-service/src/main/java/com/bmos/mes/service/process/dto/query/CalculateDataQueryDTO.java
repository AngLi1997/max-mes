package com.bmos.mes.service.process.dto.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Builder
public class CalculateDataQueryDTO {
    @Tolerate
    public CalculateDataQueryDTO() {
    }

    private String batchNo;

    private Long recordVersionId;

    private Long processId;

    private String processVersion;

    private Long productPlanId;

    private Long procedureStepId;

    private Boolean reuse;

    private Long copyVersion;

    private Long recordItemId;

    private Long procedureStepModelId;
}

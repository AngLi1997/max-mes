package com.bmos.lims2.server.stability.plan.dto.request;

import lombok.Data;

/**
 * 稳定性考察计划恢复DTO
 */
@Data
public class StabilityInspectPlanResumeDTO {

    /**
     * 计划ID
     */
    private Long id;

    /**
     * 方案版本ID（可选，恢复时可切换为其他生效版本）
     */
    private Long schemeVersionId;
}

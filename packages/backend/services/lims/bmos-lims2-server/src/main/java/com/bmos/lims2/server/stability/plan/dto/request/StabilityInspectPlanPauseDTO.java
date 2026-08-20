package com.bmos.lims2.server.stability.plan.dto.request;

import lombok.Data;

/**
 * 稳定性考察计划暂停DTO
 */
@Data
public class StabilityInspectPlanPauseDTO {

    /**
     * 计划ID
     */
    private Long id;

    /**
     * 暂停理由
     */
    private String pauseReason;
}

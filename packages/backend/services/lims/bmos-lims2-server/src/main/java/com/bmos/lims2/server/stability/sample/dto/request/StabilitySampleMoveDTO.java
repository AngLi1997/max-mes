package com.bmos.lims2.server.stability.sample.dto.request;

import lombok.Data;

/**
 * 稳定性样品移动位置DTO
 */
@Data
public class StabilitySampleMoveDTO {

    /** lm_stability_plan_sample.id */
    private Long id;

    /** 新储存位置 */
    private String storageLocation;

    /** 移动原因 */
    private String moveReason;
}

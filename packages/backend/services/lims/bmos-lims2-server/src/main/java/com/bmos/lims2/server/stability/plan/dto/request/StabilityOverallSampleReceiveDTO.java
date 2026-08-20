package com.bmos.lims2.server.stability.plan.dto.request;

import lombok.Data;

/**
 * 稳定性整体样品接收-明细行DTO
 */
@Data
public class StabilityOverallSampleReceiveDTO {

    /** 整体样品ID（lm_stability_plan_sample.id） */
    private Long sampleId;

    /** 储存位置 */
    private String storageLocation;
}

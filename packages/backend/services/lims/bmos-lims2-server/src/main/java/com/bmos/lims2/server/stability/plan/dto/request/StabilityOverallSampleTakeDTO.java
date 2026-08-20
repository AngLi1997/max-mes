package com.bmos.lims2.server.stability.plan.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 稳定性整体批量取样DTO
 */
@Data
public class StabilityOverallSampleTakeDTO {

    /** 批次ID */
    private Long batchId;

    /** 取样人ID */
    private String samplerId;

    /** 取样人姓名 */
    private String samplerName;

    /** 各试验类型取样明细 */
    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        /** 整体样品ID（lm_stability_plan_sample.id） */
        private Long sampleId;

        /** 实际取样量 */
        private String actualSampleAmount;
    }
}

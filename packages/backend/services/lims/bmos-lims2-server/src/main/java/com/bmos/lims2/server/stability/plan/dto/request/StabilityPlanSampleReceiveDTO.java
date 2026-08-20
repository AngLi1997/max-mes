package com.bmos.lims2.server.stability.plan.dto.request;

import lombok.Data;
import java.time.LocalDate;

/**
 * 稳定性考察计划样品接收DTO
 */
@Data
public class StabilityPlanSampleReceiveDTO {

    /**
     * 样品ID（lm_stability_plan_sample.id）
     */
    private Long sampleId;

    /**
     * 样品接收日期
     */
    private LocalDate receiveDate;

    /**
     * 实际取样量（可选）
     */
    private String actualSampleAmount;
}

package com.bmos.lims2.server.stability.sample.dto.response;

import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性样品管理单行样品DTO
 */
@Data
public class StabilitySampleRowDTO {

    /** lm_stability_plan_sample.id */
    private Long id;

    private Long batchId;
    private String batchNo;

    /** lm_sample.id */
    private Long sampleId;
    private String sampleNo;

    /** 接收数量（actualSampleAmount） */
    private String actualSampleAmount;

    /** 当前样品数量（lm_sample.current_quantity） */
    private String currentQuantity;

    private String sampleUnit;
    private String sampleUnitName;

    private LocalDate receiveDate;
    private String receiverName;

    private String experimentType;
    private String experimentTypeName;
    private String storageCondition;

    /** 储存位置（lm_sample.storage_location） */
    private String storageLocation;

    /** 样品状态 */
    private StabilityPlanSampleStatusEnum status;
}

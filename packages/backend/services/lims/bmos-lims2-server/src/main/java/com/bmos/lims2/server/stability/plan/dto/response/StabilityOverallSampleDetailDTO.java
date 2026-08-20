package com.bmos.lims2.server.stability.plan.dto.response;

import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性整体样品详情DTO（批次维度，含所有试验类型样品列表）
 */
@Data
public class StabilityOverallSampleDetailDTO {

    // ── 基础信息 ────────────────────────────────────────────

    private Long planId;

    private String planCode;

    private Long batchId;

    private String batchNo;

    private LocalDate productionDate;

    private String materialName;

    private String materialCode;

    private String materialSpec;

    private Long materialUnitId;

    private String materialUnitName;

    private String schemeName;

    private String schemeCode;

    private String schemeVersionNo;

    private String remark;

    private String planCreateBy;

    private LocalDateTime planCreateTime;

    // ── 取样信息列表 ─────────────────────────────────────────

    private List<SampleItemDTO> samples;

    /**
     * 单条样品信息（每个试验类型对应一行）
     */
    @Data
    public static class SampleItemDTO {

        /** StabilityPlanSample.id */
        private Long id;

        private String sampleNo;

        private String experimentType;

        private String experimentTypeName;

        private String storageCondition;

        private String plannedSampleAmount;

        private String actualSampleAmount;

        private String sampleUnit;

        private String unitName;

        /** 是否手动新增（schemePlanId 为 null 时为 true，可删除） */
        private boolean manualAdded;

        private StabilityPlanSampleStatusEnum status;

        private String samplerId;

        private String samplerName;

        private LocalDateTime samplingTime;

        private String receiverId;

        private String receiverName;

        private LocalDateTime receiveTime;

        private LocalDate receiveDate;

        private String storageLocation;
    }
}

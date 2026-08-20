package com.bmos.lims2.server.stability.plan.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 稳定性考察计划保存DTO
 */
@Data
public class StabilityInspectPlanSaveDTO {

    /**
     * 计划ID（新增时为null）
     */
    private Long id;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检品名称
     */
    private String materialName;

    /**
     * 检品编码
     */
    private String materialCode;

    /**
     * 检品规格
     */
    private String materialSpec;

    /**
     * 稳定性方案ID
     */
    private Long schemeId;

    /**
     * 稳定性方案版本ID
     */
    private Long schemeVersionId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 批次列表
     */
    private List<BatchDTO> batches;

    @Data
    public static class BatchDTO {
        /**
         * 批号
         */
        private String batchNo;

        /**
         * 批次生产日期
         */
        private LocalDate productionDate;
    }
}

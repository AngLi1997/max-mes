package com.bmos.lims2.server.stability.plan.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 稳定性时间点批量取样DTO
 */
@Data
public class StabilityTimepointBatchTakeDTO {

    /** 取样明细列表 */
    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        /** 时间点任务ID */
        private Long timepointTaskId;

        /** 取样对象（StabilityPlanSample.id，用户在取样页面选择的整体样品） */
        private Long sourcePlanSampleId;

        /** 实际取样量 */
        private String actualSampleAmount;

        /** 取样量单位（覆盖时间点配置的默认单位，可选） */
        private String sampleUnit;
    }
}

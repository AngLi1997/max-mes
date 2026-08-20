package com.bmos.mes.service.trace.material.dto;

import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialTraceHistoryDTO {

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 操作类型
     */
    private MaterialTraceOperateType operateType;
}

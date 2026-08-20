package com.bmos.lims2.server.stability.sample.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 稳定性样品销毁DTO（批量销毁时共用的销毁参数，不含样品ID）
 */
@Data
public class StabilitySampleDestroyDTO {
    /** 销毁原因 */
    private String destructionReason;

    /** 销毁方式 */
    private String destructionMethod;

    /** 销毁时间 */
    private LocalDateTime destructionTime;

    /** 销毁地点 */
    private String destructionLocation;

    /** 备注 */
    private String remark;

    /** 销毁人ID */
    private String destructorId;

    /** 销毁人名称 */
    private String destructorName;

    /** 监督人ID */
    private String supervisorId;

    /** 监督人名称 */
    private String supervisorName;
}

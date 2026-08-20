package com.bmos.lims2.server.inspect.retention.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 样品销毁请求DTO
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
public class SampleDestructionDTO {

    /**
     * 样品ID
     */
    private Long sampleId;

    /**
     * 销毁原因
     */
    private String destructionReason;

    /**
     * 销毁方式
     */
    private String destructionMethod;

    /**
     * 销毁时间
     */
    private LocalDateTime destructionTime;

    /**
     * 销毁地点
     */
    private String destructionLocation;

    /**
     * 备注
     */
    private String remark;

    /**
     * 销毁人ID（默认当前登录人）
     */
    private String destructorId;

    /**
     * 销毁人名称（默认当前登录人）
     */
    private String destructorName;

    /**
     * 监督人ID
     */
    private String supervisorId;

    /**
     * 监督人名称
     */
    private String supervisorName;
}

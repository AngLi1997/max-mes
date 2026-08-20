package com.bmos.lims2.server.inspect.retention.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 留样观察提交DTO
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
public class RetentionObservationSubmitDTO {

    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    /**
     * 观察结果（true-符合，false-不符合）
     */
    @NotNull(message = "观察结果不能为空")
    private Boolean observationResult;

    /**
     * 观察备注
     */
    private String observationRemark;
}

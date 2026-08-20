package com.bmos.lims2.server.task.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 任务终止DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class TaskTerminateDTO {

    /**
     * 任务ID列表
     */
    @NotEmpty(message = "任务ID列表不能为空")
    private List<Long> taskIds;

    /**
     * 终止理由
     */
    @NotNull(message = "终止理由不能为空")
    private String terminateReason;
}
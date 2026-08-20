package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 任务状态统计DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class TaskStatusCountDTO {

    /**
     * 任务状态
     */
    private TaskStatusEnum status;

    /**
     * 任务数量
     */
    private Long count;
}

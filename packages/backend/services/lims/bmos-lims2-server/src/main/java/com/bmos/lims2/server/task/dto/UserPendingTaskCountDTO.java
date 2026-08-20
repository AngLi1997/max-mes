package com.bmos.lims2.server.task.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户待完成任务数量DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class UserPendingTaskCountDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 待完成任务数量
     */
    private Long pendingTaskCount;
}

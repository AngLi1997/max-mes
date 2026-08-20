package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 任务分配DTO
 *
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class TaskAssignDTO {

    /**
     * 任务ID列表
     */
    @NotEmpty(message = "任务ID列表不能为空")
    private List<Long> taskIds;

    /**
     * 分配给的用户ID
     */
    @NotNull(message = "分配用户ID不能为空")
    private Long assigneeId;

    /**
     * 分配给的用户姓名
     */
    private String assigneeName;

    /**
     * 分配备注
     */
    private String remark;

    public String getAssigneeName() {
        return UserUtils.getUsername(assigneeId+"");
    }
}

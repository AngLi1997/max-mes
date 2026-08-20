package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 可分配用户DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class AssignableUserDTO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private String userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String userName;

    /**
     * 团队ID
     */
    @ApiModelProperty("班组id")
    private Long teamId;

    /**
     * 团队名称
     */
    @ApiModelProperty("班组名称")
    private String teamName;

    /**
     * 当前待完成任务数量
     */
    @ApiModelProperty("当前待完成任务数量")
    private Long pendingTaskCount;

    public String getUserName (){
        return UserUtils.getUsername(userId);
    }
}

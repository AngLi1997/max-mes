package com.bmos.lims2.server.task.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.TaskOperationTypeEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务状态变更历史DTO
 *
 * @author system
 * @since 2026/03/05
 */
@Data
@ApiModel("任务状态变更历史DTO")
public class TaskStatusHistoryDTO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("操作类型")
    private TaskOperationTypeEnum operationType;

    @ApiModelProperty("操作前状态")
    private String fromStatus;

    @ApiModelProperty("操作后状态")
    private String toStatus;

    @ApiModelProperty("操作人ID")
    private String operatorId;

    @ApiModelProperty("操作时间")
    private LocalDateTime operateTime;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("评论/备注")
    private String comment;

    @ApiModelProperty("说明")
    private String remark;

    @ApiModelProperty("操作详情（JSON）")
    private String detail;

    @ApiModelProperty("创建人ID")
    private String createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    private String createUsername;

    /**
     * 动态获取创建人姓名
     */
    public String getCreateUsername() {
        BaseUserDO user = UserUtils.getUser(createBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}

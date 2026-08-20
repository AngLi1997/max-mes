package com.bmos.lims2.web.task.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 任务终止请求VO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
@ApiModel("任务终止请求")
public class TaskTerminateReqVO {

    @ApiModelProperty(value = "任务ID列表", required = true)
    @NotEmpty(message = "任务ID列表不能为空")
    private List<Long> taskIds;

    @ApiModelProperty(value = "终止理由", required = true)
    @NotNull(message = "终止理由不能为空")
    private String terminateReason;
}

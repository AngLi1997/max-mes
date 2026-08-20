package com.bmos.lims2.web.task.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 任务退回审批请求VO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
@ApiModel("任务退回审批请求")
public class TaskApprovalReqVO {

    @ApiModelProperty(value = "任务ID列表", required = true)
    @NotEmpty(message = "任务ID列表不能为空")
    private List<Long> taskIds;

    @ApiModelProperty(value = "审批结果", required = true, notes = "true-通过 false-不通过")
    @NotNull(message = "审批结果不能为空")
    private Boolean approved;

    @ApiModelProperty("审批理由")
    private String approvalReason;
}

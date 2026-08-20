package com.bmos.lims2.web.stability.review.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性结果审核-审核不通过请求VO
 */
@Getter
@Setter
@ApiModel("稳定性结果审核不通过请求")
public class StabilityAuditRejectReqVO {

    @ApiModelProperty(value = "检验单ID", required = true)
    @NotNull(message = "检验单ID不能为空")
    private Long inspectionOrderId;

    @ApiModelProperty(value = "工作流流程实例ID", required = true)
    @NotBlank(message = "流程实例ID不能为空")
    private String processInstanceId;

    @ApiModelProperty(value = "工作流任务ID", required = true)
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    @ApiModelProperty(value = "需要退回的录入任务ID集合（lm_task.id）", required = true)
    @NotEmpty(message = "请至少选择一个要退回的任务")
    private List<Long> taskIds;

    @ApiModelProperty("审核意见")
    private String comment;

    @ApiModelProperty("备注")
    private String remark;
}

package com.bmos.lims2.web.stability.review.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 稳定性结果审核-审核通过请求VO
 */
@Getter
@Setter
@ApiModel("稳定性结果审核通过请求")
public class StabilityAuditApproveReqVO {

    @ApiModelProperty(value = "工作流流程实例ID", required = true)
    @NotBlank(message = "流程实例ID不能为空")
    private String processInstanceId;

    @ApiModelProperty(value = "工作流任务ID", required = true)
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    @ApiModelProperty("审核意见")
    private String comment;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "登录名（用于电子签名验证）", required = true)
    @NotBlank(message = "登录名不能为空")
    private String loginName;

    @ApiModelProperty(value = "密码（电子签名确认）", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}

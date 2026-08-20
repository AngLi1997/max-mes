package com.bmos.lims2.web.stability.review.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 稳定性结果审核-审核退回请求VO
 */
@Getter
@Setter
@ApiModel("稳定性结果审核退回请求")
public class StabilityAuditReturnReqVO {

    @ApiModelProperty(value = "工作流执行实例ID（用于退回上一节点）", required = true)
    @NotBlank(message = "执行实例ID不能为空")
    private String executionId;

    @ApiModelProperty("审核意见")
    private String comment;

    @ApiModelProperty("备注")
    private String remark;
}

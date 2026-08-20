package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 稳定性样品接收请求VO
 */
@Data
@ApiModel("稳定性样品接收请求")
public class StabilityPlanSampleReceiveReqVO {

    @ApiModelProperty(value = "接收人姓名", required = true)
    @NotBlank(message = "接收人姓名不能为空")
    private String receiverName;

    @ApiModelProperty(value = "接收人ID", required = true)
    @NotBlank(message = "接收人ID不能为空")
    private String receiverId;
}

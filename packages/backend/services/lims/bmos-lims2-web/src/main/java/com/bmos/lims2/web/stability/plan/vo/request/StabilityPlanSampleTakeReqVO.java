package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 稳定性样品取样请求VO
 */
@Data
@ApiModel("稳定性样品取样请求")
public class StabilityPlanSampleTakeReqVO {

    @ApiModelProperty(value = "取样人姓名", required = true)
    @NotBlank(message = "取样人姓名不能为空")
    private String samplerName;

    @ApiModelProperty(value = "取样人ID", required = true)
    @NotBlank(message = "取样人ID不能为空")
    private String samplerId;
}

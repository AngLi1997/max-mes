package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 整体样品接收-明细行请求VO
 */
@Data
@ApiModel("整体接收-明细行")
public class StabilityOverallSampleReceiveReqVO {

    @ApiModelProperty(value = "整体样品ID（lm_stability_plan_sample.id）", required = true)
    @NotNull(message = "样品ID不能为空")
    private Long sampleId;

    @ApiModelProperty(value = "储存位置", required = true)
    @NotBlank(message = "储存位置不能为空")
    private String storageLocation;
}

package com.bmos.lims2.web.stability.sample.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 稳定性样品移动位置请求VO
 */
@Data
@ApiModel("稳定性样品移动位置请求")
public class StabilitySampleMoveReqVO {

    @ApiModelProperty(value = "新储存位置", required = true)
    @NotBlank(message = "储存位置不能为空")
    private String storageLocation;

    @ApiModelProperty(value = "移动原因", required = true)
    @NotBlank(message = "移动原因不能为空")
    private String moveReason;
}

package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 稳定性考察计划暂停请求VO
 */
@Data
@ApiModel("稳定性考察计划暂停请求")
public class StabilityInspectPlanPauseReqVO {

    @ApiModelProperty(value = "计划ID", required = true)
    @NotNull(message = "计划ID不能为空")
    private Long id;

    @ApiModelProperty(value = "暂停理由", required = true)
    @NotBlank(message = "暂停理由不能为空")
    private String pauseReason;
}

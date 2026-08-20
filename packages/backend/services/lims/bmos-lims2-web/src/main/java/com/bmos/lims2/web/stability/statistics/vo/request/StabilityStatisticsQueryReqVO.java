package com.bmos.lims2.web.stability.statistics.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 稳定性统计查询请求VO
 */
@Getter
@Setter
@ApiModel("稳定性统计查询请求")
public class StabilityStatisticsQueryReqVO {

    @NotNull(message = "检品ID不能为空")
    @ApiModelProperty(value = "检品ID", required = true)
    private Long materialId;

    @NotNull(message = "稳定性考察计划ID不能为空")
    @ApiModelProperty(value = "稳定性考察计划ID", required = true)
    private Long planId;

    @NotBlank(message = "试验类型不能为空")
    @ApiModelProperty(value = "试验类型", required = true)
    private String experimentType;

    @NotBlank(message = "储存条件不能为空")
    @ApiModelProperty(value = "储存条件", required = true)
    private String storageCondition;
}

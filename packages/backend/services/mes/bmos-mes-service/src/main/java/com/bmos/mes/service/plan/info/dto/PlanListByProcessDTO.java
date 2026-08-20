package com.bmos.mes.service.plan.info.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("根据工艺查询生产计划DTO")
@Data
public class PlanListByProcessDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

}

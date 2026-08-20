package com.bmos.mes.service.plan.info.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("PlanStartPageDTO:生产计划生产前确认分页列表查询条件DTO")
public class PlanStartPageDTO {
    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("是否关联 未关联FALSE 已关联TRUE")
    private String relation;
}

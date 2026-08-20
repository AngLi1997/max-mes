package com.bmos.mes.service.plan.info.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("生产审核进度详情分页查询DTO")
@Data
public class PlanAuditProgressDetailQueryDTO {

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工步名称")
    private String procedureStepName;

    @ApiModelProperty("排序字段")
    private String orderBy;

    @ApiModelProperty("排序规则")
    private String dir;

}

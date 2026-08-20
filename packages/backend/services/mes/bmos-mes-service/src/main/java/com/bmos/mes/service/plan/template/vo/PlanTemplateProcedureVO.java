package com.bmos.mes.service.plan.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("生产计划模板工序VO")
@Data
public class PlanTemplateProcedureVO {

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("间隔时长")
    private Integer intervalDuration;

    @ApiModelProperty("执行时长")
    private Integer executionDuration;

    @ApiModelProperty("工序名称")
    private String name;

    @ApiModelProperty("排序")
    private Integer sort;

}

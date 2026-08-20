package com.bmos.mes.service.plan.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("生产计划模板工序时长DTO")
public class PlanTemplateProcedureConfigDTO {

    @ApiModelProperty("工序id(非工序模型id)")
    @NotNull
    private Long procedureId;

    @ApiModelProperty("工序名称")
    @NotBlank
    private String name;

    @NotNull
    @ApiModelProperty("工序开始时长")
    private Integer intervalDuration;

    @ApiModelProperty("工序执行时长")
    private Integer executionDuration;

    @ApiModelProperty("工序排序")
    private Integer sort;

}

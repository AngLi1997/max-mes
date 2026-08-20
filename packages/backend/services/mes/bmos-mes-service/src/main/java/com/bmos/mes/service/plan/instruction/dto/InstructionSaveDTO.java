package com.bmos.mes.service.plan.instruction.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("InstructionSaveDTO:生产计划指令单保存DTO")
public class InstructionSaveDTO {
    @NotNull
    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @NotEmpty
    @ApiModelProperty("生产工序节点id")
    private String nodeId;

    @ApiModelProperty("历史工序id(以此判断多给版本的节点是否是同一工序)")
    private Long procedureId;

    @NotNull
    @ApiModelProperty("生产工序id")
    private Long procedureModelId;

    @NotEmpty
    @ApiModelProperty("生产工序名称")
    private String procedureModelName;

    @ApiModelProperty("生产工序阶段编码")
    private String procedureModelCode;

    @NotNull
    @ApiModelProperty("负责人")
    private Long principal;

    @NotNull
    @ApiModelProperty("排序")
    private Integer sort;
}

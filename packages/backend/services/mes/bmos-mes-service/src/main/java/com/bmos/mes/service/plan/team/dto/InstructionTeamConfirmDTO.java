package com.bmos.mes.service.plan.team.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 生产计划指令单班组表
*/
@Getter
@Setter
@ApiModel("InstructionTeamConfirmDTO:生产计划指令单确认DTO")
public class InstructionTeamConfirmDTO {
    @NotNull
    @ApiModelProperty("指令单id")
    private Long instructionId;

    @NotNull
    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @NotEmpty
    @ApiModelProperty("生产工序节点id")
    private String nodeId;

    @NotNull
    @ApiModelProperty("历史工序id(以此判断多给版本的节点是否是同一工序)")
    private Long procedureId;

    @NotNull
    @ApiModelProperty("历史工序id(以此判断多给版本的节点是否是同一工序)")
    private Long procedureModelId;

    @Valid
    @NotEmpty
    @ApiModelProperty("生产工序节点id")
    private List<InstructionTeamConfirmDetailDTO> details;
}

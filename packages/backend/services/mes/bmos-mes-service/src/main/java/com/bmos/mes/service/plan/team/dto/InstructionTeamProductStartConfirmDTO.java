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
@ApiModel("InstructionTeamProductStartConfirmDTO:生产计划指令单生产前确认DTO")
public class InstructionTeamProductStartConfirmDTO {
    @ApiModelProperty("生产计划id")
    private Long planId;
    @ApiModelProperty("关联生产计划")
    private List<InstructionTeamProductStartConfirmDetailDTO> relationPlan;

    @Valid
    @NotEmpty
    @ApiModelProperty("生产工序节点id")
    private List<InstructionTeamConfirmDTO> teamConfirmDTO;
}

package com.bmos.mes.service.plan.team.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 生产计划指令单班组表
*/
@Getter
@Setter
@ApiModel("InstructionTeamProductStartConfirmDetailDTO:生产计划指令单生产前确认DTO")
public class InstructionTeamProductStartConfirmDetailDTO {
    @NotNull
    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("生产计划批号列表")
    private List<String> batchNos;

    @ApiModelProperty("生产计划Id列表")
    private List<Long> planIds;
}

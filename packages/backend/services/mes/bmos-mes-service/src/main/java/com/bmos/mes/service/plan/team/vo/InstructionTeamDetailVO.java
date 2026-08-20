package com.bmos.mes.service.plan.team.vo;

import com.bmos.mes.service.plan.info.vo.PlanDetailVO;
import com.bmos.mes.service.plan.instruction.vo.InstructionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@ApiModel("InstructionTeamDetailVO:生产计划班组详情")
public class InstructionTeamDetailVO {
    @Tolerate
    public InstructionTeamDetailVO() {}
    @ApiModelProperty("生产计划对象")
    private PlanDetailVO planDetailVO;

    @ApiModelProperty("指令单对象+班组信息")
    private InstructionVO instructionVO;
}

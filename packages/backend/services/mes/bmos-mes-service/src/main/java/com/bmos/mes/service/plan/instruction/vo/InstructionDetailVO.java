package com.bmos.mes.service.plan.instruction.vo;

import com.bmos.mes.service.plan.info.vo.PlanDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.util.List;

/**
* 生产计划指令单表
*/
@Getter
@Setter
@Builder
@ApiModel("InstructionDetailVO:指令单详情VO")
public class InstructionDetailVO {
    @Tolerate
    public InstructionDetailVO() {}
    @ApiModelProperty("生产计划对象")
    private PlanDetailVO planDetailVO;

    @ApiModelProperty("指令单对象")
    private List<InstructionVO> instructions;
}

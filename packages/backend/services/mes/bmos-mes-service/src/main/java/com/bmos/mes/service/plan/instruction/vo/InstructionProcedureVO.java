package com.bmos.mes.service.plan.instruction.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 生产计划指令单表
 * @author renjinguang
 */
@Getter
@Setter
@ApiModel("InstructionVO:工序换班查询工序详情VO")
public class InstructionProcedureVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("生产工序名称")
    private String procedureModelName;

    @ApiModelProperty("生产工序阶段编码")
    private String procedureModelCode;

    @ApiModelProperty("产线id")
    private Long lineIds;


    @ApiModelProperty("班组列表")
    private List<InstructionTeamVO> teams;

    @ApiModelProperty("排序")
    private Integer sort;

}

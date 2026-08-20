package com.bmos.mes.service.plan.team.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@ApiModel("InstructionTeamConfirmDetailDTO:生产计划指令单确认详情DTO")
public class InstructionTeamDetailItemVO {
    @ApiModelProperty("生产工序节点id")
    private String nodeId;
    @ApiModelProperty("历史工序id(以此判断多给版本的节点是否是同一工序)")
    private Long procedureId;

    @ApiModelProperty("历史工序id(以此判断多给版本的节点是否是同一工序)")
    private Long procedureModelId;

    @ApiModelProperty("生产工序步骤节点id")
    private String nodeStepId;

    @ApiModelProperty("历史工序id(以此判断多给版本的节点是否是同一工序)")
    private Long procedureStepId;

    @ApiModelProperty("生产工序步骤id")
    private Long procedureStepModelId;

    @ApiModelProperty("生产工序步骤名称")
    private String procedureStepModelName;

    @ApiModelProperty("执行时长")
    private Long procedureStepTime;

    @ApiModelProperty("执行时长单位")
    private String procedureStepTimeUnit;

    @ApiModelProperty("班组id列表")
    private List<Long> teamIds;

    @ApiModelProperty("排序")
    private Integer sort;
}

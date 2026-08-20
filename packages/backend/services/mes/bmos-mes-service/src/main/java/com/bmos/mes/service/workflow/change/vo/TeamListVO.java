package com.bmos.mes.service.workflow.change.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName TeamListVO
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/8/20 17:04
 */
@Setter
@Getter
@ToString
@ApiModel("班组信息vo")
public class TeamListVO {

    @ApiModelProperty("工序模型id")
    private Long procedureModeId;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("班组id")
    private List<Long> teamIdS;

    @ApiModelProperty("工序流程节点id")
    private String nodeId;

    @ApiModelProperty("工步节点id")
    private String nodeStepId;

    @ApiModelProperty("换班次数")
    private Integer changeTeamNumber;

    @ApiModelProperty("换班类型")
    private String changeTeamType;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;
}

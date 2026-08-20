package com.bmos.mes.service.plan.instruction.vo;

import com.bmos.mes.service.plan.team.vo.InstructionTeamDetailItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 生产计划指令单表
 */
@Getter
@Setter
@ApiModel("InstructionTeamVO:生产执行查询班组详情信息")
public class InstructionTeamVO {

    @ApiModelProperty("指令单id")
    private Long instructionId;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("生产工步名称")
    private String procedureStepModelName;


    @ApiModelProperty("工艺配置工步是否配置班组信息,true:已配置，false:未配置")
    private Boolean isFlay;

    @ApiModelProperty("执行时长")
    private Integer procedureStepTime;

    @ApiModelProperty("执行时长单位")
    private String procedureStepTimeUnit;

    @ApiModelProperty("换班表id")
    private Long changeTeamId;

    @ApiModelProperty("班组id列表")
    private List<Long> teamIds;

    @ApiModelProperty("换班次数")
    private Integer productChangeNumber;

    @ApiModelProperty("换班类型")
    private String productChangeType;


    @ApiModelProperty("排序")
    private Integer sort;
}

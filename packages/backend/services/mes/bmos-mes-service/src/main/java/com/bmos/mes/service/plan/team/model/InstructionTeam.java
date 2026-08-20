package com.bmos.mes.service.plan.team.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.Tolerate;

import java.util.List;

/**
* 生产计划指令单班组表
*/
@Getter
@Setter
@With
@AllArgsConstructor
@ToString
@TableName(value = "bm_product_instruction_team", autoResultMap = true)
public class InstructionTeam extends BaseDO {
    @Tolerate
    public InstructionTeam() {}
    @ApiModelProperty("指令单id")
    private Long instructionId;
    @ApiModelProperty("生产计划id")
    private Long productPlanId;

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

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("班组id列表")
    private List<Long> teamIds;

    @ApiModelProperty("排序")
    private Integer sort;
}

package com.bmos.mes.service.plan.instruction.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.common.enums.plan.InstructionStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

/**
* 生产计划指令单表
*/
@Getter
@Setter
@SuperBuilder
@ToString
@TableName(value = "bm_product_instruction")
public class Instruction extends BaseDO {
    @Tolerate
    public Instruction() {}
    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("生产工序节点id")
    private String nodeId;

    @ApiModelProperty("历史工序id(以此判断多给版本的节点是否是同一工序)")
    private Long procedureId;

    @ApiModelProperty("生产工序id")
    private Long procedureModelId;

    @ApiModelProperty("生产工序名称")
    private String procedureModelName;

    @ApiModelProperty("生产工序阶段编码")
    private String procedureModelCode;

    @ApiModelProperty("负责人")
    private Long principal;

    @ApiModelProperty("指令单状态 待分解WAIT_RESOLVE 已分解 RESOLVE已确认 CONFIRM")
    private InstructionStatusEnum status;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("指令单确认人id")
    private String confirmUserId;
}

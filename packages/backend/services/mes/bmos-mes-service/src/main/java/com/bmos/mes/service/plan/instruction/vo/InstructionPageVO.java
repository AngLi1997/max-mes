package com.bmos.mes.service.plan.instruction.vo;

import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@ApiModel("InstructionPageVO:生产计划指令单确认分页VO")
public class InstructionPageVO {
    @ApiModelProperty("指令单id")
    private Long id;
    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("生产时间")
    private LocalDate productDate;

    @ApiModelProperty("计划类型")
    private ProductPlanTypeEnum type;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    @ApiModelProperty("生产工序名称")
    private String procedureModelName;
}

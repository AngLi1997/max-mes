package com.bmos.mes.service.plan.production.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("直接创建指令单生成编号DTO")
@Data
public class DirectlyCreateBuildNoDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本")
    @NotBlank
    private String processVersion;

    @EnumValidate(value = ProductPlanTypeEnum.class)
    @ApiModelProperty(value = "计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次", required = true)
    private String productPlanType;

    @ApiModelProperty("产线id")
    @NotNull
    private Long productionLineId;

}

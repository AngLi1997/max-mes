package com.bmos.mes.service.plan.info.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ApiModel("PlanBatchSaveDetailDTO:计划批量保存明细Dto")
public class PlanBatchSaveDetailDTO {
    @NotEmpty
    @EnumValidate(value = ProductPlanTypeEnum.class)
    @ApiModelProperty("计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次")
    private String type;

    @NotEmpty
    @ApiModelProperty("计划编号")
    private String planNo;

    @NotEmpty
    @ApiModelProperty("生产批号")
    private String batchNo;

    @NotNull
    @ApiModelProperty("生产时间")
    private LocalDate productDate;

    @NotNull
    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @NotNull
    @ApiModelProperty("生产批量单位id")
    private Long unitId;
}

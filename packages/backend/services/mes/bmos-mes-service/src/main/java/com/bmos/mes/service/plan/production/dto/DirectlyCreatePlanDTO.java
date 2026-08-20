package com.bmos.mes.service.plan.production.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel("直接创建指令单DTO")
@Data
public class DirectlyCreatePlanDTO {

    @ApiModelProperty(value = "工艺id", required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "工艺版本", required = true)
    @NotBlank
    private String processVersion;

    @EnumValidate(value = ProductPlanTypeEnum.class)
    @ApiModelProperty(value = "计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次", required = true)
    private String productPlanType;

    @ApiModelProperty(value = "指令单编号", required = true)
    @NotBlank
    private String planNo;

    @ApiModelProperty(value = "生产批号", required = true)
    @NotBlank
    private String batchNo;

    @ApiModelProperty(value = "生产批量", required = true)
    @NotNull
    private BigDecimal batchQuantity;

    @ApiModelProperty(value = "批量单位", required = true)
    @NotNull
    private Long unitId;

    @ApiModelProperty(value = "产线id", required = true)
    @NotNull
    private Long productionLineId;

    @ApiModelProperty("编号生成日期")
    private LocalDate codeApplyTime;

    @ApiModelProperty("指令单编号规则")
    private String planNoCode;

    @ApiModelProperty("生产批号编号规则")
    private String batchNoCode;

}

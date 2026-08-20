package com.bmos.mes.service.plan.info.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("PlanUpdateDTO:计划更新Dto")
public class PlanUpdateDTO {
    @NotNull
    @ApiModelProperty("计划id")
    private Long id;

    @NotEmpty
    @EnumValidate(value = ProductPlanTypeEnum.class)
    @ApiModelProperty("计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次")
    private String type;

    @NotNull
    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("产线id")
    @NotNull
    private Long productionLineId;

    @ApiModelProperty("指令单编号")
    @NotBlank
    private String planNo;

    @ApiModelProperty("编号规则code")
    private String planNoCode;

    @ApiModelProperty("生产批号")
    @NotBlank
    private String batchNo;

    @ApiModelProperty("批号规则code")
    private String batchNoCode;

    @ApiModelProperty("关联生产计划")
    private List<ProductPlanRelationDTO> relationPlanList = new ArrayList<>();
}

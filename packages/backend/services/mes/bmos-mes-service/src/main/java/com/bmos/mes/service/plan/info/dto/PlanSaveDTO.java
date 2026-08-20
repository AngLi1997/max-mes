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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("PlanSaveDTO:计划保存Dto")
public class PlanSaveDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    @NotEmpty
    @ApiModelProperty("计划编号")
    private String planNo;

    @NotEmpty
    @ApiModelProperty("生产批号")
    private String batchNo;

    @NotNull
    @ApiModelProperty("生产时间")
    private LocalDate productDate;

    @NotEmpty
    @EnumValidate(value = ProductPlanTypeEnum.class)
    @ApiModelProperty("计划类型 PRODUCT 生产批次 EXPERIMENT 实验批次 VERIFY 验证批次")
    private String type;

    @NotNull
    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("生产批量")
    @NotNull
    private BigDecimal batchQuantity;

    @ApiModelProperty("生产批量单位id")
    @NotNull
    private Long unitId;

    @NotEmpty
    @ApiModelProperty("产品名称")
    private String productName;

    @NotEmpty
    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @NotEmpty
    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty("包装规格")
    private String packingSpecification;

    @NotNull
    @ApiModelProperty("生产工艺id")
    private Long processId;

    @NotEmpty
    @ApiModelProperty("生产工艺名称")
    private String processName;

    @NotEmpty
    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    @ApiModelProperty("关联生产计划")
    private List<ProductPlanRelationDTO> relationPlanList = new ArrayList<>();


    @NotNull
    @ApiModelProperty("生产工艺数量")
    private Integer processNum;

    @ApiModelProperty("计划编码回传编号日期")
    private LocalDate planNoCodeApplyTime;

    @ApiModelProperty("计划编码规则code")
    private String planNoCode;

    @ApiModelProperty("批号编码规则code")
    private String batchNoCode;

    @ApiModelProperty("计划类型")
    private String productPlanType;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("产线code")
    private String productionLineCode;

    @ApiModelProperty("产品阶段代码")
    private String productionStageCode;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("生产计划itemId")
    private Long productionPlanItemId;
}

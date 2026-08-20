package com.bmos.mes.service.plan.info.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ApiModel("PlanBatchSaveDTO:计划批量保存Dto")
public class PlanBatchSaveDTO {
    @Valid
    @NotEmpty
    @ApiModelProperty("生产计划明细")
    private List<PlanBatchSaveDetailDTO> details;

    @NotNull
    @ApiModelProperty("产品Id")
    private Long productId;

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

    @NotNull
    @ApiModelProperty("生产工艺数量")
    private Integer processNum;

    @ApiModelProperty("计划编码回传编号日期")
    private LocalDate planNoCodeApplyTime;

    @ApiModelProperty("计划编码规则code")
    private String planNoCode;

    @ApiModelProperty("批号回传编号日期")
    private LocalDate batchNoCodeApplyTime;

    @ApiModelProperty("批号编码规则code")
    private String batchNoCode;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("计划类型")
    private String productPlanType;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("产线code")
    private String productionLineCode;

    @ApiModelProperty("产品阶段代码")
    private String productionStageCode;
}

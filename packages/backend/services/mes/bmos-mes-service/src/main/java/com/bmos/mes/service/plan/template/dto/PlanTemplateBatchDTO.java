package com.bmos.mes.service.plan.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ApiModel("生产计划模板批次保存DTO")
@Data
public class PlanTemplateBatchDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本")
    @NotBlank
    private String processVersion;

    @ApiModelProperty("key-前端使用")
    private String processKey;

    @ApiModelProperty("工艺名称")
    @NotBlank
    private String processName;

    @ApiModelProperty("间隔时长(天数)")
    @NotNull
    private Integer intervalDuration;

    @ApiModelProperty("工艺执行时长(天数)")
    @NotNull
    private Integer executionDuration;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("产线名称")
    private String productionLineName;

    @ApiModelProperty("产线编码")
    private String productionLineCode;

    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("关联模板批次sort集合")
    private List<Integer> relationBatchSortList;

    @ApiModelProperty("是否沿用批号")
    private boolean reuseBatchNumber;

    @ApiModelProperty("工序执行时间配置列表")
    @Valid
    private List<PlanTemplateProcedureConfigDTO> procedureDurationList;

    @ApiModelProperty("前端使用配置")
    private List<RelationProcessDTO> relationProcessesList;

    @ApiModelProperty("排序,从0起")
    @NotNull
    private Integer sort;

    @ApiModelProperty("生产批量单位id")
    private Long unitId;


}

package com.bmos.mes.service.plan.production.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName ProductionPlanItemDetailVO
 * @Description 生产计划详情vo
 * @Author Ren Jin Guang
 * @Date 2024/8/27 18:46
 */
@Setter
@Getter
@ToString
@ApiModel("生产计划详情vo")
public class ProductionPlanItemDetailVO {

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("计划开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("计划结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("产线编码")
    private String productionLineCode;

    @ApiModelProperty("产线名称")
    private String productionLineName;

    @ApiModelProperty("生产指令单批号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("关联批次信息")
    private String productionBatchList;

    @ApiModelProperty("关联模板批次sort集合")
    private List<Integer> relationBatchSortList;

    @ApiModelProperty("工序相关信息")
    private String procedureListItem;

    @ApiModelProperty("模板工序详情")
    private String procedureConfig;


    @ApiModelProperty("工序相关信息")
    private List<ProcedureDetailVO> procedureListDetail;

    @ApiModelProperty("是否沿用")
    private Boolean reuseBatchNumber;

    @ApiModelProperty("批次排序")
    private Integer sort;

    @ApiModelProperty("分组信息")
    private Integer groupNumber;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("模板批次id")
    private Long templateBatchId;

    @ApiModelProperty("前端回显使用关联批次")
    private String relatedBatchInfo;

    @ApiModelProperty("计划详情id")
    private Long id;
}

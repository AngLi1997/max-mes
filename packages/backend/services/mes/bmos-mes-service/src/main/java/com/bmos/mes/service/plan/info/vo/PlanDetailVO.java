package com.bmos.mes.service.plan.info.vo;

import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import com.bmos.mes.service.plan.production.vo.ProcedureDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("PlanDetailVO:生产计划详情")
public class PlanDetailVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产时间")
    private LocalDate productDate;

    @ApiModelProperty("计划类型")
    private ProductPlanTypeEnum type;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("产险名称")
    private String productionLineName;

    @ApiModelProperty("产险编码")
    private String productionLineCode;

    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("工序列表")
    private List<ProcedureDetailVO> procedureList;
}

package com.bmos.mes.service.plan.info.vo;

import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel("生产审核进度分页VO")
@Data
public class ProductionAuditProgressPageVO {

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

    @ApiModelProperty("生产计划状态")
    private ProductPlanStartEnum start;

    @ApiModelProperty("流程实例")
    private String processInstanceId;

    @ApiModelProperty("生产批量")
    private String batchQuantity;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("审核中节点数量")
    private int auditingCount;

}

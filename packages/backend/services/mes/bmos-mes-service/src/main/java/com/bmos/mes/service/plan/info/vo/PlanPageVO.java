package com.bmos.mes.service.plan.info.vo;

import com.bmos.mes.common.enums.plan.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("PlanPageVO:生产计划分页VO")
public class PlanPageVO {
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

    @ApiModelProperty("状态 编辑EDIT 审批中AUDIT 确认CONFIRM 废弃DISCARD")
    private ProductPlanStatusEnum status;

    @ApiModelProperty("状态 待分解WAIT_DECOMPOSE 待确认WAIT_CONFIRM 待下发WAIT_SEND 已下发 SEND")
    private ProductPlanInstructStatusEnum instructStatus;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("生产计划状态")
    private ProductPlanStartEnum start;

    @ApiModelProperty("生产状态")
    private ProductionStatusEnum productionStatus;

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

    @ApiModelProperty("产线编码")
    private String code;

    @ApiModelProperty("产线名称")
    private String name;

    @ApiModelProperty("产线编码")
    private String productionLineCode;

    @ApiModelProperty("产线名称")
    private String productionLineName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @JsonIgnore
    private BigDecimal batchQuantityDecimal;

    @ApiModelProperty("归档文件Url")
    private String archiveFileUrl;

    @ApiModelProperty("归档状态")
    private PlanArchiveStatusEnum archiveStatus;

    @ApiModelProperty("异常数量")
    private Long exceptionCount;

    @ApiModelProperty("是否已被暂停")
    private Boolean paused;

    @ApiModelProperty("生产计划相关详情id")
    private Long productionPlanItemId;

    public ProductionStatusEnum getProductionStatus() {
        return ProductionStatusEnum.getByMappingEnum(start, instructStatus, paused);
    }

}

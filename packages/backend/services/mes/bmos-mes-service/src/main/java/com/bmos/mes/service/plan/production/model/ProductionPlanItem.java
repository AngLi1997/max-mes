package com.bmos.mes.service.plan.production.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @ClassName ProductionPlan
 * @Description 生产计划详情表
 * @Author Ren Jin Guang
 * @Date 2024/8/27 16:34
 */
@TableName("bm_production_plan_item")
@Getter
@Setter
public class ProductionPlanItem extends BaseDO {

    @ApiModelProperty("计划id")
    private Long productionPlanId;

    @ApiModelProperty("计划模板详情表id")
    private Long templateBatchId;

    @ApiModelProperty("计划开始日期")
    private LocalDate startTime;

    @ApiModelProperty("计划结束日期")
    private LocalDate endTime;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("产线名称")
    private String productionLineName;

    @ApiModelProperty("产线编码")
    private String productionLineCode;

    @ApiModelProperty("生产指令单批号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("关联批次信息")
    private String productionBatchList;

    @ApiModelProperty("工序相关信息")
    private String procedureList;

    @ApiModelProperty("分组信息")
    private Integer groupNumber;

    /**
     * 工序数量
     */
    private Integer processNum;

    @ApiModelProperty("前端回显使用关联批次")
    private String relatedBatchInfo;
}

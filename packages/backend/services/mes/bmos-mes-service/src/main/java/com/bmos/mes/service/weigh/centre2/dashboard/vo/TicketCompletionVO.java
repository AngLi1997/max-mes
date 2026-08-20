package com.bmos.mes.service.weigh.centre2.dashboard.vo;

import com.bmos.mes.service.weigh.centre2.dashboard.enums.DashboardWeighStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 称量工单完成情况VO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/27 17:50
 */
@Data
@ApiModel("称量工单完成情况VO")
public class TicketCompletionVO {
    
    @ApiModelProperty(value = "工单编号", example = "250527001")
    private String ticketNo;
    
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;
    
    @ApiModelProperty(value = "物料编码", example = "WH030101")
    private String materialCode;
    
    @ApiModelProperty(value = "称量中心", example = "C3-3车间称量中心")
    private String weighCentreName;
    
    @ApiModelProperty(value = "需求总量", example = "80.80")
    private BigDecimal requiredTotalQuantity;
    
    @ApiModelProperty(value = "完成重量", example = "20.80")
    private BigDecimal completedWeight;

    @ApiModelProperty(value = "单位", example = "KG")
    private String unit;
    
    @ApiModelProperty(value = "完成率", example = "0%")
    private String completionRate;
    
    @ApiModelProperty(value = "计划执行时间", example = "2025-05-24")
    private LocalDate planExecuteDate;
    
    @ApiModelEnumProperty(value = "状态", enumClass = DashboardWeighStatusEnum.class)
    private DashboardWeighStatusEnum status;
} 
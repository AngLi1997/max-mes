package com.bmos.mes.service.weigh.centre2.dashboard.vo;

import com.bmos.mes.service.weigh.centre2.dashboard.enums.DashboardWeighStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 今日工单VO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/27 17:42
 */
@Data
@ApiModel("今日工单VO")
public class TodayTicketVO {
    
    @ApiModelProperty(value = "工单编号", example = "250527001")
    private String ticketNo;
    
    @ApiModelProperty(value = "物料信息", example = "37A-氯化钠")
    private String materialName;
    
    @ApiModelProperty(value = "称量中心", example = "C3-3车间称量中心")
    private String weighCentreName;
    
    @ApiModelProperty(value = "需求总量", example = "80.80")
    private BigDecimal requiredTotalQuantity;
    
    @ApiModelProperty(value = "完成重量", example = "0")
    private BigDecimal completedWeight;

    @ApiModelProperty(value = "单位", example = "KG")
    private String unit;
    
    @ApiModelProperty(value = "完成率", example = "0%")
    private String completionRate;

    @ApiModelEnumProperty(value = "状态", enumClass = DashboardWeighStatusEnum.class)
    private DashboardWeighStatusEnum status;
} 
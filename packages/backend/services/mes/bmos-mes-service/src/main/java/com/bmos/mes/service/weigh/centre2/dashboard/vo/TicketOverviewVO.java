package com.bmos.mes.service.weigh.centre2.dashboard.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单概览VO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/27 17:40
 */
@Data
@ApiModel("工单概览VO")
public class TicketOverviewVO {
    
    @ApiModelProperty(value = "已完成工单数量", example = "150")
    private Integer completedCount;
    
    @ApiModelProperty(value = "已下发工单数量", example = "300")
    private Integer releasedCount;
} 
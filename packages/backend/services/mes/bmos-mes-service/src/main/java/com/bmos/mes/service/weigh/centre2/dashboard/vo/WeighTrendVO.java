package com.bmos.mes.service.weigh.centre2.dashboard.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 称量趋势VO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/27 17:48
 */
@Data
@ApiModel("称量趋势VO")
public class WeighTrendVO {
    
    @ApiModelProperty(value = "日期", example = "2025-05-10")
    private LocalDate date;
    
    @ApiModelProperty(value = "数量", example = "80")
    private Integer count;
} 
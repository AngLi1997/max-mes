package com.bmos.mes.service.weigh.centre2.dashboard.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 称量工单完成情况分页查询DTO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/28 10:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("称量工单完成情况分页查询DTO")
public class TicketCompletionQueryDTO extends BasePage {
    
    @ApiModelProperty(value = "查询范围（天数）：7-近7天，15-近15天", example = "7", required = true)
    private Integer recentDays = 7;
} 
package com.bmos.mes.service.weigh.centre2.dashboard.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.weigh.centre2.dashboard.dto.ProductionCompletionQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.dto.TicketCompletionQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.dto.TodayTicketQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.service.IWeighDashboardService;
import com.bmos.mes.service.weigh.centre2.dashboard.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 称量工单看板接口
 * @author liang
 * @version 1.0.0
 * @date 2025/5/27 17:30
 */
@RestController
@RequestMapping("/weigh/dashboard")
@Api(tags = "称量工单看板接口")
@Validated
public class WeighDashboardController {
    
    @Resource
    private IWeighDashboardService weighDashboardService;
    
    @GetMapping("/ticket/overview")
    @ApiOperation("获取工单概览数据")
    @ApiImplicitParam(name = "recentDays", value = "查询范围（天数）：1-今天，7-近7天，15-近15天", required = true, defaultValue = "1")
    public ResponseInfo<TicketOverviewVO> getTicketOverview(@RequestParam(defaultValue = "1") Integer recentDays) {
        return ResponseInfo.success(weighDashboardService.getTicketOverview(recentDays));
    }
    
    @GetMapping("/ticket/today")
    @ApiOperation("获取今日工单数据")
    public ResponseInfo<CommonPage<TodayTicketVO>> getTodayTicket(TodayTicketQueryDTO queryDTO) {
        return ResponseInfo.success(weighDashboardService.getTodayTicket(queryDTO));
    }
    
    @GetMapping("/production/completion")
    @ApiOperation("获取生产批次配料完成情况")
    public ResponseInfo<CommonPage<ProductionCompletionVO>> getProductionCompletion(ProductionCompletionQueryDTO queryDTO) {
        return ResponseInfo.success(weighDashboardService.getProductionCompletion(queryDTO));
    }
    
    @GetMapping("/ticket/trend")
    @ApiOperation("获取称量工单趋势")
    @ApiImplicitParam(name = "recentDays", value = "查询范围（天数）：7-近7天，15-近15天", required = true, defaultValue = "7", dataType = "Integer", paramType = "query")
    public ResponseInfo<List<WeighTrendVO>> getTicketTrend(@RequestParam(defaultValue = "7") Integer recentDays) {
        return ResponseInfo.success(weighDashboardService.getTicketTrend(recentDays));
    }
    
    @GetMapping("/requirement/trend")
    @ApiOperation("获取称量需求趋势")
    @ApiImplicitParam(name = "recentDays", value = "查询范围（天数）：7-近7天，15-近15天", required = true, defaultValue = "7", dataType = "Integer", paramType = "query")
    public ResponseInfo<List<WeighTrendVO>> getRequirementTrend(@RequestParam(defaultValue = "7") Integer recentDays) {
        return ResponseInfo.success(weighDashboardService.getRequirementTrend(recentDays));
    }
    
    @GetMapping("/ticket/completion")
    @ApiOperation("获取称量工单完成情况")
    public ResponseInfo<CommonPage<TicketCompletionVO>> getTicketCompletion(TicketCompletionQueryDTO queryDTO) {
        return ResponseInfo.success(weighDashboardService.getTicketCompletion(queryDTO));
    }
}

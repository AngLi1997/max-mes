package com.bmos.mes.service.weigh.centre2.dashboard.service;

import com.bmos.mes.service.weigh.centre2.dashboard.dto.ProductionCompletionQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.dto.TicketCompletionQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.dto.TodayTicketQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.vo.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 称量工单看板Service接口
 * @author liang
 * @version 1.0.0
 * @date 2025/5/27 17:52
 */
public interface IWeighDashboardService {

    /**
     * 获取工单概览数据
     * @param recentDays 查询范围
     * @return 工单概览数据
     */
    TicketOverviewVO getTicketOverview(Integer recentDays);

    /**
     * 获取今日工单数据
     * @param queryDTO 查询参数
     * @return 今日工单分页列表
     */
    CommonPage<TodayTicketVO> getTodayTicket(TodayTicketQueryDTO queryDTO);

    /**
     * 获取生产批次配料完成情况
     * @param queryDTO 查询参数
     * @return 生产批次配料完成情况分页列表
     */
    CommonPage<ProductionCompletionVO> getProductionCompletion(ProductionCompletionQueryDTO queryDTO);

    /**
     * 获取称量工单趋势
     * @param recentDays 查询范围
     * @return 称量工单趋势列表
     */
    List<WeighTrendVO> getTicketTrend(Integer recentDays);

    /**
     * 获取称量需求趋势
     * @param recentDays 查询范围
     * @return 称量需求趋势列表
     */
    List<WeighTrendVO> getRequirementTrend(Integer recentDays);

    /**
     * 获取称量工单完成情况
     * @param queryDTO 查询参数
     * @return 称量工单完成情况分页列表
     */
    CommonPage<TicketCompletionVO> getTicketCompletion(TicketCompletionQueryDTO queryDTO);
} 
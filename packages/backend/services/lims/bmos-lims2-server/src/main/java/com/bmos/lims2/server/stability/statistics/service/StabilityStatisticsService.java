package com.bmos.lims2.server.stability.statistics.service;

import com.bmos.lims2.server.stability.statistics.dto.StabilityExperimentTypeOptionDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityPlanOptionDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsItemDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsQueryDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 稳定性统计查询Service
 */
public interface StabilityStatisticsService {

    /**
     * 查询检项统计数据
     */
    List<StabilityStatisticsItemDTO> queryStatisticsItems(StabilityStatisticsQueryDTO queryDTO);

    /**
     * 查询稳定性统计结果（表头+数据分离）
     */
    com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsResultDTO queryStatisticsResult(StabilityStatisticsQueryDTO queryDTO);

    /**
     * 查询指定检品下的稳定性考察计划下拉列表
     */
    List<StabilityPlanOptionDTO> getPlanOptions(Long materialId);

    /**
     * 查询指定计划下的试验类型下拉列表
     */
    List<StabilityExperimentTypeOptionDTO> getExperimentTypeOptions(Long planId);

    /**
     * 导出检项统计数据
     */
    void exportStatisticsItems(StabilityStatisticsQueryDTO queryDTO, HttpServletResponse response);
}

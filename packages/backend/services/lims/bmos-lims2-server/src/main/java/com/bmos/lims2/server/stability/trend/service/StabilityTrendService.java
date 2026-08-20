package com.bmos.lims2.server.stability.trend.service;

import com.bmos.lims2.server.stability.statistics.dto.StabilitySchemeOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendBatchOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendChartResultDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendDataItemDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendDataPointOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendExperimentTypeOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendParameterOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendQueryDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendTableResultDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 稳定性趋势查询Service
 */
public interface StabilityTrendService {

    /**
     * 查询指定检品下的稳定性方案下拉列表
     */
    List<StabilitySchemeOptionDTO> getSchemeOptions(Long materialId);

    /**
     * 查询指定方案下的试验类型下拉列表
     */
    List<StabilityTrendExperimentTypeOptionDTO> getExperimentTypeOptions(Long versionId);

    /**
     * 查询指定方案下的分析项下拉列表
     */
    List<StabilityTrendParameterOptionDTO> getParameterOptions(Long versionId);

    /**
     * 查询指定方案+分析项下的数据点下拉列表
     */
    List<StabilityTrendDataPointOptionDTO> getDataPointOptions(Long versionId, Long parameterId);

    /**
     * 查询指定方案+试验类型下的批号下拉列表
     */
    List<StabilityTrendBatchOptionDTO> getBatchOptions(Long versionId, String experimentType, String storageCondition);

    /**
     * 查询趋势数据（平铺行）
     */
    List<StabilityTrendDataItemDTO> queryTrendData(StabilityTrendQueryDTO queryDTO);

    /**
     * 查询趋势数据表格结果（表头+数据行）
     */
    StabilityTrendTableResultDTO queryTrendTable(StabilityTrendQueryDTO queryDTO);

    /**
     * 查询趋势图表结果（ECharts结构）
     */
    StabilityTrendChartResultDTO queryTrendChart(StabilityTrendQueryDTO queryDTO);

    /**
     * 导出趋势数据
     */
    void exportTrendData(StabilityTrendQueryDTO queryDTO, HttpServletResponse response);
}

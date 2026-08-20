package com.bmos.lims2.server.stability.trend.mapper;

import com.bmos.lims2.server.stability.statistics.dto.StabilitySchemeOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendBatchOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendDataItemDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendDataPointOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendExperimentTypeOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendParameterOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendQueryDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendTableHeaderDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendTimepointColumnDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性趋势查询Mapper
 */
@Mapper
public interface StabilityTrendMapper {

    /**
     * 查询指定检品下的稳定性方案下拉列表
     */
    List<StabilitySchemeOptionDTO> selectSchemeOptions(@Param("materialId") Long materialId);

    /**
     * 查询指定方案下的试验类型下拉列表
     */
    List<StabilityTrendExperimentTypeOptionDTO> selectExperimentTypeOptions(@Param("versionId") Long versionId);

    /**
     * 查询指定方案下的分析项下拉列表
     */
    List<StabilityTrendParameterOptionDTO> selectParameterOptions(@Param("versionId") Long versionId);

    /**
     * 查询指定方案+分析项下的数据点下拉列表
     */
    List<StabilityTrendDataPointOptionDTO> selectDataPointOptions(@Param("versionId") Long versionId,
                                                                   @Param("parameterId") Long parameterId);

    /**
     * 查询指定方案+试验类型下的批号下拉列表
     */
    List<StabilityTrendBatchOptionDTO> selectBatchOptions(@Param("versionId") Long versionId,
                                                          @Param("experimentType") String experimentType,
                                                          @Param("storageCondition") String storageCondition);

    /**
     * 查询趋势数据（平铺行）
     */
    List<StabilityTrendDataItemDTO> selectTrendData(@Param("query") StabilityTrendQueryDTO query);

    /**
     * 查询数据点基本信息（用于表头）
     */
    StabilityTrendTableHeaderDTO selectDataPointById(@Param("versionId") Long versionId,
                                                     @Param("dataPointName") String dataPointName);

    /**
     * 查询方案配置的时间点列（用于表头）
     */
    List<StabilityTrendTimepointColumnDTO> selectTimepointColumns(@Param("versionId") Long versionId,
                                                                   @Param("experimentType") String experimentType,
                                                                   @Param("storageCondition") String storageCondition);

    /**
     * 查询指定检验单的0月数据点记录（用于趋势表格0月列）
     */
    List<StabilityTrendDataItemDTO> selectZeroMonthEntryRecords(@Param("orderId") Long orderId,
                                                                @Param("dataPointName") String dataPointName,
                                                                @Param("batchNo") String batchNo);
}

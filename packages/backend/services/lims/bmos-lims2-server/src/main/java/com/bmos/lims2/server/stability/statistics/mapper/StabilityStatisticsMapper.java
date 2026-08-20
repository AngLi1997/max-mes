package com.bmos.lims2.server.stability.statistics.mapper;

import com.bmos.lims2.server.stability.statistics.dto.StabilityExperimentTypeOptionDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityPlanOptionDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsItemDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性统计查询Mapper
 */
@Mapper
public interface StabilityStatisticsMapper {

    /**
     * 查询检项信息（统计结果）
     */
    List<StabilityStatisticsItemDTO> selectStatisticsItems(@Param("query") StabilityStatisticsQueryDTO query);

    /**
     * 查询指定检品下的稳定性考察计划下拉列表
     */
    List<StabilityPlanOptionDTO> selectPlanOptions(@Param("materialId") Long materialId);

    /**
     * 查询指定计划下的试验类型下拉列表
     */
    List<StabilityExperimentTypeOptionDTO> selectExperimentTypeOptions(@Param("planId") Long planId);

    /**
     * 查询基本信息（物料、方案等）
     */
    com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsBasicInfoDTO selectBasicInfo(@Param("query") StabilityStatisticsQueryDTO query);
}

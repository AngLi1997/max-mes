package com.bmos.lims2.server.stability.sample.mapper;

import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleManagementQueryDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySampleFlatRowDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySamplePrintTagResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性样品管理自定义Mapper（跨表查询，不继承BaseMapperX）
 */
@Mapper
public interface StabilitySampleManagementMapper {

    /**
     * 统计满足条件的不重复计划数（用于分页总数）
     */
    long countDistinctPlans(@Param("query") StabilitySampleManagementQueryDTO query);

    /**
     * 分页获取计划ID列表（按计划分页）
     */
    List<Long> selectDistinctPlanIds(@Param("query") StabilitySampleManagementQueryDTO query,
                                     @Param("offset") long offset,
                                     @Param("size") int size);

    /**
     * 查询给定计划ID列表下的所有符合条件的样品平铺行
     */
    List<StabilitySampleFlatRowDTO> selectSampleFlatRows(@Param("planIds") List<Long> planIds,
                                                          @Param("query") StabilitySampleManagementQueryDTO query);

    /**
     * 根据样品编号查询打印标签信息
     */
    StabilitySamplePrintTagResultDTO selectPrintTagResult(@Param("sampleNo") String sampleNo);

    /**
     * 根据时间点子样品编号查询周期任务取样打印标签信息
     */
    StabilitySamplePrintTagResultDTO selectTimepointPrintTagResult(@Param("sampleNo") String sampleNo);
}

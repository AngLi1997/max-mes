package com.bmos.lims2.server.inspect.parameter.mapper;

import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterTrend;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 分析项趋势线配置Mapper接口
 */
@Mapper
public interface InspectParameterTrendMapper extends BaseMapperX<InspectParameterTrend> {

    /**
     * 根据分析项id查询趋势线配置列表
     */
    default List<InspectParameterTrend> selectByDataPointId(Long dataPointId) {
        return selectList(new LambdaQueryWrapperX<InspectParameterTrend>()
                .eq(InspectParameterTrend::getDataPointId, dataPointId)
                .eq(InspectParameterTrend::getDeleted, false));
    }

    /**
     * 根据分析项id删除趋势线配置
     */
    default void deleteByParameterId(Long dataPointId) {
        delete(new LambdaQueryWrapperX<InspectParameterTrend>()
                .eq(InspectParameterTrend::getDataPointId, dataPointId));
    }
} 
package com.bmos.lims2.server.inspect.parameter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterDataPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分析项数据点Mapper接口
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Mapper
public interface InspectParameterDataPointMapper extends BaseMapper<InspectParameterDataPoint> {

    /**
     * 根据分析项id查询数据点列表
     */
    List<InspectParameterDataPoint> selectByParameterId(@Param("parameterId") Long parameterId);

    /**
     * 根据分析项id删除数据点
     */
    int deleteByParameterId(@Param("parameterId") Long parameterId);

    /**
     * 检查名称是否存在
     */
    boolean existByName(@Param("parameterId") Long parameterId, @Param("name") String name);
} 
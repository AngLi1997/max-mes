package com.bmos.lims2.server.inspect.scheme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeDataPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验方案数据点配置Mapper接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Mapper
public interface InspectionSchemeDataPointMapper extends BaseMapper<InspectionSchemeDataPoint> {

    /**
     * 根据分析项配置ID获取数据点配置列表
     *
     * @param parameterConfigId 分析项配置ID
     * @return 数据点配置列表
     */
    List<InspectionSchemeDataPointDTO > listByParameterConfigId(@Param("parameterConfigId") Long parameterConfigId);
} 
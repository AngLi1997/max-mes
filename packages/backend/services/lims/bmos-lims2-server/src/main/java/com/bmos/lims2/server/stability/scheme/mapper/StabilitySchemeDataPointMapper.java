package com.bmos.lims2.server.stability.scheme.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeDataPointDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeDataPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性方案数据点配置Mapper接口
 */
@Mapper
public interface StabilitySchemeDataPointMapper extends BaseMapper<StabilitySchemeDataPoint> {

    /**
     * 根据分析项配置ID查询数据点列表（含是否被判定引用标志）
     */
    List<StabilitySchemeDataPointDTO> listByParamConfigId(@Param("parameterConfigId") Long parameterConfigId);

    default void deleteByParamConfigId(Long parameterConfigId) {
        LambdaQueryWrapper<StabilitySchemeDataPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeDataPoint::getParameterConfigId, parameterConfigId);
        delete(wrapper);
    }

    default void deleteByItemId(Long itemConfigId) {
        LambdaQueryWrapper<StabilitySchemeDataPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeDataPoint::getItemConfigId, itemConfigId);
        delete(wrapper);
    }

    default void deleteByVersionId(Long versionId) {
        LambdaQueryWrapper<StabilitySchemeDataPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeDataPoint::getVersionId, versionId);
        delete(wrapper);
    }

    default List<StabilitySchemeDataPoint> selectByParamConfigIdSimple(Long parameterConfigId) {
        LambdaQueryWrapper<StabilitySchemeDataPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeDataPoint::getParameterConfigId, parameterConfigId)
                .eq(StabilitySchemeDataPoint::getDeleted, false)
                .orderByAsc(StabilitySchemeDataPoint::getId);
        return selectList(wrapper);
    }
}

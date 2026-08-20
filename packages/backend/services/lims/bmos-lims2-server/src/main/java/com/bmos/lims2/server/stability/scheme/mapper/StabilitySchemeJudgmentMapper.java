package com.bmos.lims2.server.stability.scheme.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeJudgmentDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeJudgment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性方案判定配置Mapper接口
 */
@Mapper
public interface StabilitySchemeJudgmentMapper extends BaseMapper<StabilitySchemeJudgment> {

    /**
     * 根据分析项配置ID查询判定列表
     */
    List<StabilitySchemeJudgmentDTO> listByParamConfigId(@Param("parameterConfigId") Long parameterConfigId);

    default void deleteByParamConfigId(Long parameterConfigId) {
        LambdaQueryWrapper<StabilitySchemeJudgment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeJudgment::getParameterConfigId, parameterConfigId);
        delete(wrapper);
    }

    default void deleteByDataPointConfigId(Long dataPointConfigId) {
        LambdaQueryWrapper<StabilitySchemeJudgment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeJudgment::getDataPointConfigId, dataPointConfigId);
        delete(wrapper);
    }

    default void deleteByItemId(Long itemConfigId) {
        LambdaQueryWrapper<StabilitySchemeJudgment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeJudgment::getItemConfigId, itemConfigId);
        delete(wrapper);
    }

    default void deleteByVersionId(Long versionId) {
        LambdaQueryWrapper<StabilitySchemeJudgment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeJudgment::getVersionId, versionId);
        delete(wrapper);
    }
}

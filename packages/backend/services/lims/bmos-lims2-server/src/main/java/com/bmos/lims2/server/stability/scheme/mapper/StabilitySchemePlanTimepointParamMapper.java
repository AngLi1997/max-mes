package com.bmos.lims2.server.stability.scheme.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlanTimepointParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 稳定性方案检验计划时间点分析项关联Mapper
 */
@Mapper
public interface StabilitySchemePlanTimepointParamMapper extends BaseMapper<StabilitySchemePlanTimepointParam> {

    default List<StabilitySchemePlanTimepointParam> selectByTimepointId(Long timepointId) {
        LambdaQueryWrapper<StabilitySchemePlanTimepointParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemePlanTimepointParam::getTimepointId, timepointId)
                .eq(StabilitySchemePlanTimepointParam::getDeleted, false)
                .orderByAsc(StabilitySchemePlanTimepointParam::getId);
        return selectList(wrapper);
    }

    default void deleteByTimepointId(Long timepointId) {
        LambdaQueryWrapper<StabilitySchemePlanTimepointParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemePlanTimepointParam::getTimepointId, timepointId);
        delete(wrapper);
    }

    default void deleteByPlanId(Long planId) {
        LambdaQueryWrapper<StabilitySchemePlanTimepointParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemePlanTimepointParam::getPlanId, planId);
        delete(wrapper);
    }

    default void deleteByVersionId(Long versionId) {
        LambdaQueryWrapper<StabilitySchemePlanTimepointParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemePlanTimepointParam::getVersionId, versionId);
        delete(wrapper);
    }
}

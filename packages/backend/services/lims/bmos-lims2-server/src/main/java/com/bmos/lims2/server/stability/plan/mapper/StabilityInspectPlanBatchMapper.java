package com.bmos.lims2.server.stability.plan.mapper;

import com.bmos.lims2.server.stability.plan.entity.StabilityInspectPlanBatch;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性考察计划批次Mapper接口
 */
@Mapper
public interface StabilityInspectPlanBatchMapper extends BaseMapperX<StabilityInspectPlanBatch> {

    /**
     * 根据计划ID查询批次列表
     */
    List<StabilityInspectPlanBatch> selectByPlanId(@Param("planId") Long planId);

    /**
     * 根据计划ID删除批次
     */
    int deleteByPlanId(@Param("planId") Long planId);
}

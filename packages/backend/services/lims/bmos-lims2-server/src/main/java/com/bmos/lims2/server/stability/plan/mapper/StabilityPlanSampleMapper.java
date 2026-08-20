package com.bmos.lims2.server.stability.plan.mapper;

import com.bmos.lims2.server.stability.plan.entity.StabilityPlanSample;
import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性考察计划样品Mapper
 */
@Mapper
public interface StabilityPlanSampleMapper extends BaseMapperX<StabilityPlanSample> {

    /**
     * 根据计划ID查询所有样品
     */
    List<StabilityPlanSample> selectByPlanId(@Param("planId") Long planId);

    /**
     * 根据批次ID查询样品列表
     */
    List<StabilityPlanSample> selectByBatchId(@Param("batchId") Long batchId);

    /**
     * 根据计划ID删除所有样品（物理删除，用于计划撤销）
     */
    int deleteByPlanId(@Param("planId") Long planId);

    /**
     * 查询批次状态为"待取样"的批次ID列表：
     * 存在至少一条 PENDING 样品
     */
    List<Long> selectPendingBatchIds();

    /**
     * 查询批次状态为"已取样待接收"的批次ID列表：
     * 无 PENDING 样品，且存在至少一条 SAMPLED 样品
     */
    List<Long> selectSampledBatchIds();

    /**
     * 根据计划ID批量更新样品状态（仅更新指定 fromStatus 的记录）
     */
    void updateSampleStatusByPlanId(@Param("planId") Long planId,
                                    @Param("fromStatus") StabilityPlanSampleStatusEnum fromStatus,
                                    @Param("toStatus") StabilityPlanSampleStatusEnum toStatus);

    /**
     * 根据计划ID、批次ID和方案检验计划ID查询样品
     */
    default StabilityPlanSample selectByPlanIdAndBatchIdAndSchemePlanId(Long planId, Long batchId, Long schemePlanId) {
        return selectOne(new LambdaQueryWrapperX<StabilityPlanSample>()
                .eq(StabilityPlanSample::getPlanId, planId)
                .eq(StabilityPlanSample::getBatchId, batchId)
                .eq(StabilityPlanSample::getSchemePlanId, schemePlanId));
    }
}

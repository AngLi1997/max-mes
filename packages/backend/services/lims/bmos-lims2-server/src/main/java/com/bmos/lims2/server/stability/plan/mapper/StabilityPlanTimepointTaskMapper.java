package com.bmos.lims2.server.stability.plan.mapper;

import com.bmos.lims2.server.stability.plan.dto.request.StabilityTimepointSampleQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityTimepointSampleDTO;
import com.bmos.lims2.server.stability.plan.entity.StabilityPlanTimepointTask;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 稳定性考察计划时间点任务Mapper
 */
@Mapper
public interface StabilityPlanTimepointTaskMapper extends BaseMapperX<StabilityPlanTimepointTask> {

    /**
     * 根据样品ID查询时间点任务列表（有序）
     */
    List<StabilityPlanTimepointTask> selectBySampleId(@Param("sampleId") Long sampleId);

    /**
     * 根据计划ID查询所有时间点任务
     */
    List<StabilityPlanTimepointTask> selectByPlanId(@Param("planId") Long planId);

    /**
     * 根据计划ID和批次ID查询时间点任务
     */
    List<StabilityPlanTimepointTask> selectByPlanIdAndBatchId(@Param("planId") Long planId,
                                                              @Param("batchId") Long batchId);

    /**
     * 根据检验单ID查询时间点任务（用于样品接收后关联任务生成）
     */
    StabilityPlanTimepointTask selectByInspectionOrderId(@Param("inspectionOrderId") Long inspectionOrderId);

    /**
     * 查询所有到期未处理的时间点任务（planned_date <= today，status=NOT_STARTED，计划状态=IN_PROGRESS）
     * 用于定时任务触发
     */
    List<StabilityPlanTimepointTask> selectDueTasks(@Param("today") LocalDate today);

    /**
     * 查询指定计划下到期未处理的时间点任务（planned_date <= today，status=NOT_STARTED）
     * 用于计划恢复时立即补触发
     */
    List<StabilityPlanTimepointTask> selectDueTasksByPlanId(@Param("today") LocalDate today,
                                                            @Param("planId") Long planId);

    /**
     * 根据计划ID删除所有时间点任务（物理删除，用于计划撤销）
     */
    int deleteByPlanId(@Param("planId") Long planId);

    /**
     * 删除指定样品下所有未发起检验单的时间点任务（物理删除，用于版本切换重建）
     */
    @Delete("DELETE FROM lm_stability_plan_timepoint_task " +
            "WHERE sample_id = #{sampleId} AND inspection_order_id IS NULL")
    int deleteUnlaunchedBySampleId(@Param("sampleId") Long sampleId);

    /**
     * 根据样品ID和方案时间点ID查询时间点任务
     */
    default StabilityPlanTimepointTask selectBySampleIdAndSchemeTimepointId(Long sampleId, Long schemeTimepointId) {
        return selectOne(new LambdaQueryWrapperX<StabilityPlanTimepointTask>()
                .eq(StabilityPlanTimepointTask::getSampleId, sampleId)
                .eq(StabilityPlanTimepointTask::getSchemeTimepointId, schemeTimepointId));
    }

    /**
     * 分页查询 WAITING_SAMPLE 状态的时间点取样列表（多表 JOIN）
     */
    List<StabilityTimepointSampleDTO> pageTimepointSamples(@Param("query") StabilityTimepointSampleQueryDTO query);

    /**
     * 根据计划ID、批次ID和方案时间点ID查询时间点任务
     */
    default StabilityPlanTimepointTask selectByPlanIdAndBatchIdAndSchemeTimepointId(
            Long planId, Long batchId, Long schemeTimepointId) {
        return selectOne(new LambdaQueryWrapperX<StabilityPlanTimepointTask>()
                .eq(StabilityPlanTimepointTask::getPlanId, planId)
                .eq(StabilityPlanTimepointTask::getBatchId, batchId)
                .eq(StabilityPlanTimepointTask::getSchemeTimepointId, schemeTimepointId));
    }

    /**
     * 根据计划ID、批次ID、试验类型、储存条件、时间点数值和单位查询时间点任务
     * （不依赖版本相关的 schemeTimepointId，用于版本切换后矩阵展示）
     */
    default StabilityPlanTimepointTask selectByPlanIdAndBatchIdAndTimepoint(
            Long planId, Long batchId, String experimentType, String storageCondition,
            Integer timeValue, String timeUnit) {
        return selectOne(new LambdaQueryWrapperX<StabilityPlanTimepointTask>()
                .eq(StabilityPlanTimepointTask::getPlanId, planId)
                .eq(StabilityPlanTimepointTask::getBatchId, batchId)
                .eq(StabilityPlanTimepointTask::getExperimentType, experimentType)
                .eq(StabilityPlanTimepointTask::getStorageCondition, storageCondition)
                .eq(StabilityPlanTimepointTask::getTimeValue, timeValue)
                .eq(StabilityPlanTimepointTask::getTimeUnit, timeUnit));
    }
}


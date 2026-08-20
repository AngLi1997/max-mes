package com.bmos.lims2.server.stability.plan.service;

import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanPauseDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanSaveDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleReceiveDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleTakeDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityPlanSampleAddItemDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityTimepointBatchTakeDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityTimepointSampleQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilitySchemeExperimentTypeDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityInspectPlanDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityInspectPlanDetailDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOrderSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOverallSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOverallSampleDetailDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityTimepointSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityTimepointSourceSampleDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 稳定性考察计划Service接口
 */
public interface StabilityInspectPlanService {

    /**
     * 分页查询稳定性考察计划列表
     */
    CommonPage<StabilityInspectPlanDTO> pageStabilityInspectPlan(StabilityInspectPlanQueryDTO queryDTO);

    /**
     * 查询稳定性考察计划详情（含矩阵）
     */
    StabilityInspectPlanDetailDTO getStabilityInspectPlan(Long id);

    /**
     * 新增稳定性考察计划
     */
    Long saveStabilityInspectPlan(StabilityInspectPlanSaveDTO saveDTO);

    /**
     * 修改稳定性考察计划（仅待开始状态可修改）
     */
    void updateStabilityInspectPlan(StabilityInspectPlanSaveDTO saveDTO);

    /**
     * 暂停稳定性考察计划
     */
    void pauseStabilityInspectPlan(StabilityInspectPlanPauseDTO pauseDTO);

    /**
     * 恢复稳定性考察计划（可选择切换方案版本）
     *
     * @param id              计划ID
     * @param schemeVersionId 可选，恢复时指定的新方案版本ID；为null则保持当前版本
     */
    void resumeStabilityInspectPlan(Long id, Long schemeVersionId);

    /**
     * 查询稳定性考察计划样品列表（含稳定性上下文）
     *
     * @param timepointTaskId 时间点任务ID
     */
    List<StabilityOrderSampleDTO> listOrderSamples(Long timepointTaskId);

    /**
     * 稳定性时间点样品取样（取样即接收，完成后状态流转至 IN_PROGRESS）
     *
     * @param timepointTaskId 时间点任务ID
     * @param samplerName     取样人姓名
     * @param samplerId       取样人ID
     */
    void takeStabilitySample(Long timepointTaskId, String samplerName, String samplerId);

    /**
     * 检验单完成回调：将对应时间点任务标记为 COMPLETED，写入 completedDate。
     * 由样品审核通过时触发，用于驱动计划完结判断。
     *
     * @param inspectionOrderId 已完成的检验单ID
     */
    void onInspectionOrderFinished(Long inspectionOrderId);

    /**
     * 撤销稳定性考察计划（仅 PENDING 状态可操作）
     *
     * @param id 计划ID
     */
    void withdrawPlan(Long id);

    /**
     * 用户手动选择0月检验单（当同批号存在多条常规检验单时调用）
     *
     * @param batchId          批次ID（lm_stability_inspect_plan_batch.id）
     * @param inspectionOrderId 用户选择的检验单ID
     */
    void selectZeroMonthOrder(Long batchId, Long inspectionOrderId);

    /**
     * 【定时任务】处理到期时间点任务：将 planned_date <= 今日 且所属计划为进行中的
     * NOT_STARTED 任务更新为 WAITING_SAMPLE，触发检验工作流
     *
     * @return 本次处理的任务数量
     */
    int triggerDueTimepointTasks();

    // ══════════════════════ 整体样品管理 ══════════════════════

    /**
     * 统计待取样的整体样品数量（用于取样登记页签徽标）
     */
    long countPendingOverallSamples();

    /**
     * 分页查询整体样品列表
     */
    CommonPage<StabilityOverallSampleDTO> pageOverallSamples(StabilityOverallSampleQueryDTO queryDTO);

    /**
     * 稳定性样品接收列表：仅展示批次状态为"已取样待接收"（无 PENDING、存在 SAMPLED）的批次
     */
    CommonPage<StabilityOverallSampleDTO> pageOverallSamplesForReceive(StabilityOverallSampleQueryDTO queryDTO);

    /** 返回批次状态为"已取样待接收"的批次ID列表（供徽标计数使用） */
    List<Long> getSampledBatchIds();

    /**
     * 获取整体样品详情（按批次维度，返回该批次下所有试验类型样品列表）
     *
     * @param batchId 批次ID（lm_stability_inspect_plan_batch.id）
     */
    StabilityOverallSampleDetailDTO getOverallSampleDetail(Long batchId);

    /**
     * 获取整体样品接收详情（按批次维度，仅返回已取样待接收和已接收的样品）
     * 用于稳定性样品接收详情页，与取样详情页使用独立接口
     *
     * @param batchId 批次ID（lm_stability_inspect_plan_batch.id）
     */
    StabilityOverallSampleDetailDTO getOverallSampleDetailForReceive(Long batchId);

    /**
     * 整体批量取样：按批次，为每个试验类型样品创建父 lm_sample，回写 sampleId/sampleNo，状态 → SAMPLED
     *
     * @param batchId 批次ID（lm_stability_inspect_plan_batch.id）
     * @param dto     取样信息（取样人 + 各样品实际取样量列表）
     */
    void takeOverallSamples(Long batchId, StabilityOverallSampleTakeDTO dto);

    /**
     * 整体批量接收：按批次，更新每个样品的 lm_sample.received，状态 → RECEIVED，生成时间点任务
     *
     * @param batchId 批次ID（lm_stability_inspect_plan_batch.id）
     * @param items   各样品接收明细列表
     */
    void receiveOverallSamples(List<StabilityOverallSampleReceiveDTO> items);

    /**
     * 整体取样-新增样品：向已有批次手动追加 StabilityPlanSample 记录（不来源于方案）
     *
     * @param batchId 批次ID
     * @param items   新增样品明细（试验类型、储存条件、计划取样量等）
     * @return 新增的样品列表（结构同取样详情中的 samples）
     */
    List<StabilityOverallSampleDetailDTO.SampleItemDTO> addOverallSamples(Long batchId, List<StabilityPlanSampleAddItemDTO> items);

    /**
     * 整体取样-删除手动新增样品（仅 schemePlanId 为 null 且状态为 PENDING 的样品可删除）
     *
     * @param sampleId StabilityPlanSample.id
     */
    void deleteManualOverallSample(Long sampleId);

    /**
     * 查询计划关联方案版本的试验类型列表（用于新增样品时下拉选项）
     *
     * @param planId 计划ID
     * @return 方案版本下所有检验计划的试验类型列表
     */
    List<StabilitySchemeExperimentTypeDTO> listSchemeExperimentTypes(Long planId);

    /**
     * 扫码识别：根据样品编号查询对应批次的整体样品详情
     * 用于扫码批量接收页面，扫码枪扫到样品编号后定位批次
     *
     * @param sampleNo 样品编号（lm_stability_plan_sample.sample_no）
     * @return 批次整体样品详情，若未找到返回 null
     */
    StabilityOverallSampleDetailDTO getOverallSampleDetailBySampleNo(String sampleNo);

    // ══════════════════════ 时间点取样 ══════════════════════

    /**
     * 分页查询待取样的时间点任务列表（状态为 WAITING_SAMPLE）
     */
    CommonPage<StabilityTimepointSampleDTO> pageTimepointSamples(StabilityTimepointSampleQueryDTO queryDTO);

    /**
     * 查询时间点任务可选取样对象列表（同批次同试验类型的已接收整体样品）
     *
     * @param timepointTaskId 时间点任务ID
     * @return 可选取样对象列表（含样品编号、量、储存位置）
     */
    List<StabilityTimepointSourceSampleDTO> listSourceSamples(Long timepointTaskId);

    /**
     * 批量提交时间点取样（多行同时提交，取样即接收，状态流转至 IN_PROGRESS）
     *
     * @param dto 批量取样信息
     */
    void batchTakeTimepointSamples(StabilityTimepointBatchTakeDTO dto);
}

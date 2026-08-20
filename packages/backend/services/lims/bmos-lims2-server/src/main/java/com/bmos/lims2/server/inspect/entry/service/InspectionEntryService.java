package com.bmos.lims2.server.inspect.entry.service;

import com.bmos.lims2.server.inspect.entry.dto.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 检验录入服务接口
 *
 * @author system
 * @since 2025/01/30
 */
public interface InspectionEntryService {

    /**
     * 分析项录入列表查询
     *
     * @param queryDTO 查询条件
     * @return 分析项录入列表
     */
    CommonPage<AnalysisItemEntryDTO> getAnalysisItemEntryPage(AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 分析项-方法分组（母列表）分页
     *
     * @param queryDTO 查询条件
     * @return 分析项-方法分组分页结果
     */
    CommonPage<com.bmos.lims2.server.inspect.entry.dto.AnalysisItemMethodGroupDTO> getAnalysisItemMethodEntryPage(AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 检验单录入列表查询
     *
     * @param queryDTO 查询条件
     * @return 检验单录入列表
     */
    CommonPage<InspectionOrderEntryDTO> getInspectionOrderEntryPage(InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 根据分析项任务ID查询数据点录入记录
     *
     * @param taskId 任务ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> getEntryRecordsByTaskId(Long taskId);

    /**
     * 根据检验单ID和分析项ID查询数据点录入记录
     *
     * @param inspectionOrderId 检验单ID
     * @param parameterId       分析项ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> getEntryRecordsByInspectionOrderIdAndParameterId(Long inspectionOrderId, Long parameterId);

    /**
     * 根据数据点配置ID查询录入记录
     *
     * @param dataPointConfigId 数据点配置ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> getEntryRecordsBySchemeDataPointId(Long dataPointConfigId);

    /**
     * 根据数据点配置ID和任务ID查询录入记录
     *
     * @param dataPointConfigId 数据点配置ID
     * @param taskId           任务ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> getEntryRecordsBySchemeDataPointIdAndTaskId(Long dataPointConfigId, Long taskId);

    /**
     * 批量录入数据点
     *
     * @param batchEntryDTO 批量录入数据
     * @return 录入成功的记录数
     */
    int batchSaveEntryRecords(BatchEntryDTO batchEntryDTO);

    /**
     * 批量设置检验时间
     *
     * @param batchTestTimeDTO 批量设置检验时间数据
     * @return 更新成功的记录数
     */
    int batchUpdateTestTime(List<BatchTestTimeDTO> batchTestTimeDTO);

    /**
     * 批量录入判定结论
     *
     * @param list 批量判定结论数据
     * @return 更新成功的记录数
     */
    int batchUpdateJudgment(java.util.List<com.bmos.lims2.server.inspect.entry.dto.BatchJudgmentDTO> list);

    /**
     * 统计录入未完成的分析项数量
     *
     * @param queryDTO 查询条件
     * @return 未完成数量
     */
    Long countIncompleteAnalysisItems(AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 统计录入未完成的检验单数量
     *
     * @param queryDTO 查询条件
     * @return 未完成数量
     */
    Long countIncompleteInspectionOrders(InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 统计判定异常的分析项数量
     *
     * @param queryDTO 查询条件
     * @return 异常数量
     */
    Long countAbnormalAnalysisItems(AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 统计判定异常的检验单数量
     *
     * @param queryDTO 查询条件
     * @return 异常数量
     */
    Long countAbnormalInspectionOrders(InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 合并统计 - 分析项维度
     *
     * @param queryDTO 查询条件
     * @return 统计信息
     */
    EntryStatsDTO getAnalysisItemStats(AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 合并统计 - 检验单维度
     *
     * @param queryDTO 查询条件
     * @return 统计信息
     */
    EntryStatsDTO getInspectionOrderStats(InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 检项查询-分页返回表头与数据
     */
    com.bmos.mybatis.page.CommonPage<DataPointQueryRespDTO.DataRow> queryDataPointRows(DataPointQueryPageReqDTO reqDTO);

    /**
     * 检项查询-表头（一级/二级）
     */
    DataPointQueryRespDTO loadHeaders(DataPointQueryPageReqDTO reqDTO);

    /**
     * 自动重算判定结论（满足配置与数据完整性时触发）
     *
     * @param taskId 任务ID
     * @param recalcJudgment 是否触发自动判定
     */
    void updateTaskJudgment(Long taskId, boolean recalcJudgment);

    /**
     * 更新分析项任务的录入状态（不负责结论重算）
     *
     * @param taskId 任务ID
     */
    void updateTaskStatus(Long taskId);

    /**
     * 兼容旧接口：同时处理结论与状态
     *
     * @param taskId 任务ID
     */
    void updateTaskEntryStatusAndJudgment(Long taskId);

    /**
     * 设置检验录入查询的数据权限
     *
     * @param reqVO 请求VO对象
     * @return 设置了权限信息的查询DTO
     */
    AnalysisItemEntryQueryDTO setAnalysisItemEntryPermission(Object reqVO);

    /**
     * 设置检验单录入查询的数据权限
     *
     * @param reqVO 请求VO对象
     * @return 设置了权限信息的查询DTO
     */
    InspectionOrderEntryQueryDTO setInspectionOrderEntryPermission(Object reqVO);

    /**
     * ELN同步录入（根据 taskId + dataPointConfigId 自动判断插入或修改）
     * - 若已存在有值记录，则走修改（赋值 id）
     * - 否则走新增
     */
    int upsertEntryRecordsFromEln(BatchEntryDTO batchEntryDTO);
}

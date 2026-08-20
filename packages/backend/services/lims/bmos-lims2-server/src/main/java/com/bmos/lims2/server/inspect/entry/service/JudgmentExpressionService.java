package com.bmos.lims2.server.inspect.entry.service;

import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;

import java.util.List;

/**
 * 判定表达式评估服务接口
 *
 * @author system
 * @since 2025/01/30
 */
public interface JudgmentExpressionService {

    /**
     * 评估分析项的判定表达式
     *
     * @param taskId      任务ID
     * @param entryRecords 数据点录入记录列表
     * @return 判定结果 true-通过，false-不通过
     */
    Boolean evaluateJudgmentExpression(Long taskId, List<InspectionEntryRecordDTO> entryRecords);

    /**
     * 检查是否存在异常情况
     *
     * @param taskId      任务ID
     * @param entryRecords 数据点录入记录列表
     * @return true-存在异常，false-正常
     */
    Boolean checkAbnormalCondition(Long taskId, List<InspectionEntryRecordDTO> entryRecords);

    /**
     * 获取判定表达式
     *
     * @param parameterId 分析项ID
     * @param schemeVersionId 方案版本ID
     * @return 判定表达式
     */
    String getJudgmentExpression(Long parameterId, Long schemeVersionId);
}

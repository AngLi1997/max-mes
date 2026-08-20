package com.bmos.lims2.server.inspect.scheme.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeJudgmentBatchUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeJudgmentSaveDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeJudgment;

import java.util.List;
import java.util.Map;

/**
 * 检验方案判定配置Service接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public interface InspectionSchemeJudgmentService extends IService<InspectionSchemeJudgment> {

    /**
     * 保存检验方案判定配置
     *
     * @param schemeId
     * @param versionId
     * @param parameterConfigId
     * @param saveDTOList 保存参数列表
     */
    void saveInspectionSchemeJudgments(Long schemeId, Long versionId,Long packageId, Long parameterConfigId, List<InspectionSchemeJudgmentSaveDTO> saveDTOList);

    /**
     * 获取检验方案判定配置列表
     *
     * @param parameterConfigId 数据点ID
     * @return 判定配置列表
     */
    List<InspectionSchemeJudgmentDTO> listInspectionSchemeJudgments(Long parameterConfigId);

    /**
     * 删除检验方案判定配置
     *
     * @param parameterConfigId 数据点ID
     */
    void deleteInspectionSchemeJudgments(Long parameterConfigId);

    /**
     * 测试判定表达式
     *
     * @param expression 判定表达式
     * @param variables 变量值映射
     * @return 判定结果
     */
    Boolean testJudgmentExpression(String expression, Map<String, Boolean> variables);

    /**
     * 计算判定结果
     *
     * @param expression 判定表达式
     * @param variables 变量值映射
     * @return 判定结果
     */
    Boolean evaluateJudgmentExpression(String expression, Map<String, Boolean> variables);

    /**
     * 批量更新检验方案判定条件配置（支持新增、更新、删除）
     *
     * @param parameterConfigId
     * @param finalExpression
     * @param batchUpdateDTO 批量更新数据
     */
    void batchUpdateInspectionSchemeJudgments(Long parameterConfigId, String finalExpression, List<InspectionSchemeJudgmentBatchUpdateDTO> batchUpdateDTO);
} 
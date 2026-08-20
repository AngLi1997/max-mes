package com.bmos.lims2.server.stability.scheme.service;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilityCopyItemsResultDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeDataPointBatchUpdateDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeItemSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeJudgmentBatchUpdateDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionSaveItemsDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO;

import java.util.List;

/**
 * 稳定性方案检验项目配置Service接口
 */
public interface StabilitySchemeItemService {

    /**
     * 保存检验项目配置（全量覆盖：更新/新增+删除不在列表中的项目）
     */
    void saveItems(StabilitySchemeItemSaveDTO saveDTO);

    /**
     * 增量更新检验项目配置（有ID则更新，无ID则新增，不在列表中的项目保持不变）
     * 与检验方案 updateInspectionSchemeItems 行为一致
     */
    void updateItems(StabilitySchemeItemSaveDTO saveDTO);

    /**
     * 增量合并方案版本的检验项目-分析项配置（前端平铺提交专用）
     * <p>
     * 前端传入平铺的分析项列表（每条含 inspectItemId），后台按 inspectItemId 分组：
     * <ul>
     *   <li>DB 中有、请求中没有的检验项目 → 级联删除</li>
     *   <li>DB 中没有、请求中有的检验项目 → 新增</li>
     *   <li>分析项以 parameterId 为唯一键做增删更新</li>
     * </ul>
     *
     * @param versionId  版本ID
     * @param parameters 前端传入的平铺分析项列表
     */
    void mergeVersionItems(Long versionId,
                           List<StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO> parameters);

    /**
     * 查询版本下的检验项目配置列表（含分析项、数据点、判定）
     */
    List<StabilitySchemeItemDTO> listItems(Long versionId);

    /**
     * 删除检验项目配置（级联删除分析项、数据点、判定）
     */
    void deleteItem(Long itemId);

    /**
     * 删除分析项配置（级联删除数据点和判定）
     */
    void deleteParameter(Long parameterId);

    /**
     * 删除数据点配置（级联删除引用该数据点的判定）
     */
    void deleteDataPoint(Long dataPointId);

    /**
     * 删除判定配置
     */
    void deleteJudgment(Long judgmentId);

    /**
     * 批量更新数据点配置（增量：无ID则新增，有ID则更新，不在列表中则删除）
     */
    void updateDataPoints(List<StabilitySchemeDataPointBatchUpdateDTO> dataPoints);

    /**
     * 批量更新判定配置（增量：无ID则新增，有ID则更新，不在列表中则删除）
     */
    void updateJudgments(Long parameterConfigId, String finalExpression, List<StabilitySchemeJudgmentBatchUpdateDTO> judgments);

    /**
     * 复制版本下的检验项目配置到新版本（含数据点、判定）
     * 返回 itemIdMap（旧itemConfigId→新itemConfigId）和 paramIdMap（旧parameterConfigId→新parameterConfigId），
     * 供调用方传给 copyPlans 以修正计划中的引用
     */
    StabilityCopyItemsResultDTO copyItems(Long sourceVersionId, Long targetVersionId, Long schemeId);

    /**
     * 查询稳定性方案分析项配置详情（含数据点和判定）
     *
     * @param parameterConfigId 分析项配置ID
     * @return 分析项配置详细信息
     */
    StabilitySchemeParameterDTO getParameterDetail(Long parameterConfigId);

    /**
     * 更新稳定性方案分析项的执行方式；当设置为ELN时，要求同时更新 recordId、recordVersionId、recordCode
     *
     * @param parameterConfigId 分析项配置ID
     * @param executeMethod 执行方式
     * @param recordId 记录ID（ELN必填）
     * @param recordVersionId 记录版本ID（ELN必填）
     * @param recordCode 记录编码（ELN必填）
     * @param recordItemId 记录项ID（ELN建议携带）
     */
    void updateExecuteMethod(Long parameterConfigId, ExecuteMethodEnum executeMethod, Long recordId, Long recordVersionId, String recordCode, Long recordItemId);
}

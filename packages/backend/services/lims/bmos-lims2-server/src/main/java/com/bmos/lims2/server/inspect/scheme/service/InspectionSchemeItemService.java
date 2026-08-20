package com.bmos.lims2.server.inspect.scheme.service;

import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeItemDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionSaveItemsDTO;

import java.util.List;

/**
 * 检验方案检验项目配置Service接口
 * 正确的业务层级：检验方案明细 → 检验项目配置 → 分析项配置
 *
 * @author yigaohui
 * @since 2025/01/21 10:30
 */
public interface InspectionSchemeItemService {

    /**
     * 保存检验项目配置
     *  @param id
     * @param versionId 方案版本id
     * @param packageId
     * @param saveDTOList 保存参数列表
     */
    void saveInspectionSchemeItems(Long id, Long versionId, Long packageId, List<InspectionSchemeItemSaveDTO> saveDTOList);

    /**
     * 增量更新检验项目配置
     *
     * @param updateDTO 检验项目配置更新参数
     */
    void updateInspectionSchemeItems(List<InspectionSchemeItemUpdateDTO> updateDTO);

    /**
     * 获取检验项目配置列表
     *
     * @param versionId 方案明细ID
     * @return 检验项目配置列表
     */
    List<InspectionSchemeItemDTO> listInspectionSchemeItems(Long versionId);

    /**
     * 删除方案分析项配置；若该检验项目下已无分析项，则同时删除检验项目配置
     *
     * @param parameterConfigId 分析项配置ID
     */
    void deleteSchemeParameter(Long parameterConfigId);

    /**
     * 删除方案检验项目配置，同时级联删除其下所有分析项及关联的数据点、判定配置
     *
     * @param itemConfigId 检验项目配置ID
     */
    void deleteSchemeItem(Long itemConfigId);

    /**
     * 增量合并方案版本的检验项目-分析项配置（save-items 接口专用）
     * <p>
     * 规则：
     * <ul>
     *   <li>DB 中有、请求中没有的检验项目 → 级联删除</li>
     *   <li>DB 中没有、请求中有的检验项目 → 新增</li>
     *   <li>分析项同理：以 parameterId 为唯一键做增删；已有记录只更新 standardRule/isReportable/isExecutable</li>
     * </ul>
     *
     * @param schemeId   方案ID
     * @param versionId  版本ID
     * @param parameters 前端传入的分析项列表（含 inspectItemId + parameterId + 可选属性）
     */
    void mergeVersionItems(Long schemeId, Long versionId,
                           List<InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO> parameters);
}
package com.bmos.lims2.server.inspect.scheme.service;

import java.util.List;

/**
 * 检验方案分析项配置Service接口
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public interface InspectionSchemeItemTeamService {

    /**
     * 保存检验方案分析项配置
     *
     * @param schemeId
     * @param versionId
     * @param itemConfigId 检验项目配置ID
     * @param saveDTOList  保存参数列表
     */
    void saveInspectionSchemeParameters(Long schemeId, Long versionId, Long packageId, Long itemConfigId, Long inspectItemId, List<Long> saveDTOList);

    /**
     * 根据检验项目配置ID查询检验项目的班组
     * @param id 检验项目配置ID
     * @return
     */
    List<Long> listByItemConfigId(Long id);
}
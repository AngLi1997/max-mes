package com.bmos.lims2.server.inspect.scheme.service;

import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeSamplingDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingUpdateDTO;

import java.util.List;

/**
 * 检验方案取样量配置Service接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public interface InspectionSchemeSamplingService {

    /**
     * 保存检验方案取样量配置
     *
     * @param versionId 方案明细ID
     * @param saveDTOList 保存参数列表
     */
    void saveInspectionSchemeSamplings(Long schemeId  ,Long versionId, List<InspectionSchemeSamplingSaveDTO> saveDTOList);

    /**
     * 获取检验方案取样量配置列表
     *
     * @param versionId 方案版本id
     * @return 取样量配置列表
     */
    List<InspectionSchemeSamplingDTO> listInspectionSchemeSamplings(Long schemeId,Long versionId);

    /**
     * 增量更新检验方案取样量配置
     *
     * @param updateDTO 取样配置更新参数
     */
    void updateInspectionSchemeSamplings(List<InspectionSchemeSamplingUpdateDTO> updateDTO);

    /**
     * 删除检验方案取样量配置
     *
     * @param versionId 方案版本id
     */
    void deleteInspectionSchemeSamplings(Long schemeId, Long versionId);
} 
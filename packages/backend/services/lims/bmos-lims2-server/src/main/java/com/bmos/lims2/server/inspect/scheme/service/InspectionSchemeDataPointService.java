package com.bmos.lims2.server.inspect.scheme.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointBatchUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointBindingUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointSaveDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeDataPoint;

import java.util.List;

/**
 * 检验方案数据点配置Service接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public interface InspectionSchemeDataPointService extends IService<InspectionSchemeDataPoint> {

    /**
     * 保存检验方案数据点配置
     *
     * @param schemeId
     * @param versionId
     * @param parameterConfigId 分析项配置ID
     * @param saveDTOList 保存参数列表
     */
    void saveInspectionSchemeDataPoints(Long schemeId, Long versionId, Long packageId,Long parameterConfigId, List<InspectionSchemeDataPointSaveDTO> saveDTOList);

    /**
     * 获取检验方案数据点配置列表
     *
     * @param parameterConfigId 分析项配置ID
     * @return 数据点配置列表
     */
    List<InspectionSchemeDataPointDTO> listInspectionSchemeDataPoints(Long parameterConfigId);

    /**
     * 删除检验方案数据点配置
     *
     * @param parameterConfigId 分析项配置ID
     */
    void deleteInspectionSchemeDataPoints(Long parameterConfigId);

    /**
     * 批量更新检验方案数据点配置（支持新增、更新、删除）
     *
     * @param dataPointBatchUpdateDTOS 批量更新数据
     */
    void batchUpdateInspectionSchemeDataPoints(List<InspectionSchemeDataPointBatchUpdateDTO> dataPointBatchUpdateDTOS);

    /**
     * 批量保存/更新 数据点与记录字段（ELN）的绑定关系
     * 仅更新以下字段：recordId、recordVersionId、componentId、recordItemId、fieldId
     */
    void saveDataPointBindings(List<InspectionSchemeDataPointBindingUpdateDTO> bindings);
}
package com.bmos.lims2.server.inspect.scheme.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeParameterDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterSaveDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;

import java.util.List;

/**
 * 检验方案分析项配置Service接口
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public interface InspectionSchemeParameterService extends IService<InspectionSchemeParameter> {

    /**
     * 保存检验方案分析项配置
     *
     * @param schemeId
     * @param versionId
     * @param itemConfigId 检验项目配置ID
     * @param saveDTOList 保存参数列表
     */
    void saveInspectionSchemeParameters(Long schemeId, Long versionId,Long packageId,Long itemConfigId, List<InspectionSchemeParameterSaveDTO> saveDTOList);

    /**
     * 获取检验方案分析项配置列表
     *
     * @param itemConfigId 检验项目配置ID
     * @return 分析项配置列表
     */
    List<InspectionSchemeParameterDTO> listInspectionSchemeParameters(Long itemConfigId);

    /**
     * 删除检验方案分析项配置
     *
     * @param itemConfigId 检验项目配置ID
     */
    void deleteInspectionSchemeParameters(Long itemConfigId);

    /**
     * 通过分析项配置ID查询分析项的详细配置信息，包括数据点和判断条件
     *
     * @param parameterConfigId 分析项配置ID
     * @return 分析项配置详细信息
     */
    InspectionSchemeParameterDTO getInspectionSchemeParameterDetail(Long parameterConfigId);

    /**
     * 更新方案分析项的执行方式；当设置为ELN时，要求同时更新 recordId、recordVersionId、recordCode
     *
     * @param parameterConfigId 分析项配置ID
     * @param executeMethod 执行方式
     * @param recordId 记录ID（ELN必填）
     * @param recordVersionId 记录版本ID（ELN必填）
     * @param recordCode 记录编码（ELN必填）
     */
    void updateExecuteMethod(Long parameterConfigId, ExecuteMethodEnum executeMethod, Long recordId, Long recordVersionId, String recordCode, Long recordItemId);
}
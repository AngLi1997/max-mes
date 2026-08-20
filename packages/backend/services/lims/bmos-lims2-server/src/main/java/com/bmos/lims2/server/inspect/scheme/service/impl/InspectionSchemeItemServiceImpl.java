package com.bmos.lims2.server.inspect.scheme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterDataPoint;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterOption;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterTrend;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterDataPointMapper;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterOptionMapper;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterTrendMapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeItemDTO;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeParameterDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionSaveItemsDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeItem;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeItemMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeDataPointService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeItemService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeItemTeamService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeJudgmentService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeParameterService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 检验方案检验项目配置Service实现类
 * @Author: yigaohui
 * @Date: 2025/01/21 10:30
 */
@Service
public class InspectionSchemeItemServiceImpl implements InspectionSchemeItemService {

    @Autowired
    private InspectionSchemeItemMapper inspectionSchemeItemMapper;

    @Autowired
    private InspectionSchemeParameterService inspectionSchemeParameterService;

    @Autowired
    private InspectionSchemeDataPointService inspectionSchemeDataPointService;
    
    @Autowired
    private InspectionSchemeJudgmentService inspectionSchemeJudgmentService;

    @Autowired
    private InspectionSchemeItemTeamService teamService;

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    @Autowired
    private InspectParameterDataPointMapper inspectParameterDataPointMapper;

    @Autowired
    private InspectParameterOptionMapper inspectParameterOptionMapper;

    @Autowired
    private InspectParameterTrendMapper inspectParameterTrendMapper;

    /**
     * 确保版本可编辑：EDITING 直接通过；COMPLETED 自动回退为 EDITING；其他状态抛异常
     */
    private void revertToEditingIfCompleted(Long versionId) {
        if (versionId == null) return;
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(versionId);
        if (version == null) return;
        if (version.getStatus() == InspectionSchemeVersionStatusEnum.COMPLETED) {
            version.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
            inspectionSchemeVersionMapper.updateById(version);
        } else if (version.getStatus() != InspectionSchemeVersionStatusEnum.EDITING) {
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_STATE_ERROR);
        }
    }

    /**
     * 将分析项主数据中已绑定的数据点自动同步到方案数据点表（仅对新增分析项调用）
     */
    private void autoSaveDataPointsFromMaster(Long schemeId, Long versionId, Long inspectItemId,
                                              Long parameterId, Long parameterConfigId) {
        List<InspectParameterDataPoint> masterDps = inspectParameterDataPointMapper.selectByParameterId(parameterId);
        if (masterDps == null || masterDps.isEmpty()) {
            return;
        }
        List<InspectionSchemeDataPointSaveDTO> saveDTOs = new ArrayList<>();
        for (InspectParameterDataPoint master : masterDps) {
            InspectionSchemeDataPointSaveDTO dto = new InspectionSchemeDataPointSaveDTO();
            dto.setDataPointId(master.getId());
            dto.setInspectItemId(inspectItemId);
            dto.setInspectParameterId(parameterId);
            dto.setName(master.getName());
            dto.setPointType(DataPointTypeEnum.valueOf(master.getResultType().name()));
            dto.setTimeFormat(master.getTimeFormat());
            dto.setDateStyle(master.getDateStyle());
            dto.setReportDisplay(master.getReportDisplay() != null ? master.getReportDisplay() : false);
            if ("OPTION".equals(master.getResultType().name())) {
                List<InspectParameterOption> options = inspectParameterOptionMapper.selectByDataPointId(master.getId());
                if (options != null && !options.isEmpty()) {
                    dto.setOptions(JSON.toJSONString(options.stream()
                            .map(InspectParameterOption::getOptionValue)
                            .collect(Collectors.toList())));
                }
            }
            if ("NUMBER".equals(master.getResultType().name())) {
                List<InspectParameterTrend> trends = inspectParameterTrendMapper.selectByDataPointId(master.getId());
                if (trends != null && !trends.isEmpty()) {
                    dto.setTrendLineConfig(JSON.toJSONString(trends));
                }
            }
            saveDTOs.add(dto);
        }
        inspectionSchemeDataPointService.saveInspectionSchemeDataPoints(
                schemeId, versionId, null, parameterConfigId, saveDTOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionSchemeItems(Long schemeId, Long versionId, Long packageId, List<InspectionSchemeItemSaveDTO> saveDTOList) {
        // 删除原有的检验项目配置
        LambdaQueryWrapper<InspectionSchemeItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeItem::getVersionId, versionId);
        inspectionSchemeItemMapper.delete(wrapper);

        if (saveDTOList == null || saveDTOList.isEmpty()) {
            return;
        }

        // 保存检验项目配置
        for (InspectionSchemeItemSaveDTO saveDTO : saveDTOList) {
            InspectionSchemeItem schemeItem = new InspectionSchemeItem();
            // 手动复制属性
            schemeItem.setVersionId(versionId);
            schemeItem.setSchemeId(schemeId);
            schemeItem.setPackageId(packageId);
            schemeItem.setInspectItemId(saveDTO.getInspectItemId());
            schemeItem.setIsRequired(saveDTO.getIsRequired());
            schemeItem.setSort(saveDTO.getSort());
            schemeItem.setDuration(saveDTO.getDuration());
            schemeItem.setTimeUnit(saveDTO.getTimeUnit());
            schemeItem.setRemark(saveDTO.getRemark());
            
            inspectionSchemeItemMapper.insert(schemeItem);
            
            // 如果班组不为空，保存班组
            if (saveDTO.getTeams() != null && !saveDTO.getTeams().isEmpty()) {
                teamService.saveInspectionSchemeParameters(schemeId, versionId, packageId, schemeItem.getId(), saveDTO.getInspectItemId(), saveDTO.getTeams());
            }

            // 保存分析项配置
            if (saveDTO.getParameters() != null && !saveDTO.getParameters().isEmpty()) {
                inspectionSchemeParameterService.saveInspectionSchemeParameters(schemeId, versionId, packageId, schemeItem.getId(), saveDTO.getParameters());
            }
        }
    }

    @Override
    public List<InspectionSchemeItemDTO> listInspectionSchemeItems(Long versionId) {
        // 查询检验项目配置及其分析项配置
        List<InspectionSchemeItemDTO> schemeItems = inspectionSchemeItemMapper.listWithAnalyzeItems(versionId);

        // 为每个分析项查询数据点配置
        schemeItems.forEach(item -> {
            if (item.getParameters() != null) {
                item.getParameters().forEach(parameter -> {
                    parameter.setDataPoints(inspectionSchemeDataPointService.listInspectionSchemeDataPoints(parameter.getId()));
                    parameter.setJudgments(inspectionSchemeJudgmentService.listInspectionSchemeJudgments(parameter.getId()));
                });
            }
            // 查询项目的班组
            item.setTeams(teamService.listByItemConfigId(item.getId()));
        });
        
        return schemeItems;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInspectionSchemeItems(List<InspectionSchemeItemUpdateDTO> updateDTO) {
        if (updateDTO == null || updateDTO.isEmpty()) {
            return;
        }
        // 取第一条的 versionId 做状态检查（同一批次必属同一版本）
        Long versionId = updateDTO.get(0).getVersionId();
        revertToEditingIfCompleted(versionId);

        for (InspectionSchemeItemUpdateDTO itemConfig : updateDTO) {
            if (itemConfig.getItemConfigId() != null) {
                // 更新现有检验项目配置
                updateExistingItem(itemConfig);
            } else {
                // 新增检验项目配置
                addNewItem(itemConfig);
            }
        }
    }

    /**
     * 更新现有检验项目配置
     */
    private void updateExistingItem(InspectionSchemeItemUpdateDTO itemConfig) {
        // 更新检验项目基本信息
        InspectionSchemeItem existingItem = inspectionSchemeItemMapper.selectById(itemConfig.getItemConfigId());
        if (existingItem == null) {
            throw new RuntimeException("检验项目配置不存在");
        }

        existingItem.setIsRequired(itemConfig.getIsRequired());
        existingItem.setSort(itemConfig.getSort());
        existingItem.setDuration(itemConfig.getDuration());
        existingItem.setTimeUnit(itemConfig.getTimeUnit());
        existingItem.setRemark(itemConfig.getRemark());
        inspectionSchemeItemMapper.updateById(existingItem);

        // 更新班组配置（简化实现：先删除再新增）
        if (itemConfig.getTeams() != null) {
            teamService.saveInspectionSchemeParameters(itemConfig.getSchemeId(), itemConfig.getVersionId(), itemConfig.getPackageId(), existingItem.getId(), itemConfig.getInspectItemId(), itemConfig.getTeams());
        }

        // 更新分析项配置（真正的增量更新）
        if (itemConfig.getInspectionParameters() != null) {
            updateParametersForItemIncremental(itemConfig.getSchemeId(), itemConfig.getVersionId(), itemConfig.getPackageId(), existingItem.getId(), itemConfig.getInspectionParameters());
        }
    }

    /**
     * 新增检验项目配置
     */
    private void addNewItem(InspectionSchemeItemUpdateDTO itemConfig) {
        // 创建新的检验项目配置
        InspectionSchemeItem newItem = new InspectionSchemeItem();
        BeanUtils.copyProperties(itemConfig, newItem);
        inspectionSchemeItemMapper.insert(newItem);

        // 保存班组配置
        if (itemConfig.getTeams() != null && !itemConfig.getTeams().isEmpty()) {
            teamService.saveInspectionSchemeParameters(itemConfig.getSchemeId(), itemConfig.getVersionId(), itemConfig.getPackageId(), newItem.getId(), itemConfig.getInspectItemId(), itemConfig.getTeams());
        }

        // 保存分析项配置
        if (itemConfig.getInspectionParameters() != null && !itemConfig.getInspectionParameters().isEmpty()) {
            List<InspectionSchemeParameterSaveDTO> parameterSaveDTOs = convertToParameterSaveDTOs(itemConfig.getInspectionParameters());
            inspectionSchemeParameterService.saveInspectionSchemeParameters(itemConfig.getSchemeId(), itemConfig.getVersionId(), itemConfig.getPackageId(), newItem.getId(), parameterSaveDTOs);
        }
    }

    /**
     * 更新检验项目的分析项配置（真正的增量更新）
     */
    private void updateParametersForItemIncremental(Long schemeId, Long versionId, Long packageId, Long itemConfigId, 
                                                  List<InspectionSchemeItemUpdateDTO.InspectParameterConfigDTO> parameters) {
        // 1. 获取现有的分析项配置
        List<InspectionSchemeParameterDTO> existingParameters = inspectionSchemeParameterService.listInspectionSchemeParameters(itemConfigId);
        
        // 2. 创建现有配置的ID映射
        Map<Long, InspectionSchemeParameterDTO> existingParameterMap = existingParameters.stream()
                .collect(Collectors.toMap(InspectionSchemeParameterDTO::getId, param -> param));
        
        // 3. 处理前端传入的分析项配置
        Set<Long> processedParameterIds = new HashSet<>();
        
        for (InspectionSchemeItemUpdateDTO.InspectParameterConfigDTO paramConfig : parameters) {
            if (paramConfig.getParameterConfigId() != null) {
                // 更新现有分析项配置
                if (existingParameterMap.containsKey(paramConfig.getParameterConfigId())) {
                    updateExistingParameter(paramConfig);
                    processedParameterIds.add(paramConfig.getParameterConfigId());
                }
            } else {
                // 新增分析项配置
                addNewParameter(schemeId, versionId, packageId, itemConfigId, paramConfig);
            }
        }
        
        // 4. 删除前端没有传入的分析项配置（表示被删除）
        for (Long existingParameterId : existingParameterMap.keySet()) {
            if (!processedParameterIds.contains(existingParameterId)) {
                // 删除分析项配置及其关联的数据点和判定配置
                deleteParameterAndRelatedData(existingParameterId);
            }
        }
    }

    /**
     * 更新现有分析项配置
     */
    private void updateExistingParameter(InspectionSchemeItemUpdateDTO.InspectParameterConfigDTO paramConfig) {
        // 更新分析项基本信息
        // 注意：这里需要直接操作mapper或者添加update方法到service
        // 暂时通过重新保存的方式实现，但这违背了增量更新的原则
        // 在实际项目中，应该添加updateParameter方法到service
        
        // 实现真正的参数更新逻辑
        updateParameterBasicInfo(paramConfig);
    }

    /**
     * 新增分析项配置
     */
    private void addNewParameter(Long schemeId, Long versionId, Long packageId, Long itemConfigId,
                               InspectionSchemeItemUpdateDTO.InspectParameterConfigDTO paramConfig) {
        // 创建新的分析项配置
        List<InspectionSchemeParameterSaveDTO> parameterSaveDTOs = new ArrayList<>();
        InspectionSchemeParameterSaveDTO parameterSaveDTO = new InspectionSchemeParameterSaveDTO();
        parameterSaveDTO.setParameterId(paramConfig.getParameterId());
        parameterSaveDTO.setStandardRule(paramConfig.getStandardRule());
        parameterSaveDTO.setRecordId(paramConfig.getRecordId());
        parameterSaveDTO.setRecordCode(paramConfig.getRecordCode());
        parameterSaveDTO.setRecordVersionId(paramConfig.getRecordVersionId());
        parameterSaveDTO.setIsReportable(paramConfig.getIsReportable());
        parameterSaveDTO.setIsExecutable(paramConfig.getIsExecutable());

        parameterSaveDTOs.add(parameterSaveDTO);
        inspectionSchemeParameterService.saveInspectionSchemeParameters(schemeId, versionId, packageId, itemConfigId, parameterSaveDTOs);
    }

    /**
     * 删除分析项配置及其关联数据
     */
    private void deleteParameterAndRelatedData(Long parameterConfigId) {
        // 删除数据点配置
        inspectionSchemeDataPointService.deleteInspectionSchemeDataPoints(parameterConfigId);

        // 删除判定配置
        inspectionSchemeJudgmentService.deleteInspectionSchemeJudgments(parameterConfigId);

        // 删除分析项配置
        // 注意：InspectionSchemeParameterService可能没有继承IService，需要使用其他删除方法
         inspectionSchemeParameterService.removeById(parameterConfigId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSchemeParameter(Long parameterConfigId) {
        InspectionSchemeParameter parameter = inspectionSchemeParameterService.getById(parameterConfigId);
        if (parameter == null) {
            throw new RuntimeException("分析项配置不存在");
        }
        revertToEditingIfCompleted(parameter.getVersionId());
        Long itemConfigId = parameter.getItemConfigId();

        // 删除分析项及其关联数据
        deleteParameterAndRelatedData(parameterConfigId);

        // 若该检验项目下已无分析项，则同时删除检验项目配置
        List<InspectionSchemeParameterDTO> remaining = inspectionSchemeParameterService.listInspectionSchemeParameters(itemConfigId);
        if (remaining == null || remaining.isEmpty()) {
            inspectionSchemeItemMapper.deleteById(itemConfigId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSchemeItem(Long itemConfigId) {
        InspectionSchemeItem item = inspectionSchemeItemMapper.selectById(itemConfigId);
        if (item == null) {
            throw new RuntimeException("检验项目配置不存在");
        }
        revertToEditingIfCompleted(item.getVersionId());
        // 级联删除该检验项目下所有分析项及其数据点、判定配置
        List<InspectionSchemeParameterDTO> parameters = inspectionSchemeParameterService.listInspectionSchemeParameters(itemConfigId);
        if (parameters != null) {
            for (InspectionSchemeParameterDTO p : parameters) {
                deleteParameterAndRelatedData(p.getId());
            }
        }
        inspectionSchemeItemMapper.deleteById(itemConfigId);
    }






    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeVersionItems(Long schemeId, Long versionId,
                                  List<InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO> parameters) {
        if (parameters == null) {
            parameters = Collections.emptyList();
        }

        // 1. 按 inspectItemId 分组，保持顺序
        Map<Long, List<InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO>> grouped =
                parameters.stream().collect(Collectors.groupingBy(
                        InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO::getInspectItemId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        // 2. 加载 DB 中当前版本已有的检验项目
        LambdaQueryWrapper<InspectionSchemeItem> itemQuery = new LambdaQueryWrapper<>();
        itemQuery.eq(InspectionSchemeItem::getVersionId, versionId);
        List<InspectionSchemeItem> existingItems = inspectionSchemeItemMapper.selectList(itemQuery);
        Map<Long, InspectionSchemeItem> existingByInspectItemId = existingItems.stream()
                .collect(Collectors.toMap(InspectionSchemeItem::getInspectItemId, i -> i));

        // 3. 删除本次请求中不再包含的检验项目（级联删除分析项、数据点、判定）
        for (InspectionSchemeItem existing : existingItems) {
            if (!grouped.containsKey(existing.getInspectItemId())) {
                deleteSchemeItem(existing.getId());
            }
        }

        // 4. 逐组处理检验项目及其分析项
        int sort = 1;
        for (Map.Entry<Long, List<InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO>> entry : grouped.entrySet()) {
            Long inspectItemId = entry.getKey();
            List<InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO> paramList = entry.getValue();

            InspectionSchemeItem schemeItem = existingByInspectItemId.get(inspectItemId);
            if (schemeItem == null) {
                // 新增检验项目记录
                schemeItem = new InspectionSchemeItem();
                schemeItem.setVersionId(versionId);
                schemeItem.setSchemeId(schemeId);
                schemeItem.setInspectItemId(inspectItemId);
                schemeItem.setIsRequired(true);
                schemeItem.setSort(sort);
                inspectionSchemeItemMapper.insert(schemeItem);
            } else if (!Objects.equals(schemeItem.getSort(), sort)) {
                schemeItem.setSort(sort);
                inspectionSchemeItemMapper.updateById(schemeItem);
            }
            sort++;

            Long itemConfigId = schemeItem.getId();

            // 5. 加载该检验项目下已有的分析项，以 parameterId 为 key
            List<InspectionSchemeParameterDTO> existingParams =
                    inspectionSchemeParameterService.listInspectionSchemeParameters(itemConfigId);
            Map<Long, InspectionSchemeParameterDTO> existingByParameterId = existingParams.stream()
                    .collect(Collectors.toMap(InspectionSchemeParameterDTO::getParameterId, p -> p));

            Set<Long> incomingParameterIds = paramList.stream()
                    .map(InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO::getParameterId)
                    .collect(Collectors.toSet());

            // 删除不在本次请求中的分析项（级联删除数据点、判定）
            for (InspectionSchemeParameterDTO existingParam : existingParams) {
                if (!incomingParameterIds.contains(existingParam.getParameterId())) {
                    deleteParameterAndRelatedData(existingParam.getId());
                }
            }

            // 新增或更新分析项
            for (InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO paramDTO : paramList) {
                InspectionSchemeParameterDTO existing = existingByParameterId.get(paramDTO.getParameterId());
                if (existing == null) {
                    // 新增分析项 — 直接 save，不调用 saveInspectionSchemeParameters
                    // （saveInspectionSchemeParameters 会先 delete itemConfigId 下所有行，循环调用时会把之前插入的删掉）
                    InspectionSchemeParameter param = new InspectionSchemeParameter();
                    param.setSchemeId(schemeId);
                    param.setVersionId(versionId);
                    param.setItemConfigId(itemConfigId);
                    param.setInspectItemId(inspectItemId);
                    param.setParameterId(paramDTO.getParameterId());
                    param.setStandardRule(paramDTO.getStandardRule());
                    param.setIsReportable(paramDTO.getIsReportable() != null ? paramDTO.getIsReportable() : true);
                    param.setIsExecutable(paramDTO.getIsExecutable() != null ? paramDTO.getIsExecutable() : true);
                    param.setExecuteMethod(ExecuteMethodEnum.LIMS);
                    inspectionSchemeParameterService.save(param);
                    // 自动同步分析项主数据中已绑定的数据点
                    autoSaveDataPointsFromMaster(schemeId, versionId, inspectItemId,
                            paramDTO.getParameterId(), param.getId());
                } else {
                    // 更新可变属性
                    InspectionSchemeParameter param = inspectionSchemeParameterService.getById(existing.getId());
                    if (param != null) {
                        param.setStandardRule(paramDTO.getStandardRule());
                        if (paramDTO.getIsReportable() != null) {
                            param.setIsReportable(paramDTO.getIsReportable());
                        }
                        if (paramDTO.getIsExecutable() != null) {
                            param.setIsExecutable(paramDTO.getIsExecutable());
                        }
                        inspectionSchemeParameterService.updateById(param);
                    }
                }
            }
        }
    }

    /**
     * 更新分析项基本信息
     */
    private void updateParameterBasicInfo(InspectionSchemeItemUpdateDTO.InspectParameterConfigDTO paramConfig) {
        // 获取现有分析项实体
        InspectionSchemeParameter existingParameter = inspectionSchemeParameterService.getById(paramConfig.getParameterConfigId());
        if (existingParameter != null) {
            // 更新分析项基本属性
            existingParameter.setStandardRule(paramConfig.getStandardRule());
            existingParameter.setRecordId(paramConfig.getRecordId());
            existingParameter.setIsReportable(paramConfig.getIsReportable());
            existingParameter.setIsExecutable(paramConfig.getIsExecutable());
            existingParameter.setRecordCode(paramConfig.getRecordCode());
            existingParameter.setRecordVersionId(paramConfig.getRecordVersionId());

            // 更新到数据库
            inspectionSchemeParameterService.updateById(existingParameter);
        }
    }

    /**
     * 转换为分析项保存DTO
     */
    private List<InspectionSchemeParameterSaveDTO> convertToParameterSaveDTOs(List<InspectionSchemeItemUpdateDTO.InspectParameterConfigDTO> parameters) {
        List<InspectionSchemeParameterSaveDTO> parameterSaveDTOs = new ArrayList<>();

        for (InspectionSchemeItemUpdateDTO.InspectParameterConfigDTO paramConfig : parameters) {
            InspectionSchemeParameterSaveDTO parameterSaveDTO = new InspectionSchemeParameterSaveDTO();
            parameterSaveDTO.setParameterId(paramConfig.getParameterId());
            parameterSaveDTO.setStandardRule(paramConfig.getStandardRule());
            parameterSaveDTO.setRecordId(paramConfig.getRecordId());
            parameterSaveDTO.setIsReportable(paramConfig.getIsReportable());
            parameterSaveDTO.setIsExecutable(paramConfig.getIsExecutable());

            parameterSaveDTOs.add(parameterSaveDTO);
        }

        return parameterSaveDTOs;
    }
}
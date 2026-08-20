package com.bmos.lims2.server.inspect.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordComponentMapper;
import com.bmos.lims2.server.eln.record.mapper.SchemeParameterComponentConfigMapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeParameterDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterSaveDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeDataPoint;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeDataPointMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeDataPointService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeJudgmentService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.*;

/**
 * 检验方案分析项配置Service实现类
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Service
public class InspectionSchemeParameterServiceImpl extends ServiceImpl<InspectionSchemeParameterMapper, InspectionSchemeParameter> implements InspectionSchemeParameterService {

    @Autowired
    private InspectionSchemeParameterMapper inspectionSchemeParameterMapper;

    @Autowired
    private InspectionSchemeDataPointService inspectionSchemeDataPointService;

    @Autowired
    private InspectionSchemeJudgmentService inspectionSchemeJudgmentService;

    @Autowired
    private SchemeParameterComponentConfigMapper schemeParameterComponentConfigMapper;

    @Autowired
    private InspectionSchemeDataPointMapper inspectionSchemeDataPointMapper;

    @Autowired
    private BatchRecordComponentMapper batchRecordComponentMapper;

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionSchemeParameters(Long schemeId, Long versionId,Long packageId, Long itemConfigId, List<InspectionSchemeParameterSaveDTO> saveDTOList) {
        // 删除原有配置
        LambdaQueryWrapper<InspectionSchemeParameter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeParameter::getItemConfigId, itemConfigId);
        inspectionSchemeParameterMapper.delete(wrapper);

        // 保存新配置
        saveDTOList.forEach(saveDTO -> {
            InspectionSchemeParameter parameter = BeanUtil.copyProperties(saveDTO, InspectionSchemeParameter.class);
            parameter.setItemConfigId(itemConfigId);
            parameter.setSchemeId(schemeId);
            parameter.setVersionId(versionId);
            parameter.setPackageId(packageId);
            parameter.setInspectItemId(saveDTO.getInspectItemId());
            parameter.setFinalExpression(saveDTO.getFinalExpression());
            // 默认执行方式：为空时设置为 LIMS
            if (saveDTO.getExecuteMethod() == null) {
                parameter.setExecuteMethod(com.bmos.lims2.common.enums.ExecuteMethodEnum.LIMS);
            } else {
                parameter.setExecuteMethod(saveDTO.getExecuteMethod());
            }
            inspectionSchemeParameterMapper.insert(parameter);

            // 保存数据点配置
            if (saveDTO.getDataPoints() != null && !saveDTO.getDataPoints().isEmpty()) {
                inspectionSchemeDataPointService.saveInspectionSchemeDataPoints(schemeId,versionId,packageId,parameter.getId(), saveDTO.getDataPoints());
            }
            // 判定条件保存
            if (saveDTO.getJudgments() != null && !saveDTO.getJudgments().isEmpty()){
                inspectionSchemeJudgmentService.saveInspectionSchemeJudgments(schemeId,versionId,packageId,parameter.getId(), saveDTO.getJudgments());
            }
        });
    }

    @Override
    public List<InspectionSchemeParameterDTO> listInspectionSchemeParameters(Long itemConfigId) {
        // 查询分析项配置列表（包含数据点信息）
        List<InspectionSchemeParameterDTO> parameters = inspectionSchemeParameterMapper.listByItemConfigId(itemConfigId);

        // 查询每个分析项的数据点和判定条件配置
        parameters.forEach(parameter -> {
            parameter.setDataPoints(inspectionSchemeDataPointService.listInspectionSchemeDataPoints(parameter.getId()));
            parameter.setJudgments(inspectionSchemeJudgmentService.listInspectionSchemeJudgments(parameter.getId()));
        });

        return parameters;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionSchemeParameters(Long itemConfigId) {
        // 查询分析项配置列表
        List<InspectionSchemeParameterDTO> parameters = inspectionSchemeParameterMapper.listByItemConfigId(itemConfigId);

        // 删除数据点配置
        parameters.forEach(parameter -> {
            inspectionSchemeDataPointService.deleteInspectionSchemeDataPoints(parameter.getId());
            inspectionSchemeJudgmentService.deleteInspectionSchemeJudgments(parameter.getId());
        });

        // 删除分析项配置
        LambdaQueryWrapper<InspectionSchemeParameter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeParameter::getItemConfigId, itemConfigId);
        inspectionSchemeParameterMapper.delete(wrapper);
    }

    @Override
    public InspectionSchemeParameterDTO getInspectionSchemeParameterDetail(Long parameterConfigId) {
        // 查询分析项配置基础信息
        InspectionSchemeParameterDTO parameter = inspectionSchemeParameterMapper.getByParameterConfigId(parameterConfigId);
        if (parameter == null) {
            return null;
        }

        // 查询分析项的数据点配置
        parameter.setDataPoints(inspectionSchemeDataPointService.listInspectionSchemeDataPoints(parameterConfigId));
        
        // 查询分析项的判定条件配置
        parameter.setJudgments(inspectionSchemeJudgmentService.listInspectionSchemeJudgments(parameterConfigId));
        computeJudgmentConsistency(parameter);

        return parameter;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExecuteMethod(Long parameterConfigId, ExecuteMethodEnum executeMethod, Long recordId, Long recordVersionId, String recordCode, Long recordItemId) {
        if (parameterConfigId == null || executeMethod == null) {
            throw new BmosException(LimsResponseCode.PARAMETER_NOT_FOUND);
        }
        InspectionSchemeParameter parameter = getById(parameterConfigId);
        if (parameter == null) {
            throw new BmosException(LimsResponseCode.PARAMETER_NOT_FOUND);
        }
        // 状态检查：COMPLETED 自动回退为 EDITING
        if (parameter.getVersionId() != null) {
            InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(parameter.getVersionId());
            if (version != null) {
                if (version.getStatus() == InspectionSchemeVersionStatusEnum.COMPLETED) {
                    version.setStatus(InspectionSchemeVersionStatusEnum.EDITING);
                    inspectionSchemeVersionMapper.updateById(version);
                } else if (version.getStatus() != InspectionSchemeVersionStatusEnum.EDITING) {
                    throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_VERSION_STATE_ERROR);
                }
            }
        }

        // 判断是否需要清空相关数据
        // 1. 执行方式切换（从ELN切换到LIMS，或从LIMS切换到ELN）
        boolean isExecuteMethodChanged = !Objects.equals(parameter.getExecuteMethod(), executeMethod);
        // 2. 执行方式为ELN但切换了ELN的记录（recordId、recordVersionId、recordItemId 发生变化）
        boolean isElnRecordChanged = executeMethod == ExecuteMethodEnum.ELN
                && parameter.getExecuteMethod() == ExecuteMethodEnum.ELN
                && (!Objects.equals(parameter.getRecordId(), recordId)
                    || !Objects.equals(parameter.getRecordVersionId(), recordVersionId)
                    || !Objects.equals(parameter.getRecordItemId(), recordItemId));

        // 如果需要清空数据（执行方式切换或ELN记录切换）
        if (isExecuteMethodChanged || isElnRecordChanged) {
            // 清空判定条件配置
            inspectionSchemeJudgmentService.deleteInspectionSchemeJudgments(parameterConfigId);
            
            // 清空分析项组件配置
            schemeParameterComponentConfigMapper.deleteByInspectionSchemeParameterConfigId(parameterConfigId);
            
            // 清空数据点中和记录关联的字段
            LambdaUpdateWrapper<InspectionSchemeDataPoint> dataPointUpdater = new LambdaUpdateWrapper<>();
            dataPointUpdater.eq(InspectionSchemeDataPoint::getParameterConfigId, parameterConfigId)
                    .set(InspectionSchemeDataPoint::getComponentId, null)
                    .set(InspectionSchemeDataPoint::getRecordVersionId, null)
                    .set(InspectionSchemeDataPoint::getRecordId, null)
                    .set(InspectionSchemeDataPoint::getRecordItemId, null)
                    .set(InspectionSchemeDataPoint::getFieldId, null);
            inspectionSchemeDataPointMapper.update(null, dataPointUpdater);
        }

        // 使用更新器，显式设置字段，确保可将列更新为 NULL
        LambdaUpdateWrapper<InspectionSchemeParameter> updater = new LambdaUpdateWrapper<>();
        updater.eq(InspectionSchemeParameter::getId, parameterConfigId)
                .set(InspectionSchemeParameter::getExecuteMethod, executeMethod);

		// 执行方式切换或 ELN 记录切换时，需要清空 final_expression
		if (isExecuteMethodChanged || isElnRecordChanged) {
			updater.set(InspectionSchemeParameter::getFinalExpression, null);
		}

        if (executeMethod == ExecuteMethodEnum.ELN) {
            // ELN 必填校验
            if (recordId == null || recordVersionId == null || recordCode == null) {
                throw new BmosException(LimsResponseCode.PARAMETER_ELN_MISSING_PARAMETER);
            }
            updater.set(InspectionSchemeParameter::getRecordId, recordId)
                   .set(InspectionSchemeParameter::getRecordVersionId, recordVersionId)
                   .set(InspectionSchemeParameter::getRecordCode, recordCode)
                   .set(InspectionSchemeParameter::getRecordItemId, recordItemId);
        } else {
            // 切换回 LIMS 时清空 ELN 绑定信息，避免脏数据
            updater.set(InspectionSchemeParameter::getRecordId, null)
                   .set(InspectionSchemeParameter::getRecordVersionId, null)
                   .set(InspectionSchemeParameter::getRecordCode, null)
                   .set(InspectionSchemeParameter::getRecordItemId, null);
        }

        this.update(updater);
    }

    private void computeJudgmentConsistency(InspectionSchemeParameterDTO parameter) {
        if (parameter == null) {
            return;
        }
        if (CollUtil.isEmpty(parameter.getJudgments())) {
            parameter.setJudgmentConfigError(false);
            parameter.setJudgmentDataPointDeleted(false);
            parameter.setJudgmentDataPointBindingMissing(false);
            parameter.setJudgmentDataPointTypeChanged(false);
            parameter.setJudgmentDataPointOptionInvalid(false);
            return;
        }
        Set<Long> aliveDpConfigIds = new HashSet<>();
        Map<Long, InspectionSchemeDataPointDTO> cfgMap = new HashMap<>();
        if (CollUtil.isNotEmpty(parameter.getDataPoints())) {
            for (InspectionSchemeDataPointDTO dp : parameter.getDataPoints()) {
                aliveDpConfigIds.add(dp.getId());
                cfgMap.put(dp.getId(), dp);
            }
        }
        boolean judgmentConfigError = false;
        boolean anyPointDeleted = false;
        boolean anyPointTypeChanged = false;
        boolean anyPointBindingMissing = false;
        boolean anyPointOptionInvalid = false;
        boolean elnExecute = ExecuteMethodEnum.ELN.equals(parameter.getExecuteMethod());
        Map<Long, Set<String>> componentOptionCache = new HashMap<>();
        for (InspectionSchemeJudgmentDTO j : parameter.getJudgments()) {
            j.setDataPointDeleted(false);
            j.setDataPointBindingMissing(false);
            j.setDataPointTypeChanged(false);
            j.setDataPointOptionInvalid(false);
            if (j.getDataPointConfigId() != null && !aliveDpConfigIds.contains(j.getDataPointConfigId())) {
                judgmentConfigError = true;
                anyPointDeleted = true;
                j.setDataPointDeleted(true);
                continue;
            }
            InspectionSchemeDataPointDTO dp = cfgMap.get(j.getDataPointConfigId());
            if (dp != null) {
                if (j.getPointType() != null && dp.getPointType() != null && !j.getPointType().equals(dp.getPointType())) {
                    judgmentConfigError = true;
                    anyPointTypeChanged = true;
                    j.setDataPointTypeChanged(true);
                }
                if (elnExecute && (dp.getComponentId() == null || dp.getFieldId() == null)) {
                    judgmentConfigError = true;
                    anyPointBindingMissing = true;
                    j.setDataPointBindingMissing(true);
                }
                if (isOptionValueInvalid(elnExecute, dp, j.getStandardValue(), componentOptionCache)) {
                    judgmentConfigError = true;
                    anyPointOptionInvalid = true;
                    j.setDataPointOptionInvalid(true);
                }
            }
        }
        parameter.setJudgmentConfigError(judgmentConfigError);
        parameter.setJudgmentDataPointDeleted(anyPointDeleted);
        parameter.setJudgmentDataPointTypeChanged(anyPointTypeChanged);
        parameter.setJudgmentDataPointBindingMissing(anyPointBindingMissing);
        parameter.setJudgmentDataPointOptionInvalid(anyPointOptionInvalid);
    }

    private boolean isOptionValueInvalid(boolean elnExecute,
                                         InspectionSchemeDataPointDTO dp,
                                         String standardValue,
                                         Map<Long, Set<String>> componentOptionCache) {
        if (!elnExecute || dp == null || dp.getPointType() != DataPointTypeEnum.OPTION) {
            return false;
        }
        if (StrUtil.isBlank(standardValue)) {
            return false;
        }
        if (dp.getComponentId() == null) {
            return true;
        }
        Set<String> optionFields = loadComponentOptionFields(dp.getComponentId(), componentOptionCache);
        if (CollUtil.isEmpty(optionFields)) {
            return true;
        }
        List<String> values = parseStandardValues(standardValue);
        for (String value : values) {
            if (StrUtil.isBlank(value) || !optionFields.contains(value)) {
                return true;
            }
        }
        return false;
    }

    private List<String> parseStandardValues(String standardValue) {
        if (StrUtil.isBlank(standardValue)) {
            return Collections.emptyList();
        }
        try {
            JSONArray array = JSON.parseArray(standardValue);
            if (array != null) {
                List<String> values = new ArrayList<>();
                array.forEach(obj -> {
                    if (obj == null) {
                        return;
                    }
                    if (obj instanceof JSONObject) {
                        String field = ((JSONObject) obj).getString("field");
                        if (StrUtil.isNotBlank(field)) {
                            values.add(StrUtil.trim(field));
                            return;
                        }
                    }
                    values.add(StrUtil.trim(obj.toString()));
                });
                if (CollUtil.isNotEmpty(values)) {
                    return values;
                }
            }
        } catch (Exception ignored) {
            // 非 JSON 数组，走逗号分隔
        }
        List<String> split = StrUtil.splitTrim(standardValue, ',');
        if (CollUtil.isEmpty(split)) {
            return Collections.singletonList(StrUtil.trim(standardValue));
        }
        return split;
    }

    private Set<String> loadComponentOptionFields(Long componentId, Map<Long, Set<String>> cache) {
        if (componentId == null) {
            return Collections.emptySet();
        }
        if (cache.containsKey(componentId)) {
            return cache.get(componentId);
        }
        Set<String> fields = new HashSet<>();
        BatchRecordComponent component = batchRecordComponentMapper.selectWithDetailById(componentId);
        if (component != null && StrUtil.isNotBlank(component.getComponentDetail())) {
            try {
                JSONArray array = JSON.parseArray(component.getComponentDetail());
                if (array != null) {
                    array.forEach(obj -> {
                        try {
                            String field = JSON.parseObject(JSON.toJSONString(obj)).getString("field");
                            if (StrUtil.isNotBlank(field)) {
                                fields.add(field);
                            }
                        } catch (Exception ignored) {
                            // 单条解析失败忽略
                        }
                    });
                }
            } catch (Exception ignored) {
                // 解析失败交由上层处理
            }
        }
        cache.put(componentId, fields);
        return fields;
    }
}
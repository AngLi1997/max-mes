package com.bmos.lims2.server.stability.scheme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordComponentMapper;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterDataPoint;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterOption;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterTrend;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterDataPointMapper;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterOptionMapper;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterTrendMapper;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilityCopyItemsResultDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeDataPointBatchUpdateDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeItemSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeJudgmentBatchUpdateDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeDataPointDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeJudgmentDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeDataPoint;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeItem;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeJudgment;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeParameter;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeDataPointMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeItemMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeJudgmentMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeItemService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeItemTeamService;
import com.bmos.lims2.server.eln.record.mapper.SchemeParameterComponentConfigMapper;
import com.bmos.lims2.server.eln.record.entity.SchemeParameterComponentConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionSaveItemsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 稳定性方案检验项目配置Service实现类
 */
@Service
@Slf4j
public class StabilitySchemeItemServiceImpl implements StabilitySchemeItemService {

    @Autowired
    private StabilitySchemeItemMapper itemMapper;

    @Autowired
    private StabilitySchemeParameterMapper parameterMapper;

    @Autowired
    private StabilitySchemeDataPointMapper dataPointMapper;

    @Autowired
    private StabilitySchemeJudgmentMapper judgmentMapper;

    @Autowired
    private StabilitySchemeVersionMapper versionMapper;

    @Autowired
    private StabilitySchemeItemTeamService teamService;

    @Autowired
    private SchemeParameterComponentConfigMapper schemeParameterComponentConfigMapper;

    @Autowired
    private BatchRecordComponentMapper batchRecordComponentMapper;

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
        StabilitySchemeVersion version = versionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }
        if (version.getStatus() == StabilitySchemeVersionStatusEnum.COMPLETED) {
            version.setStatus(StabilitySchemeVersionStatusEnum.EDITING);
            versionMapper.updateById(version);
        } else if (version.getStatus() != StabilitySchemeVersionStatusEnum.EDITING) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
        }
    }

    /**
     * 将分析项主数据中已绑定的数据点自动同步到方案数据点表（仅对新增分析项调用）
     */
    private void autoSaveDataPointsFromMaster(Long schemeId, Long versionId, Long itemConfigId,
                                              Long inspectItemId, Long parameterId, Long parameterConfigId) {
        List<InspectParameterDataPoint> masterDps = inspectParameterDataPointMapper.selectByParameterId(parameterId);
        if (CollUtil.isEmpty(masterDps)) {
            return;
        }
        List<StabilitySchemeDataPoint> toInsert = new ArrayList<>();
        for (InspectParameterDataPoint master : masterDps) {
            StabilitySchemeDataPoint dp = new StabilitySchemeDataPoint();
            dp.setSchemeId(schemeId);
            dp.setVersionId(versionId);
            dp.setItemConfigId(itemConfigId);
            dp.setParameterConfigId(parameterConfigId);
            dp.setParameterId(parameterId);
            dp.setDataPointId(master.getId());
            dp.setName(master.getName());
            dp.setPointType(DataPointTypeEnum.valueOf(master.getResultType().name()));
            dp.setTimeFormat(master.getTimeFormat());
            dp.setDateStyle(master.getDateStyle());
            dp.setReportDisplay(master.getReportDisplay() != null ? master.getReportDisplay() : false);
            // 选项（OPTION 类型）
            if (master.getResultType() != null && "OPTION".equals(master.getResultType().name())) {
                List<InspectParameterOption> options = inspectParameterOptionMapper.selectByDataPointId(master.getId());
                if (CollUtil.isNotEmpty(options)) {
                    dp.setOptions(JSON.toJSONString(options.stream()
                            .map(InspectParameterOption::getOptionValue)
                            .collect(Collectors.toList())));
                }
            }
            // 趋势线（NUMBER 类型）
            if (master.getResultType() != null && "NUMBER".equals(master.getResultType().name())) {
                List<InspectParameterTrend> trends = inspectParameterTrendMapper.selectByDataPointId(master.getId());
                if (CollUtil.isNotEmpty(trends)) {
                    dp.setTrendLineConfig(JSON.toJSONString(trends));
                }
            }
            toInsert.add(dp);
        }
        for (StabilitySchemeDataPoint dp : toInsert) {
            dataPointMapper.insert(dp);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveItems(StabilitySchemeItemSaveDTO saveDTO) {
        doSaveItems(saveDTO, true);
        log.info("保存稳定性方案检验项目配置成功（全量）：versionId={}", saveDTO.getVersionId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItems(StabilitySchemeItemSaveDTO saveDTO) {
        doSaveItems(saveDTO, false);
        log.info("更新稳定性方案检验项目配置成功（增量）：versionId={}", saveDTO.getVersionId());
    }

    private void doSaveItems(StabilitySchemeItemSaveDTO saveDTO, boolean deleteOrphans) {
        Long versionId = saveDTO.getVersionId();

        StabilitySchemeVersion version = versionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }
        if (version.getStatus() == StabilitySchemeVersionStatusEnum.COMPLETED) {
            version.setStatus(StabilitySchemeVersionStatusEnum.EDITING);
            versionMapper.updateById(version);
        } else if (version.getStatus() != StabilitySchemeVersionStatusEnum.EDITING) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
        }

        Long schemeId = version.getSchemeId();
        String userId = SysUserHolder.getUser().getUserId();

        List<StabilitySchemeItem> existingItems = itemMapper.selectByVersionId(versionId);
        Map<Long, StabilitySchemeItem> existingItemMap = existingItems.stream()
                .collect(Collectors.toMap(StabilitySchemeItem::getId, e -> e));
        Set<Long> keptItemIds = new HashSet<>();

        List<StabilitySchemeItemSaveDTO.ItemDTO> incomingItems =
                saveDTO.getItems() != null ? saveDTO.getItems() : new ArrayList<>();

        for (StabilitySchemeItemSaveDTO.ItemDTO itemDTO : incomingItems) {
            StabilitySchemeItem item;
            if (itemDTO.getItemConfigId() != null && existingItemMap.containsKey(itemDTO.getItemConfigId())) {
                // 更新现有检验项目
                item = existingItemMap.get(itemDTO.getItemConfigId());
                item.setDuration(itemDTO.getDuration());
                item.setTimeUnit(itemDTO.getTimeUnit());
                item.setUpdateBy(userId);
                itemMapper.updateById(item);
                teamService.saveTeams(schemeId, versionId, item.getId(), item.getInspectItemId(), itemDTO.getTeams());
            } else {
                // 新增检验项目
                item = new StabilitySchemeItem();
                item.setSchemeId(schemeId);
                item.setVersionId(versionId);
                item.setInspectItemId(itemDTO.getInspectItemId());
                item.setDuration(itemDTO.getDuration());
                item.setTimeUnit(itemDTO.getTimeUnit());
                item.setCreateBy(userId);
                itemMapper.insert(item);
                teamService.saveTeams(schemeId, versionId, item.getId(), itemDTO.getInspectItemId(), itemDTO.getTeams());
            }
            keptItemIds.add(item.getId());

            // 增量更新分析项
            saveParameters(itemDTO.getInspectionParameters(), item.getId(), itemDTO.getInspectItemId(), versionId, schemeId, userId);
        }

        if (deleteOrphans) {
            // 删除不在本次列表中的检验项目（级联删除）
            for (StabilitySchemeItem existingItem : existingItems) {
                if (!keptItemIds.contains(existingItem.getId())) {
                    Long itemId = existingItem.getId();
                    teamService.deleteByItemConfigId(itemId);
                    judgmentMapper.deleteByItemId(itemId);
                    dataPointMapper.deleteByItemId(itemId);
                    parameterMapper.deleteByItemId(itemId);
                    itemMapper.deleteById(itemId);
                }
            }
        }
    }

    private void saveParameters(List<StabilitySchemeItemSaveDTO.ParameterDTO> parameterDTOs,
                                Long itemConfigId, Long inspectItemId, Long versionId, Long schemeId, String userId) {
        List<StabilitySchemeParameter> existingParams = parameterMapper.selectByItemId(itemConfigId);
        Map<Long, StabilitySchemeParameter> existingParamMap = existingParams.stream()
                .collect(Collectors.toMap(StabilitySchemeParameter::getId, e -> e));
        Set<Long> keptParamIds = new HashSet<>();

        List<StabilitySchemeItemSaveDTO.ParameterDTO> incomingParams =
                parameterDTOs != null ? parameterDTOs : new ArrayList<>();

        for (StabilitySchemeItemSaveDTO.ParameterDTO paramDTO : incomingParams) {
            StabilitySchemeParameter parameter;
            if (paramDTO.getParameterConfigId() != null && existingParamMap.containsKey(paramDTO.getParameterConfigId())) {
                // 更新现有分析项
                parameter = existingParamMap.get(paramDTO.getParameterConfigId());
                parameter.setStandardRule(paramDTO.getStandardRule());
                parameter.setIsExecutable(paramDTO.getIsExecutable());
                parameter.setIsReportable(paramDTO.getIsReportable());
                parameter.setExecuteMethod(paramDTO.getExecuteMethod());
                parameter.setFinalExpression(paramDTO.getFinalExpression());
                parameter.setRecordId(paramDTO.getRecordId());
                parameter.setRecordCode(paramDTO.getRecordCode());
                parameter.setRecordVersionId(paramDTO.getRecordVersionId());
                parameter.setRecordItemId(paramDTO.getRecordItemId());
                parameter.setUpdateBy(userId);
                parameterMapper.updateById(parameter);
            } else {
                // 新增分析项
                parameter = new StabilitySchemeParameter();
                parameter.setSchemeId(schemeId);
                parameter.setVersionId(versionId);
                parameter.setItemConfigId(itemConfigId);
                parameter.setInspectItemId(inspectItemId);
                parameter.setParameterId(paramDTO.getParameterId());
                parameter.setStandardRule(paramDTO.getStandardRule());
                parameter.setIsExecutable(paramDTO.getIsExecutable());
                parameter.setIsReportable(paramDTO.getIsReportable());
                parameter.setExecuteMethod(paramDTO.getExecuteMethod());
                parameter.setFinalExpression(paramDTO.getFinalExpression());
                parameter.setRecordId(paramDTO.getRecordId());
                parameter.setRecordCode(paramDTO.getRecordCode());
                parameter.setRecordVersionId(paramDTO.getRecordVersionId());
                parameter.setRecordItemId(paramDTO.getRecordItemId());
                parameter.setCreateBy(userId);
                parameterMapper.insert(parameter);
            }
            keptParamIds.add(parameter.getId());
        }

        // 删除不在本次列表中的分析项（级联删除）
        for (StabilitySchemeParameter existingParam : existingParams) {
            if (!keptParamIds.contains(existingParam.getId())) {
                Long paramId = existingParam.getId();
                judgmentMapper.deleteByParamConfigId(paramId);
                dataPointMapper.deleteByParamConfigId(paramId);
                parameterMapper.deleteById(paramId);
            }
        }
    }


    @Override
    public List<StabilitySchemeItemDTO> listItems(Long versionId) {
        List<StabilitySchemeItemDTO> result = itemMapper.selectByVersionIdWithNames(versionId);

        for (StabilitySchemeItemDTO dto : result) {
            dto.setTeams(teamService.listByItemConfigId(dto.getId()));
            List<StabilitySchemeParameterDTO> paramDTOs = parameterMapper.selectByItemIdWithNames(dto.getId());
            for (StabilitySchemeParameterDTO paramDTO : paramDTOs) {
                paramDTO.setDataPoints(dataPointMapper.listByParamConfigId(paramDTO.getId()));
                paramDTO.setJudgments(judgmentMapper.listByParamConfigId(paramDTO.getId()));
                computeJudgmentConsistency(paramDTO);
            }
            dto.setInspectionParameters(paramDTOs);
        }

        return result;
    }

    private void computeJudgmentConsistency(StabilitySchemeParameterDTO param) {
        if (param.getJudgments() == null || param.getJudgments().isEmpty()) {
            param.setJudgmentConfigError(false);
            param.setJudgmentDataPointDeleted(false);
            param.setJudgmentDataPointBindingMissing(false);
            param.setJudgmentDataPointTypeChanged(false);
            param.setJudgmentDataPointOptionInvalid(false);
            return;
        }

        // 构建活跃数据点映射
        Set<Long> aliveDpConfigIds = new HashSet<>();
        Map<Long, StabilitySchemeDataPointDTO> configIdToDp = new HashMap<>();
        if (param.getDataPoints() != null) {
            for (StabilitySchemeDataPointDTO dp : param.getDataPoints()) {
                if (dp.getId() != null) {
                    aliveDpConfigIds.add(dp.getId());
                    configIdToDp.put(dp.getId(), dp);
                }
            }
        }

        boolean judgmentConfigError = false;
        boolean anyPointDeleted = false;
        boolean anyPointBindingMissing = false;
        boolean anyPointTypeChanged = false;
        boolean anyPointOptionInvalid = false;
        boolean elnExecute = ExecuteMethodEnum.ELN.equals(param.getExecuteMethod());
        Map<Long, Set<String>> componentOptionCache = new HashMap<>();

        for (StabilitySchemeJudgmentDTO j : param.getJudgments()) {
            j.setDataPointDeleted(false);
            j.setDataPointBindingMissing(false);
            j.setDataPointTypeChanged(false);
            j.setDataPointOptionInvalid(false);

            // 检查数据点是否被删除
            if (j.getDataPointConfigId() != null && !aliveDpConfigIds.contains(j.getDataPointConfigId())) {
                j.setDataPointDeleted(true);
                judgmentConfigError = true;
                anyPointDeleted = true;
                continue;
            }

            StabilitySchemeDataPointDTO dp = j.getDataPointConfigId() != null
                    ? configIdToDp.get(j.getDataPointConfigId()) : null;

            if (dp != null) {
                // 检查数据点类型是否变更
                if (j.getPointType() != null && dp.getPointType() != null
                        && !j.getPointType().equals(dp.getPointType())) {
                    j.setDataPointTypeChanged(true);
                    judgmentConfigError = true;
                    anyPointTypeChanged = true;
                }
                // 检查 ELN 绑定是否缺失
                if (elnExecute && (dp.getComponentId() == null || dp.getFieldId() == null)) {
                    j.setDataPointBindingMissing(true);
                    judgmentConfigError = true;
                    anyPointBindingMissing = true;
                }
                // 检查选项值是否有效
                if (isOptionValueInvalid(elnExecute, dp, j.getStandardValue(), componentOptionCache)) {
                    j.setDataPointOptionInvalid(true);
                    judgmentConfigError = true;
                    anyPointOptionInvalid = true;
                }
            }
        }

        param.setJudgmentConfigError(judgmentConfigError);
        param.setJudgmentDataPointDeleted(anyPointDeleted);
        param.setJudgmentDataPointBindingMissing(anyPointBindingMissing);
        param.setJudgmentDataPointTypeChanged(anyPointTypeChanged);
        param.setJudgmentDataPointOptionInvalid(anyPointOptionInvalid);
    }

    private boolean isOptionValueInvalid(boolean elnExecute, StabilitySchemeDataPointDTO dp,
                                         String standardValue, Map<Long, Set<String>> componentOptionCache) {
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
                        }
                    });
                }
            } catch (Exception ignored) {
            }
        }
        cache.put(componentId, fields);
        return fields;
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
        }
        List<String> split = StrUtil.splitTrim(standardValue, ',');
        if (CollUtil.isEmpty(split)) {
            return Collections.singletonList(StrUtil.trim(standardValue));
        }
        return split;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItem(Long itemId) {
        StabilitySchemeItem item = itemMapper.selectById(itemId);
        if (item == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_ITEM_NOT_EXIST);
        }
        revertToEditingIfCompleted(item.getVersionId());
        // 级联删除：班组 → 判定 → 数据点 → 分析项 → 检验项目
        teamService.deleteByItemConfigId(itemId);
        judgmentMapper.deleteByItemId(itemId);
        dataPointMapper.deleteByItemId(itemId);
        parameterMapper.deleteByItemId(itemId);
        itemMapper.deleteById(itemId);
        log.info("删除稳定性方案检验项目配置成功：itemId={}", itemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteParameter(Long parameterId) {
        StabilitySchemeParameter parameter = parameterMapper.selectById(parameterId);
        if (parameter == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PARAMETER_NOT_EXIST);
        }
        revertToEditingIfCompleted(parameter.getVersionId());
        Long itemConfigId = parameter.getItemConfigId();
        // 级联删除：判定 → 数据点 → 分析项
        judgmentMapper.deleteByParamConfigId(parameterId);
        dataPointMapper.deleteByParamConfigId(parameterId);
        parameterMapper.deleteById(parameterId);
        log.info("删除稳定性方案分析项配置成功：parameterId={}", parameterId);

        // 若该检验项目下已无分析项，则同时删除检验项目配置
        List<StabilitySchemeParameter> remaining = parameterMapper.selectByItemId(itemConfigId);
        if (remaining == null || remaining.isEmpty()) {
            teamService.deleteByItemConfigId(itemConfigId);
            itemMapper.deleteById(itemConfigId);
            log.info("检验项目下无分析项，级联删除检验项目配置：itemConfigId={}", itemConfigId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataPoint(Long dataPointId) {
        StabilitySchemeDataPoint dataPoint = dataPointMapper.selectById(dataPointId);
        if (dataPoint == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_DATA_POINT_NOT_EXIST);
        }
        revertToEditingIfCompleted(dataPoint.getVersionId());
        // 级联删除引用该数据点的判定
        judgmentMapper.deleteByDataPointConfigId(dataPointId);
        dataPointMapper.deleteById(dataPointId);
        log.info("删除稳定性方案数据点配置成功：dataPointId={}", dataPointId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJudgment(Long judgmentId) {
        StabilitySchemeJudgment judgment = judgmentMapper.selectById(judgmentId);
        if (judgment == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_JUDGMENT_NOT_EXIST);
        }
        revertToEditingIfCompleted(judgment.getVersionId());
        judgmentMapper.deleteById(judgmentId);
        log.info("删除稳定性方案判定配置成功：judgmentId={}", judgmentId);
    }

    @Override
    public StabilitySchemeParameterDTO getParameterDetail(Long parameterConfigId) {
        StabilitySchemeParameter parameter = parameterMapper.selectById(parameterConfigId);
        if (parameter == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PARAMETER_NOT_EXIST);
        }
        List<StabilitySchemeParameterDTO> list = parameterMapper.selectByItemIdWithNames(parameter.getItemConfigId());
        StabilitySchemeParameterDTO dto = list.stream()
                .filter(p -> p.getId().equals(parameterConfigId))
                .findFirst()
                .orElseThrow(() -> new BmosException(LimsResponseCode.STABILITY_SCHEME_PARAMETER_NOT_EXIST));
        dto.setDataPoints(dataPointMapper.listByParamConfigId(parameterConfigId));
        dto.setJudgments(judgmentMapper.listByParamConfigId(parameterConfigId));
        computeJudgmentConsistency(dto);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExecuteMethod(Long parameterConfigId, ExecuteMethodEnum executeMethod,
                                    Long recordId, Long recordVersionId, String recordCode, Long recordItemId) {
        StabilitySchemeParameter parameter = parameterMapper.selectById(parameterConfigId);
        if (parameter == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PARAMETER_NOT_EXIST);
        }
        revertToEditingIfCompleted(parameter.getVersionId());

        // 判断是否需要清空相关数据
        // 1. 执行方式切换（LIMS↔ELN）
        boolean isExecuteMethodChanged = !Objects.equals(parameter.getExecuteMethod(), executeMethod);
        // 2. 执行方式仍为ELN，但切换了ELN记录（recordId / recordVersionId / recordItemId 变化）
        boolean isElnRecordChanged = executeMethod == ExecuteMethodEnum.ELN
                && parameter.getExecuteMethod() == ExecuteMethodEnum.ELN
                && (!Objects.equals(parameter.getRecordId(), recordId)
                    || !Objects.equals(parameter.getRecordVersionId(), recordVersionId)
                    || !Objects.equals(parameter.getRecordItemId(), recordItemId));

        if (isExecuteMethodChanged || isElnRecordChanged) {
            // 清空判定条件配置
            judgmentMapper.deleteByParamConfigId(parameterConfigId);

            // 清空分析项组件配置
            schemeParameterComponentConfigMapper.deleteByInspectionSchemeParameterConfigId(parameterConfigId);

            // 清空数据点中和记录关联的ELN字段
            LambdaUpdateWrapper<StabilitySchemeDataPoint> dpUpdater = new LambdaUpdateWrapper<>();
            dpUpdater.eq(StabilitySchemeDataPoint::getParameterConfigId, parameterConfigId)
                    .set(StabilitySchemeDataPoint::getComponentId, null)
                    .set(StabilitySchemeDataPoint::getRecordId, null)
                    .set(StabilitySchemeDataPoint::getRecordVersionId, null)
                    .set(StabilitySchemeDataPoint::getRecordItemId, null)
                    .set(StabilitySchemeDataPoint::getFieldId, null);
            dataPointMapper.update(null, dpUpdater);
        }

        // ELN 必填校验
        if (executeMethod == ExecuteMethodEnum.ELN) {
            if (recordId == null || recordVersionId == null || recordCode == null) {
                throw new BmosException(LimsResponseCode.PARAMETER_ELN_MISSING_PARAMETER);
            }
        }

        // 使用更新器，显式设置字段，确保可将列更新为 NULL
        LambdaUpdateWrapper<StabilitySchemeParameter> updater = new LambdaUpdateWrapper<>();
        updater.eq(StabilitySchemeParameter::getId, parameterConfigId)
                .set(StabilitySchemeParameter::getExecuteMethod, executeMethod);

        // 执行方式切换或 ELN 记录切换时，需要清空 final_expression
        if (isExecuteMethodChanged || isElnRecordChanged) {
            updater.set(StabilitySchemeParameter::getFinalExpression, null);
        }

        if (executeMethod == ExecuteMethodEnum.ELN) {
            updater.set(StabilitySchemeParameter::getRecordId, recordId)
                   .set(StabilitySchemeParameter::getRecordVersionId, recordVersionId)
                   .set(StabilitySchemeParameter::getRecordCode, recordCode)
                   .set(StabilitySchemeParameter::getRecordItemId, recordItemId);
        } else {
            // 切换回 LIMS 时清空 ELN 绑定信息，避免脏数据
            updater.set(StabilitySchemeParameter::getRecordId, null)
                   .set(StabilitySchemeParameter::getRecordVersionId, null)
                   .set(StabilitySchemeParameter::getRecordCode, null)
                   .set(StabilitySchemeParameter::getRecordItemId, null);
        }

        parameterMapper.update(null, updater);
        log.info("更新稳定性方案分析项执行方式成功：parameterConfigId={}, executeMethod={}", parameterConfigId, executeMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataPoints(List<StabilitySchemeDataPointBatchUpdateDTO> dataPoints) {
        if (dataPoints == null || dataPoints.isEmpty()) {
            return;
        }
        Long versionId = dataPoints.get(0).getVersionId();
        revertToEditingIfCompleted(versionId);

        String userId = SysUserHolder.getUser().getUserId();
        // 收集本次更新保留的ID
        List<Long> keptIds = new ArrayList<>();

        for (StabilitySchemeDataPointBatchUpdateDTO dto : dataPoints) {
            if (dto.getDataPointConfigId() != null) {
                // 更新现有数据点
                StabilitySchemeDataPoint dataPoint = dataPointMapper.selectById(dto.getDataPointConfigId());
                if (dataPoint == null) {
                    throw new BmosException(LimsResponseCode.STABILITY_SCHEME_DATA_POINT_NOT_EXIST);
                }
                dataPoint.setName(dto.getName());
                dataPoint.setPointType(dto.getPointType());
                dataPoint.setTrendLineConfig(dto.getTrendLineConfig());
                dataPoint.setOptions(dto.getOptions());
                dataPoint.setTimeFormat(dto.getTimeFormat());
                dataPoint.setDateStyle(dto.getDateStyle());
                dataPoint.setRoundingUp(dto.getRoundingUp());
                dataPoint.setReportDisplay(dto.getReportDisplay());
                dataPoint.setRecordId(dto.getRecordId());
                dataPoint.setRecordVersionId(dto.getRecordVersionId());
                dataPoint.setComponentId(dto.getComponentId());
                dataPoint.setRecordItemId(dto.getRecordItemId());
                dataPoint.setFieldId(dto.getFieldId());
                dataPoint.setUpdateBy(userId);
                dataPointMapper.updateById(dataPoint);
                keptIds.add(dto.getDataPointConfigId());

                // 同步更新父分析项的最终判定表达式
                if (dto.getFinalExpression() != null && dto.getParameterConfigId() != null) {
                    StabilitySchemeParameter param = parameterMapper.selectById(dto.getParameterConfigId());
                    if (param != null) {
                        param.setFinalExpression(dto.getFinalExpression());
                        param.setUpdateBy(userId);
                        parameterMapper.updateById(param);
                    }
                }
            } else {
                // 新增数据点
                StabilitySchemeParameter param = parameterMapper.selectById(dto.getParameterConfigId());
                if (param == null) {
                    throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PARAMETER_NOT_EXIST);
                }
                StabilitySchemeDataPoint dataPoint = new StabilitySchemeDataPoint();
                dataPoint.setSchemeId(param.getSchemeId());
                dataPoint.setVersionId(versionId);
                dataPoint.setItemConfigId(param.getItemConfigId());
                dataPoint.setParameterConfigId(dto.getParameterConfigId());
                dataPoint.setParameterId(dto.getParameterId());
                dataPoint.setDataPointId(dto.getDataPointId());
                dataPoint.setName(dto.getName());
                dataPoint.setPointType(dto.getPointType());
                dataPoint.setTrendLineConfig(dto.getTrendLineConfig());
                dataPoint.setOptions(dto.getOptions());
                dataPoint.setTimeFormat(dto.getTimeFormat());
                dataPoint.setDateStyle(dto.getDateStyle());
                dataPoint.setRoundingUp(dto.getRoundingUp());
                dataPoint.setReportDisplay(dto.getReportDisplay());
                dataPoint.setRecordId(dto.getRecordId());
                dataPoint.setRecordVersionId(dto.getRecordVersionId());
                dataPoint.setComponentId(dto.getComponentId());
                dataPoint.setRecordItemId(dto.getRecordItemId());
                dataPoint.setFieldId(dto.getFieldId());
                dataPoint.setCreateBy(userId);
                dataPointMapper.insert(dataPoint);
                keptIds.add(dataPoint.getId());
            }
        }

        // 删除不在本次列表中的数据点（同一分析项下）
        Long parameterConfigId = dataPoints.get(0).getParameterConfigId();
        if (parameterConfigId != null) {
            List<StabilitySchemeDataPoint> existing = dataPointMapper.selectByParamConfigIdSimple(parameterConfigId);
            for (StabilitySchemeDataPoint dp : existing) {
                if (!keptIds.contains(dp.getId())) {
                    dataPointMapper.deleteById(dp.getId());
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJudgments(Long parameterConfigId, String finalExpression, List<StabilitySchemeJudgmentBatchUpdateDTO> judgments) {
        // 查询分析项配置
        StabilitySchemeParameter param = parameterMapper.selectById(parameterConfigId);
        if (param == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PARAMETER_NOT_EXIST);
        }
        Long versionId = param.getVersionId();
        revertToEditingIfCompleted(versionId);

        // 先更新分析项的最终判定表达式
        param.setFinalExpression(finalExpression);
        param.setUpdateBy(SysUserHolder.getUser().getUserId());
        parameterMapper.updateById(param);

        // 空列表：删除该分析项下所有判定
        if (judgments == null || judgments.isEmpty()) {
            LambdaQueryWrapper<StabilitySchemeJudgment> delWrapper = new LambdaQueryWrapper<>();
            delWrapper.eq(StabilitySchemeJudgment::getParameterConfigId, parameterConfigId);
            judgmentMapper.delete(delWrapper);
            return;
        }

        String userId = SysUserHolder.getUser().getUserId();
        List<Long> keptIds = new ArrayList<>();

        for (StabilitySchemeJudgmentBatchUpdateDTO dto : judgments) {
            if (dto.getJudgmentConfigId() != null) {
                // 更新现有判定
                StabilitySchemeJudgment judgment = judgmentMapper.selectById(dto.getJudgmentConfigId());
                if (judgment == null) {
                    throw new BmosException(LimsResponseCode.STABILITY_SCHEME_JUDGMENT_NOT_EXIST);
                }
                judgment.setJudgementConfigName(dto.getJudgementConfigName());
                judgment.setDataPointConfigId(dto.getDataPointConfigId());
                judgment.setDataPointId(dto.getDataPointId());
                judgment.setPointType(dto.getPointType());
                judgment.setJudgmentType(dto.getJudgmentType());
                judgment.setDefaultResult(dto.getDefaultResult());
                judgment.setMinValue(dto.getMinValue());
                judgment.setMinOperator(dto.getMinOperator());
                judgment.setMaxValue(dto.getMaxValue());
                judgment.setMaxOperator(dto.getMaxOperator());
                judgment.setStandardValue(dto.getStandardValue());
                judgment.setExpression(dto.getExpression());
                judgment.setMinTime(dto.getMinTime());
                judgment.setMaxTime(dto.getMaxTime());
                judgment.setUpdateBy(userId);
                judgmentMapper.updateById(judgment);
                keptIds.add(dto.getJudgmentConfigId());
            } else {
                // 新增判定
                StabilitySchemeJudgment judgment = new StabilitySchemeJudgment();
                judgment.setSchemeId(param.getSchemeId());
                judgment.setVersionId(versionId);
                judgment.setItemConfigId(param.getItemConfigId());
                judgment.setParameterConfigId(parameterConfigId);
                judgment.setDataPointConfigId(dto.getDataPointConfigId());
                judgment.setParameterId(dto.getParameterId());
                judgment.setDataPointId(dto.getDataPointId());
                judgment.setJudgementConfigName(dto.getJudgementConfigName());
                judgment.setPointType(dto.getPointType());
                judgment.setJudgmentType(dto.getJudgmentType());
                judgment.setDefaultResult(dto.getDefaultResult());
                judgment.setMinValue(dto.getMinValue());
                judgment.setMinOperator(dto.getMinOperator());
                judgment.setMaxValue(dto.getMaxValue());
                judgment.setMaxOperator(dto.getMaxOperator());
                judgment.setStandardValue(dto.getStandardValue());
                judgment.setExpression(dto.getExpression());
                judgment.setMinTime(dto.getMinTime());
                judgment.setMaxTime(dto.getMaxTime());
                judgment.setCreateBy(userId);
                judgmentMapper.insert(judgment);
                keptIds.add(judgment.getId());
            }
        }

        // 删除不在本次列表中的判定（同一分析项下）
        LambdaQueryWrapper<StabilitySchemeJudgment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeJudgment::getParameterConfigId, parameterConfigId)
               .eq(StabilitySchemeJudgment::getDeleted, false);
        List<StabilitySchemeJudgment> existing = judgmentMapper.selectList(wrapper);
        for (StabilitySchemeJudgment j : existing) {
            if (!keptIds.contains(j.getId())) {
                judgmentMapper.deleteById(j.getId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StabilityCopyItemsResultDTO copyItems(Long sourceVersionId, Long targetVersionId, Long schemeId) {
        StabilityCopyItemsResultDTO result = new StabilityCopyItemsResultDTO();

        List<StabilitySchemeItem> sourceItems = itemMapper.selectByVersionId(sourceVersionId);
        if (sourceItems == null || sourceItems.isEmpty()) {
            return result;
        }

        String userId = SysUserHolder.getUser().getUserId();

        for (StabilitySchemeItem sourceItem : sourceItems) {
            StabilitySchemeItem newItem = new StabilitySchemeItem();
            newItem.setSchemeId(schemeId);
            newItem.setVersionId(targetVersionId);
            newItem.setInspectItemId(sourceItem.getInspectItemId());
            newItem.setDuration(sourceItem.getDuration());
            newItem.setTimeUnit(sourceItem.getTimeUnit());
            newItem.setCreateBy(userId);
            itemMapper.insert(newItem);
            // 记录 itemConfigId 映射
            result.getItemIdMap().put(sourceItem.getId(), newItem.getId());

            // 复制班组配置
            List<Long> sourceTeamIds = teamService.listByItemConfigId(sourceItem.getId());
            if (!sourceTeamIds.isEmpty()) {
                teamService.saveTeams(schemeId, targetVersionId, newItem.getId(), sourceItem.getInspectItemId(), sourceTeamIds);
            }

            List<StabilitySchemeParameter> sourceParams = parameterMapper.selectByItemId(sourceItem.getId());
            for (StabilitySchemeParameter sourceParam : sourceParams) {
                StabilitySchemeParameter newParam = new StabilitySchemeParameter();
                newParam.setSchemeId(schemeId);
                newParam.setVersionId(targetVersionId);
                newParam.setItemConfigId(newItem.getId());
                newParam.setInspectItemId(sourceParam.getInspectItemId());
                newParam.setParameterId(sourceParam.getParameterId());
                newParam.setStandardRule(sourceParam.getStandardRule());
                newParam.setIsExecutable(sourceParam.getIsExecutable());
                newParam.setIsReportable(sourceParam.getIsReportable());
                newParam.setExecuteMethod(sourceParam.getExecuteMethod());
                newParam.setFinalExpression(sourceParam.getFinalExpression());
                newParam.setRecordId(sourceParam.getRecordId());
                newParam.setRecordCode(sourceParam.getRecordCode());
                newParam.setRecordVersionId(sourceParam.getRecordVersionId());
                newParam.setRecordItemId(sourceParam.getRecordItemId());
                newParam.setCreateBy(userId);
                parameterMapper.insert(newParam);
                // 记录 parameterConfigId 映射
                result.getParamIdMap().put(sourceParam.getId(), newParam.getId());

                // 复制组件配置
                List<SchemeParameterComponentConfig> srcComponentConfigs = schemeParameterComponentConfigMapper.selectByParameterConfigId(sourceParam.getId());
                if (!srcComponentConfigs.isEmpty()) {
                    List<SchemeParameterComponentConfig> newComponentConfigs = new ArrayList<>();
                    for (SchemeParameterComponentConfig sc : srcComponentConfigs) {
                        SchemeParameterComponentConfig nc = new SchemeParameterComponentConfig();
                        nc.setParameterId(sc.getParameterId());
                        nc.setParameterConfigId(newParam.getId());
                        nc.setSchemeId(schemeId);
                        nc.setSchemeVersionId(String.valueOf(targetVersionId));
                        nc.setRecordItemId(sc.getRecordItemId());
                        nc.setRecordVersionId(sc.getRecordVersionId());
                        nc.setConfigInfo(sc.getConfigInfo());
                        nc.setComponentId(sc.getComponentId());
                        nc.setFieldId(sc.getFieldId());
                        newComponentConfigs.add(nc);
                    }
                    schemeParameterComponentConfigMapper.insertBatch(newComponentConfigs);
                }

                // 复制数据点配置，记录旧ID→新ID映射（供判定使用）
                Map<Long, Long> dpIdMap = new HashMap<>();
                List<StabilitySchemeDataPoint> sourceDataPoints = dataPointMapper.selectByParamConfigIdSimple(sourceParam.getId());
                for (StabilitySchemeDataPoint sourceDP : sourceDataPoints) {
                    StabilitySchemeDataPoint newDP = new StabilitySchemeDataPoint();
                    newDP.setSchemeId(schemeId);
                    newDP.setVersionId(targetVersionId);
                    newDP.setItemConfigId(newItem.getId());
                    newDP.setParameterConfigId(newParam.getId());
                    newDP.setParameterId(sourceDP.getParameterId());
                    newDP.setDataPointId(sourceDP.getDataPointId());
                    newDP.setName(sourceDP.getName());
                    newDP.setPointType(sourceDP.getPointType());
                    newDP.setTrendLineConfig(sourceDP.getTrendLineConfig());
                    newDP.setOptions(sourceDP.getOptions());
                    newDP.setTimeFormat(sourceDP.getTimeFormat());
                    newDP.setDateStyle(sourceDP.getDateStyle());
                    newDP.setRoundingUp(sourceDP.getRoundingUp());
                    newDP.setReportDisplay(sourceDP.getReportDisplay());
                    newDP.setRecordId(sourceDP.getRecordId());
                    newDP.setRecordVersionId(sourceDP.getRecordVersionId());
                    newDP.setComponentId(sourceDP.getComponentId());
                    newDP.setRecordItemId(sourceDP.getRecordItemId());
                    newDP.setFieldId(sourceDP.getFieldId());
                    newDP.setCreateBy(userId);
                    dataPointMapper.insert(newDP);
                    dpIdMap.put(sourceDP.getId(), newDP.getId());
                }

                // 复制判定配置，使用dpIdMap重映射dataPointConfigId
                LambdaQueryWrapper<StabilitySchemeJudgment> jWrapper = new LambdaQueryWrapper<>();
                jWrapper.eq(StabilitySchemeJudgment::getParameterConfigId, sourceParam.getId())
                        .eq(StabilitySchemeJudgment::getDeleted, false)
                        .orderByAsc(StabilitySchemeJudgment::getId);
                List<StabilitySchemeJudgment> sourceJudgments = judgmentMapper.selectList(jWrapper);
                for (StabilitySchemeJudgment sourceJ : sourceJudgments) {
                    StabilitySchemeJudgment newJ = new StabilitySchemeJudgment();
                    newJ.setSchemeId(schemeId);
                    newJ.setVersionId(targetVersionId);
                    newJ.setItemConfigId(newItem.getId());
                    newJ.setParameterConfigId(newParam.getId());
                    newJ.setParameterId(sourceJ.getParameterId());
                    newJ.setDataPointId(sourceJ.getDataPointId());
                    newJ.setJudgementConfigName(sourceJ.getJudgementConfigName());
                    newJ.setPointType(sourceJ.getPointType());
                    newJ.setJudgmentType(sourceJ.getJudgmentType());
                    newJ.setDefaultResult(sourceJ.getDefaultResult());
                    newJ.setMinValue(sourceJ.getMinValue());
                    newJ.setMinOperator(sourceJ.getMinOperator());
                    newJ.setMaxValue(sourceJ.getMaxValue());
                    newJ.setMaxOperator(sourceJ.getMaxOperator());
                    newJ.setStandardValue(sourceJ.getStandardValue());
                    newJ.setExpression(sourceJ.getExpression());
                    newJ.setMinTime(sourceJ.getMinTime());
                    newJ.setMaxTime(sourceJ.getMaxTime());
                    // 重映射数据点配置ID
                    if (sourceJ.getDataPointConfigId() != null) {
                        newJ.setDataPointConfigId(dpIdMap.getOrDefault(sourceJ.getDataPointConfigId(), sourceJ.getDataPointConfigId()));
                    }
                    newJ.setCreateBy(userId);
                    judgmentMapper.insert(newJ);
                }
            }
        }

        log.info("复制稳定性方案检验项目配置成功：sourceVersionId={}, targetVersionId={}", sourceVersionId, targetVersionId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeVersionItems(Long versionId,
                                  List<StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO> parameters) {
        StabilitySchemeVersion version = versionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }
        if (version.getStatus() == StabilitySchemeVersionStatusEnum.COMPLETED) {
            version.setStatus(StabilitySchemeVersionStatusEnum.EDITING);
            versionMapper.updateById(version);
        } else if (version.getStatus() != StabilitySchemeVersionStatusEnum.EDITING) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
        }
        Long schemeId = version.getSchemeId();
        if (parameters == null) {
            parameters = Collections.emptyList();
        }
        String userId = SysUserHolder.getUser().getUserId();

        // 1. 按 inspectItemId 分组，保持顺序
        Map<Long, List<StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO>> grouped =
                parameters.stream().collect(Collectors.groupingBy(
                        StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO::getInspectItemId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        // 2. 加载 DB 中当前版本已有的检验项目
        List<StabilitySchemeItem> existingItems = itemMapper.selectByVersionId(versionId);
        Map<Long, StabilitySchemeItem> existingByInspectItemId = existingItems.stream()
                .collect(Collectors.toMap(StabilitySchemeItem::getInspectItemId, i -> i));

        // 3. 删除本次请求中不再包含的检验项目（级联删除分析项、数据点、判定）
        for (StabilitySchemeItem existing : existingItems) {
            if (!grouped.containsKey(existing.getInspectItemId())) {
                Long itemId = existing.getId();
                teamService.deleteByItemConfigId(itemId);
                judgmentMapper.deleteByItemId(itemId);
                dataPointMapper.deleteByItemId(itemId);
                parameterMapper.deleteByItemId(itemId);
                itemMapper.deleteById(itemId);
            }
        }

        // 4. 逐组处理检验项目及其分析项
        int itemSortSeq = 1;
        for (Map.Entry<Long, List<StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO>> entry : grouped.entrySet()) {
            Long inspectItemId = entry.getKey();
            List<StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO> paramList = entry.getValue();
            // 取第一条的检验项目信息作为代表
            StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO first = paramList.get(0);

            StabilitySchemeItem schemeItem = existingByInspectItemId.get(inspectItemId);
            if (schemeItem == null) {
                schemeItem = new StabilitySchemeItem();
                schemeItem.setSchemeId(schemeId);
                schemeItem.setVersionId(versionId);
                schemeItem.setInspectItemId(inspectItemId);
                schemeItem.setDuration(first.getDuration());
                schemeItem.setTimeUnit(first.getTimeUnit());
                schemeItem.setCreateBy(userId);
                itemMapper.insert(schemeItem);
                teamService.saveTeams(schemeId, versionId, schemeItem.getId(), inspectItemId, first.getTeams());
            }
            itemSortSeq++;

            Long itemConfigId = schemeItem.getId();

            // 5. 加载该检验项目下已有的分析项，以 parameterId 为 key
            List<StabilitySchemeParameter> existingParams = parameterMapper.selectByItemId(itemConfigId);
            Map<Long, StabilitySchemeParameter> existingByParameterId = existingParams.stream()
                    .collect(Collectors.toMap(StabilitySchemeParameter::getParameterId, p -> p));

            Set<Long> incomingParameterIds = paramList.stream()
                    .map(StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO::getParameterId)
                    .collect(Collectors.toSet());

            // 删除不在本次请求中的分析项（级联删除数据点、判定）
            for (StabilitySchemeParameter existingParam : existingParams) {
                if (!incomingParameterIds.contains(existingParam.getParameterId())) {
                    Long paramId = existingParam.getId();
                    judgmentMapper.deleteByParamConfigId(paramId);
                    dataPointMapper.deleteByParamConfigId(paramId);
                    parameterMapper.deleteById(paramId);
                }
            }

            // 新增或更新分析项
            for (StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO paramDTO : paramList) {
                if (paramDTO.getParameterId() == null) {
                    continue;
                }
                StabilitySchemeParameter existing = existingByParameterId.get(paramDTO.getParameterId());
                if (existing == null) {
                    StabilitySchemeParameter param = new StabilitySchemeParameter();
                    param.setSchemeId(schemeId);
                    param.setVersionId(versionId);
                    param.setItemConfigId(itemConfigId);
                    param.setInspectItemId(inspectItemId);
                    param.setParameterId(paramDTO.getParameterId());
                    param.setStandardRule(paramDTO.getStandardRule());
                    param.setIsExecutable(paramDTO.getIsExecutable());
                    param.setIsReportable(paramDTO.getIsReportable());
                    param.setExecuteMethod(paramDTO.getExecuteMethod()==null?ExecuteMethodEnum.LIMS:paramDTO.getExecuteMethod());
                    param.setFinalExpression(paramDTO.getFinalExpression());
                    param.setRecordId(paramDTO.getRecordId());
                    param.setRecordCode(paramDTO.getRecordCode());
                    param.setRecordVersionId(paramDTO.getRecordVersionId());
                    param.setRecordItemId(paramDTO.getRecordItemId());
                    param.setCreateBy(userId);
                    parameterMapper.insert(param);
                    // 自动同步分析项主数据中已绑定的数据点
                    autoSaveDataPointsFromMaster(schemeId, versionId, itemConfigId,
                            inspectItemId, paramDTO.getParameterId(), param.getId());
                } else {
                    existing.setStandardRule(paramDTO.getStandardRule());
                    existing.setIsExecutable(paramDTO.getIsExecutable());
                    existing.setIsReportable(paramDTO.getIsReportable());
                    existing.setExecuteMethod(paramDTO.getExecuteMethod());
                    existing.setFinalExpression(paramDTO.getFinalExpression());
                    existing.setRecordId(paramDTO.getRecordId());
                    existing.setRecordCode(paramDTO.getRecordCode());
                    existing.setRecordVersionId(paramDTO.getRecordVersionId());
                    existing.setRecordItemId(paramDTO.getRecordItemId());
                    existing.setUpdateBy(userId);
                    parameterMapper.updateById(existing);
                }
            }
        }

        log.info("mergeVersionItems 完成：versionId={}, 检验项目组数={}", versionId, grouped.size());
    }
}

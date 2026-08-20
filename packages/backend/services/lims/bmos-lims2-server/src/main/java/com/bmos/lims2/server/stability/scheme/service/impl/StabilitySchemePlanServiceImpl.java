package com.bmos.lims2.server.stability.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilityCopyItemsResultDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemePlanSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemePlanDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemePlanTimepointDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlan;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlanTimepoint;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlanTimepointParam;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemePlanMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemePlanTimepointMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemePlanTimepointParamMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemePlanService;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 稳定性方案检验计划Service实现类
 */
@Service
@Slf4j
public class StabilitySchemePlanServiceImpl implements StabilitySchemePlanService {

    @Autowired
    private StabilitySchemePlanMapper planMapper;

    @Autowired
    private StabilitySchemePlanTimepointMapper timepointMapper;

    @Autowired
    private StabilitySchemePlanTimepointParamMapper timepointParamMapper;

    @Autowired
    private StabilitySchemeVersionMapper versionMapper;

    @Autowired
    private UnitCache unitCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePlans(StabilitySchemePlanSaveDTO saveDTO) {
        Long versionId = saveDTO.getVersionId();

        StabilitySchemeVersion version = versionMapper.selectById(versionId);
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }
        if (version.getStatus() != StabilitySchemeVersionStatusEnum.EDITING) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_STATUS_ERROR);
        }

        Long schemeId = version.getSchemeId();
        String userId = SysUserHolder.getUser().getUserId();

        // 增量更新：获取现有检验计划
        List<StabilitySchemePlan> existingPlans = planMapper.selectByVersionId(versionId);
        Map<Long, StabilitySchemePlan> existingPlanMap = existingPlans.stream()
                .collect(Collectors.toMap(StabilitySchemePlan::getId, e -> e));
        Set<Long> keptPlanIds = new HashSet<>();

        List<StabilitySchemePlanSaveDTO.PlanDTO> incomingPlans =
                saveDTO.getPlans() != null ? saveDTO.getPlans() : new ArrayList<>();

        for (StabilitySchemePlanSaveDTO.PlanDTO planDTO : incomingPlans) {
            StabilitySchemePlan plan;
            if (planDTO.getPlanId() != null && existingPlanMap.containsKey(planDTO.getPlanId())) {
                // 更新现有计划
                plan = existingPlanMap.get(planDTO.getPlanId());
                plan.setExperimentType(planDTO.getExperimentType());
                plan.setStorageCondition(planDTO.getStorageCondition());
                plan.setTotalSampleAmount(planDTO.getTotalSampleAmount());
                plan.setTotalSampleUnit(planDTO.getTotalSampleUnit());
                plan.setUpdateBy(userId);
                planMapper.updateById(plan);
            } else {
                // 新增计划
                plan = new StabilitySchemePlan();
                plan.setSchemeId(schemeId);
                plan.setVersionId(versionId);
                plan.setExperimentType(planDTO.getExperimentType());
                plan.setStorageCondition(planDTO.getStorageCondition());
                plan.setTotalSampleAmount(planDTO.getTotalSampleAmount());
                plan.setTotalSampleUnit(planDTO.getTotalSampleUnit());
                plan.setCreateBy(userId);
                planMapper.insert(plan);
            }
            keptPlanIds.add(plan.getId());

            // 增量更新时间点
            saveTimepoints(planDTO.getTimepoints(), plan.getId(), versionId, userId);

            // 校验：时间点取样量合计不得小于总取样量
            validateTimepointAmounts(planDTO);
        }

        // 删除不在本次列表中的计划（级联删除）
        for (StabilitySchemePlan existingPlan : existingPlans) {
            if (!keptPlanIds.contains(existingPlan.getId())) {
                Long planId = existingPlan.getId();
                timepointParamMapper.deleteByPlanId(planId);
                timepointMapper.deleteByPlanId(planId);
                planMapper.deleteById(planId);
            }
        }

        log.info("保存稳定性方案检验计划成功（增量更新）：versionId={}", versionId);
    }

    private void saveTimepoints(List<StabilitySchemePlanSaveDTO.TimepointDTO> timepointDTOs,
                                Long planId, Long versionId, String userId) {
        List<StabilitySchemePlanTimepoint> existingTimepoints = timepointMapper.selectByPlanId(planId);
        Map<Long, StabilitySchemePlanTimepoint> existingTimepointMap = existingTimepoints.stream()
                .collect(Collectors.toMap(StabilitySchemePlanTimepoint::getId, e -> e));
        Set<Long> keptTimepointIds = new HashSet<>();

        List<StabilitySchemePlanSaveDTO.TimepointDTO> incomingTimepoints =
                timepointDTOs != null ? timepointDTOs : new ArrayList<>();

        for (StabilitySchemePlanSaveDTO.TimepointDTO timepointDTO : incomingTimepoints) {
            StabilitySchemePlanTimepoint timepoint;
            if (timepointDTO.getTimepointId() != null && existingTimepointMap.containsKey(timepointDTO.getTimepointId())) {
                // 更新现有时间点
                timepoint = existingTimepointMap.get(timepointDTO.getTimepointId());
                timepoint.setTimeValue(timepointDTO.getTimeValue());
                timepoint.setTimeUnit(timepointDTO.getTimeUnit());
                timepoint.setSampleAmount(timepointDTO.getSampleAmount());
                timepoint.setSampleUnit(timepointDTO.getSampleUnit());
                timepoint.setSelectAll(timepointDTO.getSelectAll());
                timepoint.setUpdateBy(userId);
                timepointMapper.updateById(timepoint);
            } else {
                // 新增时间点
                timepoint = new StabilitySchemePlanTimepoint();
                timepoint.setPlanId(planId);
                timepoint.setVersionId(versionId);
                timepoint.setTimeValue(timepointDTO.getTimeValue());
                timepoint.setTimeUnit(timepointDTO.getTimeUnit());
                timepoint.setSampleAmount(timepointDTO.getSampleAmount());
                timepoint.setSampleUnit(timepointDTO.getSampleUnit());
                timepoint.setSelectAll(timepointDTO.getSelectAll());
                timepoint.setCreateBy(userId);
                timepointMapper.insert(timepoint);
            }
            keptTimepointIds.add(timepoint.getId());

            // 分析项关联：按时间点全量刷新（ParamRefDTO 无自身记录ID）
            timepointParamMapper.deleteByTimepointId(timepoint.getId());
            saveTimepointParams(timepointDTO.getParamRefs(), timepoint.getId(),
                    planId, versionId, userId);
        }

        // 删除不在本次列表中的时间点（级联删除）
        for (StabilitySchemePlanTimepoint existingTimepoint : existingTimepoints) {
            if (!keptTimepointIds.contains(existingTimepoint.getId())) {
                timepointParamMapper.deleteByTimepointId(existingTimepoint.getId());
                timepointMapper.deleteById(existingTimepoint.getId());
            }
        }
    }

    @Override
    public List<StabilitySchemePlanDTO> listPlans(Long versionId) {
        List<StabilitySchemePlan> plans = planMapper.selectByVersionId(versionId);
        List<StabilitySchemePlanDTO> result = new ArrayList<>();

        for (StabilitySchemePlan plan : plans) {
            StabilitySchemePlanDTO dto = BeanUtil.copyProperties(plan, StabilitySchemePlanDTO.class);
            List<StabilitySchemePlanTimepoint> timepoints = timepointMapper.selectByPlanId(plan.getId());
            List<StabilitySchemePlanTimepointDTO> timepointDTOs = new ArrayList<>();
            for (StabilitySchemePlanTimepoint timepoint : timepoints) {
                StabilitySchemePlanTimepointDTO tpDTO = BeanUtil.copyProperties(timepoint, StabilitySchemePlanTimepointDTO.class);
                // 查询分析项关联记录
                List<StabilitySchemePlanTimepointParam> params = timepointParamMapper.selectByTimepointId(timepoint.getId());
                tpDTO.setParamRefs(BeanUtil.copyToList(params, StabilitySchemePlanTimepointDTO.ParamRefDTO.class));
                timepointDTOs.add(tpDTO);
            }
            dto.setTimepoints(timepointDTOs);
            result.add(dto);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long planId) {
        StabilitySchemePlan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PLAN_NOT_EXIST);
        }
        timepointParamMapper.deleteByPlanId(planId);
        timepointMapper.deleteByPlanId(planId);
        planMapper.deleteById(planId);
        log.info("删除稳定性方案检验计划成功：planId={}", planId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTimepoint(Long timepointId) {
        StabilitySchemePlanTimepoint timepoint = timepointMapper.selectById(timepointId);
        if (timepoint == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PLAN_TIMEPOINT_NOT_EXIST);
        }
        timepointParamMapper.deleteByTimepointId(timepointId);
        timepointMapper.deleteById(timepointId);
        log.info("删除稳定性方案时间点成功：timepointId={}", timepointId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyPlans(Long sourceVersionId, Long targetVersionId, Long schemeId, StabilityCopyItemsResultDTO copyItemsResult) {
        List<StabilitySchemePlan> sourcePlans = planMapper.selectByVersionId(sourceVersionId);
        if (sourcePlans == null || sourcePlans.isEmpty()) {
            return;
        }

        String userId = SysUserHolder.getUser().getUserId();
        Map<Long, Long> itemIdMap = copyItemsResult != null ? copyItemsResult.getItemIdMap() : new java.util.HashMap<>();
        Map<Long, Long> paramIdMap = copyItemsResult != null ? copyItemsResult.getParamIdMap() : new java.util.HashMap<>();

        for (StabilitySchemePlan sourcePlan : sourcePlans) {
            StabilitySchemePlan newPlan = new StabilitySchemePlan();
            newPlan.setSchemeId(schemeId);
            newPlan.setVersionId(targetVersionId);
            newPlan.setExperimentType(sourcePlan.getExperimentType());
            newPlan.setStorageCondition(sourcePlan.getStorageCondition());
            newPlan.setTotalSampleAmount(sourcePlan.getTotalSampleAmount());
            newPlan.setTotalSampleUnit(sourcePlan.getTotalSampleUnit());
            newPlan.setCreateBy(userId);
            planMapper.insert(newPlan);

            List<StabilitySchemePlanTimepoint> sourceTimepoints = timepointMapper.selectByPlanId(sourcePlan.getId());
            for (StabilitySchemePlanTimepoint sourceTimepoint : sourceTimepoints) {
                StabilitySchemePlanTimepoint newTimepoint = new StabilitySchemePlanTimepoint();
                newTimepoint.setPlanId(newPlan.getId());
                newTimepoint.setVersionId(targetVersionId);
                newTimepoint.setTimeValue(sourceTimepoint.getTimeValue());
                newTimepoint.setTimeUnit(sourceTimepoint.getTimeUnit());
                newTimepoint.setSampleAmount(sourceTimepoint.getSampleAmount());
                newTimepoint.setSampleUnit(sourceTimepoint.getSampleUnit());
                newTimepoint.setSelectAll(sourceTimepoint.getSelectAll());
                newTimepoint.setCreateBy(userId);
                timepointMapper.insert(newTimepoint);

                // 复制分析项关联记录，使用映射修正 itemConfigId / parameterConfigId
                List<StabilitySchemePlanTimepointParam> sourceParams =
                        timepointParamMapper.selectByTimepointId(sourceTimepoint.getId());
                for (StabilitySchemePlanTimepointParam sourceParam : sourceParams) {
                    StabilitySchemePlanTimepointParam newParam = new StabilitySchemePlanTimepointParam();
                    newParam.setTimepointId(newTimepoint.getId());
                    newParam.setPlanId(newPlan.getId());
                    newParam.setVersionId(targetVersionId);
                    newParam.setParameterConfigId(paramIdMap.getOrDefault(sourceParam.getParameterConfigId(), sourceParam.getParameterConfigId()));
                    newParam.setParameterId(sourceParam.getParameterId());
                    newParam.setParameterCode(sourceParam.getParameterCode());
                    newParam.setItemConfigId(itemIdMap.getOrDefault(sourceParam.getItemConfigId(), sourceParam.getItemConfigId()));
                    newParam.setInspectItemId(sourceParam.getInspectItemId());
                    newParam.setInspectItemCode(sourceParam.getInspectItemCode());
                    newParam.setCreateBy(userId);
                    timepointParamMapper.insert(newParam);
                }
            }
        }

        log.info("复制稳定性方案检验计划成功：sourceVersionId={}, targetVersionId={}", sourceVersionId, targetVersionId);
    }

    private void validateTimepointAmounts(StabilitySchemePlanSaveDTO.PlanDTO planDTO) {
        String totalStr = planDTO.getTotalSampleAmount();
        if (totalStr == null || totalStr.trim().isEmpty()) {
            return;
        }
        BigDecimal total;
        try {
            total = new BigDecimal(totalStr.trim());
        } catch (NumberFormatException e) {
            return;
        }
        List<StabilitySchemePlanSaveDTO.TimepointDTO> timepoints =
                planDTO.getTimepoints() != null ? planDTO.getTimepoints() : new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (StabilitySchemePlanSaveDTO.TimepointDTO tp : timepoints) {
            if (tp.getSampleAmount() != null && !tp.getSampleAmount().trim().isEmpty()) {
                try {
                    sum = sum.add(new BigDecimal(tp.getSampleAmount().trim()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        if (sum.compareTo(total) < 0) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PLAN_TIMEPOINT_AMOUNT_INSUFFICIENT);
        }
    }

    @Override
    public void validatePlanTotalAmounts(Long versionId) {
        List<StabilitySchemePlanDTO> plans = listPlans(versionId);
        for (StabilitySchemePlanDTO plan : plans) {
            String totalAmountStr = plan.getTotalSampleAmount();
            String totalUnitStr = plan.getTotalSampleUnit();
            if (totalAmountStr == null || totalAmountStr.trim().isEmpty()) {
                continue;
            }
            BigDecimal total;
            try {
                total = new BigDecimal(totalAmountStr.trim());
            } catch (NumberFormatException e) {
                continue;
            }
            Long totalUnitId = null;
            if (totalUnitStr != null && !totalUnitStr.trim().isEmpty()) {
                try {
                    totalUnitId = Long.parseLong(totalUnitStr.trim());
                } catch (NumberFormatException ignored) {}
            }

            List<StabilitySchemePlanTimepointDTO> timepoints = plan.getTimepoints();
            if (timepoints == null || timepoints.isEmpty()) {
                continue;
            }
            BigDecimal sum = BigDecimal.ZERO;
            for (StabilitySchemePlanTimepointDTO tp : timepoints) {
                if (tp.getSampleAmount() == null || tp.getSampleAmount().trim().isEmpty()) {
                    continue;
                }
                BigDecimal tpAmount;
                try {
                    tpAmount = new BigDecimal(tp.getSampleAmount().trim());
                } catch (NumberFormatException ignored) {
                    continue;
                }
                Long tpUnitId = null;
                if (tp.getSampleUnit() != null && !tp.getSampleUnit().trim().isEmpty()) {
                    try {
                        tpUnitId = Long.parseLong(tp.getSampleUnit().trim());
                    } catch (NumberFormatException ignored) {}
                }
                // 单位不同时换算为总取样量单位后累加
                if (tpUnitId != null && totalUnitId != null && !tpUnitId.equals(totalUnitId)) {
                    try {
                        tpAmount = unitCache.convert(tpAmount, tpUnitId, totalUnitId);
                    } catch (Exception e) {
                        log.warn("稳定性方案检验计划取样量单位换算失败，planId={}, fromUnit={}, toUnit={}", plan.getId(), tpUnitId, totalUnitId);
                    }
                }
                sum = sum.add(tpAmount);
            }
            if (sum.compareTo(total) > 0) {
                throw new BmosException(LimsResponseCode.STABILITY_SCHEME_PLAN_TIMEPOINT_AMOUNT_EXCEED);
            }
        }
    }

    private void saveTimepointParams(List<StabilitySchemePlanSaveDTO.ParamRefDTO> paramRefs,
                                     Long timepointId, Long planId, Long versionId, String userId) {
        if (paramRefs == null || paramRefs.isEmpty()) {
            return;
        }
        for (StabilitySchemePlanSaveDTO.ParamRefDTO ref : paramRefs) {
            StabilitySchemePlanTimepointParam param = new StabilitySchemePlanTimepointParam();
            param.setTimepointId(timepointId);
            param.setPlanId(planId);
            param.setVersionId(versionId);
            param.setParameterConfigId(ref.getParameterConfigId());
            param.setParameterId(ref.getParameterId());
            param.setParameterCode(ref.getParameterCode());
            param.setItemConfigId(ref.getItemConfigId());
            param.setInspectItemId(ref.getInspectItemId());
            param.setInspectItemCode(ref.getInspectItemCode());
            param.setCreateBy(userId);
            timepointParamMapper.insert(param);
        }
    }
}

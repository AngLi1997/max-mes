package com.bmos.lims2.server.stability.plan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.cache.redis.objects.BaseUser;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.InspectionOrderSourceEnum;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.common.enums.StabilityInspectPlanStatusEnum;
import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.lims2.common.enums.StabilityTimepointTaskStatusEnum;
import com.bmos.lims2.common.enums.TaskOperationTypeEnum;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.platform.system.code.PlatformCodeFeignClient;
import com.bmos.lims2.server.platform.system.code.dto.NextCodeVO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanPauseDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanSaveDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleReceiveDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleTakeDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityPlanSampleAddItemDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityTimepointBatchTakeDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityTimepointSampleQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityInspectPlanDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityInspectPlanDetailDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityInspectPlanMatrixDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOrderSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityTimepointSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityTimepointSourceSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOverallSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOverallSampleDetailDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilitySchemeExperimentTypeDTO;
import com.bmos.lims2.server.stability.plan.entity.StabilityInspectPlan;
import com.bmos.lims2.server.stability.plan.entity.StabilityInspectPlanBatch;
import com.bmos.lims2.server.stability.plan.entity.StabilityPlanSample;
import com.bmos.lims2.server.stability.plan.entity.StabilityPlanTimepointTask;
import com.bmos.lims2.server.stability.plan.mapper.StabilityInspectPlanBatchMapper;
import com.bmos.lims2.server.stability.plan.mapper.StabilityInspectPlanMapper;
import com.bmos.lims2.server.stability.plan.mapper.StabilityPlanSampleMapper;
import com.bmos.lims2.server.stability.plan.mapper.StabilityPlanTimepointTaskMapper;
import com.bmos.lims2.server.stability.plan.service.StabilityInspectPlanService;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilityScheme;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlan;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlanTimepoint;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlanTimepointParam;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeParameter;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemePlanMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemePlanTimepointMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemePlanTimepointParamMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.entity.TaskStatusHistory;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.lims2.server.task.mapper.TaskStatusHistoryMapper;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.common.util.id.IdUtils;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bmos.lims2.common.constants.DictCodeConstant;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.mapper.AuditOperationLogMapper;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.dict.feign.DictFeign;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.unit.service.UnitCache;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 稳定性考察计划Service实现类
 */
@Service
@Slf4j
public class StabilityInspectPlanServiceImpl implements StabilityInspectPlanService {

    @Autowired
    private StabilityInspectPlanMapper planMapper;

    @Autowired
    private StabilityInspectPlanBatchMapper batchMapper;

    @Autowired
    private StabilitySchemeMapper schemeMapper;

    @Autowired
    private StabilitySchemeVersionMapper schemeVersionMapper;

    @Autowired
    private StabilityPlanSampleMapper sampleMapper;

    @Autowired
    private StabilityPlanTimepointTaskMapper timepointTaskMapper;

    @Autowired
    private StabilitySchemePlanTimepointMapper schemePlanTimepointMapper;

    @Autowired
    private StabilitySchemePlanMapper schemePlanMapper;

    @Autowired
    private StabilitySchemePlanTimepointParamMapper timepointParamMapper;

    @Autowired
    private StabilitySchemeParameterMapper schemeParameterMapper;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private SampleMapper inspectSampleMapper; // lm_sample (regular inspection samples)

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskStatusHistoryMapper taskStatusHistoryMapper;

    @Autowired
    private PlatformCodeFeignClient platformCodeFeignClient;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private AuditOperationLogMapper auditOperationLogMapper;

    @Override
    public CommonPage<StabilityInspectPlanDTO> pageStabilityInspectPlan(StabilityInspectPlanQueryDTO queryDTO) {
        LambdaQueryWrapper<StabilityInspectPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getCode()), StabilityInspectPlan::getCode, queryDTO.getCode())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), StabilityInspectPlan::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getMaterialId() != null, StabilityInspectPlan::getMaterialId, queryDTO.getMaterialId())
                .in(CollUtil.isNotEmpty(queryDTO.getMaterialIds()), StabilityInspectPlan::getMaterialId, queryDTO.getMaterialIds())
                .orderByDesc(StabilityInspectPlan::getCreateTime);

        if (queryDTO.getCreateTimeStart() != null) {
            wrapper.ge(StabilityInspectPlan::getCreateTime, queryDTO.getCreateTimeStart());
        }
        if (queryDTO.getCreateTimeEnd() != null) {
            wrapper.le(StabilityInspectPlan::getCreateTime, queryDTO.getCreateTimeEnd());
        }

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<StabilityInspectPlan> planList = planMapper.selectList(wrapper);
        CommonPage<StabilityInspectPlan> entityPage = CommonPage.convertPage(planList);
        List<StabilityInspectPlanDTO> dtoList = BeanUtil.copyToList(planList, StabilityInspectPlanDTO.class);

        // 批量查询方案版本下的试验类型，翻译字典后回填
        List<Long> versionIds = planList.stream()
                .map(StabilityInspectPlan::getSchemeVersionId)
                .distinct().collect(Collectors.toList());
        if (!versionIds.isEmpty()) {
            LambdaQueryWrapper<StabilitySchemePlan> spWrapper = new LambdaQueryWrapper<>();
            spWrapper.in(StabilitySchemePlan::getVersionId, versionIds)
                    .eq(StabilitySchemePlan::getDeleted, false);
            List<StabilitySchemePlan> schemePlans = schemePlanMapper.selectList(spWrapper);

            // 查字典，构建 dictValue -> dictLabel 映射
            Map<String, String> codeLabelMap = new HashMap<>();
            try {
                DictDetailFeignVO dictDetail = FeignUtils.handleRequest(
                        data -> dictFeign.selectDictDetailByCode(data), DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
                if (dictDetail != null && dictDetail.getDictDataList() != null) {
                    dictDetail.getDictDataList().forEach(item ->
                            codeLabelMap.put(item.getDictValue(), item.getDictLabel()));
                }
            } catch (Exception e) {
                log.warn("查询试验类型字典失败，将使用原始code值", e);
            }

            // 按版本ID分组，code 翻译为中文后拼接
            Map<Long, String> versionExperimentMap = schemePlans.stream()
                    .filter(sp -> StrUtil.isNotBlank(sp.getExperimentType()))
                    .collect(Collectors.groupingBy(
                            StabilitySchemePlan::getVersionId,
                            Collectors.mapping(
                                    sp -> codeLabelMap.getOrDefault(sp.getExperimentType(), sp.getExperimentType()),
                                    Collectors.joining(","))
                    ));
            dtoList.forEach(dto -> dto.setExperimentTypeNames(
                    versionExperimentMap.get(dto.getSchemeVersionId())));
        }

        CommonPage<StabilityInspectPlanDTO> resultPage = new CommonPage<>();
        resultPage.setPageNum(entityPage.getPageNum());
        resultPage.setPageSize(entityPage.getPageSize());
        resultPage.setTotal(entityPage.getTotal());
        resultPage.setList(dtoList);
        return resultPage;
    }

    @Override
    public StabilityInspectPlanDetailDTO getStabilityInspectPlan(Long id) {
        StabilityInspectPlan plan = planMapper.selectById(id);
        if (plan == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_NOT_EXIST);
        }
        StabilityInspectPlanDetailDTO dto = BeanUtil.copyProperties(plan, StabilityInspectPlanDetailDTO.class);

        // 补充方案编码
        StabilityScheme scheme = schemeMapper.selectById(plan.getSchemeId());
        if (scheme != null) {
            dto.setSchemeCode(scheme.getCode());
        }

        // 创建人名称（姓名-登录名）
        BaseUserDO user = UserUtils.getUser(plan.getCreateBy());
        if (ObjectUtil.isNotEmpty(user)) {
            dto.setCreateByUserName(user.getUserName() + StrUtil.DASHED + user.getLoginName());
        }

        // 试验类型名称（字典翻译后逗号拼接）
        List<StabilitySchemePlan> schemePlans = schemePlanMapper.selectByVersionId(plan.getSchemeVersionId());
        Map<String, String> codeLabelMap = new HashMap<>();
        try {
            DictDetailFeignVO dictDetail = FeignUtils.handleRequest(
                    data -> dictFeign.selectDictDetailByCode(data), DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
            if (dictDetail != null && dictDetail.getDictDataList() != null) {
                dictDetail.getDictDataList().forEach(item ->
                        codeLabelMap.put(item.getDictValue(), item.getDictLabel()));
            }
        } catch (Exception e) {
            log.warn("查询试验类型字典失败，将使用原始code值", e);
        }
        String experimentTypeNames = schemePlans.stream()
                .filter(sp -> StrUtil.isNotBlank(sp.getExperimentType()))
                .map(sp -> codeLabelMap.getOrDefault(sp.getExperimentType(), sp.getExperimentType()))
                .distinct()
                .collect(Collectors.joining(","));
        dto.setExperimentTypeNames(experimentTypeNames);

        List<StabilityInspectPlanBatch> batches = batchMapper.selectByPlanId(id);
        dto.setBatches(BeanUtil.copyToList(batches, StabilityInspectPlanDetailDTO.BatchDTO.class));

        // 查询整体样品的接收时间范围
        List<StabilityPlanSample> samples = sampleMapper.selectByPlanId(id);
        LocalDateTime firstReceiveTime = null;
        LocalDateTime lastReceiveTime = null;
        boolean allReceived = !samples.isEmpty();
        for (StabilityPlanSample sample : samples) {
            if (sample.getReceiveTime() != null) {
                if (firstReceiveTime == null || sample.getReceiveTime().isBefore(firstReceiveTime)) {
                    firstReceiveTime = sample.getReceiveTime();
                }
                if (lastReceiveTime == null || sample.getReceiveTime().isAfter(lastReceiveTime)) {
                    lastReceiveTime = sample.getReceiveTime();
                }
            } else {
                allReceived = false;
            }
        }
        dto.setSampleFirstReceiveTime(firstReceiveTime);
        dto.setSampleLastReceiveTime(allReceived ? lastReceiveTime : null);

        dto.setMatrix(buildMatrix(plan, batches, codeLabelMap));

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveStabilityInspectPlan(StabilityInspectPlanSaveDTO saveDTO) {
        // 校验方案存在
        StabilityScheme scheme = schemeMapper.selectById(saveDTO.getSchemeId());
        if (scheme == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_NOT_EXIST);
        }

        // 校验方案版本存在且为生效状态
        StabilitySchemeVersion version = schemeVersionMapper.selectById(saveDTO.getSchemeVersionId());
        if (version == null) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
        }
        if (version.getStatus() != StabilitySchemeVersionStatusEnum.ACTIVE) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_NO_ACTIVE_SCHEME_VERSION);
        }

        String userId = SysUserHolder.getUser().getUserId();

        // 生成计划编号
        String code = generatePlanCode();

        // 创建计划
        StabilityInspectPlan plan = new StabilityInspectPlan();
        plan.setCode(code);
        plan.setSchemeId(saveDTO.getSchemeId());
        plan.setSchemeName(scheme.getName());
        plan.setSchemeVersionId(saveDTO.getSchemeVersionId());
        plan.setSchemeVersionNo(version.getVersionNo());
        plan.setMaterialId(saveDTO.getMaterialId());
        plan.setMaterialName(saveDTO.getMaterialName());
        plan.setMaterialCode(saveDTO.getMaterialCode());
        plan.setMaterialSpec(saveDTO.getMaterialSpec());
        plan.setStatus(StabilityInspectPlanStatusEnum.PENDING);
        plan.setRemark(saveDTO.getRemark());
        plan.setCreateBy(userId);
        planMapper.insert(plan);

        // 创建批次
        List<StabilityInspectPlanBatch> savedBatches = saveBatches(plan.getId(), saveDTO.getBatches(), userId);

        // 为每个批次×每个试验类型生成样品取样任务
        List<StabilitySchemePlan> schemePlans = schemePlanMapper.selectByVersionId(saveDTO.getSchemeVersionId());
        generatePlanSamples(plan.getId(), savedBatches, schemePlans, userId, plan.getMaterialName());

        log.info("新增稳定性考察计划成功：planId={}, code={}", plan.getId(), code);
        return plan.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStabilityInspectPlan(StabilityInspectPlanSaveDTO saveDTO) {
        StabilityInspectPlan plan = planMapper.selectById(saveDTO.getId());
        if (plan == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_NOT_EXIST);
        }
        if (plan.getStatus() != StabilityInspectPlanStatusEnum.PENDING) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_STATUS_ERROR);
        }

        String userId = SysUserHolder.getUser().getUserId();

        plan.setMaterialId(saveDTO.getMaterialId());
        plan.setMaterialName(saveDTO.getMaterialName());
        plan.setMaterialCode(saveDTO.getMaterialCode());
        plan.setMaterialSpec(saveDTO.getMaterialSpec());
        plan.setRemark(saveDTO.getRemark());
        plan.setUpdateBy(userId);
        planMapper.updateById(plan);

        // 全量刷新批次和样品
        batchMapper.deleteByPlanId(plan.getId());
        sampleMapper.deleteByPlanId(plan.getId());
        List<StabilityInspectPlanBatch> savedBatches = saveBatches(plan.getId(), saveDTO.getBatches(), userId);
        List<StabilitySchemePlan> schemePlans = schemePlanMapper.selectByVersionId(plan.getSchemeVersionId());
        generatePlanSamples(plan.getId(), savedBatches, schemePlans, userId, plan.getMaterialName());

        log.info("修改稳定性考察计划成功：planId={}", plan.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseStabilityInspectPlan(StabilityInspectPlanPauseDTO pauseDTO) {
        StabilityInspectPlan plan = planMapper.selectById(pauseDTO.getId());
        if (plan == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_NOT_EXIST);
        }
        if (plan.getStatus() != StabilityInspectPlanStatusEnum.IN_PROGRESS) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_CANNOT_PAUSE);
        }

        plan.setStatus(StabilityInspectPlanStatusEnum.PAUSED);
        plan.setPauseTime(LocalDateTime.now());
        plan.setPauseBy(SysUserHolder.getUser().getUserId());
        plan.setPauseByName(SysUserHolder.getUser().getUserName());
        plan.setPauseReason(pauseDTO.getPauseReason());
        plan.setUpdateBy(SysUserHolder.getUser().getUserId());
        planMapper.updateById(plan);

        log.info("暂停稳定性考察计划成功：planId={}", plan.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumeStabilityInspectPlan(Long id, Long schemeVersionId) {
        StabilityInspectPlan plan = planMapper.selectById(id);
        if (plan == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_NOT_EXIST);
        }
        if (plan.getStatus() != StabilityInspectPlanStatusEnum.PAUSED) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_CANNOT_RESUME);
        }

        // 可选：切换方案版本
        if (schemeVersionId != null) {
            StabilitySchemeVersion newVersion = schemeVersionMapper.selectById(schemeVersionId);
            if (newVersion == null) {
                throw new BmosException(LimsResponseCode.STABILITY_SCHEME_VERSION_NOT_EXIST);
            }
            if (newVersion.getStatus() != StabilitySchemeVersionStatusEnum.ACTIVE) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_NO_ACTIVE_SCHEME_VERSION);
            }
            plan.setSchemeVersionId(newVersion.getId());
            plan.setSchemeVersionNo(newVersion.getVersionNo());

            // 仅对尚未发起检验单的时间点任务重新对齐方案版本配置
            syncUnlaunchedTimepointTasksToNewVersion(plan.getId(), newVersion.getId());
        }

        plan.setStatus(StabilityInspectPlanStatusEnum.IN_PROGRESS);
        plan.setUpdateBy(SysUserHolder.getUser().getUserId());
        planMapper.updateById(plan);

        // 补触发暂停期间已到期但未创建检验单的时间点任务
        triggerOverdueTimepointTasksForPlan(id, SysUserHolder.getUser());

        log.info("恢复稳定性考察计划成功：planId={}", id);
    }

    /**
     * 切换方案版本时，将计划内尚未发起检验单（inspectionOrderId == null）的时间点任务
     * 重新对齐到新版本的 schemePlan / schemeTimepoint，并同步更新对应的 planSample.schemePlanId。
     * 已关联检验单的任务不受影响。
     */
    private void syncUnlaunchedTimepointTasksToNewVersion(Long planId, Long newVersionId) {
        List<StabilitySchemePlan> newSchemePlans = schemePlanMapper.selectByVersionId(newVersionId);
        if (newSchemePlans == null || newSchemePlans.isEmpty()) {
            return;
        }

        // 新版本 schemePlan 索引：experimentType+storageCondition → schemePlan
        Map<String, StabilitySchemePlan> schemePlanIndex = new HashMap<>();
        for (StabilitySchemePlan sp : newSchemePlans) {
            schemePlanIndex.put(sp.getExperimentType() + "\0" + sp.getStorageCondition(), sp);
        }

        // 新版本 schemeTimepoint 索引：schemePlanId → timepoints（排除第0月）
        Map<Long, List<StabilitySchemePlanTimepoint>> timepointsByPlanId = new HashMap<>();
        for (StabilitySchemePlan sp : newSchemePlans) {
            List<StabilitySchemePlanTimepoint> tps = schemePlanTimepointMapper.selectByPlanId(sp.getId());
            if (tps != null) {
                tps = tps.stream()
                        .filter(tp -> tp.getTimeValue() == null || tp.getTimeValue() != 0)
                        .collect(Collectors.toList());
            }
            timepointsByPlanId.put(sp.getId(), tps == null ? Collections.emptyList() : tps);
        }

        String userId = SysUserHolder.getUser().getUserId();
        StabilityInspectPlan plan = planMapper.selectById(planId);
        String materialName = plan != null ? plan.getMaterialName() : null;

        List<StabilityPlanSample> allSamples = sampleMapper.selectByPlanId(planId);
        for (StabilityPlanSample planSample : allSamples) {
            String key = planSample.getExperimentType() + "\0" + planSample.getStorageCondition();
            StabilitySchemePlan matchedPlan = schemePlanIndex.get(key);
            if (matchedPlan == null) {
                log.warn("恢复计划切换版本，样品未找到匹配的 schemePlan：sampleId={}, experimentType={}, storageCondition={}",
                        planSample.getId(), planSample.getExperimentType(), planSample.getStorageCondition());
                continue;
            }

            if (planSample.getStatus() == StabilityPlanSampleStatusEnum.PENDING) {
                // 未取样：删除旧 lm_sample + planSample，按新版本重建
                if (planSample.getSampleId() != null) {
                    inspectSampleMapper.deleteById(planSample.getSampleId());
                }
                sampleMapper.deleteById(planSample.getId());

                StabilityPlanSample newSample = new StabilityPlanSample();
                newSample.setPlanId(planId);
                newSample.setBatchId(planSample.getBatchId());
                newSample.setBatchNo(planSample.getBatchNo());
                newSample.setSchemePlanId(matchedPlan.getId());
                newSample.setExperimentType(matchedPlan.getExperimentType());
                newSample.setStorageCondition(matchedPlan.getStorageCondition());
                newSample.setPlannedSampleAmount(matchedPlan.getTotalSampleAmount());
                newSample.setSampleUnit(matchedPlan.getTotalSampleUnit() != null
                        ? matchedPlan.getTotalSampleUnit().toString() : null);
                newSample.setStatus(StabilityPlanSampleStatusEnum.PENDING);
                newSample.setCreateBy(userId);
                Sample lmSample = createLmSampleAndBindNo(newSample, materialName);
                sampleMapper.insert(newSample);
                lmSample.setStabilityPlanSampleId(newSample.getId());
                inspectSampleMapper.updateById(lmSample);

            } else if (planSample.getStatus() == StabilityPlanSampleStatusEnum.RECEIVED) {
                // 已接收：时间点任务删除未发起的，按新版本重建；样品记录完全不动
                timepointTaskMapper.deleteUnlaunchedBySampleId(planSample.getId());

                List<StabilitySchemePlanTimepoint> newTimepoints = timepointsByPlanId.get(matchedPlan.getId());
                // 已发起的任务 key 集合，避免重复创建
                List<StabilityPlanTimepointTask> launchedTasks = timepointTaskMapper.selectBySampleId(planSample.getId());
                Map<String, Boolean> launchedKeys = new HashMap<>();
                for (StabilityPlanTimepointTask t : launchedTasks) {
                    launchedKeys.put(t.getTimeValue() + "\0" + t.getTimeUnit(), true);
                }

                for (StabilitySchemePlanTimepoint newTp : newTimepoints) {
                    if (launchedKeys.containsKey(newTp.getTimeValue() + "\0" + newTp.getTimeUnit())) {
                        continue;
                    }
                    StabilityPlanTimepointTask newTask = new StabilityPlanTimepointTask();
                    newTask.setPlanId(planId);
                    newTask.setBatchId(planSample.getBatchId());
                    newTask.setSampleId(planSample.getId());
                    newTask.setSchemePlanId(matchedPlan.getId());
                    newTask.setSchemeTimepointId(newTp.getId());
                    newTask.setExperimentType(planSample.getExperimentType());
                    newTask.setStorageCondition(planSample.getStorageCondition());
                    newTask.setTimeValue(newTp.getTimeValue());
                    newTask.setTimeUnit(newTp.getTimeUnit());
                    newTask.setPlannedDate(calcPlannedDate(planSample.getReceiveDate(), newTp.getTimeValue(), newTp.getTimeUnit()));
                    newTask.setSampleAmount(newTp.getSampleAmount());
                    newTask.setSampleUnit(newTp.getSampleUnit());
                    newTask.setStatus(StabilityTimepointTaskStatusEnum.NOT_STARTED);
                    newTask.setCreateBy(userId);
                    timepointTaskMapper.insert(newTask);
                }
                // SAMPLED / 其他已取样状态：完全不动
            }
        }

        recalcPlanEndDate(planId, userId);
    }

    /**
     * 重新计算计划下所有时间点任务的最晚计划日期，并更新 lm_stability_inspect_plan.planEndDate。
     */
    private void recalcPlanEndDate(Long planId, String userId) {
        List<StabilityPlanTimepointTask> allTasks = timepointTaskMapper.selectByPlanId(planId);
        LocalDate maxPlannedDate = allTasks.stream()
                .map(StabilityPlanTimepointTask::getPlannedDate)
                .filter(d -> d != null)
                .max(LocalDate::compareTo)
                .orElse(null);
        if (maxPlannedDate == null) {
            return;
        }
        StabilityInspectPlan plan = planMapper.selectById(planId);
        if (plan != null && !maxPlannedDate.equals(plan.getPlanEndDate())) {
            plan.setPlanEndDate(maxPlannedDate);
            plan.setUpdateBy(userId);
            planMapper.updateById(plan);
        }
    }

    /**
     * 根据已接收的整体样品，生成时间点任务并更新计划状态/结束日期
     */
    private void generateTimepointTasksAndUpdatePlan(StabilityPlanSample sample, LocalDate receiveDate) {
        List<StabilitySchemePlanTimepoint> timepoints = schemePlanTimepointMapper.selectByPlanId(sample.getSchemePlanId());
        if (timepoints == null || timepoints.isEmpty()) {
            return;
        }
        String userId = SysUserHolder.getUser().getUserId();
        LocalDate maxPlannedDate = null;
        List<StabilityPlanTimepointTask> tasks = new ArrayList<>();
        for (StabilitySchemePlanTimepoint tp : timepoints) {
            if (tp.getTimeValue() != null && tp.getTimeValue() == 0) {
                continue;
            }
            StabilityPlanTimepointTask task = new StabilityPlanTimepointTask();
            task.setPlanId(sample.getPlanId());
            task.setBatchId(sample.getBatchId());
            task.setSampleId(sample.getId());
            task.setSchemePlanId(sample.getSchemePlanId());
            task.setSchemeTimepointId(tp.getId());
            task.setExperimentType(sample.getExperimentType());
            task.setStorageCondition(sample.getStorageCondition());
            task.setTimeValue(tp.getTimeValue());
            task.setTimeUnit(tp.getTimeUnit());
            task.setPlannedDate(calcPlannedDate(receiveDate, tp.getTimeValue(), tp.getTimeUnit()));
            task.setSampleAmount(tp.getSampleAmount());
            task.setSampleUnit(tp.getSampleUnit());
            task.setStatus(StabilityTimepointTaskStatusEnum.NOT_STARTED);
            task.setCreateBy(userId);
            tasks.add(task);
            if (maxPlannedDate == null || task.getPlannedDate().isAfter(maxPlannedDate)) {
                maxPlannedDate = task.getPlannedDate();
            }
        }
        tasks.forEach(timepointTaskMapper::insert);

        // 更新计划的开始日期、结束日期、状态
        StabilityInspectPlan plan = planMapper.selectById(sample.getPlanId());
        if (plan != null && maxPlannedDate != null) {
            if (plan.getStatus() != StabilityInspectPlanStatusEnum.IN_PROGRESS) {
                plan.setStatus(StabilityInspectPlanStatusEnum.IN_PROGRESS);
            }
            if (plan.getStartDate() == null || receiveDate.isBefore(plan.getStartDate())) {
                plan.setStartDate(receiveDate);
            }
            if (plan.getPlanEndDate() == null || maxPlannedDate.isAfter(plan.getPlanEndDate())) {
                plan.setPlanEndDate(maxPlannedDate);
            }
            planMapper.updateById(plan);
        }
    }

    private void triggerOverdueTimepointTasksForPlan(Long planId, SysUser user) {
        List<StabilityPlanTimepointTask> overdueTasks =
                timepointTaskMapper.selectDueTasksByPlanId(LocalDate.now(), planId);
        if (overdueTasks == null || overdueTasks.isEmpty()) {
            return;
        }
        int count = 0;
        for (StabilityPlanTimepointTask task : overdueTasks) {
            try {
                launchInspectionOrderForTimepointTask(task, user);
                count++;
            } catch (Exception e) {
                log.error("恢复计划补触发时间点任务失败：timepointTaskId={}, error={}",
                        task.getId(), e.getMessage(), e);
            }
        }
        log.info("恢复计划补触发到期时间点任务：planId={}, 共{}条，成功{}条", planId, overdueTasks.size(), count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int triggerDueTimepointTasks() {
        List<StabilityPlanTimepointTask> dueTasks = timepointTaskMapper.selectDueTasks(LocalDate.now());
        if (dueTasks == null || dueTasks.isEmpty()) {
            return 0;
        }

        SysUser user = SysUserHolder.getUser();
        if (StringUtils.isEmpty(user.getUserId())) {
            BaseUser systemUser = new BaseUser();
            systemUser.setUserId("system");
            systemUser.setUserName("system");
            user = systemUser;
        }
        int count = 0;

        for (StabilityPlanTimepointTask timepointTask : dueTasks) {
            try {
                launchInspectionOrderForTimepointTask(timepointTask, user);
                count++;
            } catch (Exception e) {
                log.error("稳定性时间点任务发起检验失败：timepointTaskId={}, error={}",
                        timepointTask.getId(), e.getMessage(), e);
            }
        }

        log.info("定时任务-触发到期时间点任务：本次处理{}条，成功{}条", dueTasks.size(), count);
        return count;
    }

    /**
     * 为一条到期时间点任务创建检验单、取样记录、生成样品，立即生成分析项任务，状态流转至 WAITING_SAMPLE。
     */
    private void launchInspectionOrderForTimepointTask(StabilityPlanTimepointTask timepointTask, SysUser user) {
        StabilityInspectPlan plan = planMapper.selectById(timepointTask.getPlanId());
        if (plan == null) {
            log.warn("稳定性考察计划不存在，跳过：planId={}", timepointTask.getPlanId());
            return;
        }

        StabilityInspectPlanBatch batch = batchMapper.selectById(timepointTask.getBatchId());
        if (batch == null) {
            log.warn("稳定性考察计划批次不存在，跳过：batchId={}", timepointTask.getBatchId());
            return;
        }

        StabilitySchemePlan schemePlan = schemePlanMapper.selectById(timepointTask.getSchemePlanId());
        if (schemePlan == null) {
            log.warn("稳定性方案检验计划不存在，跳过：schemePlanId={}", timepointTask.getSchemePlanId());
            return;
        }

        // 1. 创建检验单，直接置为 CONFIRMED（无模板、无常规方案绑定）
        NextCodeVO nextCodeVO = platformCodeFeignClient.getInspectOrderNextUseNo("INSPECTION_ORDER");
        InspectionOrder order = new InspectionOrder();
        order.setOrderNo(nextCodeVO.getNo());
        order.setMaterialId(plan.getMaterialId());
        order.setSchemeVersionId(plan.getSchemeVersionId());
        order.setBatchNo(batch.getBatchNo());
        if (batch.getProductionDate() != null) {
            order.setProductionDate(batch.getProductionDate().atStartOfDay());
        }
        order.setOrderStatus(InspectionOrderStatusEnum.CONFIRMED);
        order.setTemplateId(null);
        order.setSchemeSource(InspectionOrderSourceEnum.STABILITY);
        order.setCreateBy(user.getUserId());
        order.setUpdateBy(user.getUserId());
        inspectionOrderMapper.insert(order);
        platformCodeFeignClient.confirmInspectOrderNo(nextCodeVO.getCode(), nextCodeVO.getNo());

        // 2.5 为时间点检验单创建样品（未取样）
        createTimepointOrderSample(order, plan, timepointTask, user);

        // 2. 立即生成分析项检验任务（时间点取样后直接进入检测，无需单独接收）
        generateTimepointInspectionTasks(timepointTask, order.getId(), plan.getSchemeVersionId(), user);

        // 3. 回写时间点任务：关联检验单，状态流转至 WAITING_SAMPLE（等待检测取样）
        timepointTask.setInspectionOrderId(order.getId());
        timepointTask.setStatus(StabilityTimepointTaskStatusEnum.WAITING_SAMPLE);
        timepointTaskMapper.updateById(timepointTask);

        log.info("稳定性时间点任务检验单创建完成：timepointTaskId={}, orderId={}",
                timepointTask.getId(), order.getId());
    }

    /**
     * 为时间点任务生成分析项检验任务（Task），不改变时间点任务状态。
     */
    private void generateTimepointInspectionTasks(StabilityPlanTimepointTask timepointTask,
                                                  Long inspectionOrderId, Long schemeVersionId, SysUser user) {
        List<StabilitySchemePlanTimepointParam> timepointParams =
                timepointParamMapper.selectByTimepointId(timepointTask.getSchemeTimepointId());
        if (timepointParams == null || timepointParams.isEmpty()) {
            StabilitySchemePlanTimepoint schemeTimepoint =
                    schemePlanTimepointMapper.selectById(timepointTask.getSchemeTimepointId());
            if (schemeTimepoint == null || !Boolean.TRUE.equals(schemeTimepoint.getSelectAll())) {
                log.warn("稳定性时间点未配置分析项，跳过任务生成：schemeTimepointId={}",
                        timepointTask.getSchemeTimepointId());
                return;
            }
            List<StabilitySchemeParameterDTO> allParams =
                    schemeParameterMapper.selectByVersionIdWithNames(schemeVersionId);
            if (allParams == null || allParams.isEmpty()) {
                log.warn("稳定性方案版本未配置分析项，跳过任务生成：schemeVersionId={}", schemeVersionId);
                return;
            }
            timepointParams = allParams.stream().map(param -> {
                StabilitySchemePlanTimepointParam mapped = new StabilitySchemePlanTimepointParam();
                mapped.setParameterConfigId(param.getId());
                mapped.setParameterId(param.getParameterId());
                mapped.setParameterCode(param.getParameterCode());
                mapped.setItemConfigId(param.getItemConfigId());
                mapped.setInspectItemId(param.getInspectItemId());
                mapped.setInspectItemCode(param.getInspectItemCode());
                return mapped;
            }).collect(Collectors.toList());
        }
        LocalDateTime now = LocalDateTime.now();
        for (StabilitySchemePlanTimepointParam param : timepointParams) {
            StabilitySchemeParameter schemeParam = schemeParameterMapper.selectById(param.getParameterConfigId());
            if (schemeParam == null) {
                log.warn("稳定性方案分析项配置不存在，跳过：parameterConfigId={}", param.getParameterConfigId());
                continue;
            }
            Task task = new Task();
            task.setInspectionOrderId(inspectionOrderId);
            task.setSchemeVersionId(schemeVersionId);
            task.setInspectItemId(param.getInspectItemId());
            task.setInspectItemCode(param.getInspectItemCode());
            task.setItemConfigId(param.getItemConfigId());
            task.setParameterId(param.getParameterId());
            task.setParameterCode(param.getParameterCode());
            task.setParameterConfigId(param.getParameterConfigId());
            task.setIsExecutable(schemeParam.getIsExecutable());
            task.setIsReportable(schemeParam.getIsReportable());
            task.setExecuteMethod(schemeParam.getExecuteMethod());
            if (ExecuteMethodEnum.ELN.equals(schemeParam.getExecuteMethod())) {
                task.setRecordId(schemeParam.getRecordId());
                task.setRecordVersionId(schemeParam.getRecordVersionId());
                task.setRecordItemId(schemeParam.getRecordItemId());
            }
            task.setStatus(TaskStatusEnum.PENDING_ASSIGNMENT);
            task.setCreateBy(user.getUserId());
            task.setUpdateBy(user.getUserId());
            task.setCreateTime(now);
            task.setUpdateTime(now);
            taskMapper.insert(task);

            TaskStatusHistory history = new TaskStatusHistory();
            history.setTaskId(task.getId());
            history.setOperationType(TaskOperationTypeEnum.CREATE);
            history.setFromStatus(null);
            history.setToStatus(TaskStatusEnum.PENDING_ASSIGNMENT.getValue());
            history.setOperatorId(user.getUserId());
            history.setOperateTime(now);
            history.setCreateBy(user.getUserId());
            history.setUpdateBy(user.getUserId());
            history.setCreateTime(now);
            history.setUpdateTime(now);
            taskStatusHistoryMapper.insert(history);
        }
        log.info("稳定性时间点分析项任务生成完成：timepointTaskId={}, orderId={}, 任务数={}",
                timepointTask.getId(), inspectionOrderId, timepointParams.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onInspectionOrderFinished(Long inspectionOrderId) {
        StabilityPlanTimepointTask task = timepointTaskMapper.selectByInspectionOrderId(inspectionOrderId);
        if (task == null) {
            log.debug("检验单不属于稳定性考察，跳过回调：inspectionOrderId={}", inspectionOrderId);
            return;
        }
        if (task.getStatus() == StabilityTimepointTaskStatusEnum.COMPLETED) {
            return;
        }
        task.setStatus(StabilityTimepointTaskStatusEnum.COMPLETED);
        task.setCompletedDate(LocalDate.now());
        timepointTaskMapper.updateById(task);
        log.info("稳定性时间点任务标记完成：timepointTaskId={}, inspectionOrderId={}", task.getId(), inspectionOrderId);

        // 所有时间点任务完成后立即完结考察计划
        tryCompletePlan(task.getPlanId());
    }

    private void fillZeroMonthStatusFromDTO(StabilityInspectPlanMatrixDTO.TimepointTaskDTO taskDTO,
                                            com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO order) {
        if (order.getRequestTime() != null) {
            taskDTO.setPlannedDate(order.getRequestTime().toLocalDate());
        }
        InspectionOrderStatusEnum status = order.getOrderStatus();
        if (InspectionOrderStatusEnum.COMPLETED.equals(status) || Boolean.TRUE.equals(order.getFinished())) {
            taskDTO.setStatus(StabilityTimepointTaskStatusEnum.COMPLETED);
            taskDTO.setCompletedDate(order.getFinishedTime() != null
                    ? order.getFinishedTime().toLocalDate() : null);
        } else if (InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING.equals(status)
                || InspectionOrderStatusEnum.SAMPLE_AUDIT_REJECTED.equals(status)
                || (InspectionOrderStatusEnum.CONFIRMED.equals(status) && Boolean.TRUE.equals(order.getHasSampled()))) {
            taskDTO.setStatus(StabilityTimepointTaskStatusEnum.IN_PROGRESS);
        } else if (InspectionOrderStatusEnum.CONFIRMED.equals(status)) {
            taskDTO.setStatus(StabilityTimepointTaskStatusEnum.WAITING_SAMPLE);
        } else if (InspectionOrderStatusEnum.TERMINATED.equals(status)) {
            taskDTO.setStatus(StabilityTimepointTaskStatusEnum.TERMINATED);
        } else {
            taskDTO.setStatus(StabilityTimepointTaskStatusEnum.NOT_STARTED);
        }
    }

    /**
     * 拼接方案信息（格式：方案名称-版本号，任一为空则只返回非空部分，都为空返回 null）
     */
    private String buildSchemeInfo(String schemeName, String schemeVersion) {
        boolean hasName = schemeName != null && !schemeName.isEmpty();
        boolean hasVersion = schemeVersion != null && !schemeVersion.isEmpty();
        if (hasName && hasVersion) {
            return schemeName + "-" + schemeVersion;
        }
        if (hasName) {
            return schemeName;
        }
        if (hasVersion) {
            return schemeVersion;
        }
        return null;
    }

    /**
     * 检查计划下所有时间点任务是否均已完成，若是则立即将计划标记为已完成。
     * 由 onInspectionOrderFinished 触发。
     */
    private void tryCompletePlan(Long planId) {
        StabilityInspectPlan plan = planMapper.selectById(planId);
        if (plan == null || plan.getStatus() != StabilityInspectPlanStatusEnum.IN_PROGRESS) {
            return;
        }
        long totalCount = timepointTaskMapper.selectCount(
                new LambdaQueryWrapper<StabilityPlanTimepointTask>()
                        .eq(StabilityPlanTimepointTask::getPlanId, planId));
        if (totalCount == 0) {
            return;
        }
        long incompleteCount = timepointTaskMapper.selectCount(
                new LambdaQueryWrapper<StabilityPlanTimepointTask>()
                        .eq(StabilityPlanTimepointTask::getPlanId, planId)
                        .ne(StabilityPlanTimepointTask::getStatus, StabilityTimepointTaskStatusEnum.COMPLETED));
        if (incompleteCount > 0) {
            return;
        }
        // 检查0月检验单是否完成
        if (!isAllZeroMonthOrdersFinished(planId)) {
            log.info("稳定性考察计划非0月任务已全部完成，但存在未完成的0月检验单，计划暂不标记完成：planId={}", planId);
            return;
        }
        plan.setStatus(StabilityInspectPlanStatusEnum.COMPLETED);
        planMapper.updateById(plan);
        sampleMapper.updateSampleStatusByPlanId(planId,
                StabilityPlanSampleStatusEnum.RECEIVED,
                StabilityPlanSampleStatusEnum.PENDING_DESTROY);
        log.info("稳定性考察计划所有时间点任务已完成，计划标记为已完成：planId={}", planId);
    }

    /**
     * 检查该计划下所有批次的0月检验单是否均已完成。
     * - 批次只有一条常规检验单：直接判断该单是否 finished
     * - 批次有多条常规检验单：必须已选择（zeroMonthOrderId 不为空）且选中的单已 finished
     * - 批次没有常规检验单：视为0月未完成，返回 false
     */
    private boolean isAllZeroMonthOrdersFinished(Long planId) {
        List<StabilityInspectPlanBatch> batches = batchMapper.selectByPlanId(planId);
        if (batches == null || batches.isEmpty()) {
            return true;
        }
        StabilityInspectPlan plan = planMapper.selectById(planId);
        for (StabilityInspectPlanBatch batch : batches) {
            List<InspectionOrder> orders = inspectionOrderMapper.selectByBatchNoAndMaterialId(
                    batch.getBatchNo(), plan.getMaterialId());
            if (orders == null || orders.isEmpty()) {
                return false;
            }
            InspectionOrder targetOrder;
            if (orders.size() == 1) {
                targetOrder = orders.get(0);
            } else {
                if (batch.getZeroMonthOrderId() == null) {
                    return false;
                }
                targetOrder = orders.stream()
                        .filter(o -> batch.getZeroMonthOrderId().equals(o.getId()))
                        .findFirst().orElse(null);
                if (targetOrder == null) {
                    return false;
                }
            }
            if (!Boolean.TRUE.equals(targetOrder.getFinished())) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawPlan(Long id) {
        StabilityInspectPlan plan = planMapper.selectById(id);
        if (plan == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_NOT_EXIST);
        }
        if (plan.getStatus() != StabilityInspectPlanStatusEnum.PENDING) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_CANNOT_WITHDRAW);
        }
        // 物理删除时间点任务（若有）和样品（若有），然后软删除计划及批次
        timepointTaskMapper.deleteByPlanId(id);
        sampleMapper.deleteByPlanId(id);
        batchMapper.deleteByPlanId(id);
        planMapper.deleteById(id);
        log.info("稳定性考察计划已撤销：planId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectZeroMonthOrder(Long batchId, Long inspectionOrderId) {
        StabilityInspectPlanBatch batch = batchMapper.selectById(batchId);
        if (batch == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "批次不存在");
        }
        StabilityInspectPlan plan = planMapper.selectById(batch.getPlanId());
        if (plan == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_NOT_EXIST);
        }
        // 校验该检验单确实属于该批号+检品的常规检验单
        List<InspectionOrder> orders = inspectionOrderMapper.selectByBatchNoAndMaterialId(
                batch.getBatchNo(), plan.getMaterialId());
        boolean valid = orders != null && orders.stream().anyMatch(o -> inspectionOrderId.equals(o.getId()));
        if (!valid) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "所选检验单不属于该批次的常规检验单");
        }
        batch.setZeroMonthOrderId(inspectionOrderId);
        batchMapper.updateById(batch);
        log.info("批次0月检验单已选择：batchId={}, inspectionOrderId={}", batchId, inspectionOrderId);
    }

    @Override
    public List<StabilityOrderSampleDTO> listOrderSamples(Long timepointTaskId) {
        StabilityPlanTimepointTask task = timepointTaskMapper.selectById(timepointTaskId);
        if (task == null || task.getInspectionOrderId() == null) {
            return new ArrayList<>();
        }
        List<Sample> samples = inspectSampleMapper.selectByInspectionOrderId(task.getInspectionOrderId());
        if (samples == null || samples.isEmpty()) {
            return new ArrayList<>();
        }
        StabilityInspectPlanBatch batch = batchMapper.selectById(task.getBatchId());
        String batchNo = batch != null ? batch.getBatchNo() : null;

        return samples.stream().map(s -> {
            StabilityOrderSampleDTO dto = new StabilityOrderSampleDTO();
            dto.setSampleId(s.getId());
            dto.setSampleNo(s.getSampleNo());
            dto.setSampled(s.getSampled());
            dto.setReceived(s.getReceived());
            dto.setSamplerName(s.getSamplerName());
            dto.setSamplingTime(s.getSamplingTime());
            dto.setReceiverName(s.getReceiverName());
            dto.setReceiveTime(s.getReceiveTime());
            dto.setPlanQuantity(s.getPlanQuantity());
            dto.setUnitId(s.getUnitId());
            dto.setTimepointTaskId(task.getId());
            dto.setInspectionOrderId(task.getInspectionOrderId());
            dto.setExperimentType(task.getExperimentType());
            dto.setStorageCondition(task.getStorageCondition());
            dto.setTimeValue(task.getTimeValue());
            dto.setTimeUnit(task.getTimeUnit());
            dto.setPlannedDate(task.getPlannedDate());
            dto.setTaskStatus(task.getStatus());
            dto.setBatchNo(batchNo);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void takeStabilitySample(Long timepointTaskId, String samplerName, String samplerId) {
        StabilityPlanTimepointTask task = timepointTaskMapper.selectById(timepointTaskId);
        if (task == null) {
            throw new BmosException(LimsResponseCode.STABILITY_TIMEPOINT_TASK_NOT_EXIST);
        }
        if (task.getStatus() != StabilityTimepointTaskStatusEnum.WAITING_SAMPLE) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_STATUS_ERROR);
        }
        List<Sample> samples = inspectSampleMapper.selectByInspectionOrderId(task.getInspectionOrderId());
        if (samples == null || samples.isEmpty()) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        LocalDateTime now = LocalDateTime.now();
        for (Sample sample : samples) {
            // 取样即接收，接收信息与取样人保持一致
            sample.setSampled(true);
            sample.setSamplerName(samplerName);
            sample.setSamplerId(samplerId);
            sample.setSamplingTime(now);
            sample.setReceived(true);
            sample.setReceiverName(samplerName);
            sample.setReceiverId(samplerId);
            sample.setReceiveTime(now);
            inspectSampleMapper.updateById(sample);
        }
        // 状态流转至 IN_PROGRESS（已取样，检测进行中）
        task.setStatus(StabilityTimepointTaskStatusEnum.IN_PROGRESS);
        timepointTaskMapper.updateById(task);
        log.info("稳定性时间点样品取样完成：timepointTaskId={}, 取样人={}", timepointTaskId, samplerName);
    }

    /**
     * 构建详情页矩阵：试验类型分组 × 批次行 × 时间点列
     * <p>第0月单元格不来自时间点任务，而是从相同批号的常规检验单中查询。</p>
     */
    private StabilityInspectPlanMatrixDTO buildMatrix(StabilityInspectPlan plan,
                                                      List<StabilityInspectPlanBatch> batches,
                                                      Map<String, String> codeLabelMap) {
        List<StabilitySchemePlan> schemePlans = schemePlanMapper.selectByVersionId(plan.getSchemeVersionId());
        if (schemePlans == null || schemePlans.isEmpty()) {
            return null;
        }

        List<StabilityInspectPlanMatrixDTO.ExperimentGroupDTO> groups = new ArrayList<>();

        for (StabilitySchemePlan schemePlan : schemePlans) {
            List<StabilitySchemePlanTimepoint> timepoints =
                    schemePlanTimepointMapper.selectByPlanId(schemePlan.getId());
            if (timepoints == null || timepoints.isEmpty()) {
                continue;
            }

            StabilityInspectPlanMatrixDTO.ExperimentGroupDTO group =
                    new StabilityInspectPlanMatrixDTO.ExperimentGroupDTO();
            group.setSchemePlanId(schemePlan.getId());
            group.setExperimentType(schemePlan.getExperimentType());
            group.setExperimentTypeName(codeLabelMap.getOrDefault(schemePlan.getExperimentType(), schemePlan.getExperimentType()));
            group.setStorageCondition(schemePlan.getStorageCondition());

            // 构建时间点列头
            List<StabilityInspectPlanMatrixDTO.TimepointHeaderDTO> headers = new ArrayList<>();
            for (StabilitySchemePlanTimepoint tp : timepoints) {
                StabilityInspectPlanMatrixDTO.TimepointHeaderDTO header =
                        new StabilityInspectPlanMatrixDTO.TimepointHeaderDTO();
                header.setSchemeTimepointId(tp.getId());
                header.setTimeValue(tp.getTimeValue());
                header.setTimeUnit(tp.getTimeUnit());
                headers.add(header);
            }
            group.setTimepoints(headers);

            // 构建批次行
            List<StabilityInspectPlanMatrixDTO.BatchRowDTO> batchRows = new ArrayList<>();
            for (StabilityInspectPlanBatch batch : batches) {
                StabilityInspectPlanMatrixDTO.BatchRowDTO row =
                        new StabilityInspectPlanMatrixDTO.BatchRowDTO();
                row.setBatchId(batch.getId());
                row.setBatchNo(batch.getBatchNo());
                row.setProductionDate(batch.getProductionDate());
                row.setSampleReceiveDate(batch.getSampleReceiveDate());

                StabilityPlanSample sample = sampleMapper.selectByPlanIdAndBatchIdAndSchemePlanId(
                        plan.getId(), batch.getId(), schemePlan.getId());
                if (sample != null) {
                    row.setSampleId(sample.getId());
                    row.setSampleStatus(sample.getStatus());
                    row.setPlannedSampleAmount(sample.getPlannedSampleAmount());
                }

                // 构建各时间点任务单元格
                List<StabilityInspectPlanMatrixDTO.TimepointTaskDTO> tasks = new ArrayList<>();
                for (StabilitySchemePlanTimepoint tp : timepoints) {
                    StabilityInspectPlanMatrixDTO.TimepointTaskDTO taskDTO =
                            new StabilityInspectPlanMatrixDTO.TimepointTaskDTO();
                    taskDTO.setSchemeTimepointId(tp.getId());

                    if (tp.getTimeValue() != null && tp.getTimeValue() == 0) {
                        // 第0月：查相同批号的常规检验单（带关联信息）
                        List<com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO> orders =
                                inspectionOrderMapper.selectCandidatesByBatchNoAndMaterialId(
                                        batch.getBatchNo(), plan.getMaterialId());
                        if (orders != null && !orders.isEmpty()) {
                            if (orders.size() == 1) {
                                // 唯一一条，直接使用
                                com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO order = orders.get(0);
                                taskDTO.setInspectionOrderId(order.getId());
                                fillZeroMonthStatusFromDTO(taskDTO, order);
                            } else {
                                // 多条：始终构建候选列表（支持用户随时重新选择）
                                List<StabilityInspectPlanMatrixDTO.ZeroMonthCandidateDTO> candidates = new java.util.ArrayList<>();
                                for (com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO o : orders) {
                                    StabilityInspectPlanMatrixDTO.ZeroMonthCandidateDTO c = new StabilityInspectPlanMatrixDTO.ZeroMonthCandidateDTO();
                                    c.setId(o.getId());
                                    c.setOrderNo(o.getOrderNo());
                                    c.setBatchNo(o.getBatchNo());
                                    c.setOrderStatus(o.getOrderStatus());
                                    c.setRequestUserName(o.getRequestUserName());
                                    c.setRequestTime(o.getRequestTime());
                                    c.setSchemeInfo(buildSchemeInfo(o.getSchemeName(), o.getSchemeVersion()));
                                    candidates.add(c);
                                }
                                taskDTO.setCandidateOrders(candidates);
                                // 若已选择，同时填充选中的检验单ID和状态
                                Long selectedOrderId = batch.getZeroMonthOrderId();
                                if (selectedOrderId != null) {
                                    com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO selectedOrder = orders.stream()
                                            .filter(o -> selectedOrderId.equals(o.getId()))
                                            .findFirst().orElse(null);
                                    if (selectedOrder != null) {
                                        taskDTO.setInspectionOrderId(selectedOrder.getId());
                                        fillZeroMonthStatusFromDTO(taskDTO, selectedOrder);
                                    } else {
                                        taskDTO.setStatus(StabilityTimepointTaskStatusEnum.NOT_STARTED);
                                    }
                                } else {
                                    taskDTO.setStatus(StabilityTimepointTaskStatusEnum.NOT_STARTED);
                                }
                            }
                        } else {
                            taskDTO.setStatus(StabilityTimepointTaskStatusEnum.NOT_STARTED);
                        }
                    } else {
                        // 非0月：直接按 planId+batchId+schemeTimepointId 查任务，
                        // plannedDate 在整体样品接收时已算好存入数据库，无需重新推算
                        StabilityPlanTimepointTask task =
                                timepointTaskMapper.selectByPlanIdAndBatchIdAndTimepoint(
                                        plan.getId(), batch.getId(),
                                        schemePlan.getExperimentType(), schemePlan.getStorageCondition(),
                                        tp.getTimeValue(), tp.getTimeUnit());
                        if (task != null) {
                            taskDTO.setTaskId(task.getId());
                            taskDTO.setPlannedDate(task.getPlannedDate());
                            taskDTO.setStatus(task.getStatus());
                            taskDTO.setInspectionOrderId(task.getInspectionOrderId());
                            taskDTO.setCompletedDate(task.getCompletedDate());
                        } else {
                            taskDTO.setStatus(StabilityTimepointTaskStatusEnum.NOT_STARTED);
                        }
                    }
                    tasks.add(taskDTO);
                }
                row.setTasks(tasks);
                batchRows.add(row);
            }
            group.setBatchRows(batchRows);
            groups.add(group);
        }

        StabilityInspectPlanMatrixDTO matrix = new StabilityInspectPlanMatrixDTO();
        matrix.setExperimentGroups(groups);
        return matrix;
    }

    private LocalDate calcPlannedDate(LocalDate receiveDate, Integer timeValue, String timeUnit) {
        if (timeValue == null || timeUnit == null) {
            return receiveDate;
        }
        switch (timeUnit.toUpperCase()) {
            case "DAY":
                return receiveDate.plusDays(timeValue);
            case "WEEK":
                return receiveDate.plusWeeks(timeValue);
            case "MONTH":
                return receiveDate.plusMonths(timeValue);
            case "YEAR":
                return receiveDate.plusYears(timeValue);
            default:
                return receiveDate.plusDays(timeValue);
        }
    }

    private List<StabilityInspectPlanBatch> saveBatches(Long planId, List<StabilityInspectPlanSaveDTO.BatchDTO> batchDTOs, String userId) {
        if (batchDTOs == null || batchDTOs.isEmpty()) {
            return new ArrayList<>();
        }
        List<StabilityInspectPlanBatch> batches = new ArrayList<>();
        for (StabilityInspectPlanSaveDTO.BatchDTO dto : batchDTOs) {
            StabilityInspectPlanBatch batch = new StabilityInspectPlanBatch();
            batch.setPlanId(planId);
            batch.setBatchNo(dto.getBatchNo());
            batch.setProductionDate(dto.getProductionDate());
            batch.setCreateBy(userId);
            batches.add(batch);
        }
        batches.forEach(batchMapper::insert);
        return batches;
    }

    private void generatePlanSamples(Long planId, List<StabilityInspectPlanBatch> batches,
                                     List<StabilitySchemePlan> schemePlans, String userId, String materialName) {
        if (batches == null || batches.isEmpty() || schemePlans == null || schemePlans.isEmpty()) {
            return;
        }
        for (StabilityInspectPlanBatch batch : batches) {
            for (StabilitySchemePlan schemePlan : schemePlans) {
                StabilityPlanSample sample = new StabilityPlanSample();
                sample.setPlanId(planId);
                sample.setBatchId(batch.getId());
                sample.setBatchNo(batch.getBatchNo());
                sample.setSchemePlanId(schemePlan.getId());
                sample.setExperimentType(schemePlan.getExperimentType());
                sample.setStorageCondition(schemePlan.getStorageCondition());
                sample.setPlannedSampleAmount(schemePlan.getTotalSampleAmount());
                sample.setSampleUnit(schemePlan.getTotalSampleUnit() != null
                        ? schemePlan.getTotalSampleUnit().toString() : null);
                sample.setStatus(StabilityPlanSampleStatusEnum.PENDING);
                sample.setCreateBy(userId);
                // 先建 lm_sample，sampleId 写入 sample 对象后再 insert
                Sample lmSample = createLmSampleAndBindNo(sample, materialName);
                sampleMapper.insert(sample);
                // 回写 lm_sample 的 stabilityPlanSampleId
                lmSample.setStabilityPlanSampleId(sample.getId());
                inspectSampleMapper.updateById(lmSample);
            }
        }
    }

    /**
     * 为稳定性整体样品创建 lm_sample 记录（sampled=false），生成并回写样品编号。
     * 计划创建、手动新增样品时调用，保证样品从一开始就有编号。
     */
    /**
     * 为稳定性整体样品生成 lm_sample 并设置 sampleId/sampleNo。
     * 必须在 sampleMapper.insert(planSample) 之前调用，确保 planSample 插入时 sample_id 已有值。
     * 调用后再 insert planSample，然后通过 lmSample 更新 stabilityPlanSampleId 完成双向关联。
     *
     * @return 已插入的 lm_sample 实体（供后续回写 stabilityPlanSampleId 使用）
     */
    private Sample createLmSampleAndBindNo(StabilityPlanSample planSample, String materialName) {
        NextCodeVO nextCodeVO = platformCodeFeignClient.getInspectOrderNextUseNo("SAMPLE_NO");
        Sample lmSample = new Sample();
        lmSample.setSampleNo(nextCodeVO.getNo());
        lmSample.setSampleName(materialName != null ? materialName : planSample.getBatchNo());
        lmSample.setSampled(false);
        lmSample.setReceived(false);
        lmSample.setDivided(false);
        lmSample.setCollected(false);
        lmSample.setDiscarded(false);
        lmSample.setDestroyed(false);
        lmSample.setPlanQuantity(planSample.getPlannedSampleAmount());
        if (planSample.getSampleUnit() != null) {
            try {
                lmSample.setUnitId(Long.parseLong(planSample.getSampleUnit()));
            } catch (NumberFormatException ignored) {
            }
        }
        inspectSampleMapper.insert(lmSample);
        platformCodeFeignClient.confirmInspectOrderNo(nextCodeVO.getCode(), nextCodeVO.getNo());
        // 在 planSample insert 之前把 sampleId/sampleNo 写入对象，确保不出现 null
        planSample.setSampleId(lmSample.getId());
        planSample.setSampleNo(lmSample.getSampleNo());
        return lmSample;
    }

    private void createTimepointOrderSample(InspectionOrder order,
                                            StabilityInspectPlan plan,
                                            StabilityPlanTimepointTask timepointTask,
                                            SysUser user) {
        NextCodeVO nextCodeVO = platformCodeFeignClient.getInspectOrderNextUseNo("SAMPLE_NO");
        Sample lmSample = new Sample();
        lmSample.setSampleNo(nextCodeVO.getNo());
        lmSample.setSampleName(plan.getMaterialName());
        lmSample.setInspectionOrderId(order.getId());
        lmSample.setSampled(false);
        lmSample.setReceived(false);
        lmSample.setDivided(false);
        lmSample.setCollected(false);
        lmSample.setDiscarded(false);
        lmSample.setDestroyed(false);
        lmSample.setPlanQuantity(timepointTask.getSampleAmount());
        if (StrUtil.isNotBlank(timepointTask.getSampleUnit())) {
            try {
                lmSample.setUnitId(Long.parseLong(timepointTask.getSampleUnit()));
            } catch (NumberFormatException ignored) {
            }
        }
        lmSample.setCreateBy(user.getUserId());
        lmSample.setUpdateBy(user.getUserId());
        inspectSampleMapper.insert(lmSample);
        platformCodeFeignClient.confirmInspectOrderNo(nextCodeVO.getCode(), nextCodeVO.getNo());
    }

    private String generatePlanCode() {
        return platformCodeFeignClient.getNextNo("STABILITY_INSPECT_PLAN_NO",
                com.bmos.lims2.common.enums.CodeRuleTypeEnum.STABILITY_INSPECT_PLAN_NO);
    }

    // ══════════════════════ 整体样品管理实现 ══════════════════════

    @Override
    public long countPendingOverallSamples() {
        // 统计至少有一个待取样样品的批次数
        return sampleMapper.selectPendingBatchIds().size();
    }

    @Override
    public CommonPage<StabilityOverallSampleDTO> pageOverallSamples(StabilityOverallSampleQueryDTO queryDTO) {
        // 仅显示至少有一个待取样样品的批次
        List<Long> pendingBatchIds = sampleMapper.selectPendingBatchIds();
        if (pendingBatchIds.isEmpty()) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, queryDTO);
        }

        // 按计划维度过滤（计划编号、检品ID、请验时间范围）
        boolean needPlanFilter = StrUtil.isNotBlank(queryDTO.getPlanCode())
                || queryDTO.getMaterialId() != null
                || queryDTO.getCreateTimeStart() != null
                || queryDTO.getCreateTimeEnd() != null;

        List<Long> planIds = null;
        if (needPlanFilter) {
            LambdaQueryWrapper<StabilityInspectPlan> planWrapper = new LambdaQueryWrapper<>();
            planWrapper.like(StrUtil.isNotBlank(queryDTO.getPlanCode()),
                    StabilityInspectPlan::getCode, queryDTO.getPlanCode())
                    .eq(queryDTO.getMaterialId() != null,
                            StabilityInspectPlan::getMaterialId, queryDTO.getMaterialId())
                    .ge(queryDTO.getCreateTimeStart() != null,
                            StabilityInspectPlan::getCreateTime, queryDTO.getCreateTimeStart())
                    .le(queryDTO.getCreateTimeEnd() != null,
                            StabilityInspectPlan::getCreateTime, queryDTO.getCreateTimeEnd());
            planIds = planMapper.selectList(planWrapper).stream()
                    .map(StabilityInspectPlan::getId).collect(Collectors.toList());
            if (planIds.isEmpty()) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, queryDTO);
            }
        }

        LambdaQueryWrapper<StabilityInspectPlanBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StabilityInspectPlanBatch::getId, pendingBatchIds);
        if (StrUtil.isNotBlank(queryDTO.getBatchNo())) {
            wrapper.like(StabilityInspectPlanBatch::getBatchNo, queryDTO.getBatchNo());
        }
        if (planIds != null) {
            wrapper.in(StabilityInspectPlanBatch::getPlanId, planIds);
        }
        wrapper.orderByDesc(StabilityInspectPlanBatch::getCreateTime);

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<StabilityInspectPlanBatch> batches = batchMapper.selectList(wrapper);
        CommonPage<StabilityInspectPlanBatch> entityPage = CommonPage.convertPage(batches);

        if (batches.isEmpty()) {
            CommonPage<StabilityOverallSampleDTO> empty = new CommonPage<>();
            empty.setPageNum(entityPage.getPageNum());
            empty.setPageSize(entityPage.getPageSize());
            empty.setTotal(entityPage.getTotal());
            empty.setList(Collections.emptyList());
            return empty;
        }

        // 批量加载计划信息
        List<Long> batchPlanIds = batches.stream().map(StabilityInspectPlanBatch::getPlanId)
                .distinct().collect(Collectors.toList());
        Map<Long, StabilityInspectPlan> planMap = planMapper.selectBatchIds(batchPlanIds).stream()
                .collect(Collectors.toMap(StabilityInspectPlan::getId, p -> p));

        // 批量加载各批次的样品状态（用于确定操作按钮）
        List<Long> batchIds = batches.stream().map(StabilityInspectPlanBatch::getId)
                .collect(Collectors.toList());
        List<StabilityPlanSample> batchSamples = sampleMapper.selectList(
                new LambdaQueryWrapper<StabilityPlanSample>()
                        .in(StabilityPlanSample::getBatchId, batchIds)
                        .select(StabilityPlanSample::getBatchId, StabilityPlanSample::getStatus));
        // 按 batchId 聚合：PENDING > SAMPLED > RECEIVED
        Map<Long, StabilityPlanSampleStatusEnum> batchStatusMap = new HashMap<>();
        for (StabilityPlanSample s : batchSamples) {
            batchStatusMap.merge(s.getBatchId(), s.getStatus(), (existing, incoming) -> {
                // PENDING 优先级最高，其次 SAMPLED，再 RECEIVED
                if (existing == StabilityPlanSampleStatusEnum.PENDING
                        || incoming == StabilityPlanSampleStatusEnum.PENDING) {
                    return StabilityPlanSampleStatusEnum.PENDING;
                }
                if (existing == StabilityPlanSampleStatusEnum.SAMPLED
                        || incoming == StabilityPlanSampleStatusEnum.SAMPLED) {
                    return StabilityPlanSampleStatusEnum.SAMPLED;
                }
                return StabilityPlanSampleStatusEnum.RECEIVED;
            });
        }

        List<StabilityOverallSampleDTO> dtoList = batches.stream().map(batch -> {
            StabilityOverallSampleDTO dto = new StabilityOverallSampleDTO();
            dto.setBatchId(batch.getId());
            dto.setBatchNo(batch.getBatchNo());
            dto.setProductionDate(batch.getProductionDate());
            dto.setBatchSampleStatus(batchStatusMap.get(batch.getId()));
            StabilityInspectPlan plan = planMap.get(batch.getPlanId());
            if (plan != null) {
                dto.setPlanId(plan.getId());
                dto.setPlanCode(plan.getCode());
                dto.setMaterialName(plan.getMaterialName());
                dto.setMaterialCode(plan.getMaterialCode());
                dto.setMaterialSpec(plan.getMaterialSpec());
                dto.setPlanCreateBy(plan.getCreateBy());
                dto.setPlanCreateTime(plan.getCreateTime());
            }
            return dto;
        }).collect(Collectors.toList());

        CommonPage<StabilityOverallSampleDTO> resultPage = new CommonPage<>();
        resultPage.setPageNum(entityPage.getPageNum());
        resultPage.setPageSize(entityPage.getPageSize());
        resultPage.setTotal(entityPage.getTotal());
        resultPage.setList(dtoList);
        return resultPage;
    }

    @Override
    public List<Long> getSampledBatchIds() {
        return sampleMapper.selectSampledBatchIds();
    }

    @Override
    public CommonPage<StabilityOverallSampleDTO> pageOverallSamplesForReceive(StabilityOverallSampleQueryDTO queryDTO) {
        // 先取出批次状态为"已取样待接收"的批次ID（无PENDING、有SAMPLED）
        List<Long> sampledBatchIds = sampleMapper.selectSampledBatchIds();
        if (sampledBatchIds.isEmpty()) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, queryDTO);
        }

        // 按计划维度过滤
        boolean needPlanFilter = StrUtil.isNotBlank(queryDTO.getPlanCode())
                || queryDTO.getMaterialId() != null
                || CollUtil.isNotEmpty(queryDTO.getMaterialIds())
                || queryDTO.getCreateTimeStart() != null
                || queryDTO.getCreateTimeEnd() != null;

        List<Long> planIds = null;
        if (needPlanFilter) {
            LambdaQueryWrapper<StabilityInspectPlan> planWrapper = new LambdaQueryWrapper<>();
            planWrapper.like(StrUtil.isNotBlank(queryDTO.getPlanCode()),
                    StabilityInspectPlan::getCode, queryDTO.getPlanCode())
                    .eq(queryDTO.getMaterialId() != null,
                            StabilityInspectPlan::getMaterialId, queryDTO.getMaterialId())
                    .in(CollUtil.isNotEmpty(queryDTO.getMaterialIds()),
                            StabilityInspectPlan::getMaterialId, queryDTO.getMaterialIds())
                    .ge(queryDTO.getCreateTimeStart() != null,
                            StabilityInspectPlan::getCreateTime, queryDTO.getCreateTimeStart())
                    .le(queryDTO.getCreateTimeEnd() != null,
                            StabilityInspectPlan::getCreateTime, queryDTO.getCreateTimeEnd());
            planIds = planMapper.selectList(planWrapper).stream()
                    .map(StabilityInspectPlan::getId).collect(Collectors.toList());
            if (planIds.isEmpty()) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, queryDTO);
            }
        }

        LambdaQueryWrapper<StabilityInspectPlanBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StabilityInspectPlanBatch::getId, sampledBatchIds);
        if (StrUtil.isNotBlank(queryDTO.getBatchNo())) {
            wrapper.like(StabilityInspectPlanBatch::getBatchNo, queryDTO.getBatchNo());
        }
        if (planIds != null) {
            wrapper.in(StabilityInspectPlanBatch::getPlanId, planIds);
        }
        wrapper.orderByDesc(StabilityInspectPlanBatch::getCreateTime);

        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<StabilityInspectPlanBatch> batches = batchMapper.selectList(wrapper);
        CommonPage<StabilityInspectPlanBatch> entityPage = CommonPage.convertPage(batches);

        if (batches.isEmpty()) {
            CommonPage<StabilityOverallSampleDTO> empty = new CommonPage<>();
            empty.setPageNum(entityPage.getPageNum());
            empty.setPageSize(entityPage.getPageSize());
            empty.setTotal(entityPage.getTotal());
            empty.setList(Collections.emptyList());
            return empty;
        }

        List<Long> batchPlanIds = batches.stream().map(StabilityInspectPlanBatch::getPlanId)
                .distinct().collect(Collectors.toList());
        Map<Long, StabilityInspectPlan> planMap = planMapper.selectBatchIds(batchPlanIds).stream()
                .collect(Collectors.toMap(StabilityInspectPlan::getId, p -> p));

        List<StabilityOverallSampleDTO> dtoList = batches.stream().map(batch -> {
            StabilityOverallSampleDTO dto = new StabilityOverallSampleDTO();
            dto.setBatchId(batch.getId());
            dto.setBatchNo(batch.getBatchNo());
            dto.setProductionDate(batch.getProductionDate());
            dto.setBatchSampleStatus(StabilityPlanSampleStatusEnum.SAMPLED);
            StabilityInspectPlan plan = planMap.get(batch.getPlanId());
            if (plan != null) {
                dto.setPlanId(plan.getId());
                dto.setPlanCode(plan.getCode());
                dto.setMaterialName(plan.getMaterialName());
                dto.setMaterialCode(plan.getMaterialCode());
                dto.setMaterialSpec(plan.getMaterialSpec());
                dto.setPlanCreateBy(plan.getCreateBy());
                dto.setPlanCreateTime(plan.getCreateTime());
            }
            return dto;
        }).collect(Collectors.toList());

        CommonPage<StabilityOverallSampleDTO> resultPage = new CommonPage<>();
        resultPage.setPageNum(entityPage.getPageNum());
        resultPage.setPageSize(entityPage.getPageSize());
        resultPage.setTotal(entityPage.getTotal());
        resultPage.setList(dtoList);
        return resultPage;
    }

    @Override
    public StabilityOverallSampleDetailDTO getOverallSampleDetail(Long batchId) {
        StabilityInspectPlanBatch batch = batchMapper.selectById(batchId);
        if (batch == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        StabilityInspectPlan plan = planMapper.selectById(batch.getPlanId());

        StabilityOverallSampleDetailDTO dto = new StabilityOverallSampleDetailDTO();
        dto.setBatchId(batch.getId());
        dto.setBatchNo(batch.getBatchNo());
        dto.setProductionDate(batch.getProductionDate());

        if (plan != null) {
            dto.setPlanId(plan.getId());
            dto.setPlanCode(plan.getCode());
            dto.setMaterialName(plan.getMaterialName());
            dto.setMaterialCode(plan.getMaterialCode());
            dto.setMaterialSpec(plan.getMaterialSpec());
            if (plan.getMaterialId() != null) {
                Material material = materialMapper.selectById(plan.getMaterialId());
                if (material != null && material.getUnitId() != null) {
                    dto.setMaterialUnitId(material.getUnitId());
                    try {
                        dto.setMaterialUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
                    } catch (Exception ignored) {
                    }
                }
            }
            dto.setPlanCreateBy(plan.getCreateBy());
            dto.setPlanCreateTime(plan.getCreateTime());
            dto.setSchemeName(plan.getSchemeName());
            dto.setSchemeVersionNo(plan.getSchemeVersionNo());
            dto.setRemark(plan.getRemark());
            if (plan.getSchemeId() != null) {
                com.bmos.lims2.server.stability.scheme.entity.StabilityScheme scheme =
                        schemeMapper.selectById(plan.getSchemeId());
                if (scheme != null) {
                    dto.setSchemeCode(scheme.getCode());
                }
            }
        }

        // 加载该批次下所有样品
        List<StabilityPlanSample> samples = sampleMapper.selectByBatchId(batchId);

        // 查询试验类型字典，翻译名称
        Map<String, String> codeLabelMap = new HashMap<>();
        try {
            DictDetailFeignVO dictDetail = FeignUtils.handleRequest(
                    data -> dictFeign.selectDictDetailByCode(data), DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
            if (dictDetail != null && dictDetail.getDictDataList() != null) {
                dictDetail.getDictDataList().forEach(item ->
                        codeLabelMap.put(item.getDictValue(), item.getDictLabel()));
            }
        } catch (Exception e) {
            log.warn("查询试验类型字典失败，将使用原始code值", e);
        }

        // 批量加载 lm_sample，获取储存位置
        List<Long> lmSampleIds = samples.stream()
                .map(StabilityPlanSample::getSampleId)
                .filter(id -> id != null)
                .collect(Collectors.toList());
        Map<Long, String> locationMap = new HashMap<>();
        if (!lmSampleIds.isEmpty()) {


            inspectSampleMapper.selectBatchIds(lmSampleIds).forEach(
                    s -> locationMap.put(s.getId(), s.getStorageLocation()));
        }

        List<StabilityOverallSampleDetailDTO.SampleItemDTO> sampleItems = samples.stream()
                .map(s -> buildSampleItemDTO(s, codeLabelMap, locationMap))
                .collect(Collectors.toList());
        dto.setSamples(sampleItems);

        return dto;
    }

    @Override
    public StabilityOverallSampleDetailDTO getOverallSampleDetailForReceive(Long batchId) {
        StabilityOverallSampleDetailDTO dto = getOverallSampleDetail(batchId);
        if (dto.getSamples() != null) {
            List<StabilityOverallSampleDetailDTO.SampleItemDTO> receivable = dto.getSamples().stream()
                    .filter(s -> s.getStatus() == StabilityPlanSampleStatusEnum.SAMPLED
                            || s.getStatus() == StabilityPlanSampleStatusEnum.RECEIVED)
                    .collect(Collectors.toList());
            dto.setSamples(receivable);
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void takeOverallSamples(Long batchId, StabilityOverallSampleTakeDTO dto) {
        StabilityInspectPlanBatch batch = batchMapper.selectById(batchId);
        if (batch == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }

        LocalDateTime now = LocalDateTime.now();
        String samplerId = dto.getSamplerId();
        String samplerName = dto.getSamplerName();

        for (StabilityOverallSampleTakeDTO.ItemDTO itemDTO : dto.getItems()) {
            StabilityPlanSample sample = sampleMapper.selectById(itemDTO.getSampleId());
            if (sample == null) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
            }
            if (sample.getStatus() == StabilityPlanSampleStatusEnum.SAMPLED) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_ALREADY_SAMPLED);
            }
            if (sample.getStatus() == StabilityPlanSampleStatusEnum.RECEIVED) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_ALREADY_RECEIVED);
            }

            // 更新已有 lm_sample（计划创建时已生成，此处补充取样信息）
            Sample lmSample = inspectSampleMapper.selectById(sample.getSampleId());
            if (lmSample == null) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
            }
            String actualAmount = itemDTO.getActualSampleAmount();
            if (StrUtil.isNotBlank(sample.getPlannedSampleAmount())) {
                BigDecimal plannedAmount = new BigDecimal(sample.getPlannedSampleAmount());
                if (plannedAmount.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal actualValue = StrUtil.isNotBlank(actualAmount) ? new BigDecimal(actualAmount) : BigDecimal.ZERO;
                    if (actualValue.compareTo(plannedAmount) < 0) {
                        throw new BmosException(LimsResponseCode.STABILITY_OVERALL_ACTUAL_AMOUNT_LESS_THAN_PLAN);
                    }
                }
            }
            lmSample.setSampled(true);
            lmSample.setSamplerId(samplerId);
            lmSample.setSamplerName(samplerName);
            lmSample.setSamplingTime(now);
            if (StrUtil.isNotBlank(actualAmount)) {
                lmSample.setQuantity(actualAmount);
                lmSample.setCurrentQuantity(actualAmount);
            }
            inspectSampleMapper.updateById(lmSample);

            // 回写稳定性样品
            sample.setSamplerId(samplerId);
            sample.setSamplerName(samplerName);
            sample.setSamplingTime(now);
            if (StrUtil.isNotBlank(actualAmount)) {
                sample.setActualSampleAmount(actualAmount);
            }
            sample.setStatus(StabilityPlanSampleStatusEnum.SAMPLED);
            sample.setUpdateBy(samplerId);
            sampleMapper.updateById(sample);

            log.info("整体取样完成：batchId={}, sampleId={}, lmSampleId={}, sampleNo={}",
                    batchId, sample.getId(), lmSample.getId(), lmSample.getSampleNo());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveOverallSamples(List<StabilityOverallSampleReceiveDTO> items) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate receiveDate = LocalDate.now();

        SysUser currentUser = SysUserHolder.getUser();
        String receiverId = currentUser.getUserId();
        String receiverName = currentUser.getUserName();

        for (StabilityOverallSampleReceiveDTO item : items) {
            StabilityPlanSample sample = sampleMapper.selectById(item.getSampleId());
            if (sample == null) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
            }
            if (sample.getStatus() == StabilityPlanSampleStatusEnum.PENDING) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_SAMPLED);
            }
            if (sample.getStatus() == StabilityPlanSampleStatusEnum.RECEIVED) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_ALREADY_RECEIVED);
            }

            // 更新 lm_sample 父样品接收状态
            if (sample.getSampleId() != null) {
                Sample lmSample = inspectSampleMapper.selectById(sample.getSampleId());
                if (lmSample != null) {
                    lmSample.setReceived(true);
                    lmSample.setReceiverId(receiverId);
                    lmSample.setReceiverName(receiverName);
                    lmSample.setReceiveTime(now);
                    if (item.getStorageLocation() != null) {
                        lmSample.setStorageLocation(item.getStorageLocation());
                    }
                    lmSample.setUpdateBy(receiverId);
                    inspectSampleMapper.updateById(lmSample);
                }
            }

            // 更新稳定性整体样品
            sample.setStatus(StabilityPlanSampleStatusEnum.RECEIVED);
            sample.setReceiverId(receiverId);
            sample.setReceiverName(receiverName);
            sample.setReceiveTime(now);
            sample.setReceiveDate(receiveDate);
            sample.setUpdateBy(receiverId);
            sampleMapper.updateById(sample);

            // 若该批次所有样品均已接收，更新批次的 sampleReceiveDate
            List<StabilityPlanSample> batchSamples = sampleMapper.selectByBatchId(sample.getBatchId());
            boolean allBatchReceived = batchSamples.stream()
                    .allMatch(s -> s.getStatus() == StabilityPlanSampleStatusEnum.RECEIVED);
            if (allBatchReceived) {
                StabilityInspectPlanBatch batch = batchMapper.selectById(sample.getBatchId());
                if (batch != null) {
                    batch.setSampleReceiveDate(receiveDate);
                    batchMapper.updateById(batch);
                }
            }

            // 触发时间点任务生成逻辑
            generateTimepointTasksAndUpdatePlan(sample, receiveDate);

            // 记录接收操作日志
            auditOperationLogMapper.insert(AuditOperationLogEntity.builder()
                    .businessId(sample.getId())
                    .module(AuditBusinessModule.STABILITY_SAMPLE_MANAGE.name())
                    .operationType(OperationType.STABILITY_RECEIVED.getValue())
                    .createBy(receiverId)
                    .build());

            log.info("整体接收完成：batchId={}, sampleId={}, 接收人={}", sample.getBatchId(), sample.getId(), receiverName);
        }
    }

    private StabilityOverallSampleDetailDTO.SampleItemDTO buildSampleItemDTO(
            StabilityPlanSample s, Map<String, String> codeLabelMap, Map<Long, String> locationMap) {
        StabilityOverallSampleDetailDTO.SampleItemDTO item = new StabilityOverallSampleDetailDTO.SampleItemDTO();
        item.setId(s.getId());
        item.setSampleNo(s.getSampleNo());
        item.setExperimentType(s.getExperimentType());
        item.setExperimentTypeName(codeLabelMap.getOrDefault(s.getExperimentType(), s.getExperimentType()));
        item.setStorageCondition(s.getStorageCondition());
        item.setPlannedSampleAmount(s.getPlannedSampleAmount());
        item.setActualSampleAmount(s.getActualSampleAmount());
        item.setSampleUnit(s.getSampleUnit());
        if (s.getSampleUnit() != null) {
            try {
                item.setUnitName(unitCache.getGlobalUnitName(Long.parseLong(s.getSampleUnit())));
            } catch (NumberFormatException ignored) {
            }
        }
        item.setManualAdded(s.getSchemePlanId() == null);
        item.setStatus(s.getStatus());
        item.setSamplerId(s.getSamplerId());
        item.setSamplerName(s.getSamplerName());
        item.setSamplingTime(s.getSamplingTime());
        item.setReceiverId(s.getReceiverId());
        item.setReceiverName(s.getReceiverName());
        item.setReceiveTime(s.getReceiveTime());
        item.setReceiveDate(s.getReceiveDate());
        item.setStorageLocation(locationMap.get(s.getSampleId()));
        return item;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StabilityOverallSampleDetailDTO.SampleItemDTO> addOverallSamples(Long batchId, List<StabilityPlanSampleAddItemDTO> items) {
        StabilityInspectPlanBatch batch = batchMapper.selectById(batchId);
        if (batch == null) {
            throw new BmosException(LimsResponseCode.STABILITY_TIMEPOINT_TASK_NOT_EXIST);
        }
        StabilityInspectPlan plan = planMapper.selectById(batch.getPlanId());
        String materialName = plan != null ? plan.getMaterialName() : null;

        // 查字典，构建翻译 map（批量处理，只查一次）
        Map<String, String> codeLabelMap = new HashMap<>();
        try {
            DictDetailFeignVO dictDetail = FeignUtils.handleRequest(
                    data -> dictFeign.selectDictDetailByCode(data), DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
            if (dictDetail != null && dictDetail.getDictDataList() != null) {
                dictDetail.getDictDataList().forEach(item ->
                        codeLabelMap.put(item.getDictValue(), item.getDictLabel()));
            }
        } catch (Exception e) {
            log.warn("查询试验类型字典失败，将使用原始code值", e);
        }

        List<StabilityOverallSampleDetailDTO.SampleItemDTO> result = new ArrayList<>();
        for (StabilityPlanSampleAddItemDTO item : items) {
            StabilityPlanSample sample = new StabilityPlanSample();
            sample.setPlanId(batch.getPlanId());
            sample.setBatchId(batchId);
            sample.setBatchNo(batch.getBatchNo());
            sample.setSchemePlanId(null);
            sample.setExperimentType(item.getExperimentType());
            sample.setStorageCondition(item.getStorageCondition());
            sample.setPlannedSampleAmount(item.getPlannedSampleAmount());
            sample.setActualSampleAmount(item.getActualSampleAmount());
            sample.setSampleUnit(item.getSampleUnit());
            // 填了实际取样量则直接置为已取样
            boolean isSampled = item.getActualSampleAmount() != null;
            if (isSampled) {
                SysUser user = SysUserHolder.getUser();
                String samplerId = user != null ? user.getUserId() : null;
                String samplerName = user != null ? user.getUserName() : null;
                sample.setStatus(StabilityPlanSampleStatusEnum.SAMPLED);
                sample.setSamplerId(samplerId);
                sample.setSamplerName(samplerName);
                sample.setSamplingTime(LocalDateTime.now());
            } else {
                sample.setStatus(StabilityPlanSampleStatusEnum.PENDING);
            }
            // 先建 lm_sample，sampleId 写入 sample 对象后再 insert
            Sample lmSample = createLmSampleAndBindNo(sample, materialName);
            sampleMapper.insert(sample);
            // 回写 lm_sample 的 stabilityPlanSampleId；若已取样则同步取样信息
            lmSample.setStabilityPlanSampleId(sample.getId());
            if (isSampled) {
                lmSample.setSampled(true);
                lmSample.setSamplerId(sample.getSamplerId());
                lmSample.setSamplerName(sample.getSamplerName());
                lmSample.setSamplingTime(sample.getSamplingTime());
                lmSample.setQuantity(item.getActualSampleAmount());
                lmSample.setCurrentQuantity(item.getActualSampleAmount());
            }
            inspectSampleMapper.updateById(lmSample);

            result.add(buildSampleItemDTO(sample, codeLabelMap, Collections.emptyMap()));
        }
        return result;
    }

    @Override
    public List<StabilitySchemeExperimentTypeDTO> listSchemeExperimentTypes(Long planId) {
        StabilityInspectPlan plan = planMapper.selectById(planId);
        if (plan == null || plan.getSchemeVersionId() == null) {
            return Collections.emptyList();
        }
        List<StabilitySchemePlan> schemePlans = schemePlanMapper.selectByVersionId(plan.getSchemeVersionId());
        Map<String, String> codeLabelMap = new HashMap<>();
        try {
            DictDetailFeignVO dictDetail = FeignUtils.handleRequest(
                    data -> dictFeign.selectDictDetailByCode(data), DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
            if (dictDetail != null && dictDetail.getDictDataList() != null) {
                dictDetail.getDictDataList().forEach(item ->
                        codeLabelMap.put(item.getDictValue(), item.getDictLabel()));
            }
        } catch (Exception e) {
            log.warn("查询试验类型字典失败，将使用原始code值", e);
        }
        return schemePlans.stream().map(sp -> {
            StabilitySchemeExperimentTypeDTO dto = new StabilitySchemeExperimentTypeDTO();
            dto.setExperimentType(sp.getExperimentType());
            dto.setExperimentTypeName(codeLabelMap.getOrDefault(sp.getExperimentType(), sp.getExperimentType()));
            dto.setStorageCondition(sp.getStorageCondition());
            dto.setTotalSampleAmount(sp.getTotalSampleAmount());
            if (sp.getTotalSampleUnit() != null) {
                String unitIdStr = String.valueOf(sp.getTotalSampleUnit());
                dto.setSampleUnit(unitIdStr);
                try {
                    dto.setUnitName(unitCache.getGlobalUnitName(sp.getTotalSampleUnit()));
                } catch (Exception ignored) {
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public StabilityOverallSampleDetailDTO getOverallSampleDetailBySampleNo(String sampleNo) {
        StabilityPlanSample sample = sampleMapper.selectOne(
                new LambdaQueryWrapper<StabilityPlanSample>()
                        .eq(StabilityPlanSample::getSampleNo, sampleNo)
                        .last("LIMIT 1"));
        if (sample == null) {
            return null;
        }
        return getOverallSampleDetailForReceive(sample.getBatchId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteManualOverallSample(Long sampleId) {
        StabilityPlanSample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        if (sample.getSchemePlanId() != null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_MANUAL);
        }
        if (sample.getStatus() != StabilityPlanSampleStatusEnum.PENDING) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_CANNOT_DELETE);
        }
        sampleMapper.deleteById(sampleId);
        // 同步软删除关联的 lm_sample
        if (sample.getSampleId() != null) {
            inspectSampleMapper.deleteById(sample.getSampleId());
        }
    }

    // ══════════════════════ 时间点取样 ══════════════════════

    @Override
    public CommonPage<StabilityTimepointSampleDTO> pageTimepointSamples(StabilityTimepointSampleQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<StabilityTimepointSampleDTO> list = timepointTaskMapper.pageTimepointSamples(queryDTO);
        CommonPage<StabilityTimepointSampleDTO> page = CommonPage.convertPage(list);
        if (CollUtil.isEmpty(page.getList())) {
            return page;
        }
        // 查询试验类型字典
        Map<String, String> dictMap = new java.util.HashMap<>();
        try {
            com.bmos.platform.facade.dict.vo.DictDetailFeignVO dictDetail =
                    com.bmos.lims2.server.platform.util.FeignUtils.handleRequest(
                            data -> dictFeign.selectDictDetailByCode(data),
                            com.bmos.lims2.common.constants.DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
            if (dictDetail != null && dictDetail.getDictDataList() != null) {
                dictDetail.getDictDataList().forEach(item -> dictMap.put(item.getDictValue(), item.getDictLabel()));
            }
        } catch (Exception e) {
            log.warn("查询试验类型字典失败", e);
        }
        // 填充名称
        for (StabilityTimepointSampleDTO dto : page.getList()) {
            if (dto.getExperimentType() != null) {
                dto.setExperimentTypeName(dictMap.getOrDefault(dto.getExperimentType(), dto.getExperimentType()));
            }
            if (dto.getSampleUnit() != null) {
                try {
                    dto.setSampleUnitName(unitCache.getGlobalUnitName(Long.parseLong(dto.getSampleUnit())));
                } catch (NumberFormatException ignored) {
                }
            }
            if (dto.getMaterialUnitId() != null) {
                dto.setMaterialUnitName(unitCache.getGlobalUnitName(dto.getMaterialUnitId()));
            }
        }
        return page;
    }

    @Override
    public List<StabilityTimepointSourceSampleDTO> listSourceSamples(Long timepointTaskId) {
        StabilityPlanTimepointTask task = timepointTaskMapper.selectById(timepointTaskId);
        if (task == null) {
            return Collections.emptyList();
        }
        List<StabilityPlanSample> planSamples = sampleMapper.selectList(
                new LambdaQueryWrapper<StabilityPlanSample>()
                        .eq(StabilityPlanSample::getPlanId, task.getPlanId())
                        .eq(StabilityPlanSample::getBatchId, task.getBatchId())
                        .eq(StabilityPlanSample::getExperimentType, task.getExperimentType())
                        .eq(StabilityPlanSample::getStorageCondition, task.getStorageCondition())
                        .eq(StabilityPlanSample::getStatus, StabilityPlanSampleStatusEnum.RECEIVED)
        );
        if (CollUtil.isEmpty(planSamples)) {
            return Collections.emptyList();
        }

        List<StabilityTimepointSourceSampleDTO> result = new ArrayList<>();
        for (StabilityPlanSample ps : planSamples) {
            if (ps.getSampleId() == null) {
                continue;
            }
            Sample lmSample = inspectSampleMapper.selectById(ps.getSampleId());
            if (lmSample == null || StrUtil.isBlank(lmSample.getCurrentQuantity())) {
                continue;
            }
            BigDecimal currentQuantity;
            try {
                currentQuantity = new BigDecimal(lmSample.getCurrentQuantity());
            } catch (Exception e) {
                continue;
            }
            if (currentQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            StabilityTimepointSourceSampleDTO dto = new StabilityTimepointSourceSampleDTO();
            dto.setPlanSampleId(ps.getId());
            dto.setSampleNo(ps.getSampleNo());
            dto.setActualSampleAmount(ps.getActualSampleAmount());
            dto.setSampleUnit(ps.getSampleUnit());
            dto.setStorageLocation(lmSample.getStorageLocation());
            dto.setCurrentQuantity(lmSample.getCurrentQuantity());
            result.add(dto);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchTakeTimepointSamples(StabilityTimepointBatchTakeDTO dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            return;
        }
        SysUser currentUser = SysUserHolder.getUser();
        String samplerId = currentUser.getUserId();
        String samplerName = currentUser.getUserName();
        LocalDateTime now = LocalDateTime.now();

        for (StabilityTimepointBatchTakeDTO.ItemDTO item : dto.getItems()) {
            StabilityPlanTimepointTask task = timepointTaskMapper.selectById(item.getTimepointTaskId());
            if (task == null) {
                throw new BmosException(LimsResponseCode.STABILITY_TIMEPOINT_TASK_NOT_EXIST);
            }
            if (task.getStatus() != StabilityTimepointTaskStatusEnum.WAITING_SAMPLE) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_STATUS_ERROR);
            }

            // 1. 加载取样来源：用户选择的整体样品（lm_stability_plan_sample）
            if (item.getSourcePlanSampleId() == null) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
            }
            StabilityPlanSample sourcePlanSample = sampleMapper.selectById(item.getSourcePlanSampleId());
            if (sourcePlanSample == null || sourcePlanSample.getSampleId() == null) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
            }
            Sample parentLmSample = inspectSampleMapper.selectById(sourcePlanSample.getSampleId());
            if (parentLmSample == null) {
                throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
            }

            // 2. 解析本次取样量和单位
            BigDecimal takenAmount = StrUtil.isNotBlank(item.getActualSampleAmount())
                    ? new BigDecimal(item.getActualSampleAmount()) : BigDecimal.ZERO;
            Long takenUnitId = null;
            if (StrUtil.isNotBlank(item.getSampleUnit())) {
                try {
                    takenUnitId = Long.parseLong(item.getSampleUnit());
                } catch (NumberFormatException ignored) {
                }
            }

            // 3. 扣减整体样品剩余数量（注意单位转换）
            // parentLmSample.currentQuantity 存储的是整体样品当前剩余量，单位为 parentLmSample.unitId
            if (takenAmount.compareTo(BigDecimal.ZERO) > 0) {
                Long sourceUnitId = parentLmSample.getUnitId();
                BigDecimal deductAmount = takenAmount;
                // 若取样单位与整体样品单位不同，则换算为整体样品单位后再扣减
                if (takenUnitId != null && sourceUnitId != null && !takenUnitId.equals(sourceUnitId)) {
                    try {
                        deductAmount = unitCache.convert(takenAmount, takenUnitId, sourceUnitId);
                    } catch (Exception e) {
                        log.warn("单位换算失败，使用原始取样量扣减: takenUnitId={}, sourceUnitId={}", takenUnitId, sourceUnitId);
                    }
                }
                String currentQtyStr = parentLmSample.getCurrentQuantity();
                BigDecimal currentQty = StrUtil.isNotBlank(currentQtyStr) ? new BigDecimal(currentQtyStr) : BigDecimal.ZERO;
                if (deductAmount.compareTo(currentQty) > 0) {
                    throw new BmosException(LimsResponseCode.STABILITY_TIMEPOINT_ACTUAL_AMOUNT_EXCEED_SOURCE_REMAINING);
                }
                BigDecimal remaining = currentQty.subtract(deductAmount);
                parentLmSample.setCurrentQuantity(remaining.toPlainString());
                inspectSampleMapper.updateById(parentLmSample);

                String detail = null;
                if (StrUtil.isNotBlank(item.getActualSampleAmount()) && StrUtil.isNotBlank(item.getSampleUnit())) {
                    String unitName;
                    try {
                        unitName = unitCache.getGlobalUnitName(Long.parseLong(item.getSampleUnit()));
                    } catch (Exception e) {
                        unitName = item.getSampleUnit();
                    }
                    String detailValue = item.getActualSampleAmount() + "（" + unitName + "）";
                    detail = "{\"sampleAmount\":\"" + detailValue + "\"}";
                }
                auditOperationLogMapper.insert(AuditOperationLogEntity.builder()
                        .businessId(sourcePlanSample.getId())
                        .module(AuditBusinessModule.STABILITY_SAMPLE_MANAGE.name())
                        .operationType(OperationType.STABILITY_SAMPLED.getValue())
                        .detail(detail)
                        .createBy(samplerId)
                        .build());
            }

            // 4. 更新时间点任务：更新状态和实际取样量
            task.setSampleId(sourcePlanSample.getId());
            task.setStatus(StabilityTimepointTaskStatusEnum.IN_PROGRESS);
            if (takenAmount.compareTo(BigDecimal.ZERO) > 0) {
                task.setActualSampleAmount(item.getActualSampleAmount());
            }
            if (StrUtil.isNotBlank(item.getSampleUnit())) {
                task.setActualSampleUnit(item.getSampleUnit());
            }
            timepointTaskMapper.updateById(task);

            // 5. 更新检验单对应样品状态（取样即接收）
            if (task.getInspectionOrderId() != null) {
                List<Sample> orderSamples = inspectSampleMapper.selectByInspectionOrderId(task.getInspectionOrderId());
                if (CollUtil.isNotEmpty(orderSamples)) {
                    Sample orderSample = orderSamples.get(0);
                    orderSample.setSampled(true);
                    orderSample.setReceived(true);
                    orderSample.setCollected(true);
                    orderSample.setSamplerId(samplerId);
                    orderSample.setSamplerName(samplerName);
                    orderSample.setSamplingTime(now);
                    orderSample.setReceiverId(samplerId);
                    orderSample.setReceiverName(samplerName);
                    orderSample.setReceiveTime(now);
                    if (takenAmount.compareTo(BigDecimal.ZERO) > 0) {
                        orderSample.setQuantity(item.getActualSampleAmount());
                        orderSample.setCurrentQuantity(item.getActualSampleAmount());
                        if (takenUnitId != null) {
                            orderSample.setUnitId(takenUnitId);
                        }
                    }
                    inspectSampleMapper.updateById(orderSample);
                }
            }

            log.info("稳定性时间点样品取样完成（批量）：timepointTaskId={}, 取样人={}", item.getTimepointTaskId(), samplerName);
        }
    }
}

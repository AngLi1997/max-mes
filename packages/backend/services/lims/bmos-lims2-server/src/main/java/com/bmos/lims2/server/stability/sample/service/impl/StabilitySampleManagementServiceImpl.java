package com.bmos.lims2.server.stability.sample.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.cache.redis.objects.BaseUser;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.constants.DictCodeConstant;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.enums.StabilityInspectPlanStatusEnum;
import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.lims2.server.stability.plan.entity.StabilityInspectPlan;
import com.bmos.lims2.server.stability.plan.entity.StabilityPlanSample;
import com.bmos.lims2.server.stability.plan.mapper.StabilityInspectPlanMapper;
import com.bmos.lims2.server.stability.plan.mapper.StabilityPlanSampleMapper;
import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleDestroyDTO;
import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleManagementQueryDTO;
import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleMoveDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySampleFlatRowDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySampleGroupDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySamplePrintTagResultDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySampleRowDTO;
import com.bmos.lims2.server.stability.sample.entity.StabilitySampleMoveLog;
import com.bmos.lims2.server.stability.sample.mapper.StabilitySampleManagementMapper;
import com.bmos.lims2.server.stability.sample.mapper.StabilitySampleMoveLogMapper;
import com.bmos.lims2.server.stability.sample.service.StabilitySampleManagementService;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.dict.feign.DictFeign;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.formatter.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bmos.common.holder.SysUserHolder;

/**
 * 稳定性样品管理服务实现
 */
@Service
@Slf4j
public class StabilitySampleManagementServiceImpl implements StabilitySampleManagementService {

    @Autowired
    private StabilitySampleManagementMapper stabilitySampleManagementMapper;

    @Autowired
    private StabilityPlanSampleMapper stabilityPlanSampleMapper;

    @Autowired
    private StabilityInspectPlanMapper stabilityInspectPlanMapper;

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private StabilitySampleMoveLogMapper moveLogMapper;

    @Autowired
    private com.bmos.lims2.server.audit.operationlog.mapper.AuditOperationLogMapper auditOperationLogMapper;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private UnitCache unitCache;

    @Override
    public CommonPage<StabilitySampleGroupDTO> pageStabilitySamples(StabilitySampleManagementQueryDTO queryDTO) {
        long total = stabilitySampleManagementMapper.countDistinctPlans(queryDTO);
        if (total == 0) {
            return emptyPage(queryDTO, 0L);
        }

        long offset = (long) (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();
        List<Long> planIds = stabilitySampleManagementMapper.selectDistinctPlanIds(queryDTO, offset, queryDTO.getPageSize());
        if (CollUtil.isEmpty(planIds)) {
            return emptyPage(queryDTO, total);
        }

        List<StabilitySampleFlatRowDTO> flatRows =
                stabilitySampleManagementMapper.selectSampleFlatRows(planIds, queryDTO);

        // 查询试验类型字典，构建 code -> label 映射
        Map<String, String> dictMap = new java.util.HashMap<>();
        try {
            DictDetailFeignVO dictDetail = FeignUtils.handleRequest(
                    data -> dictFeign.selectDictDetailByCode(data), DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
            if (dictDetail != null && dictDetail.getDictDataList() != null) {
                dictDetail.getDictDataList().forEach(item -> dictMap.put(item.getDictValue(), item.getDictLabel()));
            }
        } catch (Exception e) {
            log.warn("查询试验类型字典失败", e);
        }

        // 按 planId 分组，保持 planIds 的顺序
        Map<Long, StabilitySampleGroupDTO> groupMap = new LinkedHashMap<>();
        for (Long planId : planIds) {
            groupMap.put(planId, null);
        }
        for (StabilitySampleFlatRowDTO row : flatRows) {
            StabilitySampleGroupDTO group = groupMap.get(row.getPlanId());
            if (group == null) {
                group = buildGroup(row);
                groupMap.put(row.getPlanId(), group);
            }
            group.getSamples().add(buildSampleRow(row, dictMap));
        }

        List<StabilitySampleGroupDTO> groups = groupMap.values().stream()
                .filter(g -> g != null)
                .collect(Collectors.toList());

        CommonPage<StabilitySampleGroupDTO> page = new CommonPage<>();
        page.setPageNum(queryDTO.getPageNum());
        page.setPageSize(queryDTO.getPageSize());
        page.setTotal((int) total);
        page.setList(groups);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveSampleLocation(StabilitySampleMoveDTO dto) {
        StabilityPlanSample planSample = requirePlanSample(dto.getId());
        Sample sample = requireSample(planSample.getSampleId());

        if (Boolean.TRUE.equals(sample.getDestroyed())) {
            throw new BmosException(LimsResponseCode.STABILITY_SAMPLE_ALREADY_DESTROYED);
        }

        String fromLocation = sample.getStorageLocation();

        Sample update = new Sample();
        update.setId(sample.getId());
        update.setStorageLocation(dto.getStorageLocation());
        sampleMapper.updateById(update);

        // 记录移动日志
        LocalDateTime now = LocalDateTime.now();
        String operatorId = SysUserHolder.getUser().getUserId();
        String operatorName = SysUserHolder.getUser().getUserName();

        StabilitySampleMoveLog log = new StabilitySampleMoveLog();
        log.setPlanSampleId(dto.getId());
        log.setFromLocation(fromLocation);
        log.setToLocation(dto.getStorageLocation());
        log.setMoveReason(dto.getMoveReason());
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setMoveTime(now);
        moveLogMapper.insert(log);

        // 记录操作日志
        StringBuilder detail = new StringBuilder("{");
        appendJson(detail, "fromLocation", fromLocation);
        appendJson(detail, "toLocation", dto.getStorageLocation());
        appendJson(detail, "moveReason", dto.getMoveReason());
        if (detail.length() > 1 && detail.charAt(detail.length() - 1) == ',') {
            detail.setLength(detail.length() - 1);
        }
        detail.append("}");
        AuditOperationLogEntity logEntry = AuditOperationLogEntity.builder()
                .businessId(planSample.getId())
                .module(AuditBusinessModule.STABILITY_SAMPLE_MANAGE.name())
                .operationType(OperationType.STABILITY_MOVED.getValue())
                .detail(detail.toString())
                .remark(dto.getMoveReason() != null ? dto.getMoveReason() : "")
                .createBy(SysUserHolder.getUser().getUserId())
                .build();
        auditOperationLogMapper.insert(logEntry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDestroySamples(List<Long> ids, StabilitySampleDestroyDTO dto) {
        for (Long id : ids) {
            destroyOne(id, dto);
        }
    }

    private void destroyOne(Long planSampleId, StabilitySampleDestroyDTO dto) {
        StabilityPlanSample planSample = requirePlanSample(planSampleId);
        Sample sample = requireSample(planSample.getSampleId());

        if (Boolean.TRUE.equals(sample.getDestroyed())) {
            throw new BmosException(LimsResponseCode.STABILITY_SAMPLE_ALREADY_DESTROYED);
        }

        // 仅待销毁状态（考察计划已完成）才允许销毁
        StabilityInspectPlan plan = stabilityInspectPlanMapper.selectById(planSample.getPlanId());
        if (plan == null || !StabilityInspectPlanStatusEnum.COMPLETED.equals(plan.getStatus())) {
            throw new BmosException(LimsResponseCode.STABILITY_SAMPLE_STATUS_NOT_ALLOW_DESTROY);
        }

        Sample update = new Sample();
        update.setId(sample.getId());
        update.setDestroyed(true);
        update.setProcessTime(dto.getDestructionTime() != null ? dto.getDestructionTime() : LocalDateTime.now());
        update.setProcessBy(dto.getDestructorId());
        update.setProcessMethod(dto.getDestructionMethod());
        update.setProcessRemark(dto.getDestructionReason());
        sampleMapper.updateById(update);

        StabilityPlanSample psUpdate = new StabilityPlanSample();
        psUpdate.setId(planSample.getId());
        psUpdate.setStatus(StabilityPlanSampleStatusEnum.DESTROYED);
        stabilityPlanSampleMapper.updateById(psUpdate);

        // 记录操作日志（含销毁地点、监督人等 lm_sample 无对应列的字段）
        StringBuilder detail = new StringBuilder("{");
        appendJson(detail, "destructionLocation", dto.getDestructionLocation());
        appendJson(detail, "destructorName", dto.getDestructorName());
        appendJson(detail, "supervisorId", dto.getSupervisorId());
        appendJson(detail, "supervisorName", dto.getSupervisorName());
        appendJson(detail, "destructionMethod", dto.getDestructionMethod());
        appendJson(detail, "destructionReason", dto.getDestructionReason());
        appendJson(detail, "destructionTime",  dto.getDestructionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        if (detail.length() > 1 && detail.charAt(detail.length() - 1) == ',') {
            detail.setLength(detail.length() - 1);
        }
        detail.append("}");
        AuditOperationLogEntity logEntry = AuditOperationLogEntity.builder()
                .businessId(planSample.getId())
                .module(AuditBusinessModule.STABILITY_SAMPLE_MANAGE.name())
                .operationType(OperationType.STABILITY_DESTROYED.getValue())
                .detail(detail.toString())
                .remark(dto.getRemark() != null ? dto.getRemark() : "")
                .createBy(dto.getDestructorId())
                .build();
        auditOperationLogMapper.insert(logEntry);
    }

    // ─── 私有辅助方法 ────────────────────────────────────────────────

    private StabilityPlanSample requirePlanSample(Long id) {
        if (id == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        StabilityPlanSample ps = stabilityPlanSampleMapper.selectById(id);
        if (ps == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        return ps;
    }

    private Sample requireSample(Long sampleId) {
        if (sampleId == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        Sample s = sampleMapper.selectById(sampleId);
        if (s == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        return s;
    }

    private CommonPage<StabilitySampleGroupDTO> emptyPage(StabilitySampleManagementQueryDTO q, long total) {
        CommonPage<StabilitySampleGroupDTO> page = new CommonPage<>();
        page.setPageNum(q.getPageNum());
        page.setPageSize(q.getPageSize());
        page.setTotal((int) total);
        page.setList(new ArrayList<>());
        return page;
    }

    private StabilitySampleGroupDTO buildGroup(StabilitySampleFlatRowDTO row) {
        StabilitySampleGroupDTO g = new StabilitySampleGroupDTO();
        g.setPlanId(row.getPlanId());
        g.setPlanCode(row.getPlanCode());
        g.setMaterialId(row.getMaterialId());
        g.setMaterialName(row.getMaterialName());
        g.setMaterialCode(row.getMaterialCode());
        g.setMaterialSpec(row.getMaterialSpec());
        g.setPlanCreateBy(row.getPlanCreateBy());
        if (row.getPlanCreateBy() != null) {
            BaseUserDO user = UserUtils.getUser(row.getPlanCreateBy());
            if (user != null) {
                g.setPlanCreateByUserName(user.getUserName());
            }
        }
        g.setPlanCreateTime(row.getPlanCreateTime());
        g.setStartDate(row.getStartDate());
        g.setPlanEndDate(row.getPlanEndDate());
        g.setSamples(new ArrayList<>());
        return g;
    }

    private StabilitySampleRowDTO buildSampleRow(StabilitySampleFlatRowDTO row, Map<String, String> dictMap) {
        StabilitySampleRowDTO r = new StabilitySampleRowDTO();
        r.setId(row.getOverallSampleId());
        r.setBatchId(row.getBatchId());
        r.setBatchNo(row.getBatchNo());
        r.setSampleId(row.getSampleId());
        r.setSampleNo(row.getSampleNo());
        r.setActualSampleAmount(row.getActualSampleAmount());
        r.setCurrentQuantity(row.getCurrentQuantity());
        r.setSampleUnit(row.getSampleUnit());
        if (row.getSampleUnit() != null) {
            try {
                r.setSampleUnitName(unitCache.getGlobalUnitName(Long.parseLong(row.getSampleUnit())));
            } catch (NumberFormatException ignored) {}
        }
        r.setReceiveDate(row.getReceiveDate());
        r.setReceiverName(row.getReceiverName());
        r.setExperimentType(row.getExperimentType());
        r.setExperimentTypeName(dictMap.getOrDefault(row.getExperimentType(), row.getExperimentType()));
        r.setStorageCondition(row.getStorageCondition());
        r.setStorageLocation(row.getStorageLocation());
        r.setStatus(row.getSampleStatus());
        return r;
    }

    private ListLogVO buildLog(OperationType type, LocalDateTime time, String userId, String remark) {
        ListLogVO vo = new ListLogVO();
        vo.setOperationType(type);
        vo.setCreateTime(time);
        vo.setCreateBy(userId);
        vo.setRemark(remark);
        return vo;
    }

    private void appendJson(StringBuilder sb, String key, String value) {
        if (value != null && !value.isEmpty()) {
            sb.append("\"").append(key).append("\":\"").append(value).append("\",");
        }
    }

    @Override
    public StabilitySamplePrintTagResultDTO getPrintTagResult(String sampleNo) {
        StabilitySamplePrintTagResultDTO result =
                stabilitySampleManagementMapper.selectPrintTagResult(sampleNo);
        if (result == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        // 拼装检品信息（编码-名称）
        if (result.getMaterialCode() != null && result.getMaterialName() != null) {
            result.setFullMaterialName(result.getMaterialCode() + "-" + result.getMaterialName());
        }
        // 拼装样品数量（数量+单位）
        if (result.getActualSampleAmount() != null && result.getSampleUnit() != null) {
            result.setQuantityWithUnit(result.getActualSampleAmount() + result.getSampleUnit());
        }
        return result;
    }

    @Override
    public StabilitySamplePrintTagResultDTO getTimepointPrintTagResult(String sampleNo) {
        StabilitySamplePrintTagResultDTO result =
                stabilitySampleManagementMapper.selectTimepointPrintTagResult(sampleNo);
        if (result == null) {
            throw new BmosException(LimsResponseCode.STABILITY_INSPECT_PLAN_SAMPLE_NOT_EXIST);
        }
        // 拼装检品信息（编码-名称）
        if (result.getMaterialCode() != null && result.getMaterialName() != null) {
            result.setFullMaterialName(result.getMaterialCode() + "-" + result.getMaterialName());
        }
        // sampleUnit 存的是 unit_id 字符串，通过 UnitCache 解析单位名称
        String unitName = null;
        if (result.getSampleUnit() != null) {
            try {
                Long unitId = Long.parseLong(result.getSampleUnit());
                unitName = unitCache.getGlobalUnitName(unitId);
            } catch (NumberFormatException ignored) {
                unitName = result.getSampleUnit();
            }
        }
        // 拼装样品数量（数量+单位名称）
        if (result.getActualSampleAmount() != null) {
            result.setQuantityWithUnit(result.getActualSampleAmount() + (unitName != null ? unitName : ""));
        }
        result.setSampleUnit(unitName);
        return result;
    }
}

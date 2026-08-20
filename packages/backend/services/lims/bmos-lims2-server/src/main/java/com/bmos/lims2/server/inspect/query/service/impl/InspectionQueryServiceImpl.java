package com.bmos.lims2.server.inspect.query.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.server.inspect.entry.dto.EntryByItemQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectItemTabDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;
import com.bmos.lims2.server.inspect.entry.dto.EntryRecordsGroupedByAnalysisItemDTO;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryRecordMapper;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderPageQueryDTO;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;
import com.bmos.lims2.server.inspect.query.service.InspectionDetailDTO;
import com.bmos.lims2.server.inspect.query.service.OrderInfoDTO;
import com.bmos.lims2.server.inspect.query.service.InspectionQueryService;
import com.bmos.lims2.server.inspect.sample.ledger.entity.SampleLedger;
import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerListDTO;
import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerPageQueryDTO;
import com.bmos.lims2.server.inspect.sample.ledger.service.SampleLedgerService;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeItemMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeItemMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeMapper;
import com.bmos.lims2.server.stability.scheme.entity.StabilityScheme;
import com.bmos.lims2.server.stability.plan.mapper.StabilityInspectPlanMapper;
import com.bmos.lims2.server.stability.plan.entity.StabilityInspectPlan;
import com.bmos.lims2.server.stability.plan.mapper.StabilityPlanTimepointTaskMapper;
import com.bmos.lims2.server.stability.plan.entity.StabilityPlanTimepointTask;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.report.entity.ReportGenerateTask;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO;
import com.bmos.lims2.server.report.mapper.ReportGenerateTaskMapper;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigDecimal;

/**
 * @Description: 检验查询聚合服务实现
 * @Author: yigaohui
 * @Date: 2025/09/05 10:30
 */
@Service
@RequiredArgsConstructor
public class InspectionQueryServiceImpl implements InspectionQueryService {

    private final InspectionOrderService inspectionOrderService;
    private final SampleLedgerService sampleLedgerService;
    private final ReportGenerateTaskMapper reportGenerateTaskMapper;
    private final InspectionEntryRecordMapper inspectionEntryRecordMapper;
    private final com.bmos.lims2.server.inspect.order.mapper.SampleMapper sampleMapper;
    private final com.bmos.lims2.server.inspect.sample.ledger.mapper.SampleLedgerMapper sampleLedgerMapper;
    private final InspectionSchemeItemMapper inspectionSchemeItemMapper;
    private final StabilitySchemeItemMapper stabilitySchemeItemMapper;
    private final StabilityPlanTimepointTaskMapper stabilityTimepointTaskMapper;
    private final StabilityInspectPlanMapper stabilityInspectPlanMapper;
    private final StabilitySchemeMapper stabilitySchemeMapper;
    private final TaskMapper taskMapper;
    private final UnitCache unitCache;

    @Override
    public CommonPage<InspectionOrderDTO> page(InspectionOrderPageQueryDTO queryDTO) {
        return inspectionOrderService.getInspectionOrderPage(queryDTO);
    }


    @Override
    public CommonPage<SampleLedgerListDTO> sampleLedgerPage(SampleLedgerPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        java.util.List<SampleLedgerListDTO> list = sampleLedgerMapper.selectLedgerPage(queryDTO);
        if (CollectionUtil.isNotEmpty( list)){
            list.forEach(item -> {
                item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
            });
        }
        return CommonPage.convertPage(list);
    }
    @Override
    public OrderInfoDTO getOrderInfo(Long orderId) {
        InspectionOrderDTO order = inspectionOrderService.getInspectionOrderById(orderId);
        if (order != null) {
            order.setSamplingList(null);
        }
        InspectionDetailDTO.StatusFlags flags = new InspectionDetailDTO.StatusFlags();
        flags.setRequested(InspectionOrderStatusEnum.PENDING_CONFIRM != order.getOrderStatus());
        List<com.bmos.lims2.server.inspect.order.entity.Sample> samples = sampleMapper.selectByInspectionOrderId(orderId);
        boolean allReceived = !samples.isEmpty() && samples.stream().allMatch(s -> Boolean.TRUE.equals(s.getReceived()));
        flags.setSampled(allReceived);
        List<ReportGenerateTask> tasks = reportGenerateTaskMapper.selectByOrderId(orderId);
        boolean reported = tasks.stream().anyMatch(t -> Boolean.TRUE.equals(t.getReportApproved()));
        flags.setReported(reported);
        flags.setInspected(InspectionOrderStatusEnum.COMPLETED == order.getOrderStatus());

        OrderInfoDTO resp = new OrderInfoDTO();
        resp.setOrder(order);
        resp.setFlags(flags);

        // 计算阶段时间
        LocalDateTime requestStartTime = order.getRequestTime() != null ? order.getRequestTime() : order.getCreateTime();
        LocalDateTime requestEndTime = null;
        if (InspectionOrderStatusEnum.CONFIRMED == order.getOrderStatus() || InspectionOrderStatusEnum.COMPLETED == order.getOrderStatus()) {
            // 以检验单更新为确认/完成后的更新时间作为确认时间近似
            requestEndTime = order.getUpdateTime();
        }

        LocalDateTime samplingStartTime = requestEndTime;
        LocalDateTime samplingEndTime = null;
        if (flags.isSampled()) {
            for (com.bmos.lims2.server.inspect.order.entity.Sample s : samples) {
                LocalDateTime rt = s.getReceiveTime();
                if (rt == null) { continue; }
                if (samplingEndTime == null || rt.isAfter(samplingEndTime)) {
                    samplingEndTime = rt;
                }
            }
        }

        LocalDateTime inspectionStartTime = taskMapper.selectEarliestStartTimeByOrderId(orderId);
        LocalDateTime inspectionEndTime = order.getOrderEndTime();

        LocalDateTime reportStartTime = inspectionEndTime;
        LocalDateTime reportEndTime = null;
        if (reported) {
            for (ReportGenerateTask t : tasks) {
                if (Boolean.TRUE.equals(t.getReportApproved())) {
                    LocalDateTime at = t.getReportApprovalTime();
                    if (at == null) { continue; }
                    if (reportEndTime == null || at.isAfter(reportEndTime)) {
                        reportEndTime = at;
                    }
                }
            }
        }

        resp.setRequestStartTime(requestStartTime);
        resp.setRequestEndTime(requestEndTime);
        resp.setSamplingStartTime(samplingStartTime);
        resp.setSamplingEndTime(samplingEndTime);
        resp.setInspectionStartTime(inspectionStartTime);
        resp.setInspectionEndTime(inspectionEndTime);
        resp.setReportStartTime(reportStartTime);
        resp.setReportEndTime(reportEndTime);

        StabilityPlanTimepointTask timepointTask = stabilityTimepointTaskMapper.selectByInspectionOrderId(orderId);
        if (timepointTask != null) {
            resp.setStabilityPlannedDate(timepointTask.getPlannedDate());
            StabilityInspectPlan plan = stabilityInspectPlanMapper.selectById(timepointTask.getPlanId());
            if (plan != null) {
                resp.setStabilityPlanCode(plan.getCode());
                resp.setStabilitySchemeName(plan.getSchemeName());
                resp.setStabilitySchemeVersionNo(plan.getSchemeVersionNo());
                StabilityScheme scheme = stabilitySchemeMapper.selectById(plan.getSchemeId());
                if (scheme != null) {
                    resp.setStabilitySchemeCode(scheme.getCode());
                }
                resp.setStabilityPlanCreator(UserUtils.getUserDisplayName(plan.getCreateBy()));
                resp.setStabilityPlanRemark(plan.getRemark());
            }
        }

        return resp;
    }

    @Override
    public java.util.List<ReportGeneratedItemDTO> listGeneratedReportsByOrderId(Long orderId) {
        return reportGenerateTaskMapper.selectGeneratedListByOrderId(orderId);
    }

    @Override
    public java.util.List<com.bmos.lims2.server.inspect.query.service.OrderSampleDTO> listOrderSamples(Long orderId) {
        InspectionOrderDTO order = inspectionOrderService.getInspectionOrderById(orderId);

        // 不走台账，统一按 lm_sample 查询，确保稳定性时间点子样品可见
        java.util.List<com.bmos.lims2.server.inspect.query.service.OrderSampleDTO> list = listSamplesByOrderId(orderId, order);
        if (cn.hutool.core.collection.CollectionUtil.isNotEmpty(list)) {
            return list;
        }

        // 稳定性时间点链路兜底（需要存在 timepointTask 关联）
        return listStabilityOrderSamples(orderId, order);
    }

    private java.util.List<com.bmos.lims2.server.inspect.query.service.OrderSampleDTO> listStabilityOrderSamples(Long orderId, InspectionOrderDTO order) {
        StabilityPlanTimepointTask task = stabilityTimepointTaskMapper.selectByInspectionOrderId(orderId);
        if (task == null) {
            return java.util.Collections.emptyList();
        }

        java.util.List<Sample> samples = sampleMapper.selectByInspectionOrderId(orderId);
        if (cn.hutool.core.collection.CollectionUtil.isEmpty(samples)) {
            return java.util.Collections.emptyList();
        }

        java.util.List<com.bmos.lims2.server.inspect.query.service.OrderSampleDTO> result = new java.util.ArrayList<>();
        for (Sample s : samples) {
            com.bmos.lims2.server.inspect.query.service.OrderSampleDTO dto = new com.bmos.lims2.server.inspect.query.service.OrderSampleDTO();
            dto.setId(s.getId());
            dto.setSampleNo(s.getSampleNo());
            dto.setParentSampleNo(s.getParentSampleNo());
            dto.setSampleName(s.getSampleName());
            dto.setUnitId(s.getUnitId());
            dto.setUnitName(unitCache.getGlobalUnitName(s.getUnitId()));
            dto.setQuantity(s.getQuantity());
            dto.setRecycleQuantity(s.getRecycleQuantity());
            dto.setConsumption(s.getConsumedQuantity());

            int status = 0;
            if (Boolean.TRUE.equals(s.getProcessed())) status = 6;
            else if (Boolean.TRUE.equals(s.getRecycled())) status = 5;
            else if (Boolean.TRUE.equals(s.getCollected())) status = 4;
            else if (Boolean.TRUE.equals(s.getDivided())) status = 3;
            else if (Boolean.TRUE.equals(s.getReceived())) status = 2;
            else if (Boolean.TRUE.equals(s.getSampled())) status = 1;
            dto.setStatus(status);

            String operatorName = null;
            java.time.LocalDateTime operateTime = null;
            if (Boolean.TRUE.equals(s.getProcessed())) {
                operatorName = s.getProcessBy();
                operateTime = s.getProcessTime();
            } else if (Boolean.TRUE.equals(s.getRecycled())) {
                operatorName = s.getRecycledBy();
                operateTime = s.getRecycledTime();
            } else if (Boolean.TRUE.equals(s.getCollected())) {
                operatorName = s.getCollectorName();
                operateTime = s.getCollectTime();
            } else if (Boolean.TRUE.equals(s.getDivided())) {
                operatorName = s.getDividerName();
                operateTime = s.getDivideTime();
            } else if (Boolean.TRUE.equals(s.getReceived())) {
                operatorName = s.getReceiverName();
                operateTime = s.getReceiveTime();
            } else if (Boolean.TRUE.equals(s.getSampled())) {
                operatorName = s.getSamplerName();
                operateTime = s.getSamplingTime();
            }
            dto.setOperatorName(operatorName);
            dto.setOperateTime(operateTime);

            if (order != null) {
                dto.setInspectionOrderNo(order.getOrderNo());
                dto.setBatchNo(order.getBatchNo());
                dto.setMaterialName(order.getMaterialName());
                dto.setMaterialCode(order.getMaterialCode());
                dto.setMaterialSpec(order.getMaterialSpec());
            }
            result.add(dto);
        }
        return result;
    }

    private java.util.List<com.bmos.lims2.server.inspect.query.service.OrderSampleDTO> listSamplesByOrderId(Long orderId, InspectionOrderDTO order) {
        java.util.List<Sample> samples = sampleMapper.selectByInspectionOrderId(orderId);
        if (cn.hutool.core.collection.CollectionUtil.isEmpty(samples)) {
            return java.util.Collections.emptyList();
        }

        java.util.List<com.bmos.lims2.server.inspect.query.service.OrderSampleDTO> result = new java.util.ArrayList<>();
        for (Sample s : samples) {
            com.bmos.lims2.server.inspect.query.service.OrderSampleDTO dto = new com.bmos.lims2.server.inspect.query.service.OrderSampleDTO();
            dto.setId(s.getId());
            dto.setSampleNo(s.getSampleNo());
            dto.setParentSampleNo(s.getParentSampleNo());
            dto.setSampleName(s.getSampleName());
            dto.setUnitId(s.getUnitId());
            dto.setUnitName(unitCache.getGlobalUnitName(s.getUnitId()));
            dto.setQuantity(s.getQuantity());
            dto.setRecycleQuantity(s.getRecycleQuantity());
            dto.setConsumption(s.getConsumedQuantity());

            int status = 0;
            if (Boolean.TRUE.equals(s.getProcessed())) status = 6;
            else if (Boolean.TRUE.equals(s.getRecycled())) status = 5;
            else if (Boolean.TRUE.equals(s.getCollected())) status = 4;
            else if (Boolean.TRUE.equals(s.getDivided())) status = 3;
            else if (Boolean.TRUE.equals(s.getReceived())) status = 2;
            else if (Boolean.TRUE.equals(s.getSampled())) status = 1;
            dto.setStatus(status);

            String operatorName = null;
            java.time.LocalDateTime operateTime = null;
            if (Boolean.TRUE.equals(s.getProcessed())) {
                operatorName = s.getProcessBy();
                operateTime = s.getProcessTime();
            } else if (Boolean.TRUE.equals(s.getRecycled())) {
                operatorName = s.getRecycledBy();
                operateTime = s.getRecycledTime();
            } else if (Boolean.TRUE.equals(s.getCollected())) {
                operatorName = s.getCollectorName();
                operateTime = s.getCollectTime();
            } else if (Boolean.TRUE.equals(s.getDivided())) {
                operatorName = s.getDividerName();
                operateTime = s.getDivideTime();
            } else if (Boolean.TRUE.equals(s.getReceived())) {
                operatorName = s.getReceiverName();
                operateTime = s.getReceiveTime();
            } else if (Boolean.TRUE.equals(s.getSampled())) {
                operatorName = s.getSamplerName();
                operateTime = s.getSamplingTime();
            }
            dto.setOperatorName(operatorName);
            dto.setOperateTime(operateTime);

            if (order != null) {
                dto.setInspectionOrderNo(order.getOrderNo());
                dto.setBatchNo(order.getBatchNo());
                dto.setMaterialName(order.getMaterialName());
                dto.setMaterialCode(order.getMaterialCode());
                dto.setMaterialSpec(order.getMaterialSpec());
            }
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<InspectItemTabDTO> listInspectItemTabs(Long orderId) {
        InspectionOrderDTO order = inspectionOrderService.getInspectionOrderById(orderId);
        if (order == null || order.getSchemeVersionId() == null) {
            return java.util.Collections.emptyList();
        }

        Map<Long, InspectItemTabDTO> map = new LinkedHashMap<>();

        if (com.bmos.lims2.common.enums.InspectionOrderSourceEnum.STABILITY == order.getSchemeSource()) {
            // 稳定性检验单：从稳定性方案检验项目表查
            List<com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO> items =
                    stabilitySchemeItemMapper.selectByVersionIdWithNames(order.getSchemeVersionId());
            for (com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO item : items) {
                if (item.getInspectItemId() == null) { continue; }
                if (!map.containsKey(item.getInspectItemId())) {
                    InspectItemTabDTO tab = new InspectItemTabDTO();
                    tab.setInspectItemId(item.getInspectItemId());
                    tab.setInspectItemCode(item.getInspectItemCode());
                    tab.setInspectItemName(item.getInspectItemName());
                    map.put(item.getInspectItemId(), tab);
                }
            }
        } else {
            // 常规检验单：从常规方案检验项目表查
            List<com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeItemDTO> items =
                    inspectionSchemeItemMapper.listWithAnalyzeItems(order.getSchemeVersionId());
            for (com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeItemDTO item : items) {
                if (item.getInspectItemId() == null) { continue; }
                if (!map.containsKey(item.getInspectItemId())) {
                    InspectItemTabDTO tab = new InspectItemTabDTO();
                    tab.setInspectItemId(item.getInspectItemId());
                    tab.setInspectItemCode(item.getInspectItemCode());
                    tab.setInspectItemName(item.getInspectItemName());
                    map.put(item.getInspectItemId(), tab);
                }
            }
        }

        return map.values().stream().collect(Collectors.toList());
    }

    @Override
    public java.util.List<EntryRecordsGroupedByAnalysisItemDTO> listEntriesByItem(EntryByItemQueryDTO queryDTO) {
        java.util.List<InspectionEntryRecordDTO> list = inspectionEntryRecordMapper.selectByInspectionOrderIdAndItemId(
                queryDTO.getInspectionOrderId(), queryDTO.getInspectItemId());

        java.util.Map<Long, EntryRecordsGroupedByAnalysisItemDTO> groupMap = new java.util.LinkedHashMap<>();
        for (InspectionEntryRecordDTO r : list) {
            Long parameterId = r.getParameterId();
            if (parameterId == null) { continue; }
            EntryRecordsGroupedByAnalysisItemDTO group = groupMap.get(parameterId);
            if (group == null) {
                group = new EntryRecordsGroupedByAnalysisItemDTO();
                group.setParameterId(r.getParameterId());
                group.setParameterCode(r.getParameterCode());
                group.setParameterName(r.getParameterName());
                // 将任务级字段上移到分析项分组层级
                group.setIsReportable(r.getIsReportable());
                group.setIsExecutable(r.getIsExecutable());
                group.setStandardRule(r.getStandardRule());
                group.setRecordId(r.getRecordId());
                group.setJudgedResult(r.getJudgedResult());
                group.setJudgedTime(r.getJudgedTime());
                group.setReviewedBy(r.getReviewedBy());
                group.setReviewedTime(r.getReviewedTime());
                // 分析项级：检验时间/检验员/录入时间/状态/异常
                group.setTestTime(r.getTestTime());
                group.setOwnerId(r.getOwnerId());
                group.setTaskId(r.getTaskId());
                String ownerName = r.getOwnerName();
                if (ownerName == null && r.getOwnerId() != null) {
                    ownerName = com.bmos.lims2.server.platform.util.UserUtils.getUsername(String.valueOf(r.getOwnerId()));
                }
                group.setOwnerName(ownerName);
                group.setEntryTime(r.getEntryTime());
                group.setStatus(r.getStatus());
                group.setAbnormal(r.getAbnormal());
                group.setExecuteMethod(r.getExecuteMethod());
                group.setRecords(new java.util.ArrayList<>());
                groupMap.put(parameterId, group);
            }
            // reviewedByName 由 getReviewedByName 从 reviewedBy 计算，这里无需显式置空
            group.getRecords().add(r);
        }
        return new java.util.ArrayList<>(groupMap.values());
    }
}



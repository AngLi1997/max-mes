package com.bmos.lims2.server.inspect.audit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.enums.*;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.audit.dto.FlowStartDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.inspect.audit.dto.SampleAuditPageQueryDTO;
import com.bmos.lims2.server.inspect.audit.service.SampleAuditService;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryRecordMapper;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.dto.OrderPageDTO;
import com.bmos.lims2.server.inspect.audit.dto.SampleAuditTaskDTO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.task.mapper.TaskStatusHistoryMapper;
import com.bmos.mybatis.page.CommonPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SampleAuditServiceImpl implements SampleAuditService {

    @Autowired
    private  InspectionOrderMapper inspectionOrderMapper;
    @Autowired
    private  InspectionEntryRecordMapper inspectionEntryRecordMapper;
    @Autowired
    private  FlowAuditService flowAuditService;
    @Autowired
    private  com.bmos.lims2.server.task.mapper.TaskMapper taskMapper;
    @Autowired
    private  TaskStatusHistoryMapper taskStatusHistoryMapper;
    @Autowired
    private  com.bmos.lims2.server.inspect.order.mapper.InspectionOrderCustomFieldMapper inspectionOrderCustomFieldMapper;
    @Autowired
    private  com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeJudgmentMapper judgmentMapper;
    @Autowired
    private  com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper inspectionSchemeParameterMapper;
    @Autowired
    private  com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper stabilitySchemeParameterMapper;
    @Autowired
    private AuditOperationLogService auditOperationLogService;
    @Autowired
    private com.bmos.lims2.server.inspect.mes.client.MesInspectCallbackClient mesInspectCallbackClient;
    @Autowired
    private com.bmos.lims2.server.inspect.mes.client.WmsInspectCallbackClient wmsInspectCallbackClient;
    @Autowired
    private com.bmos.lims2.server.inspect.mes.MesCallbackAssembler mesCallbackAssembler;


    @Override
    public CommonPage<SampleAuditTaskDTO> pagePendingSampleAuditOrders(SampleAuditPageQueryDTO queryDTO) {
        // 1) 先按业务条件拉出候选订单（必须已发起过流程且未完成）
        List<OrderPageDTO> candidates = inspectionOrderMapper.selectPendingSampleAuditOrders(queryDTO);
        if (candidates == null || candidates.isEmpty()) {
            return CommonPage.convertPage(java.util.Collections.emptyList());
        }

        // 2) 组织业务key列表
        java.util.Map<Long, OrderPageDTO> map = new java.util.HashMap<>();
        java.util.List<String> businessKeys = new java.util.ArrayList<>();
        for (OrderPageDTO o : candidates) {
            map.put(o.getId(), o);
            businessKeys.add(String.valueOf(o.getId()));
        }

        // 3) 查询工作流待办（按category SAMPLE_AUDIT + businessKey 列表）
        com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO flowDto = new com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO();
        flowDto.setCategory(com.bmos.lims2.common.enums.AuditCategoryCodeEnum.SAMPLE_AUDIT.getCode());
        flowDto.setCurrent(queryDTO.getPageNum());
        flowDto.setSize(queryDTO.getPageSize());
        flowDto.setBusinessKeyList(businessKeys);
        com.bmos.audit.engine.core.query.resp.PageQueryResp<java.util.List<com.bmos.audit.engine.core.query.resp.TaskListResp>> todo = flowAuditService.queryToDoListByCategory(flowDto);

        if (todo.getTotal() == 0) {
            return CommonPage.convertPage(java.util.Collections.emptyList());
        }
        // 4) 将工作流任务信息合并到业务数据结构（如需返回更丰富数据可改用 SampleAuditListDTO）
        java.util.List<SampleAuditTaskDTO> resultList = new java.util.ArrayList<>();
        for (com.bmos.audit.engine.core.query.resp.TaskListResp task : todo.getData()) {
            Long orderId = Long.valueOf(task.getBusinessKey());
            OrderPageDTO base = map.get(orderId);
            if (base != null) {
                SampleAuditTaskDTO dto = new SampleAuditTaskDTO();
                dto.setId(base.getId());
                dto.setOrderNo(base.getOrderNo());
                dto.setMaterialName(base.getMaterialName());
                dto.setMaterialCode(base.getMaterialCode());
                dto.setMaterialSpec(base.getMaterialSpec());
                dto.setBatchNo(base.getBatchNo());
                dto.setCreateTime(base.getCreateTime());
                dto.setCreateBy(base.getCreateBy());
                dto.setRemark(base.getRemark());
                dto.setTaskId(task.getTaskId());
                dto.setProcessInstanceId(task.getProcessInstanceId());
                dto.setCurrentNodeName(task.getElementName());
                dto.setInitiateTime(task.getProcessStartTime());
                dto.setInitiator(task.getProcessStartBy());
                dto.setDeploymentId(task.getDeploymentId());
                dto.setPayload(task.getPayload());
                dto.setExecutionId(task.getExecutionId());
                dto.setRequestTime(base.getRequestTime());
                dto.setRequestUserId(base.getRequestUserId());
                resultList.add(dto);
            }
        }

        com.bmos.mybatis.page.CommonPage<SampleAuditTaskDTO> page = new com.bmos.mybatis.page.CommonPage<>();
        page.setPageNum(queryDTO.getPageNum());
        page.setPageSize(queryDTO.getPageSize());
        page.setTotal(todo.getTotal().intValue());
        page.setList(resultList);
        return page;
    }

    @Override
    public InspectionOrderEntryDTO getSampleAuditDetail(Long orderId) {
        if (orderId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_ORDER_NOT_EXITS);
        }
        InspectionOrderDTO inspectionOrderDTO = inspectionOrderMapper.selectByIdWithRelation(orderId);
        InspectionOrderEntryDTO result = BeanUtil.copyProperties(inspectionOrderDTO, InspectionOrderEntryDTO.class);
        // 自定义字段集合
        java.util.List<com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField> cfEntities =
                inspectionOrderCustomFieldMapper.selectByInspectionOrderId(orderId);
        if (cfEntities != null && !cfEntities.isEmpty()) {
            java.util.List<com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO> cfList = new java.util.ArrayList<>();
            for (com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField e : cfEntities) {
                com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO cf = new com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO();
                cf.setFieldCode(e.getFieldCode());
                cf.setFieldName(e.getFieldName());
                cf.setFieldValue(e.getFieldValue());
                cf.setRequired(e.getRequired());
                cfList.add(cf);
            }
            result.setCustomFields(cfList);
        }
        // 加载子任务与数据点：根据来源（常规 vs 稳定性）选择对应的查询
        InspectionOrder order = inspectionOrderMapper.selectById(orderId);
        boolean isStability = order != null && InspectionOrderSourceEnum.STABILITY == order.getSchemeSource();
        List<InspectionOrderEntryDTO.AnalysisItemEntryItemDTO> tasks = inspectionEntryRecordMapper.selectSampleAuditTasksByInspectionOrder(orderId);
        for (InspectionOrderEntryDTO.AnalysisItemEntryItemDTO t : tasks) {
            t.setDataPoints(isStability
                    ? inspectionEntryRecordMapper.selectByTaskIdForStability(t.getId())
                    : inspectionEntryRecordMapper.selectByTaskId(t.getId()));
            if (t.getParameterConfigId() != null) {
                if (isStability) {
                    com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO pCfg =
                            stabilitySchemeParameterMapper.getByParameterConfigId(t.getParameterConfigId());
                    if (pCfg != null) {
                        t.setStandardRule(pCfg.getStandardRule());
                    }
                } else {
                    com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeParameterDTO pCfg =
                            inspectionSchemeParameterMapper.getByParameterConfigId(t.getParameterConfigId());
                    if (pCfg != null) {
                        t.setStandardRule(pCfg.getStandardRule());
                    }
                }
            }
        }
//        result.setInspectionTasks(tasks);

        // 按检验项目分组任务，便于前端分组展示
        java.util.Map<Long, com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO.InspectItemTaskGroupDTO> groupMap = new java.util.LinkedHashMap<>();
        for (InspectionOrderEntryDTO.AnalysisItemEntryItemDTO t : tasks) {
            Long key = t.getInspectItemId() == null ? -1L : t.getInspectItemId();
            com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO.InspectItemTaskGroupDTO group = groupMap.get(key);
            if (group == null) {
                group = new com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO.InspectItemTaskGroupDTO();
                group.setInspectItemId(t.getInspectItemId());
                group.setInspectItemName(t.getInspectItemName());
                group.setInspectItemCode(t.getInspectItemCode());
                group.setTasks(new java.util.ArrayList<>());
                groupMap.put(key, group);
            }
            group.getTasks().add(t);
        }
        result.setInspectItemTaskGroups(new java.util.ArrayList<>(groupMap.values()));
        // 样品审核详情：仅返回分组后的任务，不返回平铺任务
//        result.setInspectionTasks(null);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startSampleAudit(Long orderId) {
        InspectionOrder order = inspectionOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_ORDER_NOT_EXITS);
        }
        // 订单状态流转校验：应允许 CONFIRMED -> SAMPLE_AUDIT_PENDING
        com.bmos.lims2.server.inspect.order.util.OrderTransitionGuard.checkOrThrow(order.getOrderStatus(), InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING);

        FlowStartDTO startDTO = new FlowStartDTO();
        startDTO.setBusinessKey(String.valueOf(orderId));
        startDTO.setCode(AuditCategoryCodeEnum.SAMPLE_AUDIT.getCode());
        startDTO.setCategoryCode(AuditCategoryCodeEnum.SAMPLE_AUDIT.getCode());
        startDTO.setName(order.getOrderNo());
        startDTO.setExtField(order.getBatchNo());
        String processInstanceId = flowAuditService.flowAuditStart(startDTO);

        // 记录到订单，设置状态为样品待审核（状态流转守卫）
        com.bmos.lims2.server.inspect.order.util.OrderTransitionGuard.checkOrThrow(order.getOrderStatus(), com.bmos.lims2.common.enums.InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING);
        InspectionOrder update = new InspectionOrder();
        update.setId(order.getId());
        update.setSampleAuditProcessInstanceId(processInstanceId);
        update.setOrderStatus(InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING);
        inspectionOrderMapper.updateById(update);

        // 同步冻结该检验单下所有任务到 SAMPLE_AUDIT_PENDING（排除已终止），并记录历史
        java.util.List<com.bmos.lims2.server.task.entity.Task> tasks = taskMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.bmos.lims2.server.task.entity.Task>()
                        .eq(com.bmos.lims2.server.task.entity.Task::getInspectionOrderId, order.getId())
                        .ne(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.TERMINATED)
        );
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<com.bmos.lims2.server.task.entity.Task> uw = new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        uw.eq(com.bmos.lims2.server.task.entity.Task::getInspectionOrderId, order.getId())
          .ne(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.TERMINATED)
          .set(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.SAMPLE_AUDIT_PENDING);
        taskMapper.update(null, uw);

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        for (com.bmos.lims2.server.task.entity.Task t : tasks) {
            // 状态流转守卫
            com.bmos.lims2.server.task.util.TaskTransitionGuard.checkOrThrow(t.getStatus(), com.bmos.lims2.common.enums.TaskStatusEnum.SAMPLE_AUDIT_PENDING);
            com.bmos.lims2.server.task.entity.TaskStatusHistory h = new com.bmos.lims2.server.task.entity.TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(com.bmos.lims2.common.enums.TaskOperationTypeEnum.SAMPLE_AUDIT_SUBMIT);
            h.setFromStatus(t.getStatus() == null ? null : t.getStatus().getValue());
            h.setToStatus(com.bmos.lims2.common.enums.TaskStatusEnum.SAMPLE_AUDIT_PENDING.getValue());
            h.setOperatorId(com.bmos.common.holder.SysUserHolder.getUser().getUserId());
            h.setOperateTime(now);
            taskStatusHistoryMapper.insert(h);
        }
        return processInstanceId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessSuccessCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName) {
        // 审核通过：标记订单样品审核完成
        LambdaQueryWrapper<InspectionOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(InspectionOrder::getSampleAuditProcessInstanceId, processInstanceId);
        InspectionOrder order = inspectionOrderMapper.selectOne(qw);
        if (order == null) {
            log.warn("未找到样品审核订单，流程实例ID:{}", processInstanceId);
            return;
        }
        LocalDateTime now = LocalDateTime.now();

        // 订单状态流转校验：SAMPLE_AUDIT_PENDING -> COMPLETED
        com.bmos.lims2.server.inspect.order.util.OrderTransitionGuard.checkOrThrow(order.getOrderStatus(), InspectionOrderStatusEnum.COMPLETED);

        // 1) 标记订单样品审核完成并置状态，同时设置检验完成
        InspectionOrder update = new InspectionOrder();
        update.setId(order.getId());
        update.setSampleAuditTime(now);
        update.setOrderStatus(InspectionOrderStatusEnum.COMPLETED);
        update.setFinished(Boolean.TRUE);
        update.setFinishedTime(now);
        inspectionOrderMapper.updateById(update);

        // 查询这些任务以记录状态历史（排除已终止的任务）
        java.util.List<com.bmos.lims2.server.task.entity.Task> tasks = taskMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.bmos.lims2.server.task.entity.Task>()
                        .eq(com.bmos.lims2.server.task.entity.Task::getInspectionOrderId, order.getId())
                        .ne(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.TERMINATED)
        );

        // 2) 任务状态设置为 COMPLETED，并记录任务状态变更日志（样品审核通过）
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<com.bmos.lims2.server.task.entity.Task> uw = new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        uw.eq(com.bmos.lims2.server.task.entity.Task::getInspectionOrderId, order.getId())
                .ne(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.TERMINATED)
                .set(com.bmos.lims2.server.task.entity.Task::getStatus, TaskStatusEnum.COMPLETED)
                .set(com.bmos.lims2.server.task.entity.Task::getSampleAuditBy, userId)
                .set(com.bmos.lims2.server.task.entity.Task::getSampleAuditTime, now);
        taskMapper.update(null, uw);

        for (com.bmos.lims2.server.task.entity.Task t : tasks) {
            // 状态流转守卫
            com.bmos.lims2.server.task.util.TaskTransitionGuard.checkOrThrow(t.getStatus(), com.bmos.lims2.common.enums.TaskStatusEnum.COMPLETED);
            com.bmos.lims2.server.task.entity.TaskStatusHistory h = new com.bmos.lims2.server.task.entity.TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(com.bmos.lims2.common.enums.TaskOperationTypeEnum.APPROVAL_PASS);
            h.setFromStatus(t.getStatus() == null ? null : t.getStatus().getValue());
            h.setToStatus(TaskStatusEnum.COMPLETED.getValue());
            h.setOperatorId(userId);
            h.setOperateTime(now);
            h.setNodeName(nodeName);
            h.setComment(comment);
            h.setRemark(remark);
            taskStatusHistoryMapper.insert(h);
        }

        // 回传上游检验结果（样品审核通过=检验完成）。本期回传失败仅记日志、不阻断审核（设计 §0.5）。
        // 根据 order.sourceSystem 决定回到 MES 还是 WMS（默认 MES，兼容旧数据）。
        try {
            com.bmos.mes.inspect.dto.InspectResultCallBackDTO callback = mesCallbackAssembler.assemble(order);
            if ("WMS".equalsIgnoreCase(order.getSourceSystem())) {
                wmsInspectCallbackClient.inspectCallBack(toWmsCallback(callback));
            } else {
                mesInspectCallbackClient.inspectCallBack(callback);
            }
        } catch (Exception ex) {
            log.error("回传上游检验结果失败，orderNo={}, sourceSystem={}", order.getOrderNo(), order.getSourceSystem(), ex);
        }
    }

    /** MES InspectResultCallBackDTO → WMS 同款 DTO（同字段名，只是包名不同）。 */
    private static com.bmos.wms.inspect.dto.InspectResultCallBackDTO toWmsCallback(com.bmos.mes.inspect.dto.InspectResultCallBackDTO src) {
        com.bmos.wms.inspect.dto.InspectResultCallBackDTO dst = new com.bmos.wms.inspect.dto.InspectResultCallBackDTO();
        dst.setInspectNo(src.getInspectNo());
        dst.setResult(src.getResult());
        dst.setClosed(src.getClosed());
        if (src.getInspectResultItemDTOS() != null) {
            java.util.List<com.bmos.wms.inspect.dto.InspectResultItemDTO> items = new java.util.ArrayList<>(src.getInspectResultItemDTOS().size());
            for (com.bmos.mes.inspect.dto.InspectResultItemDTO i : src.getInspectResultItemDTOS()) {
                com.bmos.wms.inspect.dto.InspectResultItemDTO w = new com.bmos.wms.inspect.dto.InspectResultItemDTO();
                w.setInspectProgramNo(i.getInspectProgramNo());
                w.setAlreadyConvertProgramNo(i.getAlreadyConvertProgramNo());
                w.setInspectProgramName(i.getInspectProgramName());
                w.setInspectResult(i.getInspectResult());
                w.setInspectConclusion(i.getInspectConclusion());
                items.add(w);
            }
            dst.setInspectResultItemDTOS(items);
        }
        return dst;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName) {
        LambdaQueryWrapper<InspectionOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(InspectionOrder::getSampleAuditProcessInstanceId, processInstanceId);
        InspectionOrder order = inspectionOrderMapper.selectOne(qw);
        if (order == null) {
            log.warn("未找到样品审核订单(拒绝)，流程实例ID:{}", processInstanceId);
            return;
        }

        // 保存审核操作日志
        saveHistoryLog(comment, remark, userId, order.getId(), OperationType.REJECT_AUDIT, nodeName);

        // 查询该订单下的任务并记录任务状态历史
        java.util.List<com.bmos.lims2.server.task.entity.Task> tasks = taskMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.bmos.lims2.server.task.entity.Task>()
                        .eq(com.bmos.lims2.server.task.entity.Task::getInspectionOrderId, order.getId())
                        .ne(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.TERMINATED)
        );

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        for (com.bmos.lims2.server.task.entity.Task t : tasks) {
            com.bmos.lims2.server.task.entity.TaskStatusHistory h = new com.bmos.lims2.server.task.entity.TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(com.bmos.lims2.common.enums.TaskOperationTypeEnum.APPROVAL_REJECT);
            h.setFromStatus(t.getStatus() == null ? null : t.getStatus().getValue());
            h.setToStatus(t.getStatus() == null ? null : t.getStatus().getValue()); // 拒绝时状态可能不变
            h.setOperatorId(userId);
            h.setOperateTime(now);
            h.setNodeName(nodeName);
            h.setComment(comment);
            h.setRemark(remark);
            taskStatusHistoryMapper.insert(h);
        }
    }


    private void saveHistoryLog(String comment, String remark, String userId, Long id, OperationType operationType, String nodeName) {
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.SAMPLE_AUDIT.name())
                .businessId(id)
                .operationType(operationType.getValue())
                .remark(remark)
                .nodeName(nodeName)
                .comment(comment)
                .createBy(userId)
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTasks(com.bmos.lims2.server.inspect.audit.dto.SampleAuditRejectDTO dto) {
        if (dto == null || dto.getInspectionOrderId() == null || dto.getTaskIds() == null || dto.getTaskIds().isEmpty()) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "参数不完整");
        }
        // 校验任务归属与状态
        List<com.bmos.lims2.server.task.entity.Task> tasks = taskMapper.selectBatchIds(dto.getTaskIds());
        if (tasks.stream().anyMatch(t -> t == null || !dto.getInspectionOrderId().equals(t.getInspectionOrderId()))) {
            throw new BmosException(LimsResponseCode.TASK_ERROR_ID_EMPTY, "存在不属于该检验单的任务");
        }

        InspectionOrder order = inspectionOrderMapper.selectById(dto.getInspectionOrderId());

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String userId = com.bmos.common.holder.SysUserHolder.getUser().getUserId();
        // 订单状态流转校验：SAMPLE_AUDIT_PENDING -> SAMPLE_AUDIT_REJECTED
        com.bmos.lims2.server.inspect.order.util.OrderTransitionGuard.checkOrThrow(order.getOrderStatus(), InspectionOrderStatusEnum.SAMPLE_AUDIT_REJECTED);

        // 标记订单样品审核拒绝
        InspectionOrder update = new InspectionOrder();
        update.setId(order.getId());
        update.setOrderStatus(InspectionOrderStatusEnum.CONFIRMED);
        inspectionOrderMapper.updateById(update);

        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<com.bmos.lims2.server.task.entity.Task> uw = new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        uw.eq(com.bmos.lims2.server.task.entity.Task::getInspectionOrderId, order.getId())
                .in(com.bmos.lims2.server.task.entity.Task::getId, dto.getTaskIds())
                .ne(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.TERMINATED)
                .set(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.SAMPLE_AUDIT_REJECTED)
                .set(com.bmos.lims2.server.task.entity.Task::getSampleAuditBy, userId)
                .set(com.bmos.lims2.server.task.entity.Task::getSampleAuditTime, now)
                .set(com.bmos.lims2.server.task.entity.Task::getRejectReason, dto.getReason());
        taskMapper.update(null, uw);


        // 其他任务的状态变为复核通过
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<com.bmos.lims2.server.task.entity.Task> nw = new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        nw.eq(com.bmos.lims2.server.task.entity.Task::getInspectionOrderId, order.getId())
                .notIn(com.bmos.lims2.server.task.entity.Task::getId, dto.getTaskIds())
                .ne(com.bmos.lims2.server.task.entity.Task::getStatus, com.bmos.lims2.common.enums.TaskStatusEnum.TERMINATED)
                .set(com.bmos.lims2.server.task.entity.Task::getStatus, TaskStatusEnum.REVIEW_PASSED)
                .set(com.bmos.lims2.server.task.entity.Task::getSampleAuditBy, userId)
                .set(com.bmos.lims2.server.task.entity.Task::getSampleAuditTime, now)
                .set(com.bmos.lims2.server.task.entity.Task::getRejectReason, dto.getReason());
        taskMapper.update(null, nw);

        for (com.bmos.lims2.server.task.entity.Task t : tasks) {
            // 状态流转守卫
            com.bmos.lims2.server.task.util.TaskTransitionGuard.checkOrThrow(t.getStatus(), com.bmos.lims2.common.enums.TaskStatusEnum.SAMPLE_AUDIT_REJECTED);
            com.bmos.lims2.server.task.entity.TaskStatusHistory h = new com.bmos.lims2.server.task.entity.TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(com.bmos.lims2.common.enums.TaskOperationTypeEnum.APPROVAL_REJECT);
            h.setFromStatus(t.getStatus() == null ? null : t.getStatus().getValue());
            h.setToStatus(com.bmos.lims2.common.enums.TaskStatusEnum.SAMPLE_AUDIT_REJECTED.getValue());
            h.setOperatorId(userId);
            h.setOperateTime(now);
            h.setComment(dto.getReason());
            taskStatusHistoryMapper.insert(h);
        }

        // 调用工作流：审批不通过
        com.bmos.lims2.server.audit.dto.CompleteDTO completeDTO = new com.bmos.lims2.server.audit.dto.CompleteDTO();
        completeDTO.setProcessInstanceId(dto.getProcessInstanceId());
        completeDTO.setTaskId(dto.getTaskId());
        completeDTO.setRemark(dto.getReason());
        completeDTO.setComment(dto.getComment());
        this.flowAuditService.flowAuditCompleteNotApprove(completeDTO);
    }
}



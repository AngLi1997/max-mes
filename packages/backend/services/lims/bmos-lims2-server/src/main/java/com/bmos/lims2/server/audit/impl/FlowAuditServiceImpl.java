package com.bmos.lims2.server.audit.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.command.BackToPrevCmd;
import com.bmos.audit.engine.core.command.CompleteBatchTaskCmd;
import com.bmos.audit.engine.core.command.DeployDeploymentCmd;
import com.bmos.audit.engine.core.command.StartProcessInstanceCmd;
import com.bmos.audit.engine.core.db.repository.ProcessRepository;
import com.bmos.audit.engine.core.model.AuditDeployment;
import com.bmos.audit.engine.core.model.AuditExecutionInstance;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.audit.engine.core.model.AuditTaskInstance;
import com.bmos.audit.engine.core.query.cmd.*;
import com.bmos.audit.engine.core.query.resp.*;
import com.bmos.audit.engine.core.query.service.AuditDeploymentQueryService;
import com.bmos.audit.engine.core.query.service.AuditExecutionQueryService;
import com.bmos.audit.engine.core.query.service.AuditProcessInstanceQueryService;
import com.bmos.audit.engine.core.query.service.AuditTaskQueryService;
import com.bmos.audit.engine.core.service.AuditDeploymentService;
import com.bmos.audit.engine.core.service.AuditExecutionService;
import com.bmos.audit.engine.core.service.AuditProcessInstanceService;
import com.bmos.audit.engine.core.service.AuditTaskInstanceService;
import com.bmos.audit.engine.core.state.ProcessState;
import com.bmos.audit.engine.core.validator.ElementValidateConfig;
import com.bmos.audit.engine.core.validator.rule.PayloadValidateRule;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.enums.EnumUtils;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.lims2.common.constants.AuditMessageConstant;
import com.bmos.lims2.common.enums.*;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.*;
import com.bmos.lims2.server.audit.builder.AuditCategoryServiceEnum;
import com.bmos.lims2.server.audit.builder.AuditDataConditionBuilder;
import com.bmos.lims2.server.audit.convert.*;
import com.bmos.lims2.server.audit.dto.*;
import com.bmos.lims2.server.audit.entity.FlowAudit;
import com.bmos.lims2.server.audit.entity.FlowAuditMessage;
import com.bmos.lims2.server.audit.entity.FlowAuditProcess;
import com.bmos.lims2.server.audit.entity.FlowAuditVersion;
import com.bmos.lims2.server.audit.mapper.FlowAuditMapper;
import com.bmos.lims2.server.audit.mapper.FlowAuditProcessMapper;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.audit.utils.AuditMessageSendUtils;
import com.bmos.lims2.server.audit.validate.UserTaskPayloadValidateRule;
import com.bmos.lims2.server.audit.vo.*;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlowAuditServiceImpl implements FlowAuditService {

    @Autowired
    private FlowAuditMapper auditMapper;

    @Autowired
    private FlowAuditVersionService versionService;

    @Autowired
    private FlowAuditUserService flowAuditUserService;

    @Autowired
    private FlowAuditMessageService messageService;

    @Autowired
    private AuditDeploymentService deploymentService;

    @Autowired
    private AuditDeploymentQueryService queryService;

    @Autowired
    private AuditTaskQueryService taskQueryService;

    @Autowired
    private AuditProcessInstanceQueryService instanceQueryService;

    @Autowired
    private AuditProcessInstanceService instanceService;

    @Autowired
    private AuditTaskInstanceService taskInstanceService;

    @Autowired
    private AuditExecutionQueryService executionQueryService;

    @Autowired
    private AuditExecutionService executionService;

    @Autowired
    private FlowAuditCategoryService categoryService;

    @Autowired
    private AuditOperationLogService operationHistoryService;

    @Autowired
    private FlowAuditProcessMapper flowAuditProcessMapper;

    @Override
    public CommonPage<FlowAuditVO> flowAuditPage(AuditPageDTO dto) {
        try {
            List<FlowAuditVO> flowAuditList = null;
            if (ObjectUtil.isNotNull(dto.getId())) {
                flowAuditList = versionService.selectVersionList(dto);
            } else {
                if (StrUtil.isNotBlank(dto.getCategoryCode())) {
                    dto.setCategoryCodeList(categoryService.queryByCode(dto.getCategoryCode()));
                }
                PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
                flowAuditList = auditMapper.selectAuditList(dto);
                if (CollUtil.isNotEmpty(flowAuditList)) {
                    FlowAuditConvert.INSTANCE.convertToAuditVo(flowAuditList);
                }
            }
            return CommonPage.convertPage(flowAuditList);
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }
    }

    @Override
    public Boolean checkoutDeployment(FlowCheckoutDTO dto) {
        PayloadValidateRule userTaskPayloadValidateRule = new UserTaskPayloadValidateRule(dto.getUserList(), dto.getMegUserList());
        deploymentService.validate(dto.getFlowAuditModel(), new ElementValidateConfig(userTaskPayloadValidateRule));
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFlowAudit(SaveAuditDTO dto) {
        if (!deploymentPublishOrNot(dto.getDeploymentId())) {
            throw new BmosException(LimsResponseCode.FLOW_HAVE_PUBLISHED);
        }
        try {
            if (ObjectUtil.isNull(dto.getFlowAuditId())) {
                dto.setFlowAuditId(CustomIdGenerator.nextId());
                dto.setCode(String.valueOf(CustomIdGenerator.nextId()));
            }
            //保存流程
            dto.setDeploymentId(createDeployment(dto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_CREATE_ERROR);
        }
        saveFlowAuditData(dto);
    }

    @Override
    public FlowAuditDetailVO detailFlowAudit(Long versionId) {
        try {
            FlowAuditDetailVO flowVersion = versionService.findVersionById(versionId);
            FlowAudit audit = auditMapper.findAuditById(flowVersion.getFlowAuditId());
            flowVersion.setCode(audit.getCode());
            flowVersion.setName(audit.getName());
            flowVersion.setCategoryCode(audit.getCategoryCode());
            AuditDeployment deployment = queryService.findByDeploymentId(flowVersion.getDeploymentId());
            flowVersion.setFlowAuditModel(deployment.getMetaInfo());
            flowVersion.setAuditUserList(flowAuditUserService.queryListByDeploymentId(flowVersion.getDeploymentId()));
            flowVersion.setAuditMegDTOList(messageService.queryListByDeploymentId(flowVersion.getDeploymentId()));
            return flowVersion;
        } catch (Exception e) {
            log.error("流程查询异常",e);
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }

    }

    @Override
    @DistributedLock(expression = "#dto.businessKey")
    @Transactional(rollbackFor = Exception.class)
    public String flowAuditStart(FlowStartDTO dto) {
        if (StrUtil.isBlank(dto.getCode()) || StrUtil.isBlank(dto.getName())) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }
        String deploymentId = auditMapper.findDeploymentIdByCode(dto.getCode(), dto.getCategoryCode());
        if (StrUtil.isBlank(deploymentId)) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_NOT_ERROR);
        }
        StartProcessInstanceCmd cmd = DeploymentConvert.INSTANCE.convertToStartCmd(deploymentId, dto);
        if (StrUtil.isBlank(cmd.getStartBy())) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_START_USER_ERROR);
        }
        StartProcessInstanceResp resp = instanceService.startProcessInstance(cmd);
        SendMessageDTO message = new SendMessageDTO();
        message.setAuditCategoryCode(dto.getCategoryCode());
        message.setBusinessId(Long.valueOf(dto.getBusinessKey()));
        message.setDeploymentId(resp.getDeploymentId());
        message.setNodeId(resp.getKey());
        message.setNodeName(I18nUtils.getCodeMessage(AuditMessageConstant.AUDIT_START_I18N_CODE, AuditMessageConstant.AUDIT_START, null) + I18nUtils.getEnumMessage(AuditCategoryServiceEnum.getEnumByCode(dto.getCode())));
        message.setIsStart(Boolean.TRUE);
        AuditMessageSendUtils.sendMessage(message);
        return resp.getProcessInstanceId();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean flowAuditComplete(CompleteDTO dto) {
        if (StrUtil.isBlank(dto.getProcessInstanceId()) || StrUtil.isBlank(dto.getTaskId())) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }
        CompleteBatchTaskCmd cmd = FlowCompleteConvert.INSTANCE.convertToCompleteCmd(dto);
        taskInstanceService.completeBatch(cmd);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean flowAuditCompleteNotApprove(CompleteDTO dto) {
        if (StrUtil.isBlank(dto.getProcessInstanceId()) || StrUtil.isBlank(dto.getTaskId())) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }
        try {
            CompleteBatchTaskCmd cmd = FlowCompleteConvert.INSTANCE.convertToCompleteCmd(dto);
            taskInstanceService.completeNotApprove(cmd);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("流程审批不通过错误：{}", e.getCause() + e.getMessage());
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_COMPLETE_ERROR);
        }
    }

    @Override
    public PageQueryResp<List<TaskListResp>> queryToDoListByCategory(FlowAuditTaskDTO dto) {
        if (Objects.nonNull(dto.getCategory())) {
            PageHelper.orderBy(dto.getBusinessKeyOrderSql());
            List<String> businessKeyList = AuditDataConditionBuilder.build(dto.getCategory()).queryBusinessListByCategory();
            // 权限为空 返回
            if (CollUtil.isEmpty(businessKeyList)) {
                return PageQueryResp.<List<TaskListResp>>build().setData(Collections.emptyList()).setTotal(0L);
            }
            // 权限和搜索条件结果取交集
            if (CollUtil.isNotEmpty(dto.getBusinessKeyList())) {
                businessKeyList.retainAll(dto.getBusinessKeyList());
            }
            dto.setBusinessKeyList(businessKeyList);
            if (CollUtil.isEmpty(dto.getBusinessKeyList())) {
                return PageQueryResp.<List<TaskListResp>>build().setData(Collections.emptyList()).setTotal(0L);
            }
        }
        PageListTaskQueryCmd cmd = FlowCompleteConvert.INSTANCE.convertToQueryCmd(dto);
        return taskQueryService.findListToDoByAssigneesAndCategory(cmd);
    }


    @Override
    public FlowAuditHistoryVO listFlowAuditHistory(FlowAuditHistoryDTO dto) {
        if (StrUtil.isBlank(dto.getDeploymentId())) {
            AuditProcessInstance instance = instanceQueryService.findByProcessInstanceIdAndState(dto.getProcessInstanceId(), ProcessState.ACTIVE);
            if (ObjectUtil.isEmpty(instance)) {
                throw new BmosException(LimsResponseCode.FLOW_AUDIT_SELECT_ERROR);
            }
            dto.setDeploymentId(instance.getDeploymentId());
        }
        AuditDeployment deployment = queryService.findByDeploymentId(dto.getDeploymentId());
        FlowAuditHistoryVO vo = new FlowAuditHistoryVO();
        if (ObjectUtil.isNotEmpty(deployment)) {
            vo.setMetaInfo(deployment.getMetaInfo());
            //查询每个节点的状态信息
            List<AuditExecutionInstance> execution = executionQueryService.findByDeploymentVersionId(deployment.getDeploymentVersionId());
            //AuditExecutionInstance instance = CollectionUtils.findFirst(execution, item -> item.getState().equals(1));
            TaskHistoryCmd cmd = TaskHistoryCmd.builder().processInstanceId(dto.getProcessInstanceId());
            List<TaskHistoryResp> taskHistoryList = taskQueryService.findActiveByProcessInstanceId(cmd);
            taskHistoryList.forEach(item -> {
                if (StrUtil.equals(item.getDeleteReason(), FlowStateEnum.APPROVE_REJECT.getCode())) {
                    item.setState(Integer.valueOf(FlowStateEnum.APPROVE_REJECT.getState()));
                }
                if (StrUtil.equals(item.getDeleteReason(), FlowStateEnum.BACK_TO_PREV.getCode())) {
                    item.setState(Integer.valueOf(FlowStateEnum.BACK_TO_PREV.getState()));
                }
            });
            vo.setNodeList(FlowCompleteConvert.INSTANCE.convertToNodeList(taskHistoryList));
            if (dto.isAsc() && CollUtil.isNotEmpty(vo.getNodeList())) {
                vo.getNodeList().sort(Comparator.comparing(FlowAuditNodeVO::getEndTime));
            }
            vo.setNodeStateList(FlowCompleteConvert.INSTANCE.convertNodeState(execution));
        }
        return vo;
    }

    @Override
    public CommonPage<AuditHistoryVO> listAuditHistory(AuditHistoryDTO dto) {
        List<String> deploymentIdList = null;
        if (ObjectUtil.isNotNull(dto.getId())) {
            deploymentIdList = versionService.selectListByAuditId(dto.getId());
        }
        PageListHistoryQueryCmd cmd = PageListHistoryQueryCmd.builder()
                .category(dto.getCategoryCode())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .orderBy(dto.getOrderBy())
                .dir(dto.getDir())
                .deploymentIdList(deploymentIdList);
        BasePage page = new BasePage();
        page.setPageNum(dto.getPageNum());
        page.setPageSize(dto.getPageSize());
        List<PageHistoryInstanceResp> historyList = instanceQueryService.findHistoryByCategoryCode(cmd);
        if (CollUtil.isEmpty(historyList)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
        }
        approveRejectHandle(historyList);
        List<AuditHistoryVO> voList = FlowAuditConvert.INSTANCE.convertTList(historyList);
        if (StrUtil.isNotBlank(dto.getStartName())) {
            voList = CollectionUtils.filterList(voList, list -> list.getStartByName().contains(dto.getStartName()));
        }
        int limitStart = (dto.getPageNum() - 1) * dto.getPageSize();
        int pageSize = dto.getPageSize();
        if (limitStart > 0) {
            pageSize = limitStart + page.getPageSize();
        }
        if (ObjectUtil.isNotEmpty(voList) && limitStart < voList.size()) {
            List<AuditHistoryVO> manageVO = voList.subList(limitStart, Math.min(pageSize, voList.size()));
            return CommonPage.CommonPage(manageVO, (long) voList.size(), page);
        }
        return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
    }

    @Override
    public List<TaskHistoryVO> listTaskHistory(String processInstanceId) {
        TaskHistoryCmd cmd = TaskHistoryCmd.builder().processInstanceId(processInstanceId);
        List<TaskHistoryResp> historyRespList = taskQueryService.findHistoryByProcessInstanceId(cmd);
        if (CollUtil.isEmpty(historyRespList)) {
            return Collections.emptyList();
        }
        return FlowAuditConvert.INSTANCE.convertTHistoryList(historyRespList);
    }

    @Override
    public void exportAuditHistory(AuditHistoryExportDTO dto, HttpServletResponse response) {
        List<String> deploymentIdList = null;
        if (ObjectUtil.isNotNull(dto.getId())) {
            deploymentIdList = versionService.selectListByAuditId(dto.getId());
        }
        ExportHistoryQueryCmd cmd = ExportHistoryQueryCmd.builder()
                .category(dto.getCategoryCode())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .deploymentIdList(deploymentIdList)
                .instanceIdList(dto.getInstanceIdList());
        List<PageHistoryInstanceResp> historyList = instanceQueryService.findHistoryListByCategoryCode(cmd);
        approveRejectHandle(historyList);
        List<AuditHistoryVO> voList = FlowAuditConvert.INSTANCE.convertTList(historyList);
        String flowName = EnumUtils.getNameByValue(AuditCategoryCodeEnum.values(), dto.getCategoryCode()) + StrUtil.DASHED + dto.getName();
        if (CollUtil.isNotEmpty(voList)) {
            voList.forEach(item -> item.setFlowName(flowName));
        }
        if (StrUtil.isNotBlank(dto.getStartName())) {
            voList = voList.stream()
                    .filter(item -> item.getStartByName().contains(dto.getStartName()))
                    .collect(Collectors.toList());
        }
        List<AuditHistoryExportVO> historyExportVOList = FlowAuditExportConvert.INSTANCE.convertToHistoryExport(voList);
        String categoryName = EnumUtils.getNameByValue(AuditCategoryCodeEnum.values(), dto.getCategoryCode());
        try {
            ExcelWriterUtils.write(categoryName, response, Collections.singletonList(new SheetDataBo(categoryName, AuditHistoryExportVO.class, historyExportVOList, null)));
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_EXPORT_ERROR);
        }
    }

    private void approveRejectHandle(List<PageHistoryInstanceResp> historyList) {
        List<String> instanceIdList = CollectionUtils.convertList(historyList, PageHistoryInstanceResp::getProcessInstanceId);
        List<ExecutionListResp> executionList = executionQueryService.findByProcessInstanceIdList(instanceIdList);
        Map<String, List<ExecutionListResp>> executionMap = CollectionUtils.convertMultiMap(executionList, ExecutionListResp::getProcessInstanceId);
        historyList.forEach(item -> {
            List<ExecutionListResp> execution = executionMap.get(item.getProcessInstanceId());
            if (CollUtil.isNotEmpty(execution)) {
                item.setProcessState(FlowStateEnum.APPROVE_REJECT.getState());
            }
        });
    }

    @Override
    public void exportTaskHistory(ExportTaskHistoryDTO dto, HttpServletResponse response) {
        List<TaskHistoryVO> vos = listTaskHistory(dto.getProcessInstanceId());
        List<TaskHistoryExportVO> taskHistoryExportList = FlowAuditExportConvert.INSTANCE.converToTaskExport(vos);
        String categoryName = EnumUtils.getNameByValue(AuditCategoryCodeEnum.values(), dto.getCategoryCode());
        try {
            ExcelWriterUtils.write(categoryName, response, Collections.singletonList(new SheetDataBo(categoryName, TaskHistoryExportVO.class, taskHistoryExportList, null)));
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_EXPORT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean flowAuditBackToPrev(AuditBackToPrevDTO dto) {
        BackToPrevCmd cmd = BackToPrevCmd.builder()
                .assignees(FlowCompleteConvert.INSTANCE.convertToRoleIdList(), FlowAuditCodeEnum.ALL_ROLE.getValue())
                .assignee(SysUserHolder.getUser().getUserId(), FlowAuditCodeEnum.ALL_USER.getValue())
                .comment(dto.getComment())
                .remark(dto.getRemark())
                .executionId(dto.getExecutionId())
                .operator(SysUserHolder.getUser().getUserId());
        if (AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            cmd = BackToPrevCmd.builder()
                    .comment(dto.getComment())
                    .remark(dto.getRemark())
                    .executionId(dto.getExecutionId())
                    .operator(SysUserHolder.getUser().getUserId());
        }
        executionService.backToPrev(cmd);
        return true;
    }

    @Override
    public List<AuditCategoryCountVO> getAuditCategoryToDoCount(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new BmosException(BaseResponseCode.ILLEGAL_REQUEST_PARAMETER);
        }
        List<String> categoryCodeList = AuditCategoryServiceEnum.codes();
        List<String> businessId = new ArrayList<>();
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            categoryCodeList.forEach(item -> {
                List<String> businessKeyList = AuditDataConditionBuilder.build(item).queryBusinessListByCategory();
                if (CollUtil.isNotEmpty(businessKeyList)) {
                    businessId.addAll(businessKeyList);
                }
            });
            if (CollUtil.isEmpty(businessId)) {
                return Collections.emptyList();
            }
        }
        TaskToDoQueryCmd cmd = TaskToDoQueryCmd
                .builder()
                .assignees(FlowCompleteConvert.INSTANCE.convertToRoleIdList(), FlowAuditCodeEnum.ALL_ROLE.getValue())
                .assignee(userId, FlowAuditCodeEnum.ALL_USER.getValue())
                .businessId(businessId);
        if (AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            cmd = TaskToDoQueryCmd.builder();
        }
        List<TaskToDoCountListResp> todoTaskCountByAssignees = taskQueryService.findTodoTaskCountByAssignees(cmd);
        if (CollUtil.isEmpty(todoTaskCountByAssignees)) {
            return Collections.emptyList();
        }
        List<AuditCategoryCountVO> voList = FlowAuditConvert.INSTANCE.convertToTodoCountVo(todoTaskCountByAssignees);
        return voList;
    }

    @Override
    public void saveAuditBackHistory(String businessId, String comment, String remark, String nodeName, String modelName) {
        operationHistoryService.save(AuditOperationLogEntity.builder()
                .module(modelName)
                .businessId(Long.valueOf(businessId))
                .operationType(OperationType.BACK_AUDIT.getValue())
                .remark(remark)
                .nodeName(nodeName)
                .comment(comment)
                .createBy(SysUserHolder.getUser().getUserId())
                .build());
    }

    @Override
    public FlowAuditProcess selectBindProcessFlowAudit(String categoryCode, Long processId) {
        return flowAuditProcessMapper.selectBindProcessFlowAudit(categoryCode, processId);
    }

    @Override
    public List<Long> flowAuditProcessList(String code) {
        List<FlowAuditProcess> flowAuditProcessList = flowAuditProcessMapper.flowAuditProcessList(code);
        if (CollUtil.isEmpty(flowAuditProcessList)) {
            return new ArrayList<>();
        }
        return flowAuditProcessList.stream().map(FlowAuditProcess::getProcessId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeFlowAuditState(ChangeAuditVersionStateDTO dto) {
        FlowAuditVersion flowAuditVersion = versionService.queryById(dto.getId());
        FlowAuditStateEnum state;
        if (Objects.isNull(flowAuditVersion) || Objects.isNull(state = FlowAuditStateEnum.getEnumByCode(flowAuditVersion.getState()))) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_STATE_ERROR);
        }
        switch (state) {
            case STATE:
                if (dto.getEnable()) {
                    throw new BmosException(LimsResponseCode.FLOW_AUDIT_STATE_ERROR);
                } else {
                    versionService.changeStateById(flowAuditVersion.getId(), dto.getEnable());
                }
                break;
            case DESIGN:
                if (!dto.getEnable()) {
                    throw new BmosException(LimsResponseCode.FLOW_AUDIT_STATE_ERROR);
                } else {
                    // 校验并发布流程更改启用状态
                    validateAndDeployFlow(flowAuditVersion);
                }
                break;
            case HISTORY:
                if (!dto.getEnable()) {
                    throw new BmosException(LimsResponseCode.FLOW_AUDIT_STATE_ERROR);
                } else {
                    // 更改启用状态
                    updateToActiveVersion(flowAuditVersion);
                }
                break;
        }

    }

    private void validateAndDeployFlow(FlowAuditVersion flowAuditVersion) {
        FlowAuditDetailVO flowAuditDetailVO = detailFlowAudit(flowAuditVersion.getId());
        if (flowAuditDetailVO == null) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }
        FlowCheckoutDTO checkout = new FlowCheckoutDTO();
        checkout.setFlowAuditModel(flowAuditDetailVO.getFlowAuditModel());
        checkout.setMegUserList(FlowAuditMessageConvert.INSTANCE.convert2CheckoutUser(flowAuditDetailVO.getAuditMegDTOList()));
        checkout.setUserList(FlowAuditUserConvert.INSTANCE.convert2CheckoutUserList(flowAuditDetailVO.getAuditUserList()));
        // 校验
        checkoutDeployment(checkout);
        // 发布
        DeployDeploymentCmd cmd = new DeployDeploymentCmd();
        cmd.setDeployBy(SysUserHolder.getUser().getUserId());
        cmd.setDeploymentId(flowAuditVersion.getDeploymentId());
        cmd.setMetaInfo(flowAuditDetailVO.getFlowAuditModel());
        deploymentService.deploy(cmd);
        // 启用版本
        updateToActiveVersion(flowAuditVersion);
    }

    private void updateToActiveVersion(FlowAuditVersion flowAuditVersion) {
        flowAuditVersion.setState(FlowAuditStateEnum.STATE.getValue());
        // 停用其他生效流程
        versionService.disableByAuditId(flowAuditVersion.getFlowAuditId());
        // 更新现流程
        versionService.changeStateById(flowAuditVersion.getId(), Boolean.TRUE);
    }

    public String createDeployment(SaveAuditDTO dto) {
        return deploymentService.createDeployment(DeploymentConvert.INSTANCE.convertToCreateDeployment(dto), Boolean.FALSE);
    }

    public void saveFlowAuditData(SaveAuditDTO dto) {
        try {
            if (ObjectUtil.isEmpty(dto.getChangeVersion())) {
                dto.setChangeVersion(Boolean.FALSE);
            }
            //保存本地流程数据
            FlowAudit flowAudit = FlowAuditConvert.INSTANCE.convertToAudit(dto);
            flowAudit.setId(dto.getFlowAuditId());
            auditMapper.saveFlowAudit(flowAudit);
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_NAME_ERROR);
        }
        FlowAuditVersion flowAuditVersion = versionService.saveFlowAuditVersion(dto);
        //保存节点审批人员
        flowAuditUserService.saveFlowAuditUserList(dto.getAuditUserList(), dto.getDeploymentId());
        messageService.saveMegUserList(dto.getAuditMegDTOList(), dto.getDeploymentId());
    }

    public Boolean deploymentPublishOrNot(String deploymentId) {
        if (StrUtil.isNotBlank(deploymentId)) {
            AuditDeployment deployment = queryService.findByDeploymentId(deploymentId);
            if (ObjectUtil.isNotEmpty(deployment) && Boolean.TRUE.equals(deployment.getDeployStatus())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

}

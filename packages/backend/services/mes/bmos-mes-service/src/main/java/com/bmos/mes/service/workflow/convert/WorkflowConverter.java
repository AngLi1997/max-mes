package com.bmos.mes.service.workflow.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.plan.ProductTaskStatusEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.ProcessStateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.model.ExecuteSubsidiaryRecord;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.workflow.change.vo.TeamListVO;
import com.bmos.mes.service.workflow.dto.AppPlanHistoryDTO;
import com.bmos.mes.service.workflow.dto.query.WorkflowTodoPageDTO;
import com.bmos.mes.service.workflow.vo.*;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.orchestrator.engine.core.model.ExecutionInstance;
import com.bmos.orchestrator.engine.core.query.resp.MultiTaskQueryResp;
import com.bmos.orchestrator.engine.core.state.ProcessState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper
public interface WorkflowConverter {
    WorkflowConverter INSTANCE = Mappers.getMapper(WorkflowConverter.class);

    default List<WorkflowPlanManagePageVO> convertPage(List<Plan> plans, Map<String, String> names) {
        return plans.stream()
                .map(this::convertVO)
                .peek(e -> e.setActiveProcedureName(names.get(e.getExecuteProcessInstanceId())))
                .collect(Collectors.toList());
    }

    @Mapping(source = "id", target = "productPlanId")
    WorkflowPlanManagePageVO convertVO(Plan plan);

    default WorkflowNodeOrderedVO convertVO(List<ExecutionInstance> executions) {
        Map<Boolean, List<WorkflowNodeVO>> nonSorted = executions.stream()
                .filter(e -> !ProcessState.INACTIVE.getState().equals(e.getState()))
                .map(e ->
                        WorkflowNodeVO.builder()
                                .name(e.getElementName())
                                .nodeId(e.getElementKey())
                                .executionId(e.getExecutionId())
                                .processInstanceId(e.getProcessInstanceId())
                                .startTime(e.getStartTime())
                                .endTime(e.getEndTime())
                                .state(e.getState())
                                .build())
                .collect(Collectors.partitioningBy(e -> ProcessState.ACTIVE.getState().equals(e.getState())));
        WorkflowNodeOrderedVO vo = new WorkflowNodeOrderedVO();
        vo.setRunning(Optional.ofNullable(nonSorted.get(true)).orElse(Collections.emptyList())
                .stream().sorted(Comparator.comparing(WorkflowNodeVO::getStartTime)).collect(Collectors.toList()));
        vo.setCompleted(Optional.ofNullable(nonSorted.get(false)).orElse(Collections.emptyList()).stream()
                .sorted(Comparator.comparing(WorkflowNodeVO::getEndTime).reversed()).collect(Collectors.toList()));
        return vo;
    }

    default List<WorkflowTodoPageVO> convertList(List<MultiTaskQueryResp> data, List<Plan> plans) {
        Map<Long, Plan> planMap = CollectionUtils.convertMap(plans, Plan::getId);
        List<WorkflowTodoPageVO> list = new ArrayList<>();
        data.forEach(e ->{
            Plan plan = planMap.get(Long.valueOf(e.getBusinessKey()));
            if (ObjectUtil.isEmpty(plan)){
                return;
            }
            WorkflowTodoPageVO vo = convertTodoPageItem(plan);
            vo.setProcedureName(e.getProcessInstanceName());
            vo.setProcedureStepName(e.getElementName());
            vo.setProcessInstanceId(e.getProcessInstanceId());
            vo.setTaskId(e.getTaskId());
            vo.setNodeId(e.getElementKey());
            vo.setActiveState(ObjectUtil.isNull(e.getActiveState()) ? true : e.getActiveState());
            vo.setExecutionId(e.getExecutionId());
            vo.setStartTime(Optional.ofNullable(DateUtil.format(e.getActiveTime(), DatePattern.NORM_DATETIME_PATTERN)).orElse("-"));
            vo.setProcedureChangeNumber(e.getProcedureChangeNumber());
            vo.setProcessChangeNumber(e.getProcessChangeNumber());
            list.add(vo);
        });
        return list;
    }


    @Mapping(source = "id", target = "planId")
    @Mapping(source = "id", target = "productPlanId")
    WorkflowTodoPageVO convertTodoPageItem(Plan plan);


    default List<WorkflowPlanHistoryPageVO> convertHistoryPageVO(List<Plan> plans) {
        return plans.stream().map(this::convertHistoryPageVO).collect(Collectors.toList());
    }

    @Mapping(source = "id", target = "productPlanId")
    WorkflowPlanHistoryPageVO convertHistoryPageVO(Plan plan);

    default List<WorkflowNodeVO> convertProcedureVO(List<ExecutionInstance> executions, Long planId, List<ProcedureModel> modelList) {
        Map<String, ProcedureModel> modelMap = CollectionUtils.convertMap(modelList, ProcedureModel::getNodeId);
        return executions.stream().map(e ->
                        WorkflowNodeVO.builder()
                                .name(e.getElementName())
                                .nodeId(e.getElementKey())
                                .executionId(Optional.ofNullable(e.getExecutionId()).orElse(null))
                                .processInstanceId(e.getProcessInstanceId())
                                .procedureModelId(ObjectUtil.isEmpty(modelMap.get(e.getElementKey())) ? null : modelMap.get(e.getElementKey()).getId())
                                .planId(planId)
                                .startTime(Optional.ofNullable(e.getStartTime()).orElse(null))
                                .endTime(Optional.ofNullable(e.getEndTime()).orElse(null))
                                .state(e.getState())
                                .processChangeNumber(e.getProcessChangeNumber())
                                .procedureChangeNumber(e.getProcedureChangeNumber())
                                .sort(Optional.ofNullable(modelMap.get(e.getElementKey()).getSort()).orElse(0))
                                .nodeFunction(ObjectUtil.isNull(e.getChangeType()) ? null : ProcedureStepNodeFunctionEnum.getEnumByValue(e.getChangeType()))
                                .build())
                .sorted(Comparator.comparing(WorkflowNodeVO::getProcessChangeNumber).thenComparing(WorkflowNodeVO::getSort).thenComparing(WorkflowNodeVO::getProcedureChangeNumber))
                .collect(Collectors.toList());
    }

    default List<WorkflowNodeVO> convertProcedureStepVO(List<ExecutionInstance> executions, List<ProcedureStepModel> stepModels,
                                                         List<ProcedureTaskInstance> taskInstances, Plan plan) {
        Map<String, ProcedureStepModel> stepMap = CollectionUtils.convertMap(stepModels, ProcedureStepModel::getNodeId);
        List<WorkflowNodeVO> voList = new ArrayList<>();
        if (CollUtil.isNotEmpty(executions)) {
            List<WorkflowNodeVO> workFlowList = executions.stream().map(e ->
                    WorkflowNodeVO.builder()
                            .name(e.getElementName())
                            .nodeId(e.getElementKey())
                            .executionId(e.getExecutionId())
                            .procedureStepId(ObjectUtil.isNull(stepMap.get(e.getElementKey())) ? null : stepMap.get(e.getElementKey()).getProcedureStepId())
                            .procedureStepModelId(ObjectUtil.isNull(stepMap.get(e.getElementKey())) ? null : stepMap.get(e.getElementKey()).getId())
                            .procedureModelId(ObjectUtil.isNull(stepMap.get(e.getElementKey())) ? null : stepMap.get(e.getElementKey()).getProcedureModelId())
                            .processInstanceId(e.getProcessInstanceId())
                            .planId(plan.getId())
                            .productionLineId(plan.getProductionLineId())
                            .startTime(e.getStartTime())
                            .procedureChangeNumber(e.getProcedureChangeNumber())
                            .processChangeNumber(e.getProcessChangeNumber())
                            .sort(Optional.ofNullable(stepMap.get(e.getElementKey()).getSort()).orElse(0))
                            // 返回步骤功能
                            .nodeFunction(ObjectUtil.isEmpty(stepMap.get(e.getElementKey())) ? null :
                                    ProcedureStepNodeFunctionEnum.getEnumByValue(stepMap.get(e.getElementKey()).getNodeFunction()))
                            .endTime(e.getEndTime())
                            .activeState(ObjectUtil.isNull(e.getActiveState()) ? true : e.getActiveState())
                            .state(e.getState())
                            .build())
                    .collect(Collectors.toList());
            voList.addAll(workFlowList);
        }
        if (CollUtil.isNotEmpty(taskInstances)) {
            Map<Long, ProcedureStepModel> stepModelMap = CollectionUtils.convertMap(stepModels, ProcedureStepModel::getId,
                    Function.identity());
            handleTaskVO(taskInstances, stepModelMap, voList,plan);
        }
        return voList;
    }

    default void handleTaskVO(List<ProcedureTaskInstance> taskInstances, Map<Long, ProcedureStepModel> stepMaps,
                              List<WorkflowNodeVO> vos,Plan plan) {
        List<WorkflowNodeVO> taskList = taskInstances.stream().map(e ->
                        WorkflowNodeVO.builder()
                                .name(e.getName())
                                .nodeId(stepMaps.get(e.getProcedureStepModelId()).getNodeId())
                                .executionId(String.valueOf(e.getId()))
                                .procedureStepId(stepMaps.get(e.getProcedureStepModelId()).getProcedureStepId())
                                .procedureStepModelId(e.getProcedureStepModelId())
                                .activeState(StrUtil.equals(e.getFlowState(), ProductTaskStatusEnum.ACTIVATED.getValue()) ||
                                        StrUtil.equals(e.getFlowState(), ProductTaskStatusEnum.COMPLETE.getValue()))
                                .procedureModelId(e.getProcedureModelId())
                                .nodeFunction(ObjectUtil.isEmpty(stepMaps.get(e.getProcedureStepModelId())) ? null :
                                        ProcedureStepNodeFunctionEnum.getEnumByValue(stepMaps.get(e.getProcedureStepModelId()).getNodeFunction()))
                                .planId(e.getPlanId())
                                .startTime(e.getStartTime())
                                .endTime(Optional.ofNullable(e.getCompleteTime()).orElse(null))
                                .productionLineId(plan.getProductionLineId())
                                .state(e.getState())
                                .sort(Optional.ofNullable(stepMaps.get(e.getProcedureStepModelId()).getSort()).orElse(0))
                                .processChangeNumber(e.getProcessChangeNumber())
                                .procedureChangeNumber(e.getProcedureChangeNumber())
                                .build())
                .collect(Collectors.toList());
        vos.addAll(taskList);
    }

    CommonPage<WorkflowFreshTodoPageVO> convertToFreshTodo(CommonPage<Plan> plans);

    default List<ExecutionInstance> convertToFreshExecution(List<TeamListVO> team, List<ExecutionInstance> executions) {
        //根据权限组装数据
        List<ExecutionInstance> freshExecution = new ArrayList<>();
        Map<Integer, Map<String, List<String>>> teamMap = team.stream().collect(
                (Collectors.groupingBy(TeamListVO::getChangeTeamNumber,
                        Collectors.groupingBy(TeamListVO::getChangeTeamType,
                                Collectors.mapping(TeamListVO::getNodeId, Collectors.toList())))));
        executions.forEach(executionInstance -> {
            List<String> nodeId = new ArrayList<>();
            //使用默认班组权限
            if (executionInstance.getProcessChangeNumber() == 0 && executionInstance.getProcedureChangeNumber() == 0) {
                Map<String, List<String>> map = teamMap.get(executionInstance.getProcessChangeNumber());
                if (CollUtil.isNotEmpty(map)){
                    nodeId = map.get(ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue());
                }
            } else {
                //存在换班，优先判断工序换班
                Map<String, List<String>> map = teamMap.get(executionInstance.getProcedureChangeNumber() == 0 ?
                        executionInstance.getProcessChangeNumber() : executionInstance.getProcedureChangeNumber());
                nodeId = CollUtil.isNotEmpty(map) ? map.get(executionInstance.getChangeType()) : new ArrayList<>();
            }
            if (CollUtil.isNotEmpty(nodeId) && nodeId.contains(executionInstance.getElementKey())) {
                freshExecution.add(executionInstance);
            }
        });
        return freshExecution;
    }

    /**
     * 组件参数
     *
     * @param executionInstances 流程实例数据
     * @param planId             计划id
     * @param modelList          工序模型信息
     * @return List<ProcedureProgressVO>
     */
    default List<ProcedureProgressVO> convertToProcedure(List<ExecutionInstance> executionInstances, Long planId, List<ProcedureModel> modelList) {
        //找到唯一一条运行中的流程实例确定工艺换班次数
        List<ExecutionInstance> activeInstance = CollectionUtils.filterList(executionInstances, executionInstance ->
                executionInstance.getState().equals(ProcessState.ACTIVE.getState()));
        if (CollUtil.isEmpty(activeInstance)) {
            throw new BmosException(MesResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }
        //确定工艺换班次数
        Integer processChangeNumber = CollectionUtils.getFirst(activeInstance).getProcessChangeNumber();
        Map<String, List<ExecutionInstance>> executionMap = executionInstances.stream()
                .sorted(Comparator.comparing(ExecutionInstance::getStartTime).reversed())
                .collect((Collectors.groupingBy(ExecutionInstance::getElementKey)));
        List<ProcedureProgressVO> vos = new ArrayList<>();
        Map<String, ProcedureModel> modelMap = CollectionUtils.convertMap(modelList, ProcedureModel::getNodeId);
        executionMap.forEach((key, value) -> {
            //找到运行中的流程实例
            List<ExecutionInstance> activeExecution = CollectionUtils.filterList(value, item -> item.getProcessChangeNumber().equals(processChangeNumber));
            if (CollUtil.isEmpty(activeExecution)) {
                return;
            }
            //获取最新的
            ExecutionInstance first = CollectionUtils.getFirst(activeExecution);
            ProcedureProgressVO vo = convertToProcedureVo(first);
            vo.setEndTime(Optional.ofNullable(first.getEndTime()).orElse(null));
            vo.setFreshExecutionId(first.getExecutionId());
            vo.setNodeId(first.getElementKey());
            vo.setPlanId(planId);
            vo.setProcedureModelId(modelMap.get(first.getElementKey()).getId());
            vo.setExecutionIdList(CollectionUtils.convertList(value, ExecutionInstance::getExecutionId));
            vos.add(vo);
        });
        addInactiveExecutionInstance(vos, modelList, planId,executionMap);
        return vos;
    }

    @Mapping(target = "completedBy", source = "completeBy")
    ProcedureProgressVO convertToProcedureVo(ExecutionInstance executionInstance);

    /**
     * 添加未激活的流程数据
     *
     * @param vos       返回数据集合
     * @param modelList 模型数据
     * @param planId 计划id
     */
    default void addInactiveExecutionInstance(List<ProcedureProgressVO> vos, List<ProcedureModel> modelList, Long planId,
                                              Map<String, List<ExecutionInstance>> executionMap) {
        //找打未开始的数据
        ProcedureProgressVO progressVO = CollectionUtils.getFirst(vos);
        Boolean isProcessChangeTeam = ObjectUtil.isNotEmpty(progressVO) &&
                StrUtil.equals(progressVO.getChangeType(),ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue());
        List<String> nodeIdList = CollectionUtils.convertList(vos, ProcedureProgressVO::getNodeId);
        List<ProcedureModel> inactiveModelList = CollectionUtils.filterList(modelList, model -> !nodeIdList.contains(model.getNodeId()));
        if (CollUtil.isNotEmpty(inactiveModelList)) {
            inactiveModelList.forEach(item -> {
                ProcedureProgressVO vo = new ProcedureProgressVO();
                vo.setState(ProcessStateEnum.INACTIVE.getValue());
                vo.setNodeId(item.getNodeId());
                vo.setPlanId(planId);
                vo.setProcedureModelId(item.getId());
                vo.setProcedureChangeNumber(isProcessChangeTeam ? progressVO.getProcedureChangeNumber() : 0);
                vo.setProcessChangeNumber(isProcessChangeTeam ? progressVO.getProcessChangeNumber() : 0);
                vo.setExecutionIdList(CollectionUtils.convertList(executionMap.get(item.getNodeId()), ExecutionInstance::getExecutionId));
                vos.add(vo);
            });
        }
    }

    default List<StepProgressVO> convertToStepProgressVO(List<ExecutionInstance> executionInstances, Map<String, ProcedureStepModel> stepModelMap) {
        return executionInstances.stream().map(item -> {
            StepProgressVO vo = convertToVo(item);
            vo.setNodeId(item.getElementKey());
            vo.setProcedureStepModelId(stepModelMap.get(item.getElementKey()).getId());
            return vo;
        }).collect(Collectors.toList());
    }

    StepProgressVO convertToVo(ExecutionInstance executionInstance);

    List<PlanSubRecordVO> convertSubRecordVO(List<ExecuteSubsidiaryRecord> list);

    CommonPage<PlanSubRecordVO> convertSubRecordVOPage(CommonPage<ExecuteSubsidiaryRecord> commonPage);

    default ExecutionInstance convertToExecution(ProcedureModel model) {
        ExecutionInstance instance = new ExecutionInstance();
        instance.setElementName(model.getName());
        instance.setElementKey(model.getNodeId());
        return instance;
    }

    default PlanProcedureStepDetailVO convert2PlanProcedureStepDetailVO(Plan plan, ProcedureModel procedureModel, ProcedureStepModel procedureStepModel){
        PlanProcedureStepDetailVO result = new PlanProcedureStepDetailVO();
        result.setProductId(plan.getProductId());
        result.setProductName(plan.getProductName());
        result.setProductMergeCode(plan.getProductMergeCode());
        result.setProcessName(plan.getProcessName());
        result.setProcessVersion(plan.getProcessVersion());
        result.setBatchNo(plan.getBatchNo());
        result.setProcedureName(procedureModel.getName());
        result.setProcedureModelId(procedureModel.getId());
        result.setProcedureStepName(procedureStepModel.getName());
        result.setProcessId(plan.getProcessId());
        return result;
    }

    default CommonPage<WorkflowPlanHistoryPageVO> convertHistoryPageVO(CommonPage<Plan> planCommonPage, AppPlanHistoryDTO dto){
        return CommonPage.CommonPage(convertHistoryPageVO(planCommonPage.getList()), Long.valueOf(planCommonPage.getTotal()),dto);
    }
}

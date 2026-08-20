package com.bmos.mes.service.process.convert.Task;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.enums.plan.ProductTaskStatusEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureTask;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstanceHistory;
import com.bmos.mes.service.process.vo.Task.EquipmentEasyInfoVO;
import com.bmos.mes.service.process.vo.Task.EquipmentModuleTreeNodeVO;
import com.bmos.mes.service.workflow.vo.ProcedureStepDurationVO;
import com.bmos.mes.service.workflow.vo.TaskProgressVO;
import com.bmos.mes.service.workflow.vo.WorkflowTodoPageVO;
import com.bmos.platform.facade.equipment.vo.EquipmentModuleTreeNodeFeignVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper
public interface ProcessTaskConverter {
    ProcessTaskConverter INSTANCE = Mappers.getMapper(ProcessTaskConverter.class);

    default List<ProcedureTask> convertList(ProcedureModel procedureModel, List<ProcedureStepDTO> procedureStepDTOS) {
        return procedureStepDTOS.stream()
                .map(e -> {
                    ProcedureTask task = new ProcedureTask();
                    task.setId(IdUtils.getSnowflake());
                    task.setProcessId(procedureModel.getProcessId());
                    task.setProcedureModelId(procedureModel.getId());
                    task.setProcessVersion(procedureModel.getProcessVersion());
                    task.setName(e.getName());
                    return task;
                })
                .collect(Collectors.toList());
    }

    default List<ProcedureTaskInstance> convertInstanceId(List<ProcedureStepModel> taskList, Plan plan) {
        List<ProcedureTaskInstance> list = new ArrayList<>();
        taskList.forEach(item -> {
            ProcedureTaskInstance instance = convertInstance(item);
            instance.setId(IdUtils.getSnowflake());
            instance.setProcedureStepModelId(item.getId());
            instance.setPlanId(plan.getId());
            instance.setProcessInstanceId(plan.getExecuteProcessInstanceId());
            list.add(instance);
        });
        return list;
    }

    ProcedureTaskInstance convertInstance(ProcedureStepModel task);

    List<EquipmentModuleTreeNodeVO> convertToModelVo(List<EquipmentModuleTreeNodeFeignVO> list);

    EquipmentModuleTreeNodeVO convertToInfoVo(EquipmentEasyInfoVO vo);

    List<ProcedureTaskInstanceHistory> convertToTaskHistory(List<ProcedureTaskInstance> list);

    List<ProcedureTaskInstance> convertToTask(List<ProcedureTaskInstanceHistory> list);

    @Mapping(source = "id", target = "planId")
    @Mapping(source = "id", target = "productPlanId")
    WorkflowTodoPageVO convertToWorKFlowToDo(Plan plan);

    default List<WorkflowTodoPageVO> convertToToDoList(List<ProcedureTaskInstance> list, List<Plan> plans,
                                                       Map<Long, ProcedureStepDurationVO> procedureStepMap) {
        Map<Long, Plan> planMap = CollectionUtils.convertMap(plans, Plan::getId);
        List<WorkflowTodoPageVO> voList = new ArrayList<>();
        list.forEach(item->{
            Plan plan = planMap.get(item.getPlanId());
            if (ObjectUtil.isEmpty(plan)){
                return;
            }
            ProcedureStepDurationVO procedureStep = procedureStepMap.get(item.getProcedureStepModelId());
            WorkflowTodoPageVO vo = convertToWorKFlowToDo(plan);
            vo.setProcedureStepId(item.getProcedureStepModelId());
            vo.setProcedureStepModelId(item.getProcedureStepModelId());
            vo.setProcedureChangeNumber(item.getProcedureChangeNumber());
            vo.setProcessChangeNumber(item.getProcessChangeNumber());
            vo.setNodeId(procedureStep.getProcedureStepNodeId());
            vo.setNodeFunction(ProcedureStepNodeFunctionEnum.getEnumByValue(procedureStep.getNodeFunction()));
            vo.setStartTime(Optional.ofNullable(DateUtil.format(item.getActiveTime(),DatePattern.NORM_DATETIME_PATTERN)).orElse("-"));
            vo.setProcedureStepName(item.getName());
            vo.setTaskId(String.valueOf(item.getId()));
            vo.setProcedureStepDuration(procedureStep.getProcedureStepDuration());
            vo.setProcedureStepTimeUnit(procedureStep.getProcedureStepTimeUnit());
            vo.setProcedureStepId(procedureStep.getProcedureStepId());
            vo.setProcedureName(procedureStep.getProcedureModelName());
            vo.setProcedureStepModelId(procedureStep.getProcedureStepModelId());
            vo.setExecutionId(String.valueOf(item.getId()));
            vo.setProcedureModelId(procedureStep.getProcedureModelId());
            vo.setSort(procedureStep.getSort());
            vo.setActiveState(StrUtil.equals(item.getFlowState(), ProductTaskStatusEnum.ACTIVATED.getValue()) ||
                    StrUtil.equals(item.getFlowState(),ProductTaskStatusEnum.COMPLETE.getValue()));
            vo.setProcedureId(procedureStep.getProcedureId());
            voList.add(vo);
        });
        return voList;
    }

    default List<TaskProgressVO> convertToVos(List<ProcedureTaskInstance> taskInstances){
        //按照stepModelId分组
        Map<Long, List<ProcedureTaskInstance>> taskMap = CollectionUtils.convertMultiMap(taskInstances, ProcedureTaskInstance::getProcedureStepModelId);
        List<TaskProgressVO> vos = new ArrayList<>();
        taskMap.forEach((key,value)->{
            TaskProgressVO task = convertToProgressVo(CollectionUtils.getFirst(value));
            task.setType(StepTaskTypeEnum.TASK.getValue());
            vos.add(task);
        });
        return vos;
    }

    TaskProgressVO convertToProgressVo(ProcedureTaskInstance taskInstance);
}

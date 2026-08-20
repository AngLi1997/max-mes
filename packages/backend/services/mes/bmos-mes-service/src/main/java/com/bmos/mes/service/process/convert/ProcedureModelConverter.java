package com.bmos.mes.service.process.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.process.dto.ProcedureCopyDTO;
import com.bmos.mes.service.process.dto.ProcedureDTO;
import com.bmos.mes.service.process.dto.save.ProcedureSaveDTO;
import com.bmos.mes.service.process.dto.save.SaveProcessSortVO;
import com.bmos.mes.service.process.model.Procedure;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureModelRoom;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import com.bmos.mes.service.process.vo.Task.ProcedureStepAndTaskVO;
import com.bmos.mes.service.workflow.enums.WorkflowType;
import com.bmos.orchestrator.engine.core.command.CreateDeploymentCmd;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Mapper
public interface ProcedureModelConverter {

    ProcedureModelConverter INSTANCE = Mappers.getMapper(ProcedureModelConverter.class);

    default List<ProcedureModel> convertList(ProcessVersion processVersion, List<ProcedureDTO> procedures) {
        return procedures
                .stream()
                .map(this::convert)
                .peek(e -> {
                    e.setProcessId(processVersion.getProcessId());
                    e.setProcessVersion(processVersion.getVersion());
                }).collect(Collectors.toList());
    }

    ProcedureModel convert(ProcedureDTO dto);

    ProcedureVO convertVO(ProcedureModel procedureModel);

    default List<ProcedureVO> convertVOList(List<ProcedureModel> procedureModels, Map<Long, List<Long>> groupMap,
                                            Map<Long, List<Long>> materialMap, Map<Long, List<String>> roomMap,
                                            Map<Long,ExpressionDetailVO> expressionMap, Map<Long, Procedure> procedureMap) {
        return procedureModels.stream().map(this::convertVO).peek(e -> {
            e.setGroupIds(groupMap.get(e.getId()));
            e.setFormulaMaterialIdList(materialMap.get(e.getId()));
            if (CollUtil.isNotEmpty(roomMap)) {
                e.setRoomIdList(roomMap.get(e.getId()));
            }
            if (CollUtil.isNotEmpty(expressionMap)){
                e.setCompleteCondition(expressionMap.get(e.getId()));
            }
            e.setHistoricalName(procedureMap.get(e.getProcedureId()).getName());
        }).sorted(Comparator.comparing(ProcedureVO::getSort, Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());
    }

    default CreateDeploymentCmd convertCmd(String name, ProcedureSaveDTO dto) {
        CreateDeploymentCmd cmd = new CreateDeploymentCmd();
        cmd.setName(name);
        cmd.setMetaInfo(dto.getProcessModel());
        cmd.setCategory(WorkflowType.PROCEDURE_STEP.name());
        cmd.setCreateBy(SysUserHolder.getUser().getUserId());
        return cmd;
    }

    default List<ProcedureModel> convertList(ProcessVersion processVersion,
                                             List<ProcedureDTO> procedures,
                                             Map<String, Long> procedureIdMap,Integer maxSort) {
        AtomicInteger sort = new AtomicInteger(maxSort);
        return procedures
                .stream()
                .map(e -> {
                    ProcedureModel model = convert(e);
                    model.setProcessId(processVersion.getProcessId());
                    model.setProcessVersion(processVersion.getVersion());
                    model.setProcessVersionId(processVersion.getId());
                    if (ObjectUtil.isNull(e.getId()) && ObjectUtil.isNull(e.getProcedureId())) {
                        model.setProcedureId(procedureIdMap.get(e.getHistoricalName()));
                        model.setSort(sort.incrementAndGet());
                    } else {
                        model.setProcedureId(e.getProcedureId());
                    }
                    return model;
                })
                .collect(Collectors.toList());
    }

    ProcedureModel convertCopy(ProcedureCopyDTO copyDTO);

    default ProcedureModelDetailVO convertDetail(ProcedureModel procedureModel, ProcedureStepAndTaskVO steps,
                                                 List<Long> groupIds) {
        ProcedureModelDetailVO detailVO = convertDetail(procedureModel);
        List<ProcedureStepModelVO> stepVos = ProcessStepConverter.INSTANCE.convertList(steps);
        detailVO.setGroupIds(groupIds);
        detailVO.setSteps(stepVos);
        return detailVO;
    }

    ProcedureModelDetailVO convertDetail(ProcedureModel procedureModel);

    List<ProcedureModelRoomOrStationVO> convertToProcedureModelRoomVO(List<ProcedureModelRoom> list);

    default List<ProcedureModelDetailVO> convertDetailList(List<ProcedureModel> modelList, List<ProcedureStepModelVO> list,
                                                           Map<Long, List<Long>> groupMap) {
        Map<Long, List<ProcedureStepModelVO>> stepMap = CollectionUtils.convertMultiMap(list, ProcedureStepModelVO::getProcedureModelId);
        return modelList.stream().map(model -> {
            ProcedureModelDetailVO detailVO = convertDetail(model);
            detailVO.setGroupIds(Optional.ofNullable(groupMap.get(model.getId())).orElse(Collections.emptyList()));
            detailVO.setSteps(Optional.ofNullable(stepMap.get(model.getId())).orElse(Collections.emptyList()));
            return detailVO;
        }).collect(Collectors.toList());
    }

    List<ProcessSortVO> convertToSortVO(List<ProcedureModel> models);

    List<ProcedureModel> convertToSaveModel(List<SaveProcessSortVO> vos);
}

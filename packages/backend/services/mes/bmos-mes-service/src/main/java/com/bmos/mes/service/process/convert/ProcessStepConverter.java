package com.bmos.mes.service.process.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStep;
import com.bmos.mes.service.process.vo.HistoricVO;
import com.bmos.mes.service.process.vo.ProcedureStepModelVO;
import com.bmos.mes.service.process.vo.ProcedureStepVO;
import com.bmos.mes.service.process.vo.Task.ProcedureStepAndTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProcessStepConverter {
    ProcessStepConverter INSTANCE = Mappers.getMapper(ProcessStepConverter.class);

    default List<ProcedureStep> convertList(ProcedureModel procedureModel, List<ProcedureStepDTO> procedureStepDTOS) {
        return procedureStepDTOS.stream()
                .map(e -> {
                    ProcedureStep step = new ProcedureStep();
                    step.setProcessId(procedureModel.getProcessId());
                    step.setProcedureId(procedureModel.getProcedureId());
                    step.setName(e.getHistoricalName());
                    return step;
                })
                .collect(Collectors.toList());
    }

    List<HistoricVO> convertHistoric(List<ProcedureStep> list);

    default List<ProcedureStepModelVO> convertList(ProcedureStepAndTaskVO steps){
        if (steps == null || CollUtil.isEmpty(steps.getStepList())) {
            return new ArrayList<>();
        }
        List<ProcedureStepModelVO> list = steps.getStepList().stream().map(this::convertVO).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(steps.getTaskList())){
            list.addAll(steps.getTaskList().stream().map(this::convertVO).collect(Collectors.toList()));
        }
        return CollUtil.isEmpty(list) ? list : list.stream()
                .sorted(Comparator.comparing(ProcedureStepModelVO::getSort, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Mapping(target = "groupIds",source = "roles")
    ProcedureStepModelVO convertVO(ProcedureStepVO vo);
}

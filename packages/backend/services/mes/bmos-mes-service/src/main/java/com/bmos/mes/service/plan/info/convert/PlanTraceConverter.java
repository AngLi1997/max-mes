package com.bmos.mes.service.plan.info.convert;

import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.info.dto.PlanRetraceInfoPageDTO;
import com.bmos.mes.service.plan.info.vo.PlanRetraceRoomPageVO;
import com.bmos.mes.service.plan.info.vo.ProcedureStepTaskExecuteVO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstanceHistory;
import com.bmos.orchestrator.engine.core.query.resp.ExecutionUserTaskResp;
import com.bmos.platform.facade.factory.dto.BatchRoomCleanPageDTO;
import com.bmos.platform.facade.factory.vo.BatchRoomCleanInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface PlanTraceConverter {

    PlanTraceConverter INSTANCE = Mappers.getMapper(PlanTraceConverter.class);

    default BatchRoomCleanPageDTO convert2FeignRoomPageVO(PlanRetraceInfoPageDTO dto, String batchNo){
        BatchRoomCleanPageDTO batchRoomCleanPageDTO = new BatchRoomCleanPageDTO();
        batchRoomCleanPageDTO.setBatchNo(batchNo);
        batchRoomCleanPageDTO.setPageNum(dto.getPageNum());
        batchRoomCleanPageDTO.setPageSize(dto.getPageSize());
        batchRoomCleanPageDTO.setOrderBy(dto.getOrderBy());
        batchRoomCleanPageDTO.setDir(dto.getDir());
        batchRoomCleanPageDTO.setOrderSql(dto.getOrderSql());
        return batchRoomCleanPageDTO;
    }

    List<PlanRetraceRoomPageVO> convertRoomPageVOList(List<BatchRoomCleanInfoVO> list);

    default List<ProcedureStepTaskExecuteVO> convert2StepTaskExecuteVO(List<ProcedureModel> procedureModels,
                                                                       List<ProcedureStepModel> stepModels,
                                                                       List<ExecutionUserTaskResp> taskRespList,
                                                                       List<ProcedureTaskInstanceHistory> taskList) {
        Map<Long, ProcedureModel> procedureMap = CollectionUtils.convertMap(procedureModels, ProcedureModel::getId);
        Map<String, ProcedureStepModel> stepMap = CollectionUtils.convertMap(stepModels, ProcedureStepModel::getNodeId);
        Map<Long, ProcedureStepModel> stepIdMap = CollectionUtils.convertMap(stepModels, ProcedureStepModel::getId);
        List<ProcedureStepTaskExecuteVO> result = new ArrayList<>();
        result.addAll(taskRespList.stream().map(e->{
            ProcedureStepModel procedureStepModel = stepMap.get(e.getElementKey());
            ProcedureModel procedureModel = procedureMap.get(procedureStepModel.getProcedureModelId());
            return getProcedureStepTaskExecuteVO(procedureStepModel, procedureModel, e.getProcessChangeNumber(), e.getProcedureChangeNumber());
        }).collect(Collectors.toList()));
        result.addAll(taskList.stream().map(e->{
            ProcedureStepModel procedureStepModel = stepIdMap.get(e.getProcedureStepModelId());
            ProcedureModel procedureModel = procedureMap.get(procedureStepModel.getProcedureModelId());
            return getProcedureStepTaskExecuteVO(procedureStepModel, procedureModel, e.getProcessChangeNumber(), e.getProcedureChangeNumber());
        }).collect(Collectors.toList()));
        result.sort(Comparator
                .comparing(ProcedureStepTaskExecuteVO::getProcessChangeNumber, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(ProcedureStepTaskExecuteVO::getProcedureChangeNumber, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(ProcedureStepTaskExecuteVO::getProcedureSort, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(ProcedureStepTaskExecuteVO::getProcedureStepSort, Comparator.nullsLast(Integer::compareTo)));
        return result;
    }

    static ProcedureStepTaskExecuteVO getProcedureStepTaskExecuteVO(ProcedureStepModel procedureStepModel, ProcedureModel procedureModel, Integer e, Integer e1) {
        if (procedureModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        ProcedureStepTaskExecuteVO vo = new ProcedureStepTaskExecuteVO();
        vo.setName(procedureStepModel.getName());
        vo.setProcedureStepModelId(procedureStepModel.getId());
        vo.setProcedureModelId(procedureModel.getId());
        vo.setRecordItemId(procedureStepModel.getRecordItemId());
        vo.setRecordVersionId(procedureStepModel.getRecordVersionId());
        vo.setProcedureStepSort(procedureStepModel.getSort());
        vo.setProcedureSort(procedureModel.getSort());
        vo.setProcessChangeNumber(e);
        vo.setProcedureChangeNumber(e1);
        vo.setProcedureId(procedureModel.getProcedureId());
        vo.setProcedureStepId(procedureStepModel.getProcedureStepId());
        vo.setReuse(procedureStepModel.getReusable());
        return vo;
    }
}

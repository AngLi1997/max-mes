package com.bmos.mes.service.process.mapper.task;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.service.process.dto.query.ProcedureStepHistoricQueryDTO;
import com.bmos.mes.service.process.model.task.ProcedureTask;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcedureTaskMapper extends BaseMapperX<ProcedureTask> {

    default List<ProcedureTask> selectTaskListByModelId(Long procedureModelId) {
        return selectList(new LambdaQueryWrapperX<ProcedureTask>()
                .eq(ProcedureTask::getProcedureModelId, procedureModelId));
    }

    default List<ProcedureTask> selectTaskListByModelIds(List<Long> ids) {
        return selectList(new LambdaQueryWrapperX<ProcedureTask>()
                .in(ProcedureTask::getProcedureModelId, ids));
    }

    default List<ProcedureTask> selectListByProcessIdAndVersion(Long processId, String processVersion) {
        return selectList(new LambdaQueryWrapperX<ProcedureTask>()
                .eq(ProcedureTask::getProcessId, processId)
                .eq(ProcedureTask::getProcessVersion, processVersion));
    }

    List<ProcedureTask> getHistoricList(ProcedureStepHistoricQueryDTO dto);


    default void remove(Long processId, String processVersion,Long modelId){
        delete(new LambdaQueryWrapperX<ProcedureTask>()
                .eq(ProcedureTask::getProcessId,processId)
                .eq(ProcedureTask::getProcessVersion,processVersion)
                .eq(ProcedureTask::getProcedureModelId,modelId));
    }
}

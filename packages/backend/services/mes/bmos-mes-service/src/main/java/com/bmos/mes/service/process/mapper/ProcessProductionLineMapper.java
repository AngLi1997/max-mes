package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.model.ProcessProductionLine;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcessProductionLineMapper extends BaseMapperX<ProcessProductionLine> {

    default void deleteByProcessVersionId(Long id){
        delete(new LambdaQueryWrapperX<ProcessProductionLine>()
                .eq(ProcessProductionLine::getProcessVersionId, id));
    }

    default List<ProcessProductionLine> selectByProcessVersionId(Long id){
        return selectList(new LambdaQueryWrapperX<ProcessProductionLine>()
                .eq(ProcessProductionLine::getProcessVersionId, id));
    }

    default List<ProcessProductionLine> selectByProcessIdAndVersion(Long processId,String processVersion){
        return selectList(new LambdaQueryWrapperX<ProcessProductionLine>()
                .eq(ProcessProductionLine::getProcessId, processId)
                .eq(ProcessProductionLine::getProcessVersion,processVersion));
    }
}

package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.model.ProcedureModelGroup;
import com.bmos.mes.service.process.vo.ProcessConfigVO;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProcedureModelGroupMapper extends BaseMapperX<ProcedureModelGroup> {

    default void deleteByProcedureModelIds(List<Long> procedureModelIds) {
        delete(new LambdaQueryWrapperX<ProcedureModelGroup>().in(ProcedureModelGroup::getProcedureModelId, procedureModelIds));
    }

    default List<ProcedureModelGroup> selectByProcedureModelIds(Set<Long> procedureModelIds) {
        return selectList(new LambdaQueryWrapperX<ProcedureModelGroup>().in(ProcedureModelGroup::getProcedureModelId, procedureModelIds));
    }

    default List<ProcedureModelGroup> selectByProcedureModelId(Long procedureModelId) {
        return selectList(new LambdaQueryWrapperX<ProcedureModelGroup>().eq(ProcedureModelGroup::getProcedureModelId, procedureModelId));
    }

    List<ProcessConfigVO> selectDeleteTeamByModelIds(@Param("modelIds") Set<Long> procedureModelIds);
}

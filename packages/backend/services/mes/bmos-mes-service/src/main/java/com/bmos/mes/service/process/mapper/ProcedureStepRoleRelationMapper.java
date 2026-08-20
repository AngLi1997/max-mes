package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.model.ProcedureStepRole;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProcedureStepRoleRelationMapper extends BaseMapperX<ProcedureStepRole> {

    default List<ProcedureStepRole> selectListByProcedureStepIds(Set<Long> procedureStepIds) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepRole>()
                .in(ProcedureStepRole::getProcedureStepId, procedureStepIds));
    }

    default void deleteByProcedureStepIds(List<Long> procedureStepIds){
        delete(new LambdaQueryWrapperX<ProcedureStepRole>().in(ProcedureStepRole::getProcedureStepId,procedureStepIds));
    }
}

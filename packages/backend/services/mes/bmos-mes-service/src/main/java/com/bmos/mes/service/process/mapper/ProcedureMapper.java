package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.dto.query.ProcedureHistoricQueryDTO;
import com.bmos.mes.service.process.model.Procedure;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Mapper
public interface ProcedureMapper extends BaseMapperX<Procedure> {

    default List<Procedure> selectHistoricList(ProcedureHistoricQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<Procedure>()
                .eq(Procedure::getProcessId, dto.getProcessId())
                .likeIfPresent(Procedure::getName, dto.getName()));
    }

    default Procedure selectByProcessIdAndName(Long processId, String name){
        return selectOne(new LambdaQueryWrapperX<Procedure>()
                .eq(Procedure::getProcessId, processId)
                .eq(Procedure::getName, name).last("limit 1"));
    }
}

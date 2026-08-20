package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.dto.ProcedureStepValidateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureStepHistoricQueryDTO;
import com.bmos.mes.service.process.model.ProcedureStep;
import com.bmos.mes.service.process.vo.ProcedureStepTraceVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Mapper
public interface ProcedureStepMapper extends BaseMapperX<ProcedureStep> {

    default List<ProcedureStep> selectHistoricList(ProcedureStepHistoricQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<ProcedureStep>()
                .likeIfPresent(ProcedureStep::getName, dto.getName())
                .eq(ProcedureStep::getProcedureId, dto.getProcedureId()));
    }

    List<ProcedureStepTraceVO> selectTraceInfoListByProcedureStepIds(@Param("procedureStepIds") Collection<Long> procedureStepIds);

    default ProcedureStep selectByProcedureIdAndName(Long procedureId, String name){
        return selectOne(new LambdaQueryWrapperX<ProcedureStep>()
                .eq(ProcedureStep::getProcedureId, procedureId)
                .eq(ProcedureStep::getName, name).last("limit 1"));
    }
}

package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.model.ProcedureStepSop;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author renjinguang
 */
@Mapper
public interface ProcedureStepSopMapper extends BaseMapperX<ProcedureStepSop> {

    default List<ProcedureStepSop> queryListByStepModelId(Set<Long> stepIds){
        return selectList(new LambdaQueryWrapperX<ProcedureStepSop>()
                .in(ProcedureStepSop::getStepModelId,stepIds));
    }
}

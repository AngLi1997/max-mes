package com.bmos.mes.service.process.mapper;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.process.model.ProcedureModelRoom;
import com.bmos.mes.service.process.vo.ProcessConfigVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ProcedureModelRoomMapper extends BaseMapperX<ProcedureModelRoom> {

    default void deleteByProcedureModelIds(List<Long> ids) {
        delete(new LambdaQueryWrapperX<ProcedureModelRoom>()
                .in(ProcedureModelRoom::getProcedureModelId, ids));
    }

    default List<ProcedureModelRoom> selectByProcedureModelId(Long procedureModelId){
        return selectList(new LambdaQueryWrapperX<ProcedureModelRoom>()
                .eq(ProcedureModelRoom::getProcedureModelId, procedureModelId));
    }

    default List<ProcedureModelRoom> selectByProcedureModelIds(Set<Long> longs){
        if (CollUtil.isEmpty(longs)){
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<ProcedureModelRoom>().in(ProcedureModelRoom::getProcedureModelId, longs));
    }

    List<ProcessConfigVO> selectRoomIdByModelIds(@Param("modelIds") Set<Long> procedureModelIds);
}

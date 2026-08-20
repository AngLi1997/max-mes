package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.model.ProcessRelation;
import com.bmos.mes.service.process.vo.ProcessRelationDetailVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcessRelationMapper extends BaseMapperX<ProcessRelation> {

    default List<ProcessRelation> selectListByProcessId(Long processId) {
        return selectList(new LambdaQueryWrapperX<ProcessRelation>().eq(ProcessRelation::getProcessId, processId));
    }

    default void deleteByProcessId(Long processVersionId) {
        delete(new LambdaQueryWrapperX<ProcessRelation>().eq(ProcessRelation::getProcessId, processVersionId));
    }

    List<ProcessRelationDetailVO> selectDetailListByProcessId(@Param("processId") Long processId);
}

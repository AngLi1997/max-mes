package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.model.ProcessRelationMaterial;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProcessRelationMaterialMapper extends BaseMapperX<ProcessRelationMaterial> {

    default List<ProcessRelationMaterial> selectListByProcessRelationIds(Set<Long> processRelationIds) {
        return selectList(new LambdaQueryWrapperX<ProcessRelationMaterial>()
                .in(ProcessRelationMaterial::getProcessRelationId, processRelationIds));
    }

    default void deleteByProcessId(Long processId) {
        delete(new LambdaQueryWrapperX<ProcessRelationMaterial>()
                .eq(ProcessRelationMaterial::getProcessId, processId));
    }
}

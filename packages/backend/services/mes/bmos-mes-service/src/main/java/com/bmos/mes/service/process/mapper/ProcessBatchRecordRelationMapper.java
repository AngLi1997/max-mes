package com.bmos.mes.service.process.mapper;


import com.bmos.mes.service.process.model.ProcessBatchRecordRelation;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcessBatchRecordRelationMapper extends BaseMapperX<ProcessBatchRecordRelation> {

    default List<ProcessBatchRecordRelation> selectListByProcessVersionId(Long processVersionId) {
        return selectList(new LambdaQueryWrapperX<ProcessBatchRecordRelation>()
                .eq(ProcessBatchRecordRelation::getProcessVersionId, processVersionId));
    }

    default void deleteByProcessVersion(Long processVersionId) {
        delete(new LambdaQueryWrapperX<ProcessBatchRecordRelation>()
                .eq(ProcessBatchRecordRelation::getProcessVersionId, processVersionId));
    }

    Long existNonEnableBatchRecord(@Param("processVersionId") Long processVersionId, @Param("state") Integer state);


    default List<ProcessBatchRecordRelation> selectByProcessVersionIdList(List<Long> processVersionIdList){
        return selectList(new LambdaQueryWrapperX<ProcessBatchRecordRelation>()
                .in(ProcessBatchRecordRelation::getProcessVersionId, processVersionIdList));
    }

    List<Long> getByRecordVersionIds(@Param("batchRecordVersionIds") List<Long> batchRecordVersionIds);
}

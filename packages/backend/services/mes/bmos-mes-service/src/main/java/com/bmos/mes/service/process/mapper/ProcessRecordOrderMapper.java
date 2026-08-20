package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.process.model.ProcessRecordOrder;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcessRecordOrderMapper extends BaseMapperX<ProcessRecordOrder> {

    default List<ProcessRecordOrder> selectRecordItems(Long processId, String version) {
        return selectList(new LambdaQueryWrapperX<ProcessRecordOrder>()
                .eq(ProcessRecordOrder::getProcessId, processId)
                .eq(ProcessRecordOrder::getProcessVersion, version));
    }

    default void deleteRecordOrders(Long processId, String processVersion) {
        delete(new LambdaQueryWrapperX<ProcessRecordOrder>()
                .eq(ProcessRecordOrder::getProcessId, processId)
                .eq(ProcessRecordOrder::getProcessVersion, processVersion));
    }

    default List<ProcessRecordOrder> selectByRecordVersionIds(List<Long> batchRecordVersionIds){
        return selectList(new LambdaQueryWrapperX<ProcessRecordOrder>()
                .in(ProcessRecordOrder::getRecordVersionId, batchRecordVersionIds));
    }
}

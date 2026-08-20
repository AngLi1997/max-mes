package com.bmos.lims2.server.audit.mapper;

import com.bmos.lims2.server.audit.entity.FlowAuditProcess;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 流程工艺绑定关系表(BmFlowAuditProcess)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-05 10:08:06
 */
@Mapper
public interface FlowAuditProcessMapper extends BaseMapperX<FlowAuditProcess> {

    default void deleteByCode(String code){
        delete(new LambdaQueryWrapperX<FlowAuditProcess>()
                .eq(FlowAuditProcess::getCode, code));
    }

    default List<FlowAuditProcess> selectByProcessIdListAndCategoryCode(List<Long> processIdList, String categoryCode){
        return selectList(new LambdaQueryWrapperX<FlowAuditProcess>()
                .in(FlowAuditProcess::getProcessId, processIdList)
                .eq(FlowAuditProcess::getCategoryCode, categoryCode));
    }

    default FlowAuditProcess selectBindProcessFlowAudit(String categoryCode, Long processId){
        return selectOne(new LambdaQueryWrapperX<FlowAuditProcess>()
                .eq(FlowAuditProcess::getCategoryCode, categoryCode)
                .eq(FlowAuditProcess::getProcessId, processId));
    }

    default List<FlowAuditProcess> flowAuditProcessList(String code){
        return selectList(new LambdaQueryWrapperX<FlowAuditProcess>()
                .eq(FlowAuditProcess::getCode, code));
    }
}


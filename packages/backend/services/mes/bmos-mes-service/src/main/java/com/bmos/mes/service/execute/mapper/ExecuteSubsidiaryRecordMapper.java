package com.bmos.mes.service.execute.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.service.execute.dto.SubRecordNodeQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteSubsidiaryRecord;
import com.bmos.mes.service.workflow.dto.query.PlanSubRecordQueryDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ExecuteSubsidiaryRecordMapper extends BaseMapperX<ExecuteSubsidiaryRecord> {

    default boolean existedRecordNode(SubRecordNodeQueryDTO dto){
        return exists(new LambdaQueryWrapperX<ExecuteSubsidiaryRecord>()
                .eq(ExecuteSubsidiaryRecord::getProductPlanId, dto.getProductPlanId())
                .eq(ExecuteSubsidiaryRecord::getProcedureStepModelId, dto.getProcedureStepModelId())
                .eq(ExecuteSubsidiaryRecord::getProcessChangeNumber, dto.getProcessChangeNumber())
                .eq(ExecuteSubsidiaryRecord::getProcedureChangeNumber, dto.getProcedureChangeNumber()));
    }

    default void completeSubRecordNode(SubRecordNodeQueryDTO build, String userId){
        update(null, new LambdaUpdateWrapper<ExecuteSubsidiaryRecord>()
                .eq(ExecuteSubsidiaryRecord::getProductPlanId, build.getProductPlanId())
                .eq(ExecuteSubsidiaryRecord::getProcedureStepModelId, build.getProcedureStepModelId())
                .eq(ExecuteSubsidiaryRecord::getProcessChangeNumber, build.getProcessChangeNumber())
                .eq(ExecuteSubsidiaryRecord::getProcedureChangeNumber, build.getProcedureChangeNumber())
                .set(ExecuteSubsidiaryRecord::getEndTime, LocalDateTime.now())
                .set(ExecuteSubsidiaryRecord::getCompleteUserId, userId));
    }

    default List<ExecuteSubsidiaryRecord> selectPageList(PlanSubRecordQueryDTO dto){
        return selectList(new LambdaQueryWrapperX<ExecuteSubsidiaryRecord>()
                .eq(ExecuteSubsidiaryRecord::getProductPlanId, dto.getProductPlanId())
                .like(StrUtil.isNotEmpty(dto.getProcedureStepName()), ExecuteSubsidiaryRecord::getProcedureStepName, dto.getProcedureStepName())
                .like(StrUtil.isNotEmpty(dto.getProcedureName()), ExecuteSubsidiaryRecord::getProcedureName, dto.getProcedureName()));
    }

    default List<ExecuteSubsidiaryRecord> selectByProductPlanId(Long planId){
        return selectList(new LambdaQueryWrapperX<ExecuteSubsidiaryRecord>()
                .eq(ExecuteSubsidiaryRecord::getProductPlanId, planId));
    }
}

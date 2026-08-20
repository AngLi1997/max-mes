package com.bmos.mes.service.exception.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.common.enums.execute.ExceptionRecordModeEnum;
import com.bmos.mes.common.enums.execute.ExceptionStatusEnum;
import com.bmos.mes.service.exception.dto.BatchExceptionQueryDTO;
import com.bmos.mes.service.exception.dto.ExceptionPageQueryDTO;
import com.bmos.mes.service.exception.model.ExecuteException;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Mapper
public interface ExecuteExceptionMapper extends BaseMapperX<ExecuteException> {

    default List<ExecuteException> queryExceptionPage(ExceptionPageQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<ExecuteException>()
                .eq(Objects.nonNull(dto.getProductId()), ExecuteException::getProductId, dto.getProductId())
                .eq(StrUtil.isNotEmpty(dto.getExceptionType()), ExecuteException::getExceptionTypeCode,
                        dto.getExceptionType())
                .like(StrUtil.isNotEmpty(dto.getExceptionDescription()),
                        ExecuteException::getExceptionDescription, dto.getExceptionDescription())
                .like(StrUtil.isNotEmpty(dto.getBatchNo()), ExecuteException::getBatchNo, dto.getBatchNo())
                .eq(dto.getInvestigating(), ExecuteException::getExceptionStatus,
                        ExceptionStatusEnum.INVESTIGATING.getValue())
                .ne(!dto.getInvestigating(), ExecuteException::getExceptionStatus,
                        ExceptionStatusEnum.INVESTIGATING.getValue()).orderByDesc(ExecuteException::getUpdateTime));
    }

    default void updateAllInfo(ExecuteException model){
        update(model, new LambdaUpdateWrapper<ExecuteException>()
                .eq(ExecuteException::getId, model.getId())
                .set(ExecuteException::getRecordUserId, model.getRecordUserId())
                .set(ExecuteException::getRecordUserName, model.getRecordUserName())
                .set(ExecuteException::getRecordTime, model.getRecordTime())
                .set(ExecuteException::getProductId, model.getProductId())
                .set(ExecuteException::getProductFullName, model.getProductFullName())
                .set(ExecuteException::getExceptionType, model.getExceptionType())
                .set(ExecuteException::getExceptionDescription, model.getExceptionDescription())
                .set(ExecuteException::getProductPlanId, model.getProductPlanId())
                .set(ExecuteException::getBatchNo, model.getBatchNo())
                .set(ExecuteException::getProcessId, model.getProcessId())
                .set(ExecuteException::getProcessName, model.getProcessName())
                .set(ExecuteException::getProcessVersion, model.getProcessVersion())
                .set(ExecuteException::getProcedureId, model.getProcedureId())
                .set(ExecuteException::getProcedureModelId, model.getProcedureModelId())
                .set(ExecuteException::getProcedureName, model.getProcedureName())
                .set(ExecuteException::getProcedureName, model.getProcedureName())
                .set(ExecuteException::getProcedureStepId, model.getProcedureStepId())
                .set(ExecuteException::getProcedureStepModelId, model.getProcedureStepModelId())
                .set(ExecuteException::getProcedureStepName, model.getProcedureStepName())
                .set(ExecuteException::getProcedureName, model.getProcedureName())
                .set(ExecuteException::getExceptionTypeCode, model.getExceptionTypeCode())
                .set(ExecuteException::getRecordMode, ExceptionRecordModeEnum.MANUAL_RECORD)
                .set(ExecuteException::getUpdateTime, LocalDateTime.now())
                .set(ExecuteException::getUpdateBy, SysUserHolder.getUser().getUserId())
        );
    }

    default List<ExecuteException> queryExceptionPage(BatchExceptionQueryDTO dto){
        return selectList(new LambdaQueryWrapperX<ExecuteException>()
                .eq(ExecuteException::getProductPlanId, dto.getProductPlanId())
                .eq(StrUtil.isNotEmpty(dto.getExceptionStatus()), ExecuteException::getExceptionStatus, dto.getExceptionStatus())
                .eq(StrUtil.isNotEmpty(dto.getExceptionType()), ExecuteException::getExceptionTypeCode, dto.getExceptionType())
                .like(StrUtil.isNotEmpty(dto.getExceptionDescription()), ExecuteException::getExceptionDescription, dto.getExceptionDescription())
                .orderByAsc(!dto.isTraceQuery(), ExecuteException::getExceptionStatus)
                .orderByDesc(!dto.isTraceQuery(), ExecuteException::getUpdateTime)
                .orderByAsc(dto.isTraceQuery(), ExecuteException::getRecordTime));
    }

    default List<ExecuteException> selectByExecuteFormDataIds(List<Long> longs){
        return selectList(new LambdaQueryWrapperX<ExecuteException>()
                .in(ExecuteException::getExecuteFormDataId, longs));
    }

    /**
     * 根据生产计划id列表查询执行异常记录列表
     * @param planIds 生产计划id
     * @return
     */
    default List<ExecuteException> queryListByPlanIds(Collection<Long> planIds){
        if (CollectionUtil.isEmpty(planIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<ExecuteException>()
                .in(ExecuteException::getProductPlanId, planIds));
    }

    default void reInvestigate(Long id){
        update(null, new LambdaUpdateWrapper<ExecuteException>().eq(ExecuteException::getId, id)
                .set(ExecuteException::getExceptionStatus, ExceptionStatusEnum.INVESTIGATING)
                .set(ExecuteException::getHandleUserId, null)
                .set(ExecuteException::getHandleTime, null)
                .set(ExecuteException::getHandleResult, null)
                .set(ExecuteException::getHandleUserName, null)
                .set(ExecuteException::getUpdateTime, LocalDateTime.now())
                .set(ExecuteException::getUpdateBy, SysUserHolder.getUser().getUserId()));
    }
}

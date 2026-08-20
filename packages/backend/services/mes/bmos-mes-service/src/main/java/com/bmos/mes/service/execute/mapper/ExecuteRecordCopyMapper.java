package com.bmos.mes.service.execute.mapper;

import com.bmos.mes.service.execute.dto.*;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.vo.CopyRecordItemVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface ExecuteRecordCopyMapper extends BaseMapperX<ExecuteRecordCopy> {

    Long getMaxVersion(RecordCopySaveDTO dto);

    List<CopyRecordItemVO> getCopyVersionList(RecordCopyQueryDTO dto);

    default Boolean existsCopy(FormDataBatchSaveDTO dto) {
        return exists(new LambdaQueryWrapperX<ExecuteRecordCopy>()
                .eq(ExecuteRecordCopy::getProductPlanId,dto.getProductPlanId())
                .eq(ExecuteRecordCopy::getRecordItemId,dto.getRecordItemId())
                .eq(ExecuteRecordCopy::getReuse,dto.getReuse())
                .eq(!dto.getReuse(),ExecuteRecordCopy::getProcedureStepId,dto.getProcedureStepId())
                .last("limit 1"));
    }

    void discardRecordItem(FormDataDiscardDTO dto);

    default List<ExecuteRecordCopy> selectListBySteps(Long productPlanId, Set<Long> stepIds){
        return selectList(new LambdaQueryWrapperX<ExecuteRecordCopy>()
                .eq(ExecuteRecordCopy::getProductPlanId,productPlanId)
                .inIfPresent(ExecuteRecordCopy::getProcedureStepId,stepIds));
    }

    default List<ExecuteRecordCopy> selectCurrentStepCopies(CopiesQueryDTO build){
        return selectList(new LambdaQueryWrapperX<ExecuteRecordCopy>()
                .eq(ExecuteRecordCopy::getProductPlanId, build.getProductPlanId())
                .eq(ExecuteRecordCopy::getRecordItemId, build.getRecordItemId())
                .eq(ExecuteRecordCopy::getReuse, build.getReuse())
                .eq(ExecuteRecordCopy::getProductPlanId, build.getProductPlanId())
                .eq(ExecuteRecordCopy::getDiscard, false)
                .eq(!build.getReuse(), ExecuteRecordCopy::getProcedureStepId, build.getProcedureStepId())
        );
    }

    default List<ExecuteRecordCopy> selectByRecordItemIds(Long productPlanId, Collection<Long> recordItems){
        return selectList(new LambdaQueryWrapperX<ExecuteRecordCopy>()
                .eq(ExecuteRecordCopy::getProductPlanId, productPlanId)
                .in(ExecuteRecordCopy::getRecordItemId, recordItems));
    }

    default List<ExecuteRecordCopy> selectByRecordVersionId(Long productPlanId, Long recordVersionId){
        return selectList(new LambdaQueryWrapperX<ExecuteRecordCopy>()
                .eq(ExecuteRecordCopy::getProductPlanId, productPlanId)
                .eq(ExecuteRecordCopy::getRecordVersionId, recordVersionId));
    }

    default List<ExecuteRecordCopy> selectByPlanIdList(List<Long> planIdList){
        return selectList(new LambdaQueryWrapperX<ExecuteRecordCopy>()
                .in(ExecuteRecordCopy::getProductPlanId, planIdList));
    }
}

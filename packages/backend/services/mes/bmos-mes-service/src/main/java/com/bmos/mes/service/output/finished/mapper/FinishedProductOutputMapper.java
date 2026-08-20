package com.bmos.mes.service.output.finished.mapper;

import cn.hutool.core.util.BooleanUtil;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.output.finished.model.FinishedProductOutput;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FinishedProductOutputMapper extends BaseMapperX<FinishedProductOutput> {

    default FinishedProductOutput selectUnique(UniqueComponentQueryDTO dto){
        return selectOne(new LambdaQueryWrapperX<FinishedProductOutput>()
                .eq(FinishedProductOutput::getProductPlanId, dto.getProductPlanId())
                .eq(FinishedProductOutput::getComponentId,dto.getComponentId())
                .eq(FinishedProductOutput::getCopyVersion, dto.getCopyVersion())
                .eq(FinishedProductOutput::getRecordItemId, dto.getRecordItemId())
                .eq(FinishedProductOutput::getRecordVersionId, dto.getRecordVersionId())
                .eq(FinishedProductOutput::getReuse, dto.getReuse())
                .eq(FinishedProductOutput::getProcedureStepModelId, BooleanUtil.isTrue(dto.getReuse()) ? 0 : dto.getProcedureStepModelId()));
    }

    String getProductBatchNo(@Param("productPlanId") Long productPlanId);

}

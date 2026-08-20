package com.bmos.mes.service.requisition.mapper;

import cn.hutool.core.util.BooleanUtil;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.requisition.model.ReserveComponentInstance;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReserveComponentInstanceMapper extends BaseMapperX<ReserveComponentInstance> {

    default ReserveComponentInstance selectUnique(UniqueComponentQueryDTO dto) {
        return selectOne(new LambdaQueryWrapperX<ReserveComponentInstance>()
                .eq(ReserveComponentInstance::getProductPlanId, dto.getProductPlanId())
                .eq(ReserveComponentInstance::getComponentId, dto.getComponentId())
                .eq(ReserveComponentInstance::getCopyVersion, dto.getCopyVersion())
                .eq(ReserveComponentInstance::getRecordItemId, dto.getRecordItemId())
                .eq(ReserveComponentInstance::getRecordVersionId, dto.getRecordVersionId())
                .eq(ReserveComponentInstance::getReuse, dto.getReuse())
                .eq(BooleanUtil.isFalse(dto.getReuse()),ReserveComponentInstance::getProcedureStepModelId, dto.getProcedureStepModelId()));
    }


}

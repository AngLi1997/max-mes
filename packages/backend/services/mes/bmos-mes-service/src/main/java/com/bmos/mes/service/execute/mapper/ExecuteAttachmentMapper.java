package com.bmos.mes.service.execute.mapper;

import com.bmos.mes.common.enums.execute.AttachmentTypeEnum;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExecuteAttachmentMapper extends BaseMapperX<ExecuteAttachment> {
    default List<ExecuteAttachment> selectItemList(ExecuteAttachmentQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .eq(ExecuteAttachment::getProductPlanId, dto.getProductPlanId())
                .eq(ExecuteAttachment::getCopyVersion, dto.getCopyVersion())
                .eq(ExecuteAttachment::getRecordItemId, dto.getRecordItemId())
                .eq(!dto.getReuse(), ExecuteAttachment::getProcedureStepId, dto.getProcedureStepId())
                .eq(ExecuteAttachment::getReuse, dto.getReuse())
                .eq(ExecuteAttachment::getProcessChangeNumber, dto.getProcessChangeNumber())
                .eq(ExecuteAttachment::getProcedureChangeNumber, dto.getProcedureChangeNumber())
                .eqIfPresent(ExecuteAttachment::getType, dto.getType()));
    }

    default List<ExecuteAttachment> selectItemList(Long productPlanId, Long recordItemId,
                                                   Long stepId, AttachmentTypeEnum attachmentTypeEnum) {
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .eq(ExecuteAttachment::getProductPlanId, productPlanId)
                .eq(ExecuteAttachment::getRecordItemId, recordItemId)
                .eq(ExecuteAttachment::getProcedureStepId, stepId)
                .eqIfPresent(ExecuteAttachment::getType, attachmentTypeEnum.getValue()));
    }

    default List<ExecuteAttachment> selectByProductPlanId(Long productPlanId) {
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .eq(ExecuteAttachment::getProductPlanId, productPlanId));
    }

    default List<ExecuteAttachment> selectListById(List<String> attachmentIdList){
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .in(ExecuteAttachment::getId, attachmentIdList));
    }

    default List<ExecuteAttachment> selectByPlanIdListAndType(List<Long> sortPlanIdList, AttachmentTypeEnum attachmentTypeEnum){
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .in(ExecuteAttachment::getProductPlanId, sortPlanIdList)
                .eq(ExecuteAttachment::getAttachmentType, attachmentTypeEnum.getValue()));
    }
}

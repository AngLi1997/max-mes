package com.bmos.lims2.server.eln.entry.mapper;

import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentQueryDTO;
import com.bmos.lims2.server.eln.entry.entity.ExecuteAttachment;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExecuteAttachmentMapper extends BaseMapperX<ExecuteAttachment> {
    default List<ExecuteAttachment> selectItemList(ExecuteAttachmentQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .eq(ExecuteAttachment::getInspectionOrderId, dto.getInspectionOrderId())
                .eq(ExecuteAttachment::getRecordVersionId, dto.getRecordVersionId())
                .eq(ExecuteAttachment::getParameterConfigId, dto.getParameterConfigId())
                .eqIfPresent(ExecuteAttachment::getAttachmentType, dto.getAttachmentType())
                .eqIfPresent(ExecuteAttachment::getType, dto.getType()));
    }

    default List<ExecuteAttachment> selectListById(List<String> attachmentIdList){
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .in(ExecuteAttachment::getId, attachmentIdList));
    }

    default List<ExecuteAttachment> selectByInspectionOrderId(Long inspectionOrderId){
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .eq(ExecuteAttachment::getInspectionOrderId, inspectionOrderId));
    }

    default List<ExecuteAttachment> getListByTaskId(Long taskId){
        return selectList(new LambdaQueryWrapperX<ExecuteAttachment>()
                .eq(ExecuteAttachment::getTaskId, taskId));
    }
}

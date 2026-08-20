package com.bmos.mes.service.execute.convert;

import com.bmos.mes.common.enums.execute.AttachmentTypeEnum;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentUploadDTO;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.AttachmentVO;
import com.bmos.mes.service.execute.vo.IntactFormAttachmentItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ExecuteAttachmentConvert {
    ExecuteAttachmentConvert INSTANCE = Mappers.getMapper(ExecuteAttachmentConvert.class);

    ExecuteAttachment convert(ExecuteAttachmentUploadDTO dto);

    List<AttachmentVO> convertList(List<ExecuteAttachment> attachments);

    List<IntactFormAttachmentItemVO> convertList2(List<ExecuteAttachment> attachments);

    ExecuteAttachment convertExecuteAttachment(ExecuteFormData formData);

    default List<ExecuteAttachment> convertVoList(List<AttachmentVO> pictureList, ExecuteFormData formData,Long recordVersionId) {
        return pictureList.stream().map(item -> {
            ExecuteAttachment attachment = convertExecuteAttachment(formData);
            attachment.setId(item.getId());
            attachment.setType(item.getType());
            attachment.setCreateTime(item.getCreateTime());
            attachment.setCreateBy(item.getCreateBy());
            attachment.setAttachmentType(AttachmentTypeEnum.MODULE_PICTURE.getValue());
            //处理地址后期归档时候使用
            attachment.setPath(item.getPath());
            attachment.setProcessChangeNumber(formData.getProcessChangeNumber());
            attachment.setProcedureChangeNumber(formData.getProcedureChangeNumber());
            attachment.setRecordVersionId(recordVersionId);
            attachment.setRemark(item.getRemark());
            return attachment;
        }).collect(Collectors.toList());
    }
}

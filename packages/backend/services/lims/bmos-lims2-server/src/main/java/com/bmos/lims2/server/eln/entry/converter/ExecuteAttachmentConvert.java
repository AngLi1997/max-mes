package com.bmos.lims2.server.eln.entry.converter;

import com.bmos.lims2.server.eln.entry.dto.AttachmentDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentUploadDTO;
import com.bmos.lims2.server.eln.entry.entity.ExecuteAttachment;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.enums.AttachmentTypeEnum;
import com.bmos.lims2.server.eln.entry.vo.AttachmentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ExecuteAttachmentConvert {
    ExecuteAttachmentConvert INSTANCE = Mappers.getMapper(ExecuteAttachmentConvert.class);

    ExecuteAttachment convert(ExecuteAttachmentUploadDTO dto);

    List<AttachmentDTO> convertList(List<ExecuteAttachment> attachments);

    ExecuteAttachment convertExecuteAttachment(ExecuteFormData formData);

    default List<ExecuteAttachment> convertVoList(List<AttachmentVO> pictureList, ExecuteFormData formData,Long recordVersionId) {
        return pictureList.stream().map(item -> {
            ExecuteAttachment attachment = convertExecuteAttachment(formData);
            attachment.setId(item.getId());
            attachment.setType(item.getType());
            attachment.setCreateTime(item.getCreateTime());
            attachment.setCreateBy(item.getCreateBy());
            attachment.setAttachmentType(AttachmentTypeEnum.MODULE_PICTURE.getValue());
            attachment.setRemark(item.getRemark());
            attachment.setRecordVersionId(recordVersionId);
            attachment.setPath(item.getPath());
            return attachment;
        }).collect(Collectors.toList());
    }

}

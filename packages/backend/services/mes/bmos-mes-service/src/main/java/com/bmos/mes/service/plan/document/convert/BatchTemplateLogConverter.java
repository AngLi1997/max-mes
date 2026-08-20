package com.bmos.mes.service.plan.document.convert;

import com.bmos.common.base.user.SysUser;
import com.bmos.mes.service.plan.document.model.BatchTemplateOperateLog;
import com.bmos.mes.service.plan.document.service.dto.BatchTemplateLogSaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface BatchTemplateLogConverter {
    BatchTemplateLogConverter INSTANCE = Mappers.getMapper(BatchTemplateLogConverter.class);

    default BatchTemplateOperateLog convert(BatchTemplateLogSaveDTO dto, SysUser user){
        BatchTemplateOperateLog batchTemplateOperateLog = new BatchTemplateOperateLog();
        batchTemplateOperateLog.setOperateType(dto.getOperateType().getValue());
        batchTemplateOperateLog.setOperateTime(LocalDateTime.now());
        batchTemplateOperateLog.setPath(dto.getPath());
        batchTemplateOperateLog.setBatchTemplateVersionId(dto.getBatchTemplateVersionId());
        batchTemplateOperateLog.setRemark(dto.getRemark());
        batchTemplateOperateLog.setOperatorId(user.getUserId());
        batchTemplateOperateLog.setOperatorLoginName(user.getLoginName());
        batchTemplateOperateLog.setOperatorName(user.getUserName());
        return batchTemplateOperateLog;
    }
}

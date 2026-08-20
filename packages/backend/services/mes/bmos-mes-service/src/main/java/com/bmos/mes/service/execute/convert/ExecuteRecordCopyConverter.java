package com.bmos.mes.service.execute.convert;

import com.bmos.mes.service.execute.dto.RecordCopySaveDTO;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExecuteRecordCopyConverter {

    ExecuteRecordCopyConverter INSTANCE = Mappers.getMapper(ExecuteRecordCopyConverter.class);

    @Mapping(target = "version",source = "maxVersion")
    ExecuteRecordCopy convert(RecordCopySaveDTO dto, Long maxVersion);
}

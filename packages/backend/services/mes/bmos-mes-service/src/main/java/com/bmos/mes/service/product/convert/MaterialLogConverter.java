package com.bmos.mes.service.product.convert;

import com.bmos.mes.service.product.dto.MaterialLogSaveDTO;
import com.bmos.mes.service.product.model.MaterialLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MaterialLogConverter {

    MaterialLogConverter INSTANCE = Mappers.getMapper(MaterialLogConverter.class);

    MaterialLog convertToMaterialLog(MaterialLogSaveDTO log);

    List<MaterialLog> convertToMaterialLog(List<MaterialLogSaveDTO> logs);
}

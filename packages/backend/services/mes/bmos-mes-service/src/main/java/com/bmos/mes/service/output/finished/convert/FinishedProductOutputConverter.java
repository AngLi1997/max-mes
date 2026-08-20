package com.bmos.mes.service.output.finished.convert;

import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import com.bmos.mes.service.output.finished.vo.FinishedProductOutputListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FinishedProductOutputConverter {
    FinishedProductOutputConverter INSTANCE = Mappers.getMapper(FinishedProductOutputConverter.class);

    List<FinishedProductOutputListVO> convertToFinishedProductOutputListVO(List<FinishedProductOutputResult> results);
}

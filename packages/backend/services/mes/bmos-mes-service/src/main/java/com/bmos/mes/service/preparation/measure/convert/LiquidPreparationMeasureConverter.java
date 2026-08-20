package com.bmos.mes.service.preparation.measure.convert;

import com.bmos.mes.service.preparation.measure.dto.LiquidPreparationMeasureLogSaveDTO;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureLog;
import com.bmos.mes.service.preparation.measure.vo.LiquidMeasureLogPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LiquidPreparationMeasureConverter {

    LiquidPreparationMeasureConverter INSTANCE = Mappers.getMapper(LiquidPreparationMeasureConverter.class);

    LiquidPreparationMeasureLog convertToLog(LiquidPreparationMeasureLogSaveDTO dto);

    List<LiquidMeasureLogPageVO> convertToLogPage(List<LiquidPreparationMeasureLog> logs);
}

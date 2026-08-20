package com.bmos.mes.service.preparation.measure.service;

import com.bmos.mes.service.preparation.measure.dto.LiquidMeasureLogPageQueryDTO;
import com.bmos.mes.service.preparation.measure.dto.LiquidPreparationMeasureLogSaveDTO;
import com.bmos.mes.service.preparation.measure.vo.LiquidMeasureLogPageVO;
import com.bmos.mybatis.page.CommonPage;

public interface LiquidPreparationMeasureLogService {

    void saveLog(LiquidPreparationMeasureLogSaveDTO dto);

    CommonPage<LiquidMeasureLogPageVO> queryMeasureLogPage(LiquidMeasureLogPageQueryDTO dto);
}

package com.bmos.mes.service.ingredient.weigh.service;

import com.bmos.mes.service.ingredient.weigh.dto.WeighLogQueryDTO;
import com.bmos.mes.service.ingredient.weigh.dto.WeighLogSaveDTO;
import com.bmos.mes.service.ingredient.weigh.vo.WeighLogPageVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface WeighLogService {
    CommonPage<WeighLogPageVO> queryWeighLogPage(WeighLogQueryDTO dto);

    void saveLog(WeighLogSaveDTO dto);

    void saveLogs(List<WeighLogSaveDTO> dtos);
}

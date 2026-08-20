package com.bmos.mes.service.output.finished.service;

import com.bmos.mes.service.output.finished.dto.SaveFinishedProductOutputDTO;
import com.bmos.mes.service.output.finished.dto.ValidateFinishedProductComponentDTO;
import com.bmos.mes.service.output.finished.vo.FinishedProductComponentDetailVO;
import com.bmos.mes.service.output.finished.vo.FinishedProductOutputListVO;

import java.util.List;

public interface FinishedProductOutputService {
    FinishedProductComponentDetailVO getComponentDetail(ValidateFinishedProductComponentDTO dto);

    List<FinishedProductOutputListVO> getFinishedProductOutputList(Long id);

    void saveFinishedProductOutputList(SaveFinishedProductOutputDTO dto);
}

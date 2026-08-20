package com.bmos.mes.service.product.service;

import com.bmos.mes.service.product.dto.MaterialLogPageQueryDTO;
import com.bmos.mes.service.product.dto.MaterialLogSaveDTO;
import com.bmos.mes.service.product.model.MaterialLog;
import com.bmos.mes.service.product.vo.MaterialLogPageVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface MaterialLogService {

    CommonPage<MaterialLogPageVO> getMaterialLogPage(MaterialLogPageQueryDTO dto);

    void saveMaterialLog(MaterialLogSaveDTO log);

    void saveMaterialLogs(List<MaterialLog> logs);

}

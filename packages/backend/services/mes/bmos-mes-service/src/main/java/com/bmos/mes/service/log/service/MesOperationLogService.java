package com.bmos.mes.service.log.service;

import com.bmos.logging.service.OperationLogService;
import com.bmos.mes.service.log.dto.ExportOperationLogDTO;
import com.bmos.mes.service.log.dto.OperationLogDetailDTO;
import com.bmos.mes.service.log.dto.QueryLogPageDTO;
import com.bmos.mes.service.log.model.MesLogModel;
import com.bmos.mes.service.log.vo.MesLogDetailVO;
import com.bmos.mes.service.log.vo.MesLogPageVO;
import com.bmos.mybatis.page.CommonPage;

import java.io.IOException;

public interface MesOperationLogService extends OperationLogService<MesLogModel> {
    CommonPage<MesLogPageVO> getPage(QueryLogPageDTO dto);

    void exportOperationLog(ExportOperationLogDTO dto);

    MesLogDetailVO getDetail(OperationLogDetailDTO dto);

}

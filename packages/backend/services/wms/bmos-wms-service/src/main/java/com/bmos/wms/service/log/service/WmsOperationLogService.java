package com.bmos.wms.service.log.service;


import com.bmos.logging.service.OperationLogService;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.log.dto.ExportOperationLogDTO;
import com.bmos.wms.service.log.dto.QueryLogPageDTO;
import com.bmos.wms.service.log.model.WmsLogModel;
import com.bmos.wms.service.log.vo.WmsLogDetailVO;
import com.bmos.wms.service.log.vo.WmsLogPageVO;

import java.io.IOException;

public interface WmsOperationLogService extends OperationLogService<WmsLogModel> {
    CommonPage<WmsLogPageVO> getPage(QueryLogPageDTO dto);

    void exportOperationLog(ExportOperationLogDTO dto) throws IOException;

    WmsLogDetailVO getDetail(Long id);
}

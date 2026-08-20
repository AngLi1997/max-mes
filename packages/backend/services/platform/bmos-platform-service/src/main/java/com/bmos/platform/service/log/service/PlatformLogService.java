package com.bmos.platform.service.log.service;

import com.bmos.logging.service.OperationLogService;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.log.dto.ExportOperationLogDTO;
import com.bmos.platform.service.log.dto.OperationLogDetailDTO;
import com.bmos.platform.service.log.dto.QueryOperationLogPageDTO;
import com.bmos.platform.service.log.model.OperationLogModel;
import com.bmos.platform.service.log.vo.OperationLogDetailVO;
import com.bmos.platform.service.log.vo.OperationLogPageVO;

public interface PlatformLogService extends OperationLogService<OperationLogModel> {

    CommonPage<OperationLogPageVO> getOperationLogPage(QueryOperationLogPageDTO dto);

    void exportOperationLog(ExportOperationLogDTO dto);

    /**
     * 查询所选操作日志详情
     * @param dto
     * @return
     */
    OperationLogDetailVO getOperationLogDetailInfo(OperationLogDetailDTO dto);

}

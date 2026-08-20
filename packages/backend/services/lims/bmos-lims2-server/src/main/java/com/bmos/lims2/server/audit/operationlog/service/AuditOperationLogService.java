package com.bmos.lims2.server.audit.operationlog.service;

import com.bmos.lims2.server.audit.operationlog.dto.OperationHistorySaveDTO;
import com.bmos.lims2.server.audit.operationlog.dto.OperationLogPageQueryDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.lims2.server.audit.operationlog.vo.OperationLogPageVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface AuditOperationLogService {
    void save(AuditOperationLogEntity model);

    CommonPage<OperationLogPageVO> getPage(OperationLogPageQueryDTO dto);

    List<ListLogVO> listRecordLog(Long versionId);

    void saveLog(OperationHistorySaveDTO dto);

    void saveBatch(List<AuditOperationLogEntity> collect);
}

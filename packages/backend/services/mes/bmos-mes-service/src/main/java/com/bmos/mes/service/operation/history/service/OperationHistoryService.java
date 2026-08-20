package com.bmos.mes.service.operation.history.service;

import com.bmos.mes.service.operation.history.dto.OperationHistorySaveDTO;
import com.bmos.mes.service.operation.history.dto.OperationLogPageQueryDTO;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.vo.OperationLogPageVO;
import com.bmos.mes.service.record.vo.VersionLogVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface OperationHistoryService {
    void save(OperationLogModel model);

    CommonPage<OperationLogPageVO> getPage(OperationLogPageQueryDTO dto);

    List<VersionLogVO> listRecordLog(Long versionId);

    void saveLog(OperationHistorySaveDTO dto);

    void saveBatch(List<OperationLogModel> collect);

    List<VersionLogVO> getPlanHistoryList(Long businessId);
}

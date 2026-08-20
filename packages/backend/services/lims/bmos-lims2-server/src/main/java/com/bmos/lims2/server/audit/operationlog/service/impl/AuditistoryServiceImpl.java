package com.bmos.lims2.server.audit.operationlog.service.impl;

import com.bmos.lims2.server.audit.operationlog.dto.OperationHistorySaveDTO;
import com.bmos.lims2.server.audit.operationlog.dto.OperationLogPageQueryDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.mapper.AuditOperationLogMapper;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.lims2.server.audit.operationlog.vo.OperationLogPageVO;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditistoryServiceImpl implements AuditOperationLogService {

    @Autowired
    private AuditOperationLogMapper operationLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AuditOperationLogEntity model) {
        operationLogMapper.insert(model);
    }

    @Override
    public CommonPage<OperationLogPageVO> getPage(OperationLogPageQueryDTO dto) {
        return CommonPage.convertPage(operationLogMapper.selectPageList(dto));
    }

    @Override
    public List<ListLogVO> listRecordLog(Long versionId) {
        return operationLogMapper.listRecordLog(versionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(OperationHistorySaveDTO dto) {
        AuditOperationLogEntity model = new AuditOperationLogEntity();
        model.setBusinessId(dto.getBusinessId());
        model.setOperationType(dto.getType());
        operationLogMapper.insert(model);
    }

    @Override
    public void saveBatch(List<AuditOperationLogEntity> collect) {
        operationLogMapper.insertBatch(collect);
    }

}

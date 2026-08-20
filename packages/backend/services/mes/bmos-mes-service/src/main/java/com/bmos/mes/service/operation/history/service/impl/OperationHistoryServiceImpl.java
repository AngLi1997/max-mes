package com.bmos.mes.service.operation.history.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.logging.enums.OperationTypeEnum;
import com.bmos.mes.service.operation.history.dto.OperationHistorySaveDTO;
import com.bmos.mes.service.operation.history.dto.OperationLogPageQueryDTO;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.operation.history.mapper.OperationLogMapper;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.operation.history.vo.OperationLogPageVO;
import com.bmos.mes.service.record.vo.VersionLogVO;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OperationHistoryServiceImpl implements OperationHistoryService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OperationLogModel model) {
        operationLogMapper.insert(model);
    }

    @Override
    public CommonPage<OperationLogPageVO> getPage(OperationLogPageQueryDTO dto) {
        return CommonPage.convertPage(operationLogMapper.selectPageList(dto));
    }

    @Override
    public List<VersionLogVO> listRecordLog(Long versionId) {
        return operationLogMapper.listRecordLog(versionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(OperationHistorySaveDTO dto) {
        OperationLogModel model = new OperationLogModel();
        model.setBusinessId(dto.getBusinessId());
        model.setModule(BusinessModule.BATCH_RECORD.name());
        model.setOperationType(dto.getType());
        operationLogMapper.insert(model);
    }

    @Override
    public void saveBatch(List<OperationLogModel> collect) {
        operationLogMapper.insertBatch(collect);
    }

    @Override
    public List<VersionLogVO> getPlanHistoryList(Long businessId) {
        List<VersionLogVO> vos = this.listRecordLog(businessId);
        if (CollUtil.isEmpty(vos)){
            return Collections.emptyList();
        }
        return CollectionUtils.filterList(vos,item-> item.getOperationType().equals(OperationType.view) ||
                        item.getOperationType().equals(OperationType.PRINT));
    }

}

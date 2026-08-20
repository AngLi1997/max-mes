package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.process.convert.ProcessRecordOrderConverter;
import com.bmos.mes.service.process.dto.save.ProcessRecordOrderSaveDTO;
import com.bmos.mes.service.process.mapper.ProcessRecordOrderMapper;
import com.bmos.mes.service.process.model.ProcessRecordOrder;
import com.bmos.mes.service.process.service.ProcessRecordOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class ProcessRecordOrderServiceImpl implements ProcessRecordOrderService {

    @Autowired
    private ProcessRecordOrderMapper processRecordOrderMapper;


    @Override
    public List<ProcessRecordOrder> getRecordItems(Long processId, String version) {
        return processRecordOrderMapper.selectRecordItems(processId,version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecordOrders(ProcessRecordOrderSaveDTO dto) {
        processRecordOrderMapper.deleteRecordOrders(dto.getProcessId(),dto.getProcessVersion());
        processRecordOrderMapper.insertBatch(ProcessRecordOrderConverter.INSTANCE.convertList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Collection<ProcessRecordOrder> orders) {
        if (CollUtil.isEmpty(orders)){
            return;
        }
        processRecordOrderMapper.insertBatch(orders);
    }
}

package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.dto.save.ProcessRecordOrderSaveDTO;
import com.bmos.mes.service.process.model.ProcessRecordOrder;

import java.util.Collection;
import java.util.List;

public interface ProcessRecordOrderService {
    List<ProcessRecordOrder> getRecordItems(Long processId, String version);

    void saveRecordOrders(ProcessRecordOrderSaveDTO dto);

    void saveBatch(Collection<ProcessRecordOrder> orders);
}

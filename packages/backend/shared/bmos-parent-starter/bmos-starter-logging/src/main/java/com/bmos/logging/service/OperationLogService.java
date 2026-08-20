package com.bmos.logging.service;

import com.bmos.logging.model.LogModel;

public interface OperationLogService<T extends LogModel> {

    void save(T logModel);

}

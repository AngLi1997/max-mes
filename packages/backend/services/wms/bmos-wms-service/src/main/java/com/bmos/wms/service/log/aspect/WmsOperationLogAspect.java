package com.bmos.wms.service.log.aspect;

import com.bmos.logging.aspect.OperationLogAspect;
import com.bmos.wms.service.log.model.WmsLogModel;
import com.bmos.wms.service.log.service.WmsOperationLogService;
import org.springframework.stereotype.Component;

@Component
public class WmsOperationLogAspect extends OperationLogAspect<WmsLogModel, WmsOperationLogService> {

    @Override
    public WmsLogModel initLogModel() {
        return new WmsLogModel();
    }

    public WmsOperationLogAspect(WmsOperationLogService service) {
        super(service);
    }
}

package com.bmos.platform.service.log.aspect;

import com.bmos.logging.aspect.OperationLogAspect;
import com.bmos.platform.service.log.model.OperationLogModel;
import com.bmos.platform.service.log.service.PlatformLogService;
import org.springframework.stereotype.Component;

@Component
public class PlatformLogAspect extends OperationLogAspect<OperationLogModel, PlatformLogService> {
    public PlatformLogAspect(PlatformLogService service) {
        super(service);
    }

    @Override
    public OperationLogModel initLogModel() {
        return new OperationLogModel();
    }
}

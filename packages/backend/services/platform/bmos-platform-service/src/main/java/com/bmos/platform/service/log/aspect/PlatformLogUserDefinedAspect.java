package com.bmos.platform.service.log.aspect;

import com.bmos.logging.aspect.defined.OperationUserDefinedAspect;
import com.bmos.platform.service.log.model.OperationLogModel;
import com.bmos.platform.service.log.service.PlatformLogService;
import org.springframework.stereotype.Component;

@Component
public class PlatformLogUserDefinedAspect extends OperationUserDefinedAspect<OperationLogModel, PlatformLogService> {

    public PlatformLogUserDefinedAspect(PlatformLogService service) {
        super(service);
    }

    @Override
    public OperationLogModel initLogModel() {
        return new OperationLogModel();
    }
}

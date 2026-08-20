package com.bmos.mes.service.log.aspect;

import com.bmos.logging.aspect.OperationLogAspect;
import com.bmos.mes.service.log.model.MesLogModel;
import com.bmos.mes.service.log.service.MesOperationLogService;
import org.springframework.stereotype.Component;

@Component
public class MesOperationLogAspect extends OperationLogAspect<MesLogModel, MesOperationLogService> {

    @Override
    public MesLogModel initLogModel() {
        return new MesLogModel();
    }

    public MesOperationLogAspect(MesOperationLogService service) {
        super(service);
    }
}

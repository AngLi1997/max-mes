package com.bmos.mes.service.workflow.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.convert.ProcessConfirmConverter;
import com.bmos.mes.service.process.model.ProcessConfirm;
import com.bmos.mes.service.process.service.ProcedureConfirmService;
import com.bmos.mes.service.process.service.ProcessConfirmService;
import com.bmos.orchestrator.engine.core.listener.InfiniteEvent;
import com.bmos.orchestrator.engine.core.listener.InfiniteEventListener;
import com.bmos.orchestrator.engine.core.listener.InfiniteEventType;
import com.bmos.orchestrator.engine.core.listener.InfiniteProcessEngineListenerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 工序结束=>子流程结束
 */
@Component
@Slf4j
public class WorkflowProcedureEndEventListener implements InfiniteEventListener {

    @Autowired
    private ProcessConfirmService processConfirmService;

    @Autowired
    private ProcedureConfirmService procedureConfirmService;

    @PostConstruct
    public void addListener() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.CALL_ACTIVITY_END, this);
    }

    @Override
    public void notified(InfiniteEvent event) {
        Map<String, Object> payload = (Map<String, Object>) event.getPayload();
        String instanceId = (String) payload.get("instanceId");
        String procedureName = (String) payload.get("procedureName");
        if (StrUtil.isBlank(instanceId) || StrUtil.isBlank(procedureName)) {
            throw new BmosException(MesResponseCode.PROCEDURE_CONDITION_ERROR);
        }
        ProcessConfirm processConfirm = processConfirmService.queryProcessConfirmByInstanceId(instanceId);
        if (ObjectUtil.isNotEmpty(processConfirm)) {
            procedureConfirmService.saveProcedureConfirm(ProcessConfirmConverter.INSTANCE.convertToProcedureDto(processConfirm, procedureName));
        }
    }
}

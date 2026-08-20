package com.bmos.mes.service.workflow.change.execute;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.audit.FlowAuditCodeEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.audit.complete.CountersignComplete;
import com.bmos.mes.service.audit.complete.OrViseComplete;
import com.bmos.mes.service.audit.complete.TaskComplete;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author renjinguang
 */
@Component
public class ChangeTeamFactory {

    private final Map<String, ChangeTeamService> changeTeamCache = new ConcurrentHashMap<>();

    public ChangeTeamService getChangeTeam(String nodeFunction) {
        if (StrUtil.isEmpty(nodeFunction) || !ProcedureStepNodeFunctionEnum.changeTeamFlag(nodeFunction)) {
            throw new BmosException(MesResponseCode.STEP_MODEL_NOT_TEAM);
        }
        return getChangeTeamImpl(nodeFunction);
    }

    protected ChangeTeamService getChangeTeamImpl(String nodeFunction) {
        if (ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue().equals(nodeFunction)) {
            return changeTeamCache.computeIfAbsent(nodeFunction, key -> new ProcedureChangeTeamServiceImpl());
        }
        if (ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue().equals(nodeFunction)) {
            return changeTeamCache.computeIfAbsent(nodeFunction, key -> new ProcessChangeTeamServiceImpl());
        }
        return null;
    }
}

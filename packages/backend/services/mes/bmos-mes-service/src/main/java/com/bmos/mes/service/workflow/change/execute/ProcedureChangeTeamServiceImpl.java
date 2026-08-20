package com.bmos.mes.service.workflow.change.execute;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceService;
import com.bmos.mes.service.workflow.dto.ProcedureRestartDTO;

/**
 * @ClassName ProcessChangeTeam
 * @Description 工艺换班
 * @Author Ren Jin Guang
 * @Date 2024/8/16 10:29
 */
public class ProcedureChangeTeamServiceImpl implements ChangeTeamService {


    @Override
    public void changeTeam(ChangeTeamContext changeTeam) {
        ProcedureTaskInstanceService procedureTaskInstanceService = SpringUtil.getBean(ProcedureTaskInstanceService.class);
        //任务进行换班操作
        ProcedureRestartDTO dto = new ProcedureRestartDTO();
        dto.setIsChangeTeam(true);
        dto.setPlanId(changeTeam.getPlan().getId());
        dto.setProcedureModelId(changeTeam.getProcedureModelId());
        dto.setProcedureChangeNumber(changeTeam.getProcedureChangeNumber()-1);
        dto.setProcessChangeNumber(changeTeam.getProcessChangeNumber());
        procedureTaskInstanceService.restart(dto);
    }
}

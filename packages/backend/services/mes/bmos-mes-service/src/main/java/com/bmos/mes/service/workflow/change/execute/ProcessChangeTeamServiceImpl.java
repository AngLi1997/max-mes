package com.bmos.mes.service.workflow.change.execute;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceService;

/**
 * @ClassName ProcessChangeTeam
 * @Description 工艺换班
 * @Author Ren Jin Guang
 * @Date 2024/8/16 10:29
 */
public class ProcessChangeTeamServiceImpl implements ChangeTeamService {

    @Override
    public void changeTeam(ChangeTeamContext changeTeam) {
        ProcedureTaskInstanceService taskInstanceService = SpringUtil.getBean(ProcedureTaskInstanceService.class);
        //工艺换班处理任务
        taskInstanceService.changeTeamProcess(changeTeam.getPlan(),changeTeam.getProcessChangeNumber());
    }
}

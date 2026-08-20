package com.bmos.mes.service.config.user;

import com.bmos.mes.service.plan.team.service.InstructionTeamService;
import com.bmos.mes.service.plan.team.service.ProductPlanTeamService;
import com.bmos.mes.service.utils.ChangeTeamUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ChangeTeamUtilConfiguration implements CommandLineRunner {

    @Resource
    private ProductPlanTeamService productPlanTeamService;

    @Resource
    private InstructionTeamService instructionTeamService;

    @Override
    public void run(String... args) {
        ChangeTeamUtils.init(productPlanTeamService,instructionTeamService);
    }
}

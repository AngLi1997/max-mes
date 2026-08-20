package com.bmos.mes.service.config.user;

import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.utils.QueryProcessConfigSortUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class QueryProcessConfiguration implements CommandLineRunner {

    @Autowired
    private ProcedureModelService procedureModelService;

    @Autowired
    private ProcedureStepModelService stepModelService;

    @Override
    public void run(String... args) {
        QueryProcessConfigSortUtils.init(procedureModelService, stepModelService);
    }
}

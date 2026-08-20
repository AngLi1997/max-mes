package com.bmos.mes.service.platform.plan.impl;

import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.plan.PlatformCodeRuleClient;
import com.bmos.mes.service.platform.plan.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.plan.dto.ConfirmNextUseCodeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlatformCodeRuleClientImpl {
    @Autowired
    private PlatformCodeRuleClient platformCodeRuleClient;

    public void confirmNo(ConfirmNextUseCodeDTO dto) {
        FeignUtils.handleRequest(data -> platformCodeRuleClient.confirmNo(data), dto);
    }

    public void batchConfirmNo(BatchConfirmNextUseCodeDTO dto) {
        FeignUtils.handleRequest(data -> platformCodeRuleClient.batchConfirmNo(data), dto);
    }
}

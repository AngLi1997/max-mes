package com.bmos.mes.service.plan.document.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.enums.plan.PlanArchiveStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.service.plan.document.service.IPlanArchiveService;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.mq.message.PlanStatusChangeMessage;
import com.bmos.mes.service.plan.info.mq.topic.PlanStatusChangeTopic;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.info.vo.PlanPageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yigaohui
 * @date 2024/6/7
 **/
@RestController
@RequestMapping("/plan/archive")
@Api(tags = "计划归档接口")
public class PlanArchiveController {

    @Autowired
    private IPlanArchiveService archiveService;

    @Autowired
    private PlanStatusChangeTopic planStatusChangeTopic;

    @Autowired
    private PlanService planService;

    @ApiOperation("归档")
    @PostMapping("{planId}")
    public ResponseInfo<Void> page(@PathVariable("planId") Long planId) {
        Plan plan = planService.getById(planId);
        planStatusChangeTopic.product(PlanStatusChangeMessage.builder()
                .currentPlanStatus(plan.getStart()).plan(plan).build());
//        archiveService.archive(planId);
        return ResponseInfo.success();
    }
}

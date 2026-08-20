package com.bmos.mes.service.weigh.centre2.requirement.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.weigh.centre2.requirement.dto.RequirementQueryDTO;
import com.bmos.mes.service.weigh.centre2.requirement.service.ITicketRequirementService;
import com.bmos.mes.service.weigh.centre2.requirement.vo.WeighRequirementListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工单称量（联环）
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 18:53
 */
@RestController
@RequestMapping("/weigh/ticket/requirement")
@Api(tags = "称量工单需求管理")
@Validated
public class TicketWeighRequirementController {

    @Resource
    private ITicketRequirementService requirementService;

    @PostMapping("/list")
    @ApiOperation("查询需求列表")
    public ResponseInfo<List<WeighRequirementListVO>> list(@RequestBody RequirementQueryDTO queryDTO) {
        List<WeighRequirementListVO> list = requirementService.list(queryDTO);
        return ResponseInfo.success(list);
    }
}

package com.bmos.mes.service.weigh.centre.requirement.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.weigh.centre.requirement.dto.WeighRequirementPageQuery;
import com.bmos.mes.service.weigh.centre.requirement.service.IWeighRequirementService;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 称量中心称量需求接口
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:49
 */
@RestController
@RequestMapping("/weigh/centre/requirement")
@Api(tags = "称量中心称量需求接口")
public class WeighRequirementController {

    @Resource
    private IWeighRequirementService weighRequirementService;

    @GetMapping("/queryPage")
    @ApiOperation("查询未规划的称量需求分页")
    public ResponseInfo<CommonPage<WeighRequirementVO>> queryPage(@Validated WeighRequirementPageQuery pageQuery) {
        return ResponseInfo.success(weighRequirementService.queryPage(pageQuery));
    }
}

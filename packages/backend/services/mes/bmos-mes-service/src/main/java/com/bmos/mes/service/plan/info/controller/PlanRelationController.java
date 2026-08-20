package com.bmos.mes.service.plan.info.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.plan.info.service.ProductPlanRelationService;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelatedProcessVO;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelationListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/plan/relation")
@Api(tags = "生产计划关联工艺")
public class PlanRelationController {
    @Autowired
    private ProductPlanRelationService planRelationService;

    @ApiOperation("关联工艺列表")
    @GetMapping("/detail/{planId}")
    public ResponseInfo<List<ProductPlanRelationListVO>> detail(@ApiParam(name = "生产计划id", value = "planId") @PathVariable Long planId) {
        return ResponseInfo.success(planRelationService.detail(planId));
    }

    @ApiOperation("关联工艺列表")
    @GetMapping("/detailWithSelf/{planId}")
    public ResponseInfo<List<ProductPlanRelationListVO>> detailWithSelf(@ApiParam(name = "生产计划id", value = "planId") @PathVariable Long planId) {
        return ResponseInfo.success(planRelationService.detailWithSelf(planId));
    }

    @GetMapping("/list")
    @ApiOperation("关联生产工艺及批次列表")
    public ResponseInfo<List<ProductPlanRelatedProcessVO>> queryProductPlanRelationList(@ApiParam(name = "planId", value = "生产计划id") @NotNull Long planId) {
        return ResponseInfo.success(planRelationService.queryProductPlanRelationList(planId));
    }
}

package com.bmos.mes.service.plan.template.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.plan.template.dto.PlanTemplateChangeStateDTO;
import com.bmos.mes.service.plan.template.dto.PlanTemplateEditDTO;
import com.bmos.mes.service.plan.template.dto.PlanTemplatePageQueryDTO;
import com.bmos.mes.service.plan.template.dto.PlanTemplateSaveDTO;
import com.bmos.mes.service.plan.template.service.PlanTemplateService;
import com.bmos.mes.service.plan.template.vo.PlanTemplateDetailVO;
import com.bmos.mes.service.plan.template.vo.PlanTemplateListVO;
import com.bmos.mes.service.plan.template.vo.PlanTemplatePageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/plan/template")
@Api(tags = "生产计划模板")
@Validated
public class PlanTemplateController {

    @Resource
    private PlanTemplateService planTemplateService;

    @PostMapping("/save")
    @ApiOperation("新增模板")
    @OperationLog
    public ResponseInfo<Void> savePlanTemplate(@Validated @RequestBody PlanTemplateSaveDTO dto) {
        planTemplateService.savePlanTemplate(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/edit")
    @ApiOperation("编辑")
    @OperationLog
    public ResponseInfo<Void> editPlanTemplate(@Validated @RequestBody PlanTemplateEditDTO dto) {
        planTemplateService.editPlanTemplate(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("模板分页")
    public ResponseInfo<CommonPage<PlanTemplatePageVO>> queryPlanTemplatePage(@Validated PlanTemplatePageQueryDTO dto) {
        return ResponseInfo.success(planTemplateService.queryPlanTemplatePage(dto));
    }

    @PostMapping("/changeState")
    @ApiOperation("启停")
    @OperationLog
    public ResponseInfo<Void> changePlanTemplateState(@RequestBody @Validated PlanTemplateChangeStateDTO dto) {
        planTemplateService.changePlanTemplateState(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除")
    @OperationLog
    public ResponseInfo<Void> deletePlanTemplate(@RequestParam @NotNull Long id) {
        planTemplateService.deletePlanTemplate(id);
        return ResponseInfo.success();
    }

    @GetMapping("/detail")
    @ApiOperation("详情")
    public ResponseInfo<PlanTemplateDetailVO> getPlanTemplateDetail(@NotNull Long id) {
        return ResponseInfo.success(planTemplateService.getPlanTemplateDetail(id));
    }


    @GetMapping("/list")
    @ApiOperation("获取启用模板列表")
    public ResponseInfo<List<PlanTemplateListVO>> getEnablePlanTemplateList() {
        return ResponseInfo.success(planTemplateService.getEnablePlanTemplateList());
    }
    

}

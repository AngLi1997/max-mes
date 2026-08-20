package com.bmos.mes.service.plan.team.controller;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.facotry.controller.vo.FactoryLineInfoVO;
import com.bmos.mes.service.plan.team.dto.*;
import com.bmos.mes.service.plan.team.service.ProductPlanTeamService;
import com.bmos.mes.service.plan.team.vo.ProductPlanPageTeamVO;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamDetailVO;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamListVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidatorContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan/team")
@Api(tags = "生产计划班组")
public class ProductPlanTeamController {
    @Autowired
    private ProductPlanTeamService productPlanTeamService;

    @ApiOperation("分页列表")
    @GetMapping("/page")
    public ResponseInfo<CommonPage<ProductPlanPageTeamVO>> page(ProductPlanTeamPageDTO dto) {
        return ResponseInfo.success(
            CommonPage.convertPage(productPlanTeamService.page(dto))
        );
    }

    @ApiOperation("详情")
    @GetMapping("/detail/{id}")
    public ResponseInfo<ProductPlanTeamDetailVO> detail(@PathVariable Long id) {
        return ResponseInfo.success(productPlanTeamService.detail(id));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public ResponseInfo<List<ProductPlanPageTeamVO>> list(ProductPlanTeamListDTO dto) {
        return ResponseInfo.success(productPlanTeamService.list(dto));
    }

    @PostMapping("/save")
    @ApiOperation("指令单保存-班组信息保存")
    @OperationLog
    public ResponseInfo<Void> save(@RequestBody @Validated ProductPlanTeamSaveDTO dto) {
        productPlanTeamService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("指令单保存-班组信息更新")
    @OperationLog
    public ResponseInfo<Void> update(@RequestBody @Validated ProductPlanTeamUpdateDTO dto) {
        productPlanTeamService.update(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/enable/{id}")
    @ApiOperation("启用")
    @OperationLog
    public ResponseInfo<Void> enable(@PathVariable Long id) {
        productPlanTeamService.enable(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable/{id}")
    @ApiOperation("停用")
    @OperationLog
    public ResponseInfo<Void> disable(@PathVariable Long id) {
        productPlanTeamService.disable(id);
        return ResponseInfo.success();
    }

    @GetMapping("listByProcessVersionId")
    @ApiOperation("根据工艺版本id获取列表")
    public ResponseInfo<List<ProductPlanTeamListVO>> getTeamListByProcessVersionId(@NotNull Long processVersionId) {
        return ResponseInfo.success(productPlanTeamService.getTeamListByProcessVersionId(processVersionId));
    }

    @GetMapping("/procedure/step/listByProcessVersionId")
    @ApiOperation("根据工艺版本id获取列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "工艺版本id",name = "processVersionId",required = true),
            @ApiImplicitParam(value = "工序模型id",name = "procedureModelId"),

    })
    public ResponseInfo<List<ProductPlanTeamListVO>> getStepTeamListByProcessVersionId(@Validated @NotNull Long processVersionId,Long procedureModelId) {
        return ResponseInfo.success(productPlanTeamService.getStepTeamListByProcessVersionId(processVersionId,procedureModelId));
    }

    @GetMapping("/listByProductionLineIds")
    @ApiOperation("根据产线id列表获取列表")
    public ResponseInfo<List<ProductPlanTeamListVO>> getTeamListByProductionLineIds(@NotBlank String lineIds) {
        List<String> split = StrUtil.split(lineIds, StrUtil.COMMA);
        return ResponseInfo.success(productPlanTeamService.getTeamListByProductionLineIds(split.stream()
                .map(Long::valueOf).collect(Collectors.toList())));
    }

    @GetMapping("/list/process/team")
    @ApiOperation("根据产线id查询班组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "产线id集合,使用逗号分割", name = "lineIds", required = true),
            @ApiImplicitParam(value = "工艺版本id", name = "processVersionId"),
    })
    public ResponseInfo<List<ProductPlanTeamListVO>> getProcessTeamListByProductionLineIds(@NotBlank String lineIds,Long processVersionId) {
        List<String> split = StrUtil.split(lineIds, StrUtil.COMMA);
        return ResponseInfo.success(productPlanTeamService.getProcessTeamListByProductionLineIds(split.stream()
                .map(Long::valueOf).collect(Collectors.toList()),processVersionId));
    }

    @GetMapping("/listByProductPlanId")
    @ApiOperation("根据生产计划id获取列表")
    public ResponseInfo<List<ProductPlanTeamListVO>> getTeamListByProductPlanId(@NotNull Long productPlanId) {
        return ResponseInfo.success(productPlanTeamService.getTeamListByProductPlanId(productPlanId));
    }

    @PostMapping("/boundProductionLine")
    @ApiOperation("绑定产线")
    public ResponseInfo<Void> boundProductionLines(@RequestBody @Validated TeamBoundProductionLineDTO dto){
        productPlanTeamService.boundProductionLines(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/listLinesByTeamId")
    @ApiOperation("根据班组id获取产线列表")
    public ResponseInfo<List<FactoryLineInfoVO>> listLinesByTeamId(@NotNull Long teamId) {
        return ResponseInfo.success(productPlanTeamService.listLinesByTeamId(teamId));
    }
}

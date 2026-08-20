package com.bmos.mes.service.inspect.controller;

import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.inspect.controller.vo.InspectDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectPageVO;
import com.bmos.mes.service.inspect.controller.vo.InspectResultVO;
import com.bmos.mes.service.inspect.service.InspectService;
import com.bmos.mes.service.inspect.service.dto.InitiateInspectDTO;
import com.bmos.mes.service.inspect.service.dto.InitiateRetryInspectDTO;
import com.bmos.mes.service.inspect.service.dto.InspectPageDTO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 发起请验
 */
@RestController
@RequestMapping("/inspect")
@Api(tags = "请验接口")
@Validated
public class InspectController {

    @Autowired
    private InspectService inspectService;

    @PostMapping("/initiate")
    @ApiOperation("发起请验")
    @OperationLog
    @DistributedLock(key = "#dto.materialBatchNo + '-' + #dto.formulaMaterialId")
    public ResponseInfo<Void> initiateInspect(@RequestBody InitiateInspectDTO dto) {
        inspectService.initiateInspect(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询请验单")
    public ResponseInfo<CommonPage<InspectPageVO>> queryPage(InspectPageDTO dto) {
        return ResponseInfo.success(inspectService.queryPage(dto));
    }

    @GetMapping("/info")
    @ApiOperation("获取请验单详情")
    public ResponseInfo<InspectDetailVO> queryDetail(@ApiParam("请验单详情id") @NotNull Long id) {
        return ResponseInfo.success(inspectService.queryDetail(id));
    }

    @GetMapping("/program/result")
    @ApiOperation("获取请验结果")
    public ResponseInfo<InspectResultVO> queryInspectResult(@ApiParam("请验单详情id") @NotNull Long id) {
        return ResponseInfo.success(inspectService.queryInspectResult(id));
    }

    @PostMapping("/retry/initiate")
    @ApiOperation("重新发起请验")
    @OperationLog
    @DistributedLock(key = "#dto.materialBatchNo + '-' + #dto.formulaMaterialId")
    public ResponseInfo<Void> retryInitiateInspect(@RequestBody InitiateRetryInspectDTO dto) {
        inspectService.retryInitiateInspect(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/query/materialBatchNo/{materialBatchNo}/materialId/{materialId}")
    @ApiOperation("根据物料批号和物料id查询请验结果")
    public ResponseInfo<InspectResultVO> queryInspectResultByMaterialBatchNoAndMaterialId(@ApiParam("物料批号") @NotNull @PathVariable("materialBatchNo") String materialBatchNo, @ApiParam("物料id") @NotNull  @PathVariable("materialId") Long materialId) {
        return ResponseInfo.success(inspectService.queryInspectResultByMaterialBatchNoAndMaterialId(materialBatchNo, materialId));
    }

    @GetMapping("/schemes/{formulaMaterialId}")
    @ApiOperation("根据配方物料id查询检验方案（自研LIMS）")
    public ResponseInfo<java.util.List<com.bmos.mes.service.inspect.controller.vo.InspectSchemeVO>> querySchemes(
            @ApiParam("配方物料id") @NotNull @PathVariable("formulaMaterialId") Long formulaMaterialId) {
        return ResponseInfo.success(inspectService.querySchemes(formulaMaterialId));
    }


}

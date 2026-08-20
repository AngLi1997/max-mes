package com.bmos.wms.service.inspect.controller;

import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.wms.service.inspect.controller.vo.InspectDetailVO;
import com.bmos.wms.service.inspect.controller.vo.InspectPageVO;
import com.bmos.wms.service.inspect.controller.vo.InspectSchemeVO;
import com.bmos.wms.service.inspect.service.IInspectService;
import com.bmos.wms.service.inspect.service.dto.InitiateInspectDTO;
import com.bmos.wms.service.inspect.service.dto.InitiateRetryInspectDTO;
import com.bmos.wms.service.inspect.service.dto.InspectPageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * WMS 检验对接控制器。
 *
 * <p>分布式锁 key：{@code batchNo + '-' + cargoId}（与 MES 同结构，去掉生产维度）。
 */
@RestController
@RequestMapping("/inspect")
@Api(tags = "WMS 检验对接接口")
@Validated
public class InspectController {

    @Resource
    private IInspectService inspectService;

    @PostMapping("/initiate")
    @ApiOperation("发起请验")
    @OperationLog
    @DistributedLock(key = "#dto.inventoryBatchId")
    public ResponseInfo<String> initiateInspect(@Validated @RequestBody InitiateInspectDTO dto) {
        return ResponseInfo.success(inspectService.initiateInspect(dto));
    }

    @PostMapping("/retry/initiate")
    @ApiOperation("重新发起请验")
    @OperationLog
    @DistributedLock(key = "#dto.id")
    public ResponseInfo<String> retryInitiateInspect(@Validated @RequestBody InitiateRetryInspectDTO dto) {
        return ResponseInfo.success(inspectService.retryInitiateInspect(dto));
    }

    @GetMapping("/page")
    @ApiOperation("分页查询检验单")
    public ResponseInfo<CommonPage<InspectPageVO>> queryPage(@Validated InspectPageDTO dto) {
        return ResponseInfo.success(inspectService.queryPage(dto));
    }

    @GetMapping("/info")
    @ApiOperation("查询检验单详情（含字段值 + 检验项结果）")
    public ResponseInfo<InspectDetailVO> queryDetail(@ApiParam("检验单id") @NotNull Long id) {
        return ResponseInfo.success(inspectService.queryDetail(id));
    }

    @GetMapping("/history")
    @ApiOperation("按库存批次查询历史检验单")
    public ResponseInfo<List<InspectPageVO>> queryHistory(
            @ApiParam("库存批次id") @NotNull Long inventoryBatchId) {
        return ResponseInfo.success(inspectService.queryHistory(inventoryBatchId));
    }

    @GetMapping("/configs/{inventoryBatchId}")
    @ApiOperation("查询批次对应货品的所有请验单配置（一物料可对应多个，前端选择）")
    public ResponseInfo<List<InspectConfigDetailVO>> queryConfigByBatchId(
            @ApiParam("库存批次id") @NotNull @PathVariable("inventoryBatchId") Long inventoryBatchId) {
        return ResponseInfo.success(inspectService.queryConfigByBatchId(inventoryBatchId));
    }

    @GetMapping("/schemes/{inventoryBatchId}")
    @ApiOperation("查询批次对应货品的检验方案（前端选择）")
    public ResponseInfo<List<InspectSchemeVO>> querySchemesByBatchId(
            @ApiParam("库存批次id") @NotNull @PathVariable("inventoryBatchId") Long inventoryBatchId) {
        return ResponseInfo.success(inspectService.querySchemesByBatchId(inventoryBatchId));
    }
}

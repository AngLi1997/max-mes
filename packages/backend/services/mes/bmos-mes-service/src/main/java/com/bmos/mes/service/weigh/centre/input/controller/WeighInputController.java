package com.bmos.mes.service.weigh.centre.input.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.weigh.centre.input.dto.WeighInputDTO;
import com.bmos.mes.service.weigh.centre.input.service.IWeighInputService;
import com.bmos.mes.service.weigh.centre.input.vo.WeighInputRecordResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 称量中心物料投入接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/16 14:08
 */
@RestController
@RequestMapping("/weigh/centre/input")
@Api(tags = "称量中心物料投入接口")
public class WeighInputController {

    @Resource
    private IWeighInputService weighInputService;

    @GetMapping("/getInputList")
    @ApiImplicitParam(name = "componentInstanceId", value = "组件实例ID", required = true)
    @ApiOperation(value = "根据组件实例id获取物料投入列表")
    public ResponseInfo<WeighInputRecordResultVO> getInputList(@RequestParam Long componentInstanceId) {
        return ResponseInfo.success(weighInputService.getInputList(componentInstanceId));
    }

    @PostMapping("/input")
    @ApiOperation("投料")
    @OperationLog
    public ResponseInfo<Void> input(@RequestBody @Validated WeighInputDTO dto) {
        weighInputService.input(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/finishInput")
    @ApiOperation("完成投料")
    @OperationLog
    @ApiImplicitParam(name = "componentInstanceId", value = "组件实例ID", required = true)
    public ResponseInfo<Void> finishInput(@RequestParam Long componentInstanceId) {
        weighInputService.finishInput(componentInstanceId);
        return ResponseInfo.success();
    }
}

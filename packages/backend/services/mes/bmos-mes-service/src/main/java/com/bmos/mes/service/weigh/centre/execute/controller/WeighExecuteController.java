package com.bmos.mes.service.weigh.centre.execute.controller;

import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.weigh.centre.execute.dto.*;
import com.bmos.mes.service.weigh.centre.execute.service.IWeighExecuteService;
import com.bmos.mes.service.weigh.centre.execute.vo.*;
import com.bmos.mes.service.weigh.centre.task.service.IWeighTaskService;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 称量执行接口
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 09:54
 */
@RestController
@RequestMapping("/weigh/centre/execute")
@Api(tags = "称量中心称量执行接口")
public class WeighExecuteController {

    @Resource
    private IWeighTaskService weighTaskService;

    @Resource
    private IWeighExecuteService weighExecuteService;

    @GetMapping("/queryExecuteTaskPage")
    @ApiOperation("查询待执行称量任务分页")
    public ResponseInfo<CommonPage<WeighExecuteTaskPageVO>> queryExecuteTaskPage(@Validated WeighExecuteTaskPageQuery pageQuery) {
        return ResponseInfo.success(weighTaskService.queryExecuteTaskPage(pageQuery));
    }

    @GetMapping("/queryHistoryTaskPage")
    @ApiOperation("查询历史称量任务分页")
    public ResponseInfo<CommonPage<WeighExecuteTaskPageVO>> queryHistoryTaskPage(@Validated WeighExecuteTaskPageQuery pageQuery) {
        return ResponseInfo.success(weighTaskService.queryHistoryTaskPage(pageQuery));
    }

    @GetMapping("/queryTaskById")
    @ApiOperation("根据任务id查询任务详情")
    @ApiImplicitParam(name = "taskId", value = "任务id", required = true, example = "1")
    public ResponseInfo<WeighExecuteTaskDetailVO> queryTaskById(@RequestParam Long taskId) {
        return ResponseInfo.success(weighExecuteService.queryTaskById(taskId));
    }

    @GetMapping("/queryRecordResultByTaskId")
    @ApiOperation("根据任务id查询称量结果列表")
    @ApiImplicitParam(name = "taskId", value = "任务id", required = true, example = "1")
    public ResponseInfo<WeighExecuteWeighRecordListVO> queryRecordResultByTaskId(@RequestParam Long taskId) {
        return ResponseInfo.success(weighExecuteService.queryRecordResultByTaskId(taskId));
    }

    @GetMapping("/queryRequirementById")
    @ApiOperation("根据需求id查询需求详情")
    @ApiImplicitParam(name = "requirementId", value = "需求id", required = true, example = "1")
    public ResponseInfo<WeighExecuteRequirementDetailVO> queryRequirementById(@RequestParam Long requirementId) {
        return ResponseInfo.success(weighExecuteService.queryRequirementById(requirementId));
    }

    @GetMapping("/queryPendingRequirementListByTaskId")
    @ApiOperation("根据任务id查询任务下未称量的需求列表")
    @ApiImplicitParam(name = "taskId", value = "任务id", required = true, example = "1")
    public ResponseInfo<List<WeighExecutePendingRequirementSimpleVO>> queryPendingRequirementListByTaskId(@RequestParam Long taskId) {
        return ResponseInfo.success(weighExecuteService.queryPendingRequirementListByTaskIds(taskId));
    }

    @PutMapping("/makeSureWeigh")
    @ApiOperation("确认称量")
    @OperationLog
    public ResponseInfo<Void> makeSureWeigh(@Validated @RequestBody WeighExecuteMakeSureWeighDTO makeSureWeighDTO) {
        weighExecuteService.makeSureWeigh(makeSureWeighDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/addConsumeStorageMaterial")
    @ApiOperation("添加称量消耗物料件")
    @OperationLog
    public ResponseInfo<Void> addConsumeStorageMaterial(@Validated @RequestBody WeighExecuteAddConsumeMaterialWeighDTO dto) {
        weighExecuteService.addConsumeStorageMaterial(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/weighAndPrint")
    @ApiOperation("称量打码")
    @OperationLog
    @DistributedLock(expression = "#weighAndPrintDTO.requirementId")
    public ResponseInfo<WeighExecuteWeighResult> weighAndPrint(@Validated @RequestBody WeighExecuteWeighAndPrintDTO weighAndPrintDTO) {
        return ResponseInfo.success(weighExecuteService.weighAndPrint(weighAndPrintDTO));
    }

    @PutMapping("/changeBatch")
    @ApiOperation("更换批次")
    @OperationLog
    public ResponseInfo<Long> changeBatch(@Validated @RequestBody WeighExecuteChangeBatchDTO dto) {
        weighExecuteService.changeBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/finish")
    @ApiOperation("完成称量")
    @OperationLog
    public ResponseInfo<Void> finish(@Validated @RequestBody WeighExecuteWeighFinishDTO weighFinishDTO) {
        weighExecuteService.finish(weighFinishDTO);
        return ResponseInfo.success();
    }

    @PutMapping("/changeWeigher")
    @ApiOperation("更换称量人员")
    @OperationLog
    public ResponseInfo<Long> changeWeigher(@Validated @RequestBody WeighExecuteChangeWeigherDTO dto) {
        weighExecuteService.changeWeigher(dto, true);
        return ResponseInfo.success();
    }

    @PostMapping("/sign")
    @ApiOperation("签名")
    @OperationLog
    public ResponseInfo<Void> sign(@RequestBody @Validated WeighExecuteWeighSignDTO signDTO) {
        weighExecuteService.sign(signDTO);
        return ResponseInfo.success();
    }
}

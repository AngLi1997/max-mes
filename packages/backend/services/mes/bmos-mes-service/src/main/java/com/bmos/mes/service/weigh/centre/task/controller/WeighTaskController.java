package com.bmos.mes.service.weigh.centre.task.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementVO;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskEditDTO;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskInfoListQuery;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskPageQuery;
import com.bmos.mes.service.weigh.centre.task.service.IWeighTaskService;
import com.bmos.mes.service.weigh.centre.task.vo.WeighTaskAndRequirementPageVO;
import com.bmos.mes.service.weigh.centre.task.vo.WeighTaskPageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 称量中心称量任务接口
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 15:15
 */
@RestController
@RequestMapping("/weigh/centre/task")
@Api(tags = "称量中心称量任务接口")
public class WeighTaskController {

    @Resource
    private IWeighTaskService weighTaskService;

    @GetMapping("/queryPage")
    @ApiOperation("查询称量任务分页")
    public ResponseInfo<CommonPage<WeighTaskPageVO>> queryPage(@Validated WeighTaskPageQuery pageQuery) {
        return ResponseInfo.success(weighTaskService.queryPage(pageQuery));
    }

    @PostMapping("/programAuto")
    @ApiOperation("自动规划")
    @OperationLog
    public ResponseInfo<CommonPage<Void>> programAuto() {
        weighTaskService.programAuto();
        return ResponseInfo.success();
    }

    @PostMapping("/programManual")
    @ApiOperation("任务规划")
    @OperationLog
    public ResponseInfo<CommonPage<Void>> programManual(@RequestBody @Validated @NotEmpty List<Long> requirementIds) {
        weighTaskService.programManual(requirementIds);
        return ResponseInfo.success();
    }

    @PostMapping("/queryRequirementListByTaskId")
    @ApiOperation("查询任务详情和称量需求分页")
    public ResponseInfo<WeighTaskAndRequirementPageVO> queryRequirementListByTaskId(@Validated WeighTaskInfoListQuery query) {
        return ResponseInfo.success(weighTaskService.queryRequirementListByTaskId(query));
    }

    @GetMapping("/queryUnPlanedRequirementListByTaskId")
    @ApiOperation("查询称量任务对应的物料、称量中心、单位详情相同的未规划的称量需求列表")
    @ApiImplicitParam(name = "taskId", value = "称量任务id", required = true)
    public ResponseInfo<List<WeighRequirementVO>> queryUnPlanedRequirementListByTaskId(@RequestParam Long taskId) {
        return ResponseInfo.success(weighTaskService.queryUnPlanedRequirementListByTaskId(taskId));
    }

    @PutMapping("/edit")
    @ApiOperation("保存编辑")
    @OperationLog
    public ResponseInfo<Void> edit(@RequestBody @Validated WeighTaskEditDTO editDTO) {
        weighTaskService.edit(editDTO);
        return ResponseInfo.success();
    }

    @PutMapping("/makeSure")
    @ApiOperation("确认任务")
    @OperationLog
    @ApiImplicitParam(name = "taskId", value = "称量任务id", required = true)
    public ResponseInfo<Void> makeSure(@RequestParam Long taskId) {
        weighTaskService.makeSure(taskId);
        return ResponseInfo.success();
    }

    @PutMapping("/send")
    @ApiOperation("下发任务")
    @OperationLog
    @ApiImplicitParam(name = "taskId", value = "称量任务id", required = true)
    public ResponseInfo<Void> send(@RequestParam Long taskId) {
        weighTaskService.send(taskId);
        return ResponseInfo.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("取消任务")
    @OperationLog
    @ApiImplicitParam(name = "taskId", value = "称量任务id", required = true)
    public ResponseInfo<Void> cancel(@RequestParam Long taskId) {
        weighTaskService.cancel(taskId);
        return ResponseInfo.success();
    }
}

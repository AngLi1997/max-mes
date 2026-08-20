package com.bmos.lims2.web.inspect.retention;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.retention.dto.BatchRetentionObservationSubmitDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationSubmitDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationTaskListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationTaskPageQueryDTO;
import com.bmos.lims2.server.inspect.retention.service.RetentionObservationService;
import com.bmos.lims2.web.inspect.retention.vo.req.BatchRetentionObservationSubmitReqVO;
import com.bmos.lims2.web.inspect.retention.vo.req.RetentionObservationSubmitReqVO;
import com.bmos.lims2.web.inspect.retention.vo.req.RetentionObservationTaskPageReqVO;
import com.bmos.lims2.web.inspect.retention.vo.resp.RetentionObservationTaskListRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 留样观察控制器
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@RestController
@RequestMapping("/retention-observation")
@Api(tags = "留样观察-接口")
@Validated
@Slf4j
public class RetentionObservationController {

    @Autowired
    private RetentionObservationService retentionObservationService;

    @ApiOperation("分页查询留样观察任务列表")
    @PostMapping("/task/page")
    public ResponseInfo<CommonPage<RetentionObservationTaskListRespVO>> getObservationTaskPageList(
            @RequestBody @Valid RetentionObservationTaskPageReqVO reqVO) {

        // 转换参数
        RetentionObservationTaskPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, RetentionObservationTaskPageQueryDTO.class);

        // 查询列表
        CommonPage<RetentionObservationTaskListDTO> pageData =
            retentionObservationService.getObservationTaskPageList(queryDTO);

        // 转换结果
        List<RetentionObservationTaskListRespVO> respList = BeanUtil.copyToList(
            pageData.getList(), RetentionObservationTaskListRespVO.class);

        CommonPage<RetentionObservationTaskListRespVO> result = new CommonPage<>();
        result.setTotal(pageData.getTotal());
        result.setPageNum(pageData.getPageNum());
        result.setPageSize(pageData.getPageSize());
        result.setList(respList);

        return ResponseInfo.success(result);
    }

    @ApiOperation("提交留样观察结果（单个）")
    @PostMapping("/task/{taskId}/submit")
    public ResponseInfo<Void> submitObservation(
            @PathVariable @NotNull(message = "任务ID不能为空") Long taskId,
            @RequestBody @Valid RetentionObservationSubmitReqVO reqVO) {

        RetentionObservationSubmitDTO submitDTO = new RetentionObservationSubmitDTO();
        submitDTO.setTaskId(taskId);
        submitDTO.setObservationResult(reqVO.getObservationResult());
        submitDTO.setObservationRemark(reqVO.getObservationRemark());

        retentionObservationService.submitObservation(submitDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("批量提交留样观察结果")
    @PostMapping("/task/batch-submit")
    public ResponseInfo<Void> batchSubmitObservation(
            @RequestBody @Valid BatchRetentionObservationSubmitReqVO reqVO) {

        // 转换参数
        BatchRetentionObservationSubmitDTO batchSubmitDTO = new BatchRetentionObservationSubmitDTO();

        List<BatchRetentionObservationSubmitDTO.ObservationTaskItem> tasks = reqVO.getTasks().stream()
            .map(item -> {
                BatchRetentionObservationSubmitDTO.ObservationTaskItem taskItem =
                    new BatchRetentionObservationSubmitDTO.ObservationTaskItem();
                taskItem.setTaskId(item.getTaskId());
                taskItem.setObservationResult(item.getObservationResult());
                taskItem.setObservationRemark(item.getObservationRemark());
                return taskItem;
            })
            .collect(Collectors.toList());

        batchSubmitDTO.setTasks(tasks);

        retentionObservationService.batchSubmitObservation(batchSubmitDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("查询临期任务数量")
    @GetMapping("/task/upcoming/count")
    public ResponseInfo<Long> countUpcomingTasks(
            @ApiParam(value = "天数（默认7天）", example = "7")
            @RequestParam(required = false, defaultValue = "7") Integer days) {

        Long count = retentionObservationService.countUpcomingTasks(days);
        return ResponseInfo.success(count);
    }
}

package com.bmos.lims2.web.eln.record.controller;


import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.eln.record.dto.ExpressionBindRecordDTO;
import com.bmos.lims2.server.eln.record.service.BatchRecordService;
import com.bmos.lims2.server.eln.record.vo.BatchRecordTreeNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/record/expression")
@Api(tags = "记录-公式接口")
@Validated
@Slf4j
public class BatchRecordExpressionController {

    @Resource
    private BatchRecordService batchRecordService;

    @PostMapping("/expressionBindRecord")
    @ApiOperation("公式绑定记录")
    public ResponseInfo<Void> expressionBindBatchRecord(@Validated @RequestBody ExpressionBindRecordDTO dto) {
        batchRecordService.expressionBindBatchRecord(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/expressionBindTree")
    @ApiModelProperty("获取记录树")
    public ResponseInfo<List<BatchRecordTreeNodeVO>> getExpressionBindTree(@ApiParam(required = true, name = "expressionId", value = "公式id") @NotNull Long expressionId) {
        return ResponseInfo.success(batchRecordService.getRecordTreeByExpressionId(expressionId));
    }

    @GetMapping("/boundRecordIdList")
    @ApiOperation("根据公式id获取绑定的记录id列表")
    public ResponseInfo<List<Long>> getBoundRecordIdList(@NotNull Long expressionId) {
        return ResponseInfo.success(batchRecordService.getBoundRecordIdList(expressionId));
    }

}

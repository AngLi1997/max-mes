package com.bmos.mes.service.output.finished.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.output.finished.dto.SaveFinishedProductOutputDTO;
import com.bmos.mes.service.output.finished.dto.ValidateFinishedProductComponentDTO;
import com.bmos.mes.service.output.finished.service.FinishedProductOutputService;
import com.bmos.mes.service.output.finished.vo.FinishedProductComponentDetailVO;
import com.bmos.mes.service.output.finished.vo.FinishedProductOutputListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Api(tags = "成品产出")
@RequestMapping("/output/finished")
public class FinishedProductOutPutController {


    @Resource
    private FinishedProductOutputService finishedProductOutputService;

    @GetMapping("/detail")
    @ApiOperation("组件详情")
    public ResponseInfo<FinishedProductComponentDetailVO> getComponentDetail(@Validated ValidateFinishedProductComponentDTO dto) {
        return ResponseInfo.success(finishedProductOutputService.getComponentDetail(dto));
    }

    @GetMapping("/list")
    @ApiOperation("获取组件产出列表")
    public ResponseInfo<List<FinishedProductOutputListVO>> getFinishedProductOutputList(@NotNull @ApiParam(name = "id"
            , value = "产出主键id") Long id) {
        return ResponseInfo.success(finishedProductOutputService.getFinishedProductOutputList(id));
    }

    @PostMapping("/save")
    @ApiOperation("产出成品")
    @OperationLog
    public ResponseInfo<Void> saveFinishedProductOutputList(@Validated @RequestBody SaveFinishedProductOutputDTO dto) {
        finishedProductOutputService.saveFinishedProductOutputList(dto);
        return ResponseInfo.success();
    }

}

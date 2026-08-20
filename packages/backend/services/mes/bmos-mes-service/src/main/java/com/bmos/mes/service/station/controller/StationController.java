package com.bmos.mes.service.station.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.station.service.IStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 16:59
 */
@RestController
@RequestMapping("/station")
@Api(tags = "工位信息查询")
public class StationController {

    @Resource
    private IStationService stationService;

    @ApiOperation(value = "根据组件业务字段查询组件的工位信息")
    @GetMapping("/getStationIdListByComponentProps")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procedureStepModelId", value = "工序步骤模板id"),
            @ApiImplicitParam(name = "componentId", value = "组件id"),
            @ApiImplicitParam(name = "planId", value = "生产计划id")
    })
    public ResponseInfo<List<Long>> getStationIdListByComponentProps(@RequestParam Long procedureStepModelId,
                                                                     @RequestParam Long componentId,
                                                                     @RequestParam Long planId) {
        return ResponseInfo.success(stationService.getStationIdsByProcedureStepModelIdAndComponentId(procedureStepModelId, componentId, planId));
    }

    @ApiOperation(value = "根据组件实例id查询组件的工位信息")
    @GetMapping("/getStationIdListByComponentInstanceId")
    @ApiImplicitParam(name = "componentInstanceId", value = "组件实例id")
    public ResponseInfo<List<Long>> getStationIdListByComponentInstanceId(@RequestParam Long componentInstanceId) {
        return ResponseInfo.success(stationService.getStationIdListByComponentInstanceId(componentInstanceId));
    }
}

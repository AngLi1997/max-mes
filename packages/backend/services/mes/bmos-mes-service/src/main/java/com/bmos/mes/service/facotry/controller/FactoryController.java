package com.bmos.mes.service.facotry.controller;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.facotry.controller.vo.FactoryLineInfoVO;
import com.bmos.mes.service.facotry.controller.vo.FactoryLineModuleTreeVO;
import com.bmos.mes.service.facotry.controller.vo.FactoryRoomInfoVO;
import com.bmos.mes.service.facotry.controller.vo.FactoryRoomVO;
import com.bmos.mes.service.facotry.service.FactoryService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/factory")
@Api(tags = "房间Web端相关接口")
public class FactoryController {

    @Autowired
    private FactoryService factoryService;

    @GetMapping("/getRoomCleanInfo/{roomId}")
    @ApiOperation("根据房间id获取房间基础信息")
    public ResponseInfo<FactoryRoomInfoVO> getRoomInfo(@PathVariable("roomId") Long roomId) {
        return ResponseInfo.success(factoryService.getRoomInfo(roomId));
    }

    @GetMapping("/line/list")
    @ApiOperation("获取产线列表")
    public ResponseInfo<List<FactoryLineModuleTreeVO>> getFactoryLine(){
        return ResponseInfo.success(factoryService.getFactoryLine());
    }

    @GetMapping("/process/line/list")
    @ApiOperation("获取工艺产线列表")
    public ResponseInfo<List<FactoryLineModuleTreeVO>> getFactoryProcessLine(Long processVersionId){
        return ResponseInfo.success(factoryService.getFactoryProcessLine(processVersionId));
    }

    @GetMapping("/line/room")
    @ApiOperation("根据产线id列表获取房间树")
    public ResponseInfo<List<FactoryRoomVO>> getLineRoom(@NotBlank String lineIds) {
        List<String> split = StrUtil.split(lineIds, StrUtil.COMMA);
        return ResponseInfo.success(factoryService.getLineRoom(split.stream().map(Long::valueOf).collect(Collectors.toList())));
    }

    @GetMapping("/process/line/room")
    @ApiOperation("根据产线id列表获取工艺配置房间树")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "产线id集合,使用逗号分割", name = "lineIds", required = true),
            @ApiImplicitParam(value = "工艺模型id", name = "procedureModelId"),
    })
    public ResponseInfo<List<FactoryRoomVO>> getProcessLineRoom(@NotBlank String lineIds,Long procedureModelId) {
        List<String> split = StrUtil.split(lineIds, StrUtil.COMMA);
        return ResponseInfo.success(factoryService.getProcessLineRoom(split.stream().map(Long::valueOf).collect(Collectors.toList()),procedureModelId));
    }

    @GetMapping("/line/listByProcessVersionId")
    @ApiOperation("根据工艺版本id获取产线列表")
    public ResponseInfo<List<FactoryLineInfoVO>> getFactoryLineByProcessVersionId(@NotNull Long processVersionId){
        return ResponseInfo.success(factoryService.getFactoryLineByProcessVersionId(processVersionId));
    }

    @GetMapping("/line/listByProcessVersion")
    @ApiOperation("根据工艺版本获取产线列表")
    public ResponseInfo<List<FactoryLineInfoVO>> getFactoryLineByProcessVersion(@NotNull @ApiParam(value = "工艺id",
                                                                                name = "id", required = true) Long id,
                                                                                @NotBlank @ApiParam(value = "工艺版本",
                                                                                        name = "version", required =
                                                                                        true) String version) {
        return ResponseInfo.success(factoryService.getFactoryLineByProcessVersion(id, version));
    }

}

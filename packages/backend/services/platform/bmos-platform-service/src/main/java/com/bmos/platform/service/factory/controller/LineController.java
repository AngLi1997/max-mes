package com.bmos.platform.service.factory.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.factory.controller.vo.LineInfoVO;
import com.bmos.platform.service.factory.controller.vo.LinePageVO;
import com.bmos.platform.service.factory.controller.vo.FactoryTreeNodeVO;
import com.bmos.platform.service.factory.service.LineService;
import com.bmos.platform.service.factory.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产线分类接口
 */
@RestController
@RequestMapping("/factory/line")
@Validated
@Api(tags = "产线相关接口")
public class LineController {
    
    @Autowired
    LineService lineService;
    
    @PostMapping("/save")
    @ApiOperation("新建产线")
    @OperationLog
    public ResponseInfo<Void> saveLine(@RequestBody LineSaveDTO dto) {
        lineService.saveLine(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑产线")
    @OperationLog
    public ResponseInfo<Void> updateLine(@RequestBody LineUpdateDTO dto) {
        lineService.updateLine(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/delete/{id}")
    @ApiOperation("删除产线")
    @OperationLog
    public ResponseInfo<Void> deleteLine(@PathVariable @NotNull @ApiParam("工位id") Long id) {
        lineService.deleteLine(id);
        return ResponseInfo.success();
    }

    @PutMapping("/enable")
    @ApiOperation("启停产线")
    @OperationLog
    public ResponseInfo<Void> enableLine(@RequestBody LineEnableDTO dto) {
        lineService.enableLine(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("获取产线列表")
    public ResponseInfo<CommonPage<LinePageVO>> getLinePage(LinePageDTO dto) {
        return ResponseInfo.success(lineService.getLinePage(dto));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取产线详情")
    public ResponseInfo<LineInfoVO> getLineInfo(@PathVariable @NotNull @ApiParam("产线id") Long id) {
        return ResponseInfo.success(lineService.getLineInfo(id));
    }

    @PostMapping("/bind/room")
    @ApiOperation("产线绑定房间")
    @OperationLog
    public ResponseInfo<Void> bindRoom(@RequestBody LineBindRoomDTO dto) {
        lineService.bindRoom(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/bind/station")
    @ApiOperation("产线绑定工位")
    @OperationLog
    public ResponseInfo<Void> bindStation(@RequestBody LineBindStationDTO dto) {
        lineService.bindStation(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/user/line")
    @ApiOperation("获取某个用户具有数据权限的产线/房间下的所有工位")
    public ResponseInfo<FactoryResourceUserVO> getLineByUser(@RequestParam("userId") String userId) {
        return ResponseInfo.success(lineService.getLineByUser(userId));
    }
}

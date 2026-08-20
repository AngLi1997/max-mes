package com.bmos.platform.service.factory.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.service.factory.controller.vo.ModuleVO;
import com.bmos.platform.service.factory.controller.vo.StationModuleTreeNodeVO;
import com.bmos.platform.service.factory.service.FactoryModuleService;
import com.bmos.platform.service.factory.service.dto.ModuleSaveDTO;
import com.bmos.platform.service.factory.service.dto.ModuleUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 房间模型树接口
 */
@RestController
@RequestMapping("/factory/line/module")
@Validated
@Api(tags = "产线模型相关接口")
public class LineModuleController {

    @Autowired
    FactoryModuleService factoryModuleService;

    @PostMapping("/save")
    @ApiOperation("新建产线模型")
    @OperationLog
    public ResponseInfo<Void> saveModule(@RequestBody ModuleSaveDTO dto){
        factoryModuleService.saveModule(dto, FactoryModuleEnum.LINE);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("修改产线模型")
    @OperationLog
    public ResponseInfo<Void> updateModule(@RequestBody ModuleUpdateDTO dto){
        factoryModuleService.updateModule(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除产线模型")
    @OperationLog
    public ResponseInfo<Void> deleteModule(@PathVariable @NotNull @ApiParam("房间模型id") Long id){
        factoryModuleService.deleteModule(id);
        return ResponseInfo.success();
    }

    @GetMapping("/tree/list")
    @ApiOperation("获取设备产线模型树(不包含房间信息)")
    public ResponseInfo<List<StationModuleTreeNodeVO>> getModuleTree(){
        return ResponseInfo.success(factoryModuleService.getModuleTree(FactoryModuleEnum.LINE));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取产线模型基础信息")
    public ResponseInfo<ModuleVO> getModuleInfo(@PathVariable @NotNull @ApiParam("房间模型id") Long id){
        return ResponseInfo.success(factoryModuleService.getModuleTreeInfo(id));
    }

}

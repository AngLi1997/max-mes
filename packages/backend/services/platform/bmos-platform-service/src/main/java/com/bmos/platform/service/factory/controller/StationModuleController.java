package com.bmos.platform.service.factory.controller;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.service.equipment.controller.vo.ChoiceBoxVO;
import com.bmos.platform.service.factory.service.FactoryModuleService;
import com.bmos.platform.service.factory.service.dto.ModuleSaveDTO;
import com.bmos.platform.service.factory.controller.vo.StationModuleTreeNodeVO;
import com.bmos.platform.service.factory.service.dto.ModuleUpdateDTO;
import com.bmos.platform.service.factory.controller.vo.ModuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设备工厂建模相关接口
 */
@RestController
@RequestMapping("/factory/station/module")
@Validated
@Api(tags = "工位分类树接口")
public class StationModuleController {

    @Autowired
    private FactoryModuleService factoryModuleService;

    @PostMapping("/save")
    @ApiOperation("新建设备工厂模型")
    @OperationLog
    public ResponseInfo<Void> saveModule(@RequestBody ModuleSaveDTO dto){
        factoryModuleService.saveModule(dto, FactoryModuleEnum.STATION);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("修改设备工厂模型")
    @OperationLog
    public ResponseInfo<Void> updateModule(@RequestBody ModuleUpdateDTO dto){
        factoryModuleService.updateModule(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除工厂模型")
    @OperationLog
    public ResponseInfo<Void> deleteModule(@PathVariable @NotNull @ApiParam("设备工厂模型id") Long id){
        factoryModuleService.deleteModule(id);
        return ResponseInfo.success();
    }

    @GetMapping("/tree/list")
    @ApiOperation("工位模型树(不包含工位信息)")
    public ResponseInfo<List<StationModuleTreeNodeVO>> getModuleTree(){
        return ResponseInfo.success(factoryModuleService.getModuleTree(FactoryModuleEnum.STATION));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取设备工厂模型基础信息")
    public ResponseInfo<ModuleVO> getModuleTreeInfo(@PathVariable @NotNull @ApiParam("设备工厂模型id") Long id){
        return ResponseInfo.success(factoryModuleService.getModuleTreeInfo(id));
    }

    @GetMapping("/list/production/line")
    @ApiOperation("查询生成产线接口")
    public ResponseInfo<List<ChoiceBoxVO>> listProductionLine(){
        return ResponseInfo.success(factoryModuleService.listProductionLine());
    }

    @GetMapping("/list/production/room")
    @ApiOperation("根据生产产线id查询工位房间信息")
    public ResponseInfo<List<ChoiceBoxVO>> listProductionRoom(@Valid  @NotEmpty String lineIdList){
        return ResponseInfo.success(factoryModuleService.listProductionRoom(StrUtil.split(lineIdList,StrUtil.C_COMMA)));
    }

}

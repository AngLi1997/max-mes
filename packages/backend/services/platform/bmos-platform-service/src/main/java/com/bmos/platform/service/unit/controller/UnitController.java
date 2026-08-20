package com.bmos.platform.service.unit.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.expression.model.RoundingVO;
import com.bmos.expression.util.RoundingUtil;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.unit.dto.*;
import com.bmos.platform.service.unit.service.UnitExtendService;
import com.bmos.platform.service.unit.service.UnitService;
import com.bmos.platform.service.unit.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/unit")
@Api(tags = "单位配置相关接口")
@Validated
public class UnitController {

    @Autowired
    private UnitService unitService;

    @Autowired
    private UnitExtendService extendService;

    @GetMapping("/list/unit")
    @ApiOperation(value = "查询标准单位列表")
    public ResponseInfo<CommonPage<UnitVO>> listUnit(@Validated UnitListQueryDTO dto) {
        return ResponseInfo.success(CommonPage.convertPage(unitService.listUnit(dto)));
    }

    @GetMapping("/list/unit/extend")
    @ApiOperation(value = "查询扩展单位列表")
    public ResponseInfo<CommonPage<UnitExtendVO>> listUnitExtend(@Validated UnitListQueryDTO dto) {
        return ResponseInfo.success(CommonPage.convertPage(extendService.listUnitExtend(dto)));
    }

    @PostMapping("/save/unit")
    @ApiOperation(value = "新增标准单位信息")
    @OperationLog
    public ResponseInfo<Boolean> saveUnit(@Validated @RequestBody SaveUnitDTO dto) {
        return ResponseInfo.success(unitService.saveUnit(dto));
    }

    @GetMapping("/watch/unit")
    @ApiOperation(value = "查看标准单位信息")
    @ApiParam(name = "id", value = "标准单位主键id", required = true)
    public ResponseInfo<UnitVO> watchUnit(@NotNull Long id) {
        return ResponseInfo.success(unitService.watchUnit(id));
    }

    @GetMapping("/delete/unit")
    @ApiOperation(value = "删除标准单位信息")
    @ApiParam(name = "id", value = "标准单位主键id", required = true)
    @OperationLog
    public ResponseInfo<Boolean> deleteUnit(@NotNull Long id) {
        return ResponseInfo.success(unitService.deleteUnit(id));
    }

    @PostMapping("/update/unit")
    @ApiOperation(value = "编辑标准单位信息")
    @OperationLog
    public ResponseInfo<Boolean> updateUnit(@Validated @RequestBody UpdateUnitDTO dto) {
        return ResponseInfo.success(unitService.updateUnit(dto));
    }

    @GetMapping("/list/down/box")
    @ApiOperation(value = "查询标准单位下拉框")
    public ResponseInfo<List<UnitPullDownBoxVO>> listDownBox() {
        return ResponseInfo.success(unitService.listDownBox());
    }

    @PostMapping("/save/unit/extend")
    @ApiOperation(value = "添加扩展单位信息")
    @OperationLog
    public ResponseInfo<Boolean> saveUnitExtend(@Validated @RequestBody SaveUnitExtendDTO dto) {
        return ResponseInfo.success(extendService.saveUnitExtend(dto));
    }

    @GetMapping("/watch/unit/extend")
    @ApiOperation(value = "查看扩展单位信息")
    @ApiParam(name = "id", value = "扩展单位id", required = true)
    public ResponseInfo<UnitExtendVO> watchUnitExtend(@NotNull Long id) {
        return ResponseInfo.success(extendService.watchUnitExtend(id));
    }

    @GetMapping("/delete/unit/extend")
    @ApiOperation(value = "删除扩展单位信息")
    @ApiParam(name = "id", value = "扩展单位id", required = true)
    @OperationLog
    public ResponseInfo<Boolean> deleteUnitExtend(@NotNull Long id) {
        return ResponseInfo.success(extendService.deleteUnitExtend(id));
    }

    @PostMapping("/update/unit/extend")
    @ApiOperation(value = "更新扩展单位信息")
    @OperationLog
    public ResponseInfo<Boolean> updateUnitExtend(@Validated @RequestBody UpdateUnitExtendDTO dto) {
        return ResponseInfo.success(extendService.updateUnitExtend(dto));
    }

    @PostMapping("/update/extend/state")
    @ApiOperation(value = "更新扩展单位启停状态")
    @OperationLog
    public ResponseInfo<Boolean> updateExtendState(@Validated @RequestBody UpdateUnitExtendDTO dto){
        return ResponseInfo.success(extendService.updateExtendState(dto));
    }

    @GetMapping("/list/rounding")
    @ApiOperation(value = "查询修约规则")
    public ResponseInfo<List<RoundingVO>> listRounding(){
        return ResponseInfo.success(RoundingUtil.getRoundingList());
    }

    @GetMapping("/list/extendUnit")
    @ApiOperation("查询拓展单位下拉列表")
    public ResponseInfo<List<UnitExtendListVO>> getExtendUnitList(@NotNull @ApiParam(value = "单位id", name="unitId", required = true) Long unitId){
        return ResponseInfo.success(extendService.getExtendUnitListByUnitId(Collections.singletonList(unitId)));
    }

    @PostMapping("/list/unitAndExtend")
    public ResponseInfo<UnitAndExtendDTO> getUnitAndExtend(@Validated @RequestBody RemoteQueryDTO remoteQueryDTO) {
        return ResponseInfo.success(extendService.getUnitAndExtend(remoteQueryDTO));
    }

    @PostMapping("/getUnitById")
    public ResponseInfo<CommonUnitVO> getUnitById(@RequestBody @Validated UnitQueryDTO dto) {
        return ResponseInfo.success(extendService.getUnitById(dto));
    }

    @PostMapping("/getUnitListByIds")
    public ResponseInfo<List<CommonUnitVO>> getUnitListByIds(@RequestBody @Valid List<UnitQueryDTO> list) {
        return ResponseInfo.success(extendService.getUnitListByIds(list));
    }

    @PostMapping("/getCommonUnitByIds")
    public ResponseInfo<List<CommonUnitVO>> getCommonUnitByIds(@RequestBody List<Long> ids){
        return ResponseInfo.success(unitService.getCommonUnitByIds(ids));
    }

    @GetMapping("/getAllUnit")
    @ApiOperation("查询所有单位列表")
    @PermitAll
    public ResponseInfo<CommonGlobalUnit> getAllUnit() {
        return ResponseInfo.success(unitService.getAllUnit());
    }

    @GetMapping("/getUnitById")
    @ApiOperation("根据id获取单位")
    public ResponseInfo<CommonUnitVO> getUnitById(@RequestParam Long id) {
        return ResponseInfo.success(unitService.getUnitById(id));
    }
}

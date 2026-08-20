package com.bmos.mes.service.unit.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.unit.service.UnitService;
import com.bmos.mes.service.unit.vo.ExtendUnitPullDownBoxVO;
import com.bmos.mes.service.unit.vo.UnitPullDownBoxVO;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.bmos.unit.vo.UnitCalcDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/unit")
@Api(tags = "单位相关接口")
@Validated
public class UnitController {

//    @Autowired
//    private UnitService unitService;

    @Resource
    private UnitCache unitCache;

    @Resource
    private UnitService unitService;

    @GetMapping("/list/down/box")
    @ApiOperation(value = "查询标准单位下拉框")
    public ResponseInfo<List<UnitPullDownBoxVO>> listDownBox() {
        return ResponseInfo.success(unitCache.getUnitCache().values().stream()
                .filter(unit -> !unit.getExtend())
                .map(unit -> UnitPullDownBoxVO.builder().unitId(unit.getUnitId()).unitName(unit.getUnitName()).build())
                .collect(Collectors.toList()));
    }

    @GetMapping("/list/down/extend")
    @ApiOperation("查询拓展单位下拉框")
    public ResponseInfo<List<ExtendUnitPullDownBoxVO>> listExtendDownBox(@NotNull Long standardUnitId) {
        // todo 该接口可能已被废弃 直接调用平台接口
        return ResponseInfo.success(unitCache.getUnitCache().values().stream()
                .filter(CacheUnit::getExtend)
                .map(unit -> ExtendUnitPullDownBoxVO.builder()
                        .id(unit.getUnitId())
                        .extendUnitName(unit.getUnitName())
                        .expression(unit.getRate().toPlainString())
                        .build())
                .collect(Collectors.toList()));
    }

    @GetMapping("/list/down/extend/bound")
    @ApiOperation("查询物料绑定拓展单位")
    public ResponseInfo<List<ExtendUnitPullDownBoxVO>> listByMaterialId(@NotNull @ApiParam(name = "materialId", value = "物料id", required = true) Long materialId){
        return ResponseInfo.success(unitService.listByMaterialId(materialId));
    }

    @PostMapping("/calcSumAdapt")
    @ApiOperation("单位换算求和")
    public ResponseInfo<UnitCalcDTO.UnitCalc> calcSumAdapt(@RequestBody @Validated UnitCalcDTO dto) {
        return ResponseInfo.success(unitCache.calcSumAdapt(dto));
    }

    @GetMapping("/getUnitById/{unitId}")
    @ApiOperation("根据id获取单位信息")
    public ResponseInfo<CacheUnit> calcSumAdapt(@PathVariable Long unitId) {
        return ResponseInfo.success(unitCache.getGlobalUnit(unitId));
    }
}

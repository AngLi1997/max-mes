package com.bmos.wms.service.unit.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.bmos.unit.vo.UnitCalcDTO;
import com.bmos.wms.service.unit.vo.UnitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 单位信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/11 11:44
 */
@RestController
@RequestMapping("/unit")
@Validated
@Api(tags = "单位信息")
@Slf4j
public class UnitController {

    @Resource
    private UnitCache unitCache;

    @GetMapping("/getAllUnit")
    @ApiOperation("获取全部单位信息")
    public ResponseInfo<List<UnitVO>> getAllUnit() {
        Map<String, CacheUnit> cache = unitCache.getUnitCache();
        List<UnitVO> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(cache.values())) {
            for (CacheUnit cacheUnit : cache.values()) {
                UnitVO unitVO = new UnitVO();
                unitVO.setId(cacheUnit.getUnitId());
                unitVO.setParentId(cacheUnit.getParentUnitId() == null ? 0L : cacheUnit.getParentUnitId());
                unitVO.setIsExtend(cacheUnit.getExtend());
                unitVO.setName(cacheUnit.getUnitName());
                if (cacheUnit.getExtend()) {
                    unitVO.setExpression("1" + cacheUnit.getUnitName() + "=" + cacheUnit.getRate() + unitCache.getGlobalUnitName(cacheUnit.getParentUnitId()));
                }
                list.add(unitVO);
            }
        }
        return ResponseInfo.success(TreeUtil.buildTree(list, false));
    }

    @GetMapping("/getBaseUnitList")
    @ApiOperation("获取全部基本单位信息")
    public ResponseInfo<List<UnitVO>> getBaseUnitList() {
        Map<String, CacheUnit> cache = unitCache.getUnitCache();
        List<CacheUnit> unitList = cache.values().stream().filter(item -> !item.getExtend()).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(unitList)) {
            return ResponseInfo.success(new ArrayList<>());
        }
        List<UnitVO> result = unitList.stream().map(cacheUnit -> {
            UnitVO unitVO = new UnitVO();
            unitVO.setId(cacheUnit.getUnitId());
            unitVO.setParentId(cacheUnit.getParentUnitId() == null ? 0L : cacheUnit.getParentUnitId());
            unitVO.setIsExtend(cacheUnit.getExtend());
            unitVO.setName(cacheUnit.getUnitName());
            return unitVO;
        }).collect(Collectors.toList());
        return ResponseInfo.success(result);
    }

    @GetMapping("/getExtUnitListByBaseId")
    @ApiOperation("根据基本单位id获取扩展单位信息")
    @ApiImplicitParam(name = "unitId", value = "基本单位id", required = true, example = "1")
    public ResponseInfo<List<UnitVO>> getExtUnitListByBaseId(@RequestParam Long unitId) {
        Map<String, CacheUnit> cache = unitCache.getUnitCache();
        List<CacheUnit> extUnitList = cache.values().stream()
                .filter(CacheUnit::getExtend)
                .filter(item -> Objects.equals(item.getParentUnitId(), unitId))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(extUnitList)) {
            return ResponseInfo.success(new ArrayList<>());
        }
        List<UnitVO> result = extUnitList.stream().map(cacheUnit -> {
            UnitVO unitVO = new UnitVO();
            unitVO.setId(cacheUnit.getUnitId());
            unitVO.setParentId(cacheUnit.getParentUnitId() == null ? 0L : cacheUnit.getParentUnitId());
            unitVO.setIsExtend(cacheUnit.getExtend());
            unitVO.setName(cacheUnit.getUnitName());
            unitVO.setExpression("1" + cacheUnit.getUnitName() + "=" + cacheUnit.getRate() + unitCache.getGlobalUnitName(cacheUnit.getParentUnitId()));
            return unitVO;
        }).collect(Collectors.toList());
        return ResponseInfo.success(result);
    }

    @PostMapping("/calcSumAdapt")
    @ApiOperation("单位换算求和(必须标准单位一致才可转换，否则返回null)")
    public ResponseInfo<UnitCalcDTO.UnitCalc> calcSumAdapt(@RequestBody @Validated UnitCalcDTO dto) {
        return ResponseInfo.success(unitCache.calcSumAdapt(dto));
    }
}

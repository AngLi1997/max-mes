package com.bmos.platform.service.unit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.material.model.Material;
import com.bmos.platform.service.material.service.MaterialService;
import com.bmos.platform.service.unit.convert.UnitConvert;
import com.bmos.platform.service.unit.dto.SaveUnitDTO;
import com.bmos.platform.service.unit.dto.UnitListQueryDTO;
import com.bmos.platform.service.unit.dto.UpdateUnitDTO;
import com.bmos.platform.service.unit.mapper.UnitMapper;
import com.bmos.platform.service.unit.model.Unit;
import com.bmos.platform.service.unit.model.UnitExtend;
import com.bmos.platform.service.unit.service.UnitExtendService;
import com.bmos.platform.service.unit.service.UnitService;
import com.bmos.platform.service.unit.vo.CommonGlobalUnit;
import com.bmos.platform.service.unit.vo.CommonUnitVO;
import com.bmos.platform.service.unit.vo.UnitPullDownBoxVO;
import com.bmos.platform.service.unit.vo.UnitVO;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UnitServiceImpl implements UnitService {

    private static final String LOG_PREFIX = "[单位]";

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    private UnitExtendService extendService;

    @Autowired
    private MaterialService materialService;

    @Resource
    private UnitCache unitCache;

    @Override
    public List<UnitVO> listUnit(UnitListQueryDTO dto) {
        return unitMapper.listUnit(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUnit(SaveUnitDTO dto) {
        List<Unit> list = unitMapper.queryByUnitName(dto.getUnitName());
        if (CollUtil.isNotEmpty(list)) {
            throw new BmosException(PlatformResponseCode.UNIT_NAME_UNIQUE);
        }
        try {
            Boolean result = unitMapper.saveUnit(UnitConvert.INSTANCE.convertToUnit(dto));
            // 刷新缓存
            unitCache.reload();
            return result;
        } catch (Exception e) {
            throw new BmosException(PlatformResponseCode.UNIT_NAME_UNIQUE);
        }
    }

    @Override
    public UnitVO watchUnit(Long id) {
        return UnitConvert.INSTANCE.converToUnitVo(unitMapper.selectUnitOne(id));
    }

    @Override
    public Boolean deleteUnit(Long id) {
        List<UnitExtend> extendList = extendService.queryListByUnitId(id);
        List<Material> materialList = materialService.queryByUnitId(id);
        if (CollUtil.isNotEmpty(materialList)) {
            throw new BmosException(PlatformResponseCode.MATERIAL_USE_UNIT_EXTEND);
        }
        if (CollUtil.isNotEmpty(extendList)) {
            throw new BmosException(PlatformResponseCode.USE_UNIT_EXTEND);
        }
        unitMapper.deleteUnit(id, SysUserHolder.getUser().getUserId());
        // 刷新缓存
        unitCache.reload();
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUnit(UpdateUnitDTO dto) {
        List<UnitExtend> extendList = extendService.queryListByUnitId(dto.getId());
        if (CollUtil.isNotEmpty(extendList) && CollectionUtils.convertList(extendList, UnitExtend::getState).contains(Boolean.TRUE)) {
            throw new BmosException(PlatformResponseCode.UNIT_USE_STATE);
        }
        try {
            Boolean result = unitMapper.updateUnit(UnitConvert.INSTANCE.convertToUpdateUnit(dto));
            // 刷新缓存
            unitCache.reload();
            return result;
        } catch (Exception e) {
            throw new BmosException(PlatformResponseCode.UNIT_NAME_UNIQUE);
        }

    }

    @Override
    public List<UnitPullDownBoxVO> listDownBox() {
        List<Unit> list = unitMapper.selectUnitList(StatusEnum.ON.getValue());
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<UnitPullDownBoxVO> boxList = list.stream().map(unit -> {
            UnitPullDownBoxVO box = new UnitPullDownBoxVO();
            box.setUnitId(unit.getId());
            box.setUnitName(unit.getUnitName());
            return box;
        }).collect(Collectors.toList());
        return boxList;
    }

    @Override
    public List<CommonUnitVO> getCommonUnitByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Unit> units = unitMapper.selectBatchIds(ids);
        List<CommonUnitVO> collect = units.stream().map(unit -> CommonUnitVO.builder()
                .unitId(unit.getId())
                .unitName(unit.getUnitName())
                .precision(unit.getUnitPrecision())
                .rounding(RoundingEnum.getEnumByCode(unit.getRoundCode()))
                .rate("1")
                .extend(false)
                .build()).collect(Collectors.toList());
        List<UnitExtend> extendUnitList = extendService.getCommonUnitByIds(ids);
        List<Long> extendIds = extendUnitList.stream().map(UnitExtend::getUnitId).collect(Collectors.toList());
        List<Unit> unit = unitMapper.selectBatchIds(extendIds);
        if (extendIds.size() != unit.size()) {
            throw new BmosException(PlatformResponseCode.UNIT_NOTFOUND);
        }
        Map<Long, Unit> unitMap = unit.stream().collect(Collectors.toMap(Unit::getId, Function.identity()));
        List<CommonUnitVO> collect1 = extendUnitList.stream().map(extend -> CommonUnitVO.builder()
                .unitId(extend.getId())
                .unitName(extend.getExtendUnitName())
                .precision(extend.getExtendPrecision())
                .rate(extend.getExpressionValue())
                .rounding(Optional.ofNullable(extend.getUnitId())
                        .map(unitId -> unitMap.get(extend.getUnitId()))
                        .map(Unit::getRoundCode)
                        .map(RoundingEnum::getEnumByCode)
                        .orElse(null)
                )
                .extend(true)
                .build()).collect(Collectors.toList());
        collect.addAll(collect1);
        return collect;
    }

    @Override
    public CommonGlobalUnit getAllUnit() {
        List<Unit> units = unitMapper.selectList();
        List<CommonUnitVO> unitList = units.stream().map(unit -> CommonUnitVO.builder()
                .unitId(unit.getId())
                .unitName(unit.getUnitName())
                .precision(unit.getUnitPrecision())
                .rounding(RoundingEnum.getEnumByCode(unit.getRoundCode()))
                .rate("1")
                .extend(false)
                .enabled(unit.getState())
                .build()).collect(Collectors.toList());
        List<UnitExtend> extendUnitList = extendService.listAll();
        Map<Long, Unit> unitMap = units.stream().collect(Collectors.toMap(Unit::getId, Function.identity()));
        List<CommonUnitVO> extendsUnitList = extendUnitList.stream().map(extend -> CommonUnitVO.builder()
                .unitId(extend.getId())
                .unitName(extend.getExtendUnitName())
                .precision(extend.getExtendPrecision())
                .rate(extend.getExpressionValue())
                .parentUnitId(extend.getUnitId())
                .rounding(Optional.ofNullable(extend.getUnitId())
                        .map(unitId -> unitMap.get(extend.getUnitId()))
                        .map(Unit::getRoundCode)
                        .map(RoundingEnum::getEnumByCode)
                        .orElse(null)
                )
                .extend(true)
                .enabled(extend.getState())
                .build()).collect(Collectors.toList());
        return new CommonGlobalUnit(
                unitList.stream()
                        .collect(Collectors.toMap(item -> item.getUnitId().toString(), Function.identity(), (oldValue, newValue) -> newValue)),
                extendsUnitList.stream().collect(Collectors.toMap(item -> item.getUnitId().toString(), Function.identity(), (oldValue, newValue) -> newValue)));
    }

    @Override
    public CommonUnitVO getUnitById(Long id) {
        CacheUnit globalUnit = unitCache.getGlobalUnit(id);
        return UnitConvert.INSTANCE.convertToCommonUnitVO(globalUnit);
    }
}

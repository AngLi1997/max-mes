package com.bmos.unit.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.cache.redis.RedisService;
import com.bmos.unit.constant.UnitRedisKeyDefine;
import com.bmos.cache.redis.objects.CommonUnit;
import com.bmos.common.response.ResponseInfo;
import com.bmos.unit.exception.UnitConvertException;
import com.bmos.unit.feign.UnitOpenFeign;
import com.bmos.unit.vo.CacheUnit;
import com.bmos.unit.vo.CommonGlobalUnit;
import com.bmos.unit.vo.UnitCalcDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单位缓存
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/26 18:43
 */
@Slf4j
public class UnitCache {

    private static final String LOG_PREFIX = "[单位信息缓存]";

    @Resource
    private RedisService redisService;

    @Resource
    private UnitOpenFeign unitOpenFeign;

    /**
     * 刷新缓存
     */
    public Map<String, CacheUnit> reload() {
        log.info("{}开始缓存单位信息", LOG_PREFIX);
        redisService.clearGroup(UnitRedisKeyDefine.UNIT_INFO_CACHE);
        CommonGlobalUnit unitData = new CommonGlobalUnit();
        try {
            ResponseInfo<CommonGlobalUnit> responseInfo = unitOpenFeign.getAllUnit();
            if (responseInfo.isSuccess()) {
                unitData = responseInfo.getData();
            }
        } catch (Exception e) {
            log.error("{}初始化单位信息失败:{}", LOG_PREFIX, e.getMessage());
            return new HashMap<>();
        }
        if (MapUtil.isEmpty(unitData.getTotalUnit())) {
            return new HashMap<>();
        }
        unitData.getTotalUnit().forEach((key, value) -> redisService.set(key, value, UnitRedisKeyDefine.UNIT_INFO_CACHE));
        // 基本单位以名称为key缓存(平台基本单位名称不可重复)
        unitData.getBaseUnitMap().forEach((key, value) -> redisService.set(key, value, UnitRedisKeyDefine.UNIT_INFO_CACHE));
        log.info("{}单位信息缓存成功:{}", LOG_PREFIX, unitData.getTotalUnit());
        return unitData.getTotalUnit();
    }

    /**
     * 获取单位缓存
     *
     * @return
     */
    public Map<String, CacheUnit> getUnitCache() {
        return reload();
    }

    /**
     * 获取基本单位信息
     *
     * @param unitId 单位信息
     * @return 单位信息
     */
    @Nullable
    public CacheUnit getUnit(Long unitId) {
        CacheUnit unit = getGlobalUnit(unitId);
        if (unit == null) {
            return null;
        }
        return !unit.getExtend() ? unit : null;
    }

    /**
     * 获取基本单位信息
     *
     * @param unitName 单位名称
     * @return 单位信息
     */
    public CacheUnit getBaseUnitByName(String unitName) {
        if (StrUtil.isBlank(unitName)) {
            return null;
        }
        Object cache = redisService.get(unitName, UnitRedisKeyDefine.UNIT_INFO_CACHE);
        if (ObjectUtil.isEmpty(cache)) {
            // 从数据库查询
            reload();
            cache = redisService.get(unitName, UnitRedisKeyDefine.UNIT_INFO_CACHE);
        }
        if (ObjectUtil.isEmpty(cache)) {
            return null;
        } else {
            return CacheUnit.from((CommonUnit) cache);
        }
    }

    /**
     * 获取扩展单位信息
     *
     * @param extendUnitId 扩展单位id
     * @return 扩展单位信息
     */
    @Nullable
    public CacheUnit getExtendUnitId(Long extendUnitId) {
        CacheUnit unit = getGlobalUnit(extendUnitId);
        if (unit == null) {
            return null;
        }
        return unit.getExtend() ? unit : null;
    }

    /**
     * 根据id查询单位信息
     *
     * @param unitIdOrExtendUnitId 单位id或者扩展单位id
     * @return 单位信息
     */
    @Nullable
    public CacheUnit getGlobalUnit(Long unitIdOrExtendUnitId) {
        if (unitIdOrExtendUnitId == null) {
            return null;
        }
        Object cache = redisService.get(unitIdOrExtendUnitId.toString(), UnitRedisKeyDefine.UNIT_INFO_CACHE);
        if (ObjectUtil.isEmpty(cache)) {
            // 从数据库查询
            Map<String, CacheUnit> load = reload();
            if (MapUtil.isEmpty(load)) {
                return null;
            }
            load.forEach((key, value) -> redisService.set(key, value, UnitRedisKeyDefine.UNIT_INFO_CACHE));
            return load.get(unitIdOrExtendUnitId.toString());
        } else {
            return CacheUnit.from((CommonUnit) cache);
        }
    }

    /**
     * 获取单位名称
     *
     * @param unitId 单位id
     * @return 单位名称
     */
    @Nullable
    public String getGlobalUnitName(Long unitId) {
        return Optional.ofNullable(unitId)
                .map(this::getGlobalUnit)
                .map(CacheUnit::getUnitName)
                .orElse(null);
    }


    /**
     * 根据扩展单位id获取基本单位
     *
     * @param extendId 扩展单位id
     * @return
     */
    @Nullable
    public CacheUnit getUnitByExtendId(Long extendId) {
        CacheUnit extend = getExtendUnitId(extendId);
        if (extend == null) {
            return null;
        }
        Long parentUnitId = extend.getParentUnitId();
        return getUnit(parentUnitId);
    }

    /**
     * 根据单位id获取扩展单位列表
     *
     * @param unitId 单位id
     * @return 扩展单位列表
     */
    public List<CacheUnit> getExtendIdListByUnitId(Long unitId) {
        List<CacheUnit> result = new ArrayList<>();
        if (unitId == null) {
            return result;
        }
        for (CacheUnit unit : getUnitCache().values()) {
            if (unit.getExtend() && Objects.equals(unitId, unit.getParentUnitId())) {
                result.add(unit);
            }
        }
        return result;
    }

    /**
     * 查询基本单位id
     *
     * @param unitId 单位id 基本单位id返回自身， 扩展单位返回基本单位id
     * @return
     */
    public Long getBaseUnitId(Long unitId) {
        CacheUnit unit = getGlobalUnit(unitId);
        if (unit == null) {
            return null;
        }
        if (!unit.getExtend()) {
            // 本身即是基本单位id
            return unitId;
        } else {
            CacheUnit baseUnit = getUnit(unit.getParentUnitId());
            if (baseUnit == null) {
                return null;
            }
            return baseUnit.getUnitId();
        }
    }

    /**
     * 单位数值转换
     *
     * @param value      原始值
     * @param fromUnitId 原始单位
     * @param toUnitId   转换单位
     * @return 转换值
     */
    public BigDecimal convert(BigDecimal value, Long fromUnitId, Long toUnitId) {
        if (value == null || fromUnitId == null || toUnitId == null) {
            throw new IllegalArgumentException("单位转换参数错误: 参数错误");
        }
        CacheUnit fromUnit = getGlobalUnit(fromUnitId);
        CacheUnit toUnit = getGlobalUnit(toUnitId);
        if (fromUnit == null || toUnit == null) {
            throw new UnitConvertException("单位转换参数错误: 单位不存在");
        } else if (!fromUnit.getExtend() && !toUnit.getExtend() && Objects.equals(fromUnit.getUnitId(), toUnit.getUnitId())) {
            // 基本 -> 基本
            return value;
        } else if (!fromUnit.getExtend() && toUnit.getExtend() && Objects.equals(fromUnit.getUnitId(), toUnit.getParentUnitId())) {
            // 基本 -> 扩展
            return value.divide(toUnit.getRate(), 10, RoundingMode.HALF_UP);
        } else if (fromUnit.getExtend() && !toUnit.getExtend() && Objects.equals(fromUnit.getParentUnitId(), toUnit.getUnitId())) {
            // 扩展 -> 基本
            return value.multiply(fromUnit.getRate());
        } else if (fromUnit.getExtend() && toUnit.getExtend() && Objects.equals(fromUnit.getParentUnitId(), toUnit.getParentUnitId())) {
            // 扩展 -> 扩展
            return value.multiply(fromUnit.getRate()).divide(toUnit.getRate(), 10, RoundingMode.HALF_UP);
        } else {
            throw new UnitConvertException("单位转换参数错误: 不在同一个基本单位 无法转换");
        }
    }

    /**
     * 转换为基本单位量
     *
     * @param value      原单位量
     * @param fromUnitId 原单位
     * @return 基本单位量
     */
    public BigDecimal toBasic(BigDecimal value, Long fromUnitId) {
        return convert(value, fromUnitId, getBaseUnitId(fromUnitId));
    }

    /**
     * 转换为目标单位量
     *
     * @param value    基本单位量
     * @param toUnitId 目标单位
     * @return 目标单位量
     */
    public BigDecimal toExt(BigDecimal value, Long toUnitId) {
        return convert(value, getBaseUnitId(toUnitId), toUnitId);
    }

    /**
     * 单位换算求和（必须基本单位一致 否则返回null）
     *
     * @param dto
     * @return
     */
    @Nullable
    public UnitCalcDTO.UnitCalc calcSumAdapt(@RequestBody @Validated UnitCalcDTO dto) {
        Set<Long> baseUnitIds = dto.getList().stream()
                .map(UnitCalcDTO.UnitCalc::getUnitId)
                .map(this::getBaseUnitId)
                .collect(Collectors.toSet());
        baseUnitIds.add(this.getBaseUnitId(dto.getTargetUnitId()));
        if (CollectionUtil.isEmpty(baseUnitIds) || baseUnitIds.size() != 1) {
            log.info("单位信息转换失败，标准单位不一致");
            return null;
        }
        BigDecimal sum = dto.getList().stream()
                .map(item -> this.toBasic(new BigDecimal(item.getValue()), item.getUnitId()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return UnitCalcDTO.UnitCalc.builder()
                .value(this.toExt(sum, dto.getTargetUnitId()).stripTrailingZeros().toPlainString())
                .unitId(dto.getTargetUnitId())
                .build();
    }
}

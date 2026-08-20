package com.bmos.unit.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 单位信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/26 18:19
 */
@Data
@NoArgsConstructor
public class CommonGlobalUnit {

    /**
     * 所有基本单位
     */
    Map<String, CacheUnit> unit = new HashMap<>();

    /**
     * 所有扩展单位
     */
    Map<String, CacheUnit> existUnit = new HashMap<>();

    /**
     * 合并所有单位
     */
    Map<String, CacheUnit> totalUnit = new HashMap<>();

    /**
     * 基本单位map
     * key:单位name
     * value:单位信息
     */
    Map<String, CacheUnit> baseUnitMap = new HashMap<>();


    public CommonGlobalUnit(Map<String, CacheUnit> unit, Map<String, CacheUnit> existUnit) {
        this.unit = unit;
        this.existUnit = existUnit;
        totalUnit.putAll(unit);
        totalUnit.putAll(existUnit);
    }
}

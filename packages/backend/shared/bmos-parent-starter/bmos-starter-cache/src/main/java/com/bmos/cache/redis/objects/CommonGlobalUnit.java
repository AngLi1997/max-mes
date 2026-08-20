package com.bmos.cache.redis.objects;

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
    Map<String, CommonUnit> unit = new HashMap<>();

    /**
     * 所有扩展单位
     */
    Map<String, CommonUnit> existUnit = new HashMap<>();

    /**
     * 合并所有单位
     */
    Map<String, CommonUnit> totalUnit = new HashMap<>();


    public CommonGlobalUnit(Map<String, CommonUnit> unit, Map<String, CommonUnit> existUnit) {
        this.unit = unit;
        this.existUnit = existUnit;
        totalUnit.putAll(unit);
        totalUnit.putAll(existUnit);
    }
}

package com.bmos.platform.service.unit.vo;

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
    Map<String, CommonUnitVO> unit = new HashMap<>();

    /**
     * 所有扩展单位
     */
    Map<String, CommonUnitVO> existUnit = new HashMap<>();

    /**
     * 合并所有单位
     */
    Map<String, CommonUnitVO> totalUnit = new HashMap<>();


    public CommonGlobalUnit(Map<String, CommonUnitVO> unit, Map<String, CommonUnitVO> existUnit) {
        this.unit = unit;
        this.existUnit = existUnit;
        totalUnit.putAll(unit);
        totalUnit.putAll(existUnit);
    }
}

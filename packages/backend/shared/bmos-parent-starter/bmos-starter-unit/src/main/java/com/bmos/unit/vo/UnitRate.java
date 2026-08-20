package com.bmos.unit.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 公共单位信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:47
 */
@ApiModel(value = "单位转换率")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitRate {

    /**
     * 原始值字段名
     */
    private Set<String> valueFields = new HashSet<>();

    /**
     * 单位id字段名
     */
    private String unitIdField;
}

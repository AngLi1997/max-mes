package com.bmos.mes.service.tag.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 码类型
 * @author liang
 * @version 1.0.0
 * @date 2025/3/5 14:43
 */
@Getter
@AllArgsConstructor
public enum CodeType implements CommonEnum<String> {

    MATERIAL("物料", "material"),

    CONTAINER("容器", "container");

    private final String name;

    @EnumValue
    private final String value;
}

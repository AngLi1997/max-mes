package com.bmos.mes.common.enums.ingredient;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量模式
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:24
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WeighMode implements CommonEnum<Integer> {

    /**
     * 称具称量
     */
    BALANCE(1, "称具称量"),

    /**
     * 手动称量
     */
    MANUAL(2, "手动称量");

    @EnumValue
    private final Integer value;
    private final String name;

    public static WeighMode getByValue(Integer weighType) {
        for (WeighMode e : WeighMode.values()) {
            if (ObjectUtil.equal(e.value, weighType)) {
                return e;
            }
        }
        return null;
    }
}

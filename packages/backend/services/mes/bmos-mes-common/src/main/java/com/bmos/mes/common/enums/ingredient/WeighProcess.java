package com.bmos.mes.common.enums.ingredient;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量阶段
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:24
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WeighProcess implements CommonEnum<Integer> {

    /**
     * 配料称量中
     */
    INGREDIENT(1, "配料称量"),

    /**
     * 余料称量中
     */
    ODD(2, "余料称量"),

    /**
     * 已完成
     */
    FINISHED(3, "已完成");

    @EnumValue
    private final Integer value;

    private final String name;

    public static WeighProcess getByValue(Integer weighProcess) {
        for (WeighProcess e : WeighProcess.values()) {
            if (ObjectUtil.equal(e.value, weighProcess)) {
                return e;
            }
        }
        return null;
    }
}

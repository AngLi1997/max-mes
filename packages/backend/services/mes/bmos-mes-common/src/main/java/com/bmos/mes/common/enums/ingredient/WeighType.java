package com.bmos.mes.common.enums.ingredient;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量类型
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:24
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WeighType implements CommonEnum<Integer> {

    /**
     * 配料称量
     */
    INGREDIENT(1, "配料称量"),

    /**
     * 余料称量
     */
    ODD(2, "余料称量"),

    /**
     * 产出称量
     */
    PRODUCT(3, "产出称量"),

    /**
     * 拆包称量
     */
    PACKAGE(4, "拆包称量"),

    /**
     * 物料称量
     */
    MAIN(5, "物料称量"),

    /**
     * 工单执行
     */
    WEIGH_TICKET_EXECUTE(6, "称量工单执行"),

    /**
     * 称量工单余料称量
     */
    WEIGH_TICKET_ODD_EXECUTE(7, "称量工单余料称量");

    @EnumValue
    private final Integer value;

    private final String name;

    public static WeighType getByValue(Integer weighType) {
        for (WeighType e : WeighType.values()) {
            if (ObjectUtil.equal(e.value, weighType)) {
                return e;
            }
        }
        return null;
    }
}

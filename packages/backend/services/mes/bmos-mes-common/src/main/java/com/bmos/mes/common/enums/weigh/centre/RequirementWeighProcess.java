package com.bmos.mes.common.enums.weigh.centre;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
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
public enum RequirementWeighProcess implements CommonEnum<Integer> {

    /**
     * 物料称量
     */
    MAIN(1, "物料称量"),

    /**
     * 更换批次
     */
    CHANGE_REQUIREMENT(2, "更换批次"),

    /**
     * 余料称量中
     */
    ODD(3, "余料称量"),

    /**
     * 已完成称量
     */
    FINISHED_WEIGH(4, "已完成称量"),

    /**
     * 已完成签名
     */
    FINISHED_SIGN(5, "已完成签名"),

    /**
     * 余料称量更换批次
     */
    ODD_CHANGE_REQUIREMENT(6, "更换批次");

    @EnumValue
    private final Integer value;

    private final String name;

    public static RequirementWeighProcess getByValue(Integer weighProcess) {
        for (RequirementWeighProcess e : RequirementWeighProcess.values()) {
            if (ObjectUtil.equal(e.value, weighProcess)) {
                return e;
            }
        }
        return null;
    }
}

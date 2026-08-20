package com.bmos.mes.common.enums.weigh.centre;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量任务规划类型
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 18:24
 */
@Getter
@AllArgsConstructor
public enum TaskProgramTypeEnum implements CommonEnum<Integer> {

    /**
     * 自动规划
     */
    AUTO(1, "自动规划"),

    /**
     * 手动规划
     */
    MANUAL(2, "手动规划");

    @EnumValue
    private final Integer value;
    private final String name;
}

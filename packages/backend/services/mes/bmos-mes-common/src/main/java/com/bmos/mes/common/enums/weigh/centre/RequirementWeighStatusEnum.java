package com.bmos.mes.common.enums.weigh.centre;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量需求称量状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 18:24
 */
@Getter
@AllArgsConstructor
public enum RequirementWeighStatusEnum implements CommonEnum<Integer> {

    /**
     * 未称量
     */
    PENDING(0, "未称量"),
    /**
     * 称量中
     */
    PROCESSING(1, "称量中"),
    /**
     * 已完成称量
     */
    FINISHED_WEIGH(2, "已完成称量"),
    /**
     * 已完成签名
     */
    FINISHED_SIGN(3, "已完成签名");


    @EnumValue
    private final Integer value;
    private final String name;
}

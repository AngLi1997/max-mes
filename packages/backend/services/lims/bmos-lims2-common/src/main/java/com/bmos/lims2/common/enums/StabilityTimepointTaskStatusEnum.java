package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 稳定性考察计划时间点任务状态枚举
 * 对应详情页矩阵单元格状态
 */
@AllArgsConstructor
@Getter
public enum StabilityTimepointTaskStatusEnum implements KeyValueEnum<String> {

    /**
     * 未开始 — 时间点尚未到达
     */
    NOT_STARTED("NOT_STARTED", "未开始"),

    /**
     * 待取样 — 时间点已到，等待取样
     */
    WAITING_SAMPLE("WAITING_SAMPLE", "待取样"),

    /**
     * 进行中 — 已取样并创建检验单，检验中
     */
    IN_PROGRESS("IN_PROGRESS", "检验中"),

    /**
     * 检验完成
     */
    COMPLETED("COMPLETED", "检验完成"),

    TERMINATED("TERMINATED", "已终止");


    @EnumValue
    private final String value;

    private final String name;
}

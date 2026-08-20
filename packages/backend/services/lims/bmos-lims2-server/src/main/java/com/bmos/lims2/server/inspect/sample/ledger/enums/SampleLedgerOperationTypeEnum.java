package com.bmos.lims2.server.inspect.sample.ledger.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 样品台账操作类型
 * @Author: yigaohui
 * @Date: 2025/09/10 10:30
 */
@Getter
@AllArgsConstructor
public enum SampleLedgerOperationTypeEnum implements KeyValueEnum<String> {
    GENERATED("GENERATED", "生成"),
    SAMPLED("SAMPLED", "取样"),
    RECEIVED("RECEIVED", "接收"),
    DIVIDED("DIVIDED", "分样"),
    COLLECTED("COLLECTED", "领取"),
    RECYCLED("RECYCLED", "回收"),
    PROCESSED("PROCESSED", "处理"),
    DISCARDED("DISCARDED", "弃用");

    @EnumValue
    private final String value;
    private final String name;
}



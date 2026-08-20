package com.bmos.mes.service.lotsummary.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 摘要数据类型
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 15:08
 */
@AllArgsConstructor
@Getter
public enum LotSummaryItemType implements CommonEnum<String> {

    DATASET_POINT("DATASET_POINT", "数据点");

    @EnumValue
    private final String value;

    private final String name;
}

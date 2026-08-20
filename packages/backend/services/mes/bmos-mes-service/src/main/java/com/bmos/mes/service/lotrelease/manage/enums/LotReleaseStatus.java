package com.bmos.mes.service.lotrelease.manage.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 批签发状态
 * @author liang
 * @version 1.0.0
 * @date 2024/8/13 15:08
 */
@AllArgsConstructor
@Getter
public enum LotReleaseStatus implements CommonEnum<String> {

    EDIT("EDIT", "编辑"),

    PROCESSING("PROCESSING", "审批中"),

    EFFECTIVE("EFFECTIVE", "生效"),

    SCRAPED("SCRAPED", "作废");

    @EnumValue
    private final String value;

    private final String name;
}

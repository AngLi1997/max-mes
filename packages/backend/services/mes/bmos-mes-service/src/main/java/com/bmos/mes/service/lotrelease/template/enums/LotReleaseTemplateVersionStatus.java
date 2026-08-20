package com.bmos.mes.service.lotrelease.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 批签发模板版本状态
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 14:44
 */
@AllArgsConstructor
@Getter
public enum LotReleaseTemplateVersionStatus implements CommonEnum<String> {

    EDIT("EDIT", "编辑"),
    MAKE_SURE("MAKE_SURE", "确认"),
    SCRAP("SCRAP", "作废");

    @EnumValue
    private final String value;

    private final String name;
}

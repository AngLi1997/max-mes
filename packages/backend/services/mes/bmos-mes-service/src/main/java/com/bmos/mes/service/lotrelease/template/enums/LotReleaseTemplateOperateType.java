package com.bmos.mes.service.lotrelease.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 14:50
 */
@AllArgsConstructor
@Getter
public enum LotReleaseTemplateOperateType implements CommonEnum<String> {

    CREATE("CREATE", "新增模板"),

    CREATE_VERSION("CREATE_VERSION", "新增版本"),

    UPLOAD("UPLOAD", "上传"),

    DOWNLOAD("DOWNLOAD", "下载"),

    VALIDATE("VALIDATE", "验证"),

    MAKE_DEFAULT("MAKE_DEFAULT", "设为默认"),

    MAKE_SURE("MAKE_SURE", "确认"),

    SCRAP("SCRAP", "作废");

    @EnumValue
    private final String value;

    private final String name;
}

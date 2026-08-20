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
public enum LotReleaseOperateType implements CommonEnum<String> {

    GENERATE("GENERATE", "生成批签发"),
    SCRAP("SCRAP", "作废"),
    DOWNLOAD("DOWNLOAD", "下载"),
    NOT_PASS("NOT_PASS", "审核不通过"),
    PASS("PASS", "审核通过"),
    SUBMIT("SUBMIT", "提交审核"),
    UPLOAD("UPLOAD", "上传"),
    RE_GENERATE("RE_GENERATE", "重新生成");

    @EnumValue
    private final String value;

    private final String name;
}

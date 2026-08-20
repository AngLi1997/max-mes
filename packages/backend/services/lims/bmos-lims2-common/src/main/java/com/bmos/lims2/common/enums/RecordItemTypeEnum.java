package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RecordItemTypeEnum implements CommonEnum<String> {

    HEADER_TYPE("herder", "页眉"),
    FOOTER_TYPE("footer", "页脚"),
    CONTENT("content", "内容");

    @EnumValue
    private String type;

    private String value;

    @Override
    public String getName() {
        return this.type;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}

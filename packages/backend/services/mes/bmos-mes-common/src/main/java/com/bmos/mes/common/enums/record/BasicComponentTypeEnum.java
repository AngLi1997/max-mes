package com.bmos.mes.common.enums.record;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BasicComponentTypeEnum implements CommonEnum<String> {

    TEXT("TEXT", "文字"),
    NUMBER("NUMBER", "数字"),
    RADIO("RADIO", "单选"),
    CHECKBOX("CHECKBOX", "多选"),
    SELECT("SELECT", "选择"),
    DATE("DATE", "日期"),
    TIME("TIME", "时间"),
    ATTACHMENT("ATTACHMENT", "附件"),
    SUBMIT_SIGN("SUBMIT_SIGN", "提交"),
    REVIEW_SIGN("REVIEW_SIGN", "复核"),
    PHOTO("PHOTO", "拍照上传组件"),
    ;

    @EnumValue
    private final String value;

    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BasicComponentTypeEnum getEnumByValue(String value) {
        return Arrays.stream(BasicComponentTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}

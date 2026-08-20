package com.bmos.mes.service.dataset.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据点组装数据类型
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 15:25
 */
@Getter
@AllArgsConstructor
public enum DatasetTransValueDataType implements CommonEnum<String> {

    /**
     * ERROR
     */
    ERROR("ERROR", "错误"),

    /**
     * 文本
     */
    TEXT("TEXT", "文本"),

    /**
     * 图片
     */
    IMAGE("IMAGE", "图片"),

    /**
     * excel引用
     */
    EXCEL("EXCEL", "Excel引用"),

    /**
     * 选择框
     */
    CHECKBOX("CHECKBOX", "选择框"),

    /**
     * 拍照组件
     */
    TAKE_PHOTO("TAKE_PHOTO", "拍照组件"),

    /**
     * 图片下含图注
     */
    IMAGE_CAPTION("IMAGE_CAPTION", "图片(底含有图注)"),
    ;

    @EnumValue
    @JsonValue
    private final String value;

    private final String name;
}

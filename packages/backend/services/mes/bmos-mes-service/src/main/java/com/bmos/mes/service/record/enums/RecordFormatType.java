package com.bmos.mes.service.record.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 记录格式化类型
 */
@AllArgsConstructor
@Getter
public enum RecordFormatType implements CommonEnum<String> {

    HYPER_LINK("超链接","HYPER_LINK", "超链接将自动替换为文本，请检查"),
    BOOKMARKS("书签","BOOKMARKS", "书签无法保留，请检查"),
    HIDDEN_TEXT("隐藏文本","HIDDEN_TEXT", "隐藏文本无法保留，请检查"),
    DISTRACT_TEXT("分散对齐文本","DISTRACT_TEXT", "分散对齐无法保留，自动优化为左对齐"),
    TEXT_WRAPPING("文字环绕","TEXT_WRAPPING", "文字环绕无法保留，请手动优化"),
    TABLE_TEXT_ROTATION("表格文字方向","TABLE_TEXT_ROTATION", "文字方向无法保留，请手动优化");

    private final String name;

    @EnumValue
    private final String value;

    private final String msg;
}

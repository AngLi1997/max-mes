package com.bmos.mes.common.enums.plan;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;

/**
 * 操作类型美剧
 * 对应国际化的code：8301xx
 */
@AllArgsConstructor
public enum TemplateVersionOperateTypeEnum implements CommonEnum<Integer> {

    /**
     * 新增
     */
    ADD(830101,"新增"),

    /**
     * 上传
     */
    UPLOAD(830102, "上传"),

    /**
     * 删除
     */
    DELETE(830103, "删除"),

    /**
     * 确认
     */
    CONFIRM(830104, "确认"),

    /**
     * 作废
     */
    SCRAP(830105, "作废"),

    /**
     * 设为默认
     */
    NORMAL(830106, "设为默认"),
    /**
     * 下载
     */
    DOWNLOAD(830107, "下载")
    ;

    private Integer value;

    private String name;


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static TemplateVersionOperateTypeEnum getEnumByValue(Integer value) {
        for (TemplateVersionOperateTypeEnum typeEnum : TemplateVersionOperateTypeEnum.values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}

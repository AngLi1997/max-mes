package com.bmos.mes.common.enums.plan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;

/**
 * 模板版本状态枚举
 * 对应国际化的code：8302xx
 */
@AllArgsConstructor
public enum TemplateVersionStatusEnum implements CommonEnum<Integer> {

    EDIT(830201,"编辑"),
    CONFIRM(830202,"确认"),
    SCRAP(830203 ,"作废")
    ;
    @EnumValue
    private Integer value;

    private String name;


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public static TemplateVersionStatusEnum getEnumByValue(Integer value) {
        for (TemplateVersionStatusEnum item : TemplateVersionStatusEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}

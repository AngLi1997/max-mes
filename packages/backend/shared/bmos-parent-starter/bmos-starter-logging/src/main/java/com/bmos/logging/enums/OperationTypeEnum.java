package com.bmos.logging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationTypeEnum implements CommonEnum<Integer> {
    INSERT(0, "新增"),
    UPDATE(1, "编辑"),
    DELETE(2, "删除"),
    EXPORT(3, "导出"),
    RELATE(4, "关联"),
    PROCESS(5, "审核"),

    ;
    @EnumValue
    private final Integer value;
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}

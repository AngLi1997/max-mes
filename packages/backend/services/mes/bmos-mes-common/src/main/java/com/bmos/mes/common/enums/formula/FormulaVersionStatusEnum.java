package com.bmos.mes.common.enums.formula;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物料数量类型
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FormulaVersionStatusEnum implements CommonEnum<Integer> {

    EDIT(0,"编辑"),
    APPROVAL(1,"审批"),
    CONFIRM(2,"确认"),
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

package com.bmos.mes.common.enums.release;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum VersionStatusEnum implements CommonEnum<Integer> {
    EDIT(0,"编辑中"),
    ENABLED(1,"启用中"),
    CONFIRMED(2,"已确认")
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

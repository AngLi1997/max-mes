package com.bmos.lims2.common.enums;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryInfoTypeEnum implements KeyValueEnum<Integer> {
    INSPECTION(3,"检品信息", 3),
    ;

    private final Integer value;
    private final String name;
    private final Integer childCode;

    public static String getNameByValue(Integer code){
        CategoryInfoTypeEnum[] values = CategoryInfoTypeEnum.values();
        for (CategoryInfoTypeEnum type : values) {
            if(ObjectUtil.equal(code,type.value)){
                return type.name;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public Integer getChildCode() {
        return childCode;
    }
}

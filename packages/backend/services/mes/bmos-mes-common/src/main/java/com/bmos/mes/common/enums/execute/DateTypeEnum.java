package com.bmos.mes.common.enums.execute;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum DateTypeEnum implements CommonEnum<String> {

    SECOND("second",60L,""),
    MINUTE("minute",60L, "* 60"),
    TIME("hour",24L,"* 60 * 60"),
    DAY("day",365L,"* 24 * 60 * 60");





    private final String name;

    private final Long thresholdValue;

    private final String formula;


    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static List<String> getNames() {
        return Arrays.stream(DateTypeEnum.values())
                .map(DateTypeEnum::getName)
                .collect(Collectors.toList());
    }

    public static String getFormula(String name) {
        return Arrays.stream(DateTypeEnum.values())
                .filter(item-> item.getName().equals(name))
                .findFirst()
                .get()
                .getFormula();
    }

    public static Boolean thresholdCompare(String value,Long oldValue){
        DateTypeEnum dateType = Arrays.stream(DateTypeEnum.values())
                .filter(dateTypeEnum -> dateTypeEnum.getName().equals(value))
                .findFirst()
                .orElse(null);
        if (ObjectUtils.isNotEmpty(dateType) && oldValue >= dateType.getThresholdValue()){
            return true;
        }
        return false;
    }
}

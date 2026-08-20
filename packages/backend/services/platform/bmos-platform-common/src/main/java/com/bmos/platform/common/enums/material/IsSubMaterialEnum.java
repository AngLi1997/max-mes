package com.bmos.platform.common.enums.material;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IsSubMaterialEnum implements KeyValueEnum<Boolean> {

    TRUE(true, "是"),
    FALSE(false, "否")
    ;
    @EnumValue
    private final Boolean value;
    private final String name;

    public static IsSubMaterialEnum findByName(String name) {
        for (IsSubMaterialEnum typeEnum : IsSubMaterialEnum.values()) {
            if (typeEnum.getName().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }

    public static IsSubMaterialEnum findByValue(Boolean value) {
        for (IsSubMaterialEnum typeEnum : IsSubMaterialEnum.values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }

    public static List<String> getNameList(){
        return Arrays.asList(IsSubMaterialEnum.values())
                .stream()
                .map(IsSubMaterialEnum::getName)
                .collect(Collectors.toList());
    }

}

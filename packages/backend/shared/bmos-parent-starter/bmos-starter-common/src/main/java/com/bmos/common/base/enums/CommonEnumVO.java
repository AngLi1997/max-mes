package com.bmos.common.base.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonEnumVO<T> {
    @ApiModelProperty("枚举值")
    private T value;

    @ApiModelProperty("枚举标签")
    private String label;

    @ApiModelProperty("枚举标签 与label相同，为了进行兼容")
    private String name;

    public CommonEnumVO(T value, String message, String name) {
        this.value = value;
        this.label = message;
        this.name = name;
    }
}

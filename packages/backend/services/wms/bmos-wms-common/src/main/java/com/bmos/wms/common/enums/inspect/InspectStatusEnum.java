package com.bmos.wms.common.enums.inspect;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 检验单状态（与 MES InspectStatusEnum 同语义）
 *
 * <p>1=请验中（PENDING），2=已完成（FINISHED），3=已退回（REJECTED）
 */
@Getter
@AllArgsConstructor
public enum InspectStatusEnum implements KeyValueEnum<Integer> {

    PENDING("检验中", 1),
    FINISHED("已完成", 2),
    REJECTED("已退回", 3),

    ;

    private final String name;

    @EnumValue
    private final Integer value;
}

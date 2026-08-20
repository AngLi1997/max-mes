package com.bmos.mes.service.equipment.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yigaohui
 * @date 2024/4/24
 **/
@AllArgsConstructor
@Getter
public enum EquipmentAcquisitionComponentInputTypeEnum implements CommonEnum<String> {

    MANUAL("Manual", "手动录入"),
    ACQUISITION("ACQUISITION", "采集");

    @EnumValue
    private final String value;
    private final String name;
}

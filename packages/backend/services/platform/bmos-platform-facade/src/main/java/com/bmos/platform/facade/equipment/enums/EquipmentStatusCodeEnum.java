package com.bmos.platform.facade.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备状态
 */
@Getter
@AllArgsConstructor
public enum EquipmentStatusCodeEnum implements CommonEnum<Integer> {
    /**
     * 可用
     */
    AVAILABLE(1, "可用", EquipmentStatusOperateEnum.OPERATE.getCode(), EquipmentStatusLogEnum.AVAILABLE.getCode()),
    /**
     * 不可用
     */
    UNAVAILABLE(2, "不可用", EquipmentStatusOperateEnum.OPERATE.getCode(), EquipmentStatusLogEnum.UNAVAILABLE.getCode()),
    /**
     * 占用
     */
    OCCUPY(3, "占用", EquipmentStatusOperateEnum.OPERATE.getCode(), EquipmentStatusLogEnum.OCCUPY.getCode()),
    /**
     * 故障
     */
    FAULT(4, "故障", EquipmentStatusOperateEnum.OPERATE.getCode(), EquipmentStatusLogEnum.FAULT.getCode()),

    ;

    @EnumValue
    private Integer code;

    private String desc;

    private String operateCode;

    private String statusLogCode;

    public static EquipmentStatusCodeEnum getByCode(Integer code) {
        for (EquipmentStatusCodeEnum statusCodeEnum : EquipmentStatusCodeEnum.values()) {
            if (statusCodeEnum.getCode().equals(code)) {
                return statusCodeEnum;
            }
        }
        return null;
    }


    @Override
    public String getName() {
        return desc;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}

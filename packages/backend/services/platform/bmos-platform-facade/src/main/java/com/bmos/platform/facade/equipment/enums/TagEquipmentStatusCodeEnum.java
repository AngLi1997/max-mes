package com.bmos.platform.facade.equipment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagEquipmentStatusCodeEnum {

    /**
     * 清洁
     */
    CLEAN("CLEAN_001",  "清洁", EquipmentStatusOperateEnum.CLEAN.getCode(), EquipmentStatusLogEnum.ALREADY_CALIBRATION.getCode(), EquipmentStatusLogEnum.NOT_CLEAN.getCode()),
     /**
     * 消毒
     */
    DISINFECT("DISINFECT_002", "消毒", EquipmentStatusOperateEnum.DISINFECTION.getCode(), EquipmentStatusLogEnum.ALREADY_DISINFECT.getCode(), EquipmentStatusLogEnum.NOT_DISINFECT.getCode()),
    /**
     * 校准
     */
    CALIBRATION("CALIBRATION_003", "校准",EquipmentStatusOperateEnum.CALIBRATION.getCode(), EquipmentStatusLogEnum.ALREADY_CALIBRATION.getCode(), EquipmentStatusLogEnum.NOT_CALIBRATION.getCode()),

    ;
    private String code;

    private String name;

    private String operateCode;

    private String finishName;

    private String noFinishName;

    public static TagEquipmentStatusCodeEnum getByCode(String code) {
        for (TagEquipmentStatusCodeEnum statusCodeEnum : TagEquipmentStatusCodeEnum.values()) {
            if (statusCodeEnum.getCode().equals(code)) {
                return statusCodeEnum;
            }
        }
        return null;
    }

}

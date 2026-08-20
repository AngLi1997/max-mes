package com.bmos.platform.service.equipment.service.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备标签下的某个设备状态信息
 */
@Getter
@Setter
public class EquipmentTagStatusData {

    /**
     * 设备属性id
     */
    private Long id;

    /**
     * 设备状态code
     */
    private String code;

    /**
     * 设备状态名称
     */
    private String name;

    /**
     * 是否内置
     */
    private Boolean embed;

    /**
     * 属性值/默认效期
     */
    private String value;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 是否完成该状态
     */
    private Boolean finishStatus;

    /**
     * 有效期
     */
    private LocalDateTime expireDateTime;

}

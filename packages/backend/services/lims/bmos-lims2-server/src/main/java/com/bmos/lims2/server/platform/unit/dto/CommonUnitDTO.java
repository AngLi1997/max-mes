package com.bmos.lims2.server.platform.unit.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 公共单位
 */
@Getter
@Setter
public class CommonUnitDTO {

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 是否为扩展单位
     */
    private Boolean extend;
}
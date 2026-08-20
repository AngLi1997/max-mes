package com.bmos.lims2.server.platform.unit.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/22 16:22
 */
@Getter
@Setter
public class UnitQueryDTO {

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 是否为扩展单位
     */
    private Boolean isExtend;
}

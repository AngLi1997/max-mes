package com.bmos.platform.service.unit.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/22 16:22
 */
@Data
public class UnitQueryDTO {

    /**
     * 单位id
     */
    @NotNull
    private Long unitId;

    /**
     * 是否为扩展单位
     */
    @NotNull
    private Boolean isExtend;
}

package com.bmos.lims2.server.platform.material.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 物料取消注册DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnregisterMaterialDTO {

    /**
     * 平台名称 必传
     */
    private String platformName;

    /**
     * 子业务码 必传
     */
    private Integer childCode;

    /**
     * 物料id 必传
     */
    private Long materialId;


}

package com.bmos.wms.service.cargo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 物料信息扩展信息DTO
 */
@Getter
@Setter
public class MaterialExpandInfoDTO {

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 生产商
     */
    private String producer;

    /**
     * 级别
     */
    private String level;

    /**
     * 预置皮重
     */
    private Double presetTareWeight;

    /**
     * 剂型
     */
    private String formulation;

    /**
     * 默认效期
     */
    private Integer defaultExpiration;

    /**
     * 默认效期时间单位
     */
    private Integer timeUnit;

}

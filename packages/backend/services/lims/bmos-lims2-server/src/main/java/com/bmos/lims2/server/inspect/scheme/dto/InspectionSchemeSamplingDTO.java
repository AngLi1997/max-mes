package com.bmos.lims2.server.inspect.scheme.dto;

import lombok.Data;

/**
 * 检验方案取样量配置DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeSamplingDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的方案ID
     */
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    private Long versionId;

    /**
     * 检验项目ID，为NULL时表示整体取样
     */
    private Long inspectItemId;

    private String inspectItemName;

    private String inspectItemCode;

    /**
     * 取样量
     */
    private String samplingAmount;

    /**
     * 取样单位
     */
    private String samplingUnit;

    /**
     * 取样份数
     */
    private Integer samplingCount;
} 
package com.bmos.lims2.server.inspect.scheme.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 检验方案取样量配置保存请求DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeSamplingSaveDTO {


    /**
     * 关联的方案id
     */
    private Long schemeId;

    /**
     * 关联的方案版本id
     */
    private Long versionId;

    /**
     * 检验项目ID，为NULL时表示整体取样
     */
    private Long inspectItemId;

    /**
     * 取样量
     */
    @NotNull(message = "取样量不能为空")
    private String samplingAmount;

    /**
     * 取样单位
     */
    @NotNull(message = "取样单位不能为空")
    private String samplingUnit;

    /**
     * 取样份数
     */
    @NotNull(message = "取样份数不能为空")
    @Positive(message = "取样份数必须大于0")
    private Integer samplingCount;
} 
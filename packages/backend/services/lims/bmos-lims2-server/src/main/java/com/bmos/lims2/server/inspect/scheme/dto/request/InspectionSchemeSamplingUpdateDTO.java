package com.bmos.lims2.server.inspect.scheme.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @Description: 检验方案取样配置编辑保存请求DTO
 * @Author: yigaohui
 * @Date: 2025/01/21 17:05
 */
@Data
public class InspectionSchemeSamplingUpdateDTO {

    /**
     * 方案ID
     */
    private Long schemeId;

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 取样配置ID（修改时需要）
     */
    private Long samplingConfigId;

    /**
     * 检验项目ID
     */
    private Long inspectItemId;

    /**
     * 取样数量
     */
    private String samplingAmount;

    /**
     * 取样单位
     */
    private String samplingUnit;

    /**
     * 取样次数
     */
    private Integer samplingCount;
}

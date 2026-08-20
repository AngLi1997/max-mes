package com.bmos.lims2.server.stability.scheme.dto.response;

import lombok.Data;

/**
 * 稳定性方案基础信息保存响应DTO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
public class StabilitySchemeBasicSaveRespDTO {

    /**
     * 方案ID
     */
    private Long schemeId;

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 方案名称
     */
    private String schemeName;

    /**
     * 方案编码
     */
    private String schemeCode;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 是否新增
     */
    private Boolean isNew;
}

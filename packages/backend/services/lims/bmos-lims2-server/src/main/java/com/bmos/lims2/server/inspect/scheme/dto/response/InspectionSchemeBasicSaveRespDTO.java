package com.bmos.lims2.server.inspect.scheme.dto.response;

import lombok.Data;

/**
 * @Description: 检验方案基础信息保存响应DTO
 * @Author: yigaohui
 * @Date: 2025/01/21 16:05
 */
@Data
public class InspectionSchemeBasicSaveRespDTO {

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
    private String name;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本描述
     */
    private String description;
}
package com.bmos.lims2.server.inspect.scheme.dto.request;

import lombok.Data;

/**
 * @Description: 检验方案基础信息保存请求DTO
 * @Author: yigaohui
 * @Date: 2025/01/21 16:00
 */
@Data
public class InspectionSchemeBasicSaveDTO {



    /**
     * 方案ID（编辑时必填）
     */
    private Long schemeId;

    /**
     * 版本ID（编辑时必填）
     */
    private Long versionId;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 父版本ID（基于现有版本创建时必填）
     */
    private Long parentVersionId;

    /**
     * 物料信息
     */
    private MaterialInfoDTO material;

    /**
     * 物料信息DTO
     */
    @Data
    public static class MaterialInfoDTO {
        /**
         * 物料ID
         */
        private Long materialId;

        /**
         * 物料名称
         */
        private String materialName;

        /**
         * 物料编码
         */
        private String materialCode;
    }
}

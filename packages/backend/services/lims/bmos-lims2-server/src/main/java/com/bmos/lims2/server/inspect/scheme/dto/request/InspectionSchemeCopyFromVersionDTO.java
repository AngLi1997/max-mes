package com.bmos.lims2.server.inspect.scheme.dto.request;

import lombok.Data;

/**
 * @Description: 新增方案-从现有方案版本复制 请求参数
 * @Author: yigaohui
 * @Date: 2025/09/03 10:30
 */
@Data
public class InspectionSchemeCopyFromVersionDTO {

    /**
     * 源方案版本ID（从该版本复制配置和物料/包信息）
     */
    private Long sourceVersionId;

    /**
     * 新方案名称
     */
    private String newSchemeName;

    /**
     * 新方案的首个版本号
     */
    private String newVersionNo;

    /**
     * 版本描述（可选）
     */
    private String description;

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



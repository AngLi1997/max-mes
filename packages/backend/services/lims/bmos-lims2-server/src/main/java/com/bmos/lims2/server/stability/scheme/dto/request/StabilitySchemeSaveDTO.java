package com.bmos.lims2.server.stability.scheme.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性方案保存请求DTO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
public class StabilitySchemeSaveDTO {

    /**
     * 方案ID（新增时为null）
     */
    private Long id;

    /**
     * 版本ID（编辑时必填）
     */
    private Long versionId;

    /**
     * 方案名称
     */
    @NotBlank(message = "方案名称不能为空")
    private String name;

    /**
     * 方案编码
     */
    @NotBlank(message = "方案编码不能为空")
    private String code;

    /**
     * 版本号
     */
    @NotBlank(message = "版本号不能为空")
    private String versionNo;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 检品信息
     */
    @NotNull(message = "检品信息不能为空")
    private MaterialInfoDTO material;

    /**
     * 父版本ID（复制版本时使用）
     */
    private Long parentVersionId;

    /**
     * 数据权限部门ID集合
     */
    private List<Long> deptIds;

    /**
     * 检品信息DTO
     */
    @Data
    public static class MaterialInfoDTO {
        /**
         * 检品ID
         */
        @NotNull(message = "检品ID不能为空")
        private Long materialId;

        /**
         * 检品名称
         */
        private String materialName;

        /**
         * 检品编码
         */
        private String materialCode;
    }
}

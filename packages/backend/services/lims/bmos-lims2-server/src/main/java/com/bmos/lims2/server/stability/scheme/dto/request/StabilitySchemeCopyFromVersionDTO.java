package com.bmos.lims2.server.stability.scheme.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 新增稳定性方案（从现有方案版本复制）DTO
 */
@Data
public class StabilitySchemeCopyFromVersionDTO {

    /** 源方案版本ID */
    private Long sourceVersionId;

    /** 新方案名称 */
    private String newSchemeName;

    /** 新方案编码 */
    private String newSchemeCode;

    /** 新方案的首个版本号 */
    private String newVersionNo;

    /** 版本描述 */
    private String description;

    /** 数据权限-部门ID列表 */
    private List<Long> deptIds;

    /** 物料信息 */
    private MaterialInfoDTO material;

    @Data
    public static class MaterialInfoDTO {
        private Long materialId;
        private String materialName;
        private String materialCode;
    }
}

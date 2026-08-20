package com.bmos.lims2.server.stability.scheme.dto.response;

import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性方案版本完整配置响应DTO（用于编辑和查看明细）
 */
@Data
public class StabilitySchemeVersionFullConfigDTO {

    private Long id;
    private Long schemeId;
    private String schemeCode;
    private String schemeName;
    private String description;
    private LocalDate effectiveDate;
    private String versionNo;
    private StabilitySchemeVersionStatusEnum status;
    private Long parentVersionId;
    private String parentVersionNo;
    private LocalDateTime createTime;

    private MaterialInfoDTO material;
    private List<StabilitySchemeItemDTO> inspectionItems;
    private List<StabilitySchemePlanDTO> plans;

    @Data
    public static class MaterialInfoDTO {
        private Long materialId;
        private String materialName;
        private String materialCode;
    }
}

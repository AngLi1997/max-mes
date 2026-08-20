package com.bmos.lims2.server.stability.scheme.dto.response;

import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性方案版本响应DTO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
public class StabilitySchemeVersionDTO {

    /**
     * 版本ID
     */
    private Long id;

    /**
     * 方案ID
     */
    private Long schemeId;

    /**
     * 方案名称
     */
    private String schemeName;

    private String schemeCode;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本状态
     */
    private StabilitySchemeVersionStatusEnum status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 生效日期
     */
    private LocalDate effectiveDate;

    /**
     * 父版本ID
     */
    private Long parentVersionId;

    /**
     * 父版本号
     */
    private String parentVersionNo;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检品名称
     */
    private String materialName;

    /**
     * 检品编码
     */
    private String materialCode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 数据权限部门ID列表
     */
    private List<Long> deptIds;

    /**
     * 审批流程实例ID
     */
    private String processInstanceId;
}

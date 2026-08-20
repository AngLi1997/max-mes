package com.bmos.lims2.server.stability.scheme.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性方案响应DTO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
public class StabilitySchemeDTO {

    /**
     * 方案ID
     */
    private Long id;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 方案编码
     */
    private String code;

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
     * 当前生效版本号
     */
    private String activeVersionNo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 数据权限部门ID集合
     */
    private List<Long> deptIds;
}

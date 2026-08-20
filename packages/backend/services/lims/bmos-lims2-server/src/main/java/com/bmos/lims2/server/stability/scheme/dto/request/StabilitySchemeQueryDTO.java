package com.bmos.lims2.server.stability.scheme.dto.request;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import java.util.List;

/**
 * 稳定性方案查询请求DTO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StabilitySchemeQueryDTO extends BasePage {

    /**
     * 方案名称（模糊查询）
     */
    private String name;

    /**
     * 方案编码（模糊查询）
     */
    private String code;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检品ID集合（由 categoryId 解析后填入）
     */
    private List<Long> materialIds;
}

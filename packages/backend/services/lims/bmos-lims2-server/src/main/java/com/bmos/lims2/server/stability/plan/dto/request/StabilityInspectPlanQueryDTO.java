package com.bmos.lims2.server.stability.plan.dto.request;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性考察计划查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StabilityInspectPlanQueryDTO extends BasePage {

    /**
     * 考察计划编号（模糊查询）
     */
    private String code;

    /**
     * 计划状态
     */
    private String status;

    /**
     * 创建时间起
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建时间止
     */
    private LocalDateTime createTimeEnd;

    /**
     * 检品分类ID（用于左侧树过滤）
     */
    private Long materialCategoryId;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检品ID集合（按物料分类筛选时使用）
     */
    private List<Long> materialIds;
}

package com.bmos.lims2.server.stability.plan.dto.request;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性整体样品分页查询DTO（批次维度）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StabilityOverallSampleQueryDTO extends BasePage {

    /**
     * 稳定性考察计划编号（模糊查询）
     */
    private String planCode;

    /**
     * 批号（模糊查询）
     */
    private String batchNo;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检品ID集合（按物料分类筛选时使用）
     */
    private List<Long> materialIds;

    /**
     * 请验时间起（计划创建时间起，含）
     */
    private LocalDateTime createTimeStart;

    /**
     * 请验时间止（计划创建时间止，含）
     */
    private LocalDateTime createTimeEnd;
}

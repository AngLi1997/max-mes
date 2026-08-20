package com.bmos.lims2.server.stability.plan.dto.response;

import com.bmos.lims2.common.enums.StabilityInspectPlanStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性考察计划详情DTO
 */
@Data
public class StabilityInspectPlanDetailDTO {

    private Long id;

    /**
     * 考察计划编号
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
     * 检品规格
     */
    private String materialSpec;

    /**
     * 稳定性方案ID
     */
    private Long schemeId;

    /**
     * 稳定性方案编码
     */
    private String schemeCode;

    /**
     * 稳定性方案名称
     */
    private String schemeName;

    /**
     * 稳定性方案版本ID
     */
    private Long schemeVersionId;

    /**
     * 稳定性方案版本号
     */
    private String schemeVersionNo;

    /**
     * 计划状态
     */
    private StabilityInspectPlanStatusEnum status;

    /**
     * 开始时间
     */
    private LocalDate startDate;

    /**
     * 计划结束时间
     */
    private LocalDate planEndDate;

    /**
     * 暂停时间
     */
    private LocalDateTime pauseTime;

    /**
     * 暂停操作人姓名
     */
    private String pauseByName;

    /**
     * 暂停理由
     */
    private String pauseReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人ID
     */
    private String createBy;

    /**
     * 创建人名称（格式：姓名-登录名）
     */
    private String createByUserName;

    /**
     * 试验类型名称（多个用逗号隔开，字典翻译后）
     */
    private String experimentTypeNames;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 样品开始接收时间（第一个整体样品的接收时间）
     */
    private LocalDateTime sampleFirstReceiveTime;

    /**
     * 样品结束接收时间（最后一个整体样品的接收时间）
     */
    private LocalDateTime sampleLastReceiveTime;

    /**
     * 批次列表
     */
    private List<BatchDTO> batches;

    /**
     * 详情矩阵（试验类型 × 批次 × 时间点）
     */
    private StabilityInspectPlanMatrixDTO matrix;

    @Data
    public static class BatchDTO {
        private Long id;
        private String batchNo;
        private LocalDate productionDate;
        private LocalDate sampleReceiveDate;
        private Integer sort;
    }
}

package com.bmos.lims2.server.stability.plan.dto.response;

import com.bmos.lims2.common.enums.StabilityInspectPlanStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性考察计划列表DTO
 */
@Data
public class StabilityInspectPlanDTO {

    private Long id;

    /**
     * 考察计划编号
     */
    private String code;

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
     * 稳定性方案名称
     */
    private String schemeName;

    /**
     * 稳定性方案版本号
     */
    private String schemeVersionNo;

    /**
     * 稳定性方案版本ID
     */
    private Long schemeVersionId;

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
     * 创建人ID
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 试验类型名称（多个用逗号隔开，来自方案版本下的检验计划）
     */
    private String experimentTypeNames;
}

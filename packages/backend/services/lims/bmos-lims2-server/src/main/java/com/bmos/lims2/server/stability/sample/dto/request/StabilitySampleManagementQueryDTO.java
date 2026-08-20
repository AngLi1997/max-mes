package com.bmos.lims2.server.stability.sample.dto.request;

import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.mybatis.page.BasePage;
import lombok.Data;

import java.time.LocalDate;

/**
 * 稳定性样品管理分页查询DTO
 */
@Data
public class StabilitySampleManagementQueryDTO extends BasePage {

    /** 检品ID */
    private Long materialId;

    /** 稳定性考察计划编号 */
    private String planCode;

    /** 批号 */
    private String batchNo;

    /** 样品编号 */
    private String sampleNo;

    /** 样品状态 */
    private StabilityPlanSampleStatusEnum status;

    /** 计划结束时间起 */
    private LocalDate planEndDateStart;

    /** 计划结束时间止 */
    private LocalDate planEndDateEnd;
}

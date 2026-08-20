package com.bmos.lims2.web.stability.sample.vo.request;

import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性样品管理分页查询请求VO
 */
@Data
@ApiModel("稳定性样品管理查询请求")
public class StabilitySampleManagementQueryReqVO extends BasePage {

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("稳定性考察计划编号")
    private String planCode;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品状态（RECEIVED/PENDING_DESTROY/DESTROYED）")
    private StabilityPlanSampleStatusEnum status;

    @ApiModelProperty("计划结束时间起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planEndDateStart;

    @ApiModelProperty("计划结束时间止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planEndDateEnd;
}

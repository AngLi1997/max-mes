package com.bmos.lims2.server.inspect.query.service;

import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: 检验单信息（含标志位）
 * @Author: yigaohui
 * @Date: 2025/09/11 10:30
 */
@Getter
@Setter
@ApiModel("检验单信息（含标志位）")
public class OrderInfoDTO {

    @ApiModelProperty("检验单")
    private InspectionOrderDTO order;

    @ApiModelProperty("状态标志位")
    private InspectionDetailDTO.StatusFlags flags;

    @ApiModelProperty("请验开始时间（请验发起时间）")
    private LocalDateTime requestStartTime;

    @ApiModelProperty("请验结束时间（请验确认时间）")
    private LocalDateTime requestEndTime;

    @ApiModelProperty("取样开始时间（请验确认时间）")
    private LocalDateTime samplingStartTime;

    @ApiModelProperty("取样结束时间（最后一个样品接收时间）")
    private LocalDateTime samplingEndTime;

    @ApiModelProperty("检验开始时间（第一条任务分配/领取时间）")
    private LocalDateTime inspectionStartTime;

    @ApiModelProperty("检验结束时间（样品审核通过时间）")
    private LocalDateTime inspectionEndTime;

    @ApiModelProperty("报告开始时间（样品审核通过时间）")
    private LocalDateTime reportStartTime;

    @ApiModelProperty("报告结束时间（报告审批通过时间）")
    private LocalDateTime reportEndTime;

    @ApiModelProperty("稳定性考察计划编号（仅稳定性检验单）")
    private String stabilityPlanCode;

    @ApiModelProperty("稳定性考察方案编码（仅稳定性检验单）")
    private String stabilitySchemeCode;

    @ApiModelProperty("稳定性考察方案名称（仅稳定性检验单）")
    private String stabilitySchemeName;

    @ApiModelProperty("稳定性方案版本号（仅稳定性检验单）")
    private String stabilitySchemeVersionNo;

    @ApiModelProperty("稳定性周期任务计划发起日期（仅稳定性检验单）")
    private LocalDate stabilityPlannedDate;

    @ApiModelProperty("稳定性计划创建人（姓名-登录名，仅稳定性检验单）")
    private String stabilityPlanCreator;

    @ApiModelProperty("稳定性计划备注（仅稳定性检验单）")
    private String stabilityPlanRemark;
}



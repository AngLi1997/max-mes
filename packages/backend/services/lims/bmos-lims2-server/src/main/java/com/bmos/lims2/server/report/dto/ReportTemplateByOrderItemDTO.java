package com.bmos.lims2.server.report.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("首页-按检验单分页查询模板-列表项")
public class ReportTemplateByOrderItemDTO {

    @ApiModelProperty("模板ID")
    private Long templateId;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("有效报告所使用的模板版本ID（按检验单维度）")
    private Long reportTemplateVersionId;

    @ApiModelProperty("有效报告所使用的模板版本号（按检验单维度）")
    private String reportTemplateVersionNo;

    @ApiModelProperty("检验单ID")
    private Long orderId;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("是否存在有效报告")
    private Boolean hasValidReport;

    @ApiModelProperty("报告生成任务ID（最新且有效）")
    private Long reportTaskId;

    @ApiModelProperty("报告编号（最新且有效）")
    private String reportNo;

    @ApiModelProperty("报告生成完成时间（最新且有效）")
    private LocalDateTime reportEndTime;

    @ApiModelProperty("报告是否审批通过（有效）")
    private Boolean reportApproved;

    @ApiModelProperty("报告生成人（最新且有效）")
    private String reportGeneratedBy;

    @ApiModelProperty("报告生成人姓名（最新且有效）")
    private String reportGeneratedByName;

    @ApiModelProperty("报告生效时间/审批通过时间（最新且有效）")
    private LocalDateTime reportApprovalTime;

    @ApiModelProperty("报告路径（用于下载）（最新且有效）")
    private String reportPath;

    public String getReportGeneratedByName() {
        return UserUtils.getUserDisplayName(reportGeneratedBy);
    }
}



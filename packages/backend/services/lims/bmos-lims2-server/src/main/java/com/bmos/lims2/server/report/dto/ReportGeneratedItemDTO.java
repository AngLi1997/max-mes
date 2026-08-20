package com.bmos.lims2.server.report.dto;

import com.bmos.lims2.common.enums.ReportGenerateStatusEnum;
import com.bmos.lims2.common.enums.ReportLifecycleStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 已生成报告-列表项（不分页）
 * @Author: yigaohui
 * @Date: 2025/09/09 00:00
 */
@Getter
@Setter
@ApiModel("已生成报告-列表项")
public class ReportGeneratedItemDTO {

    @ApiModelProperty("生成任务ID")
    private Long taskId;

    @ApiModelProperty("报告编号")
    private String reportNo;

    @ApiModelProperty("报告状态")
    private ReportGenerateStatusEnum status;
    @ApiModelProperty("报告生命周期状态")
    private ReportLifecycleStatusEnum lifecycleStatus;

    @ApiModelProperty("报告备注")
    private String reportRemark;

    @ApiModelProperty("报告模板ID")
    private Long templateId;

    @ApiModelProperty("报告模板版本ID")
    private Long templateVersionId;

    @ApiModelProperty("报告模板版本号")
    private String templateVersionNo;

    @ApiModelProperty("报告模板名称")
    private String templateName;

    @ApiModelProperty("报告文件路径（相对）")
    private String path;

    @ApiModelProperty("生成完成时间")
    private LocalDateTime endTime;

    @ApiModelProperty("报告是否审批通过")
    private Boolean reportApproved;

    @ApiModelProperty("报告审批通过时间")
    private LocalDateTime reportApprovalTime;

    @ApiModelProperty("报告生成人（用户ID）")
    private String generatedBy;

    @ApiModelProperty("报告生成人（用户ID）")
    private String generatedByName;


    @ApiModelProperty("报告生成人姓名")
    public String getGeneratedByName() {
        return UserUtils.getUserDisplayName(generatedBy);
    }

    @ApiModelProperty("流程实例ID")
    private String processInstanceId;


}



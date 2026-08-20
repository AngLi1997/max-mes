package com.bmos.lims2.web.inspect.query.vo.resp;

import com.bmos.lims2.common.enums.ReportLifecycleStatusEnum;
import com.bmos.lims2.common.enums.ReportGenerateStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 报告任务信息
 * @Author: yigaohui
 * @Date: 2025/09/11 11:00
 */
@Getter
@Setter
@ApiModel("报告任务信息")
public class ReportTaskRespVO {

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("模板版本ID")
    private Long templateVersionId;

    @ApiModelProperty("模板ID")
    private Long templateId;

    @ApiModelProperty("模板版本号")
    private String templateVersionNo;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("报告编号")
    private String reportNo;

    @ApiModelProperty("生成状态")
    private ReportGenerateStatusEnum status;

    @ApiModelProperty("生命周期状态")
    private ReportLifecycleStatusEnum lifecycleStatus;

    @ApiModelProperty("存储路径")
    private String path;

    @ApiModelProperty("生成开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("生成结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("是否审批通过")
    private Boolean reportApproved;

    @ApiModelProperty("审批通过时间")
    private LocalDateTime reportApprovalTime;

    @ApiModelProperty("生成备注/校验消息")
    private String reportRemark;

    @ApiModelProperty("审批流程实例ID")
    private String processInstanceId;

    @ApiModelProperty("生成人")
    private String generatedBy;

    @ApiModelProperty("报告生成人")
    private String generatedByName;


    @ApiModelProperty("报告生成人姓名")
    public String getGeneratedByName() {
        return UserUtils.getUserDisplayName(generatedBy);
    }
}



package com.bmos.lims2.server.report.dto;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.common.enums.ReportGenerateStatusEnum;
import com.bmos.lims2.common.enums.ReportLifecycleStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 已生成报告-分页列表项
 * @Author: yigaohui
 * @Date: 2025/09/02 00:00
 */
@Getter
@Setter
@ApiModel("已生成报告-分页列表项")
public class ReportGeneratedDTO {

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

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("请验单状态")
    private InspectionOrderStatusEnum orderStatus;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("生产日期")
    private LocalDateTime productionDate;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("检品单位ID")
    private Long unitId;

    @ApiModelProperty("检品单位名称")
    private String unitName;

    @ApiModelProperty("检验方案版本ID")
    private Long schemeVersionId;

    @ApiModelProperty("检验方案名称")
    private String schemeName;

    @ApiModelProperty("检验方案版本号")
    private String schemeVersionNo;

    @ApiModelProperty("请验单备注")
    private String orderRemark;

    @ApiModelProperty("请验是否完成")
    private Boolean orderFinished;

    @ApiModelProperty("请验完成时间")
    private LocalDateTime orderFinishedTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人名称")
    public String getCreateByName() {
        return UserUtils.getUserDisplayName(createBy);
    }

    @ApiModelProperty("终止原因")
    private String terminateReason;

    @ApiModelProperty("终止时间")
    private LocalDateTime terminateTime;

    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("请验人名称")
    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }

    @ApiModelProperty("请验单模板ID")
    private Long orderTemplateId;

    @ApiModelProperty("请验单模板名称")
    private String orderTemplateName;

    @ApiModelProperty("报告模板ID")
    private Long templateId;

    @ApiModelProperty("报告模板版本ID")
    private Long templateVersionId;

    @ApiModelProperty("报告模板版本号")
    private String templateVersionNo;

    @ApiModelProperty("报告文件路径（相对）")
    private String path;

    @ApiModelProperty("报告是否审批通过")
    private Boolean reportApproved;

    @ApiModelProperty("报告审批通过时间")
    private LocalDateTime reportApprovalTime;

    @ApiModelProperty("报告生成人（用户ID）")
    private String generatedBy;

    @ApiModelProperty("报告生人姓名")
    private String generatedByName;

    @ApiModelProperty("报告生成人姓名")
    public String getGeneratedByName() {
        return UserUtils.getUserDisplayName(generatedBy);
    }

    @ApiModelProperty("生成完成时间")
    private LocalDateTime endTime;

    @ApiModelProperty("自定义字段值列表")
    private java.util.List<CustomFieldValueDTO> customFields;
}



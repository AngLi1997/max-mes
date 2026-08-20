package com.bmos.lims2.server.report.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description: 报告审批待办项（含任务ID与订单展示信息）
 * @Author: yigaohui
 * @Date: 2025/09/03 15:10
 */
@Getter
@Setter
@ApiModel("报告审批待办项")
public class ReportApprovalPendingItemDTO {

    @ApiModelProperty("报告生成任务ID")
    private Long taskId;

    @ApiModelProperty("检验单ID")
    private Long orderId;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("备注")
    private String remark;

    // ============== 报告相关 ==============

    @ApiModelProperty("报告编号")
    private String reportNo;

    @ApiModelProperty("报告生成完成时间")
    private LocalDateTime generateTime;

    @ApiModelProperty("模板版本ID")
    private Long templateVersionId;

    @ApiModelProperty("报告模板ID")
    private Long templateId;

    @ApiModelProperty("报告模板名称")
    private String templateName;

    @ApiModelProperty("报告文件路径")
    private String path;

    @ApiModelProperty("方案版本ID")
    private Long schemeVersionId;

    @ApiModelProperty("报告审批流程实例ID")
    private String reportApprovalProcessInstanceId;

    // ============== 订单/检品补充 ==============

    @ApiModelProperty("请验时间")
    private LocalDateTime inspectionRequestTime;

    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("请验人名称")
    private String requestUserName;

    // ============== 工作流相关（对齐方案审批任务列表） ==============

    @ApiModelProperty("工作流任务ID")
    private String workflowTaskId;

    @ApiModelProperty("流程实例ID")
    private String processInstanceId;

    @ApiModelProperty("当前节点名")
    private String currentNodeName;

    @ApiModelProperty("流程发起时间")
    private LocalDateTime initiateTime;

    @ApiModelProperty("流程发起人")
    private String initiator;

    @ApiModelProperty("流程发起人名称")
    private String initiatorName;

    @ApiModelProperty("部署ID")
    private String deploymentId;

    @ApiModelProperty("执行ID")
    private String executionId;

    @ApiModelProperty("工作流负载")
    private Map<String,Object> payload;

    @ApiModelProperty("业务键")
    private String businessKey;

    public String getInitiatorName() {
        return UserUtils.getUserDisplayName(initiator);
    }
}



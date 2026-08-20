package com.bmos.lims2.web.stability.review.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 稳定性结果审核列表项响应VO
 */
@Getter
@Setter
@ApiModel("稳定性结果审核列表项")
public class StabilityResultReviewRespVO {

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("稳定性考察编号")
    private String planCode;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("检验单状态")
    private String orderStatus;

    @ApiModelProperty("审核流程实例ID")
    private String processInstanceId;

    // ===== 工作流待办字段 =====

    @ApiModelProperty("工作流任务ID（审核通过/退回/不通过需传入）")
    private String taskId;

    @ApiModelProperty("工作流执行实例ID（审核退回需传入）")
    private String executionId;

    @ApiModelProperty("流程定义ID")
    private String deploymentId;

    @ApiModelProperty("当前审核节点名称")
    private String currentNodeName;

    @ApiModelProperty("发起人")
    private String initiator;

    @ApiModelProperty("发起时间")
    private LocalDateTime initiateTime;

    @ApiModelProperty("节点挂载属性")
    private Map<String, Object> payload;
}

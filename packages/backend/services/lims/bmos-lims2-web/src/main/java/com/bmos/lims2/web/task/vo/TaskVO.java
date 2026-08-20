package com.bmos.lims2.web.task.vo;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 任务展示VO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
@ApiModel("任务信息")
public class TaskVO {

    @ApiModelProperty("任务ID")
    private Long id;

    @ApiModelProperty("任务编号")
    private String taskNo;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单编号")
    private String inspectionOrderNo;

    @ApiModelProperty("样品ID")
    private Long sampleId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ApiModelProperty("任务状态")
    private TaskStatusEnum status;

    @ApiModelProperty("任务状态名称")
    private String statusName;

    @ApiModelProperty("任务所有人ID")
    private Long ownerId;

    @ApiModelProperty("任务所有人姓名")
    private String ownerName;

    @ApiModelProperty("分配人ID")
    private Long assignerId;

    @ApiModelProperty("分配人姓名")
    private String assignerName;

    @ApiModelProperty("分配时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assignTime;

    @ApiModelProperty("领取时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime claimTime;

    @ApiModelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTime;

    @ApiModelProperty("终止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime terminateTime;

    @ApiModelProperty("终止原因")
    private String terminateReason;

    @ApiModelProperty("退回原因")
    private String returnReason;

    @ApiModelProperty("审批不通过原因")
    private String rejectReason;

    @ApiModelProperty("预计完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedCompleteTime;

    @ApiModelProperty("优先级")
    private Integer priority;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("请验时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;

    @ApiModelProperty("请验人姓名")
    private String requesterName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

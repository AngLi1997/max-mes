package com.bmos.lims2.server.inspect.entry.dto;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: APP 任务子列表数据对象（两个查询接口统一的子项结构）
 * @Author: yigaohui
 * @Date: 2025/11/27 00:00
 */
@Getter
@Setter
@ApiModel("APP-任务子项")
public class AppTaskEntryItemDTO {

    @ApiModelProperty("任务ID")
    private Long id;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单编号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("请验人名称")
    private String requestUserName;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("方案版本ID")
    private Long schemeVersionId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("方案检验项目配置ID")
    private Long itemConfigId;

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("方案分析项配置ID")
    private Long parameterConfigId;

    @ApiModelProperty("分析项标准规定（standard_rule）")
    private String standardRule;

    @ApiModelProperty("是否可执行")
    private Boolean isExecutable;

    @ApiModelProperty("是否报告显示")
    private Boolean isReportable;

    @ApiModelProperty("任务状态")
    private TaskStatusEnum status;

    @ApiModelProperty("是否判定异常")
    private Boolean abnormal;

    @ApiModelProperty("判定结果")
    private Boolean judgedResult;

    @ApiModelProperty("判定时间")
    private LocalDateTime judgedTime;

    @ApiModelProperty("检验时间")
    private LocalDateTime testTime;

    @ApiModelProperty("是否允许前端录入判定结论（未配置判定条件时为true）")
    private Boolean canInputJudgment;

    @ApiModelProperty("检验人ID")
    private Long ownerId;

    @ApiModelProperty("检验人名称")
    private String ownerName;

    @ApiModelProperty("复核人ID")
    private String reviewedBy;

    @ApiModelProperty("复核人名称")
    private String reviewedByName;

    @ApiModelProperty("复核时间")
    private LocalDateTime reviewedTime;

    @ApiModelProperty("录入时间")
    private LocalDateTime entryTime;

    @ApiModelProperty("数据点列表")
    private List<InspectionEntryRecordDTO> dataPoints;

    @ApiModelProperty("分析方法ID")
    private Long recordId;

    @ApiModelProperty("分析方法版本ID")
    private Long recordVersionId;

    @ApiModelProperty("分析方法名称")
    private String recordName;

    @ApiModelProperty("分析方法编码")
    private String recordCode;

    @ApiModelProperty("分析方法版本号")
    private String recordVersionNo;

    @ApiModelProperty("记录明细ID（record_item_id）")
    private Long recordItemId;

    @ApiModelProperty("执行方式：LIMS/ELN")
    private ExecuteMethodEnum executeMethod;

    @ApiModelProperty("分配人ID")
    private String assignerId;

    @ApiModelProperty("分配人姓名")
    private String assignerName;

    @ApiModelProperty("分配时间")
    private LocalDateTime assignTime;

    @ApiModelProperty("领取时间")
    private LocalDateTime claimTime;

    @ApiModelProperty("完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty("终止时间")
    private LocalDateTime terminateTime;

    @ApiModelProperty("终止原因")
    private String terminateReason;

    @ApiModelProperty("退回原因")
    private String returnReason;

    @ApiModelProperty("审批不通过原因")
    private String rejectReason;

    @ApiModelProperty("复核不通过原因")
    private String reviewRejectReason;

    @ApiModelProperty("备注")
    private String remark;

    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }

    public String getReviewedByName() {
        return UserUtils.getUserDisplayName(reviewedBy);
    }
}


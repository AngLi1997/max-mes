package com.bmos.lims2.server.inspect.entry.dto;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单录入DTO - 用于检验单录入列表展示（母列表）
 * 参照任务分配的InspectionOrderAssignmentDTO结构
 *
 * @author system
 * @since 2025/01/30
 */
@Getter
@Setter
@ApiModel("检验单录入数据对象")
public class InspectionOrderEntryDTO {

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单编号")
    private String orderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("请验人名称")
    private String requestUserName;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("检验方案名称")
    private String schemeName;

    @ApiModelProperty("该检验单下的任务列表")
    private List<AnalysisItemEntryItemDTO> inspectionTasks;

    @ApiModelProperty("请验单自定义字段")
    private List<com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO> customFields;

    @ApiModelProperty("按检验项目分组的任务列表")
    private List<InspectItemTaskGroupDTO> inspectItemTaskGroups;

    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }

    /**
     * 分析项录入项DTO（子列表）
     * 参照任务分配的TaskDTO结构，增加数据点信息
     */
    @Getter
    @Setter
    @ApiModel("分析项录入项数据对象（子列表，与“分析项录入列表查询”的任务子列表字段一致）")
    public static class AnalysisItemEntryItemDTO {

        @ApiModelProperty("任务ID")
        private Long id;

        @ApiModelProperty("任务编号")
        private String taskNo;

        @ApiModelProperty("方案版本ID")
        private Long schemeVersionId;

        @ApiModelProperty("方案ID")
        private Long schemeId;

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

        @ApiModelProperty("方案检验项目配置ID")
        private Long itemConfigId;

        @ApiModelProperty("方案分析项配置ID")
        private Long parameterConfigId;

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

        @ApiModelProperty("分析项标准规定（standard_rule）")
        private String standardRule;

        /**
         * 分析方法ID
         */
        private Long recordId;

        /**
         * 分析方法名称（优先standard，其次version，再次code）
         */
        private String recordName;

        /**
         * 分析方法编码（ELN为batchRecord.code；LIMS为im.code）
         */
        private String recordCode;

        @ApiModelProperty("方法生效版本ID（ELN）")
        private Long recordVersionId;

        @ApiModelProperty("方法项ID（ELN）")
        private Long recordItemId;

        @ApiModelProperty("方法版本号（ELN为batchRecordVersion.version；LIMS为im.version）")
        private String recordVersionNo;

        @ApiModelProperty("复核不通过原因")
        private String reviewRejectReason;

        @ApiModelProperty("审批不通过原因")
        private String rejectReason;

        @ApiModelProperty("执行方式：LIMS/ELN")
        private ExecuteMethodEnum executeMethod;

        public String getReviewedByName() {
            return UserUtils.getUserDisplayName(reviewedBy);
        }
    }

    /**
     * 按检验项目分组的任务DTO（子列表分组）
     */
    @Getter
    @Setter
    @ApiModel("检验项目任务分组数据对象")
    public static class InspectItemTaskGroupDTO {

        @ApiModelProperty("检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("检验项目名称")
        private String inspectItemName;

        @ApiModelProperty("检验项目编码")
        private String inspectItemCode;

        @ApiModelProperty("该检验项目下的任务列表")
        private List<AnalysisItemEntryItemDTO> tasks;
    }
}

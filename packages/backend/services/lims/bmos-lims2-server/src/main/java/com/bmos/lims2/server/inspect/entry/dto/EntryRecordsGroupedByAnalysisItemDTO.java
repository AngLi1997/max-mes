package com.bmos.lims2.server.inspect.entry.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;

/**
 * @Description: 按分析项分组的数据点录入记录DTO
 * @Author: yigaohui
 * @Date: 2025/09/12 11:30
 */
@Getter
@Setter
@ApiModel("按分析项分组的数据点录入记录DTO")
public class EntryRecordsGroupedByAnalysisItemDTO {

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("该分析项下的数据点录入记录")
    private List<InspectionEntryRecordDTO> records;

    @ApiModelProperty("是否可上报")
    private Boolean isReportable;

    @ApiModelProperty("是否可执行")
    private Boolean isExecutable;

    @ApiModelProperty("标准判定规则")
    private String standardRule;


    /**
     * 分析方法ID
     */
    private Long recordId;

    private Long taskId;

    @ApiModelProperty("判定结果")
    private String judgedResult;

    @ApiModelProperty("判定时间")
    private java.time.LocalDateTime judgedTime;

    @ApiModelProperty("复核人")
    private String reviewedBy;

    @ApiModelProperty("复核时间")
    private java.time.LocalDateTime reviewedTime;

    @ApiModelProperty("复核人姓名")
    private String reviewedByName;

    @ApiModelProperty("检验时间")
    private java.time.LocalDateTime testTime;

    @ApiModelProperty("检验人ID")
    private Long ownerId;

    @ApiModelProperty("检验人名称")
    private String ownerName;

    @ApiModelProperty("录入时间")
    private java.time.LocalDateTime entryTime;

    @ApiModelProperty("任务状态")
    private TaskStatusEnum status;

    @ApiModelProperty("是否判定异常")
    private Boolean abnormal;

    @ApiModelProperty("执行方式")
    private ExecuteMethodEnum executeMethod;

    public String getReviewedByName() {
        return UserUtils.getUserDisplayName(reviewedBy);
    }

    public String getOwnerName() {
        return UserUtils.getUserDisplayName(String.valueOf(ownerId));
    }


}



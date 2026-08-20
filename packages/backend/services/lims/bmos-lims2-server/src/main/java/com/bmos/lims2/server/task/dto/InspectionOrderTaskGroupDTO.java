package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单任务分组DTO（子列表）
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class InspectionOrderTaskGroupDTO {


    @ApiModelProperty("任务Id")
    private Long id;

    /**
     * 检验单ID
     */
    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    /**
     * 检验单编号
     */
    @ApiModelProperty("检验单编号")
    private String orderNo;

    /**
     * 委托单位
     */
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


    @ApiModelProperty("请验人名称")
    private String requestUserName;


    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("任务状态")
    private TaskStatusEnum status;

    @ApiModelProperty("检验人名称")
    private String ownerName;

    @ApiModelProperty("检验人ID")
    private Long ownerId;

    @ApiModelProperty("是否配置判定条件")
    private Boolean hasJudgmentCondition;

    /** 方案版本ID（内部使用，用于稳定性任务补全） */
    private Long schemeVersionId;

    /** 分析项配置ID（内部使用，用于稳定性任务判定条件检查） */
    private Long parameterConfigId;

    @ApiModelProperty("退回原因")
    private String returnReason;

    @ApiModelProperty("审批不通过原因")
    private String rejectReason;

    @ApiModelProperty("终止原因")
    private String terminateReason;

    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }

    public String getOwnerName() {
        return UserUtils.getUserDisplayName(ownerId+"");
    }

}
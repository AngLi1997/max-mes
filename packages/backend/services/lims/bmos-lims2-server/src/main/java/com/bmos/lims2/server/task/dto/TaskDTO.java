package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 任务DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class TaskDTO extends BaseDO {

    /**
     * 任务ID
     */
    @ApiModelProperty("任务ID")
    private Long id;

    /**
     * 检验单ID
     */
    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    /**
     * 检验项目ID
     */
    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;
    /**
     * 分析项ID
     */
    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    /**
     * 任务状态
     */
    @ApiModelEnumProperty(value = "任务状态", enumClass = TaskStatusEnum.class)
    private TaskStatusEnum status;

    /**
     * 所有人ID
     */
    @ApiModelProperty("所有人ID")
    private String ownerId;

    /**
     * 所有人姓名
     */
    @ApiModelProperty("所有人姓名")
    private String ownerName;

    /**
     * 实际完成时间
     */
    @ApiModelProperty("实际完成时间")
    private LocalDateTime completeTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 终止原因
     */
    @ApiModelProperty("终止原因")
    private String terminateReason;

    /**
     * 终止时间
     */
    @ApiModelProperty("终止时间")
    private LocalDateTime terminateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("退回原因")
    private String returnReason;

    @ApiModelProperty("审批不通过原因")
    private String rejectReason;

    @ApiModelProperty("是否配置判定条件")
    private Boolean hasJudgmentCondition;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    /** 方案版本ID（内部使用，用于稳定性任务补全 schemeId） */
    private Long schemeVersionId;

    /** 分析项配置ID（内部使用，用于稳定性任务判定条件检查） */
    private Long parameterConfigId;

    public String getOwnerName() {
        return UserUtils.getUserDisplayName(ownerId);
    }
}

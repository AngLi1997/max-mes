package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分析项分配DTO（母列表）
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class AnalysisItemAssignmentDTO {

    /**
     * 分析项ID
     */
    @ApiModelProperty("分析项ID")
    private Long inspectParameterId;

    /**
     * 分析项名称
     */
    @ApiModelProperty("分析项名称")
    private String inspectParameterName;

    /**
     * 分析项编码
     */
    @ApiModelProperty("分析项编码")
    private String inspectParameterCode;


    @ApiModelProperty("检验项ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项名称")
    private String inspectItemName;


    @ApiModelProperty("检验项编码")
    private String inspectItemCode;

    /**
     * 该分析项下的检验单列表（子列表）
     * 默认展开
     */
    @ApiModelProperty("该分析项下的任务列表")
    private List<InspectionOrderTaskGroupDTO> inspectionOrders;
}


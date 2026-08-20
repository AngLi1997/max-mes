package com.bmos.mes.service.dataset.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据点详情vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:13
 */
@Data
@ApiModel("数据点详情vo")
public class DatasetPointVO {

    @ApiModelProperty(value = "数据点id", example = "1")
    private Long id;

    @ApiModelProperty(value = "数据集名称", example = "数据集名称")
    private String name;

    @ApiModelProperty(value = "数据点key(流水号)", example = "1")
    private String datasetPointKey;

    @ApiModelProperty(value = "工步id", example = "1")
    private Long procedureStepId;

    @ApiModelProperty(value = "字段id", example = "1")
    private Long fieldId;

    @ApiModelProperty(value = "组件id", example = "1")
    private Long componentId;

    @ApiModelProperty(value = "组件名称", example = "组件名称")
    private String componentName;

    @ApiModelProperty(value = "组件关联表格最大下标值")
    private Long componentNumber;

    @ApiModelProperty(value = "记录项id", example = "1")
    private Long recordItemId;

    @ApiModelProperty(value = "记录项id", example = "记录项名称")
    private String recordItemName;

    @ApiModelProperty(value = "扩展信息(前端组件信息)", example = "{}")
    private String extra;
}

package com.bmos.mes.service.dataset.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据点创建dto
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 15:07
 */
@Data
@ApiModel("数据点创建dto")
public class DatasetPointCreateDTO {

    @ApiModelProperty(value = "数据点名称", example = "1")
    private String name;

    @ApiModelProperty(value = "工步id", example = "1")
    private Long procedureStepId;

    @ApiModelProperty(value = "字段id", example = "1")
    private Long fieldId;

    @ApiModelProperty(value = "扩展信息(前端组件信息)", example = "{}")
    private String extra;

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
}

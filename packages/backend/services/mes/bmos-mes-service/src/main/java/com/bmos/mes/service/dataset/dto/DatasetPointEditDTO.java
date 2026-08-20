package com.bmos.mes.service.dataset.dto;

import com.bmos.mes.service.dataset.model.DatasetPoint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * 数据点编辑dto
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 15:07
 */
@Data
@ApiModel("数据点编辑dto")
@EqualsAndHashCode(callSuper = true)
public class DatasetPointEditDTO extends DatasetPointEditBaseDTO {

    @ApiModelProperty(value = "数据点名称", example = "1")
    private String name;

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

    @Override
    public void copyToDatasetPoint(DatasetPoint datasetPoint) {
        datasetPoint.setName(this.name);
        datasetPoint.setProcedureStepId(this.procedureStepId);
        datasetPoint.setFieldId(this.fieldId);
        datasetPoint.setExtra(this.extra);
        datasetPoint.setComponentId(this.componentId);
        datasetPoint.setComponentName(this.componentName);
        datasetPoint.setComponentNumber(this.componentNumber);
        datasetPoint.setRecordItemId(this.recordItemId);
        datasetPoint.setRecordItemName(this.recordItemName);
    }

    @Override
    public boolean compare(DatasetPoint datasetPoint) {
        return !Objects.equals(datasetPoint.getName(), this.name)
                || !Objects.equals(datasetPoint.getProcedureStepId(), this.procedureStepId)
                || !Objects.equals(datasetPoint.getFieldId(), this.fieldId)
                || !Objects.equals(datasetPoint.getExtra(), this.extra)
                || !Objects.equals(datasetPoint.getComponentId(), this.componentId)
                || !Objects.equals(datasetPoint.getComponentName(), this.componentName)
                || !Objects.equals(datasetPoint.getComponentNumber(), this.componentNumber)
                || !Objects.equals(datasetPoint.getRecordItemId(), this.recordItemId)
                || !Objects.equals(datasetPoint.getRecordItemName(), this.recordItemName);
    }
}

package com.bmos.mes.service.dataset.dto;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mes.service.dataset.enums.DatasetDynamicReportDataType;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 动态数据填报编辑dto
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:43
 */
@Data
@ApiModel("动态数据填报编辑dto")
@EqualsAndHashCode(callSuper = true)
public class DatasetDynamicReportDataEditDTO extends DatasetPointEditBaseDTO {

    @ApiModelProperty(value = "数据名称", example = "数据名称")
    private String dataName;

    @ApiModelEnumProperty(enumClass = DatasetDynamicReportDataType.class, value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "默认值", example = "默认值")
    private String defaultValue;

    @Override
    public void copyToDatasetPoint(DatasetPoint datasetPoint) {
        datasetPoint.setDynamicDataType(CommonEnum.getEnumByValue(DatasetDynamicReportDataType.class, this.dataType));
        datasetPoint.setName(dataName);
        datasetPoint.setDefaultValue(defaultValue);
    }
}

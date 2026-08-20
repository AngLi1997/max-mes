package com.bmos.mes.service.dataset.vo;

import com.bmos.mes.service.dataset.enums.DatasetDynamicReportDataType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 动态数据填报vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:43
 */
@Data
@ApiModel("动态数据填报vo")
public class DatasetDynamicReportDataVO {

    @ApiModelProperty(value = "动态数据填报id", example = "1")
    private Long id;

    @ApiModelProperty(value = "数据名称", example = "数据名称")
    private String dataName;

    @ApiModelProperty(value = "数据集key(流水号)", example = "1")
    private String datasetPointKey;

    @ApiModelEnumProperty(enumClass = DatasetDynamicReportDataType.class, value = "数据类型")
    private DatasetDynamicReportDataType dynamicDataType;

    @ApiModelProperty(value = "默认值", example = "默认值")
    private String defaultValue;
}

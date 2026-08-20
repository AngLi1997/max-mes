package com.bmos.mes.service.dataset.dto;

import com.bmos.mes.service.dataset.enums.DatasetDynamicReportDataType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 动态数据填报创建dto
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:43
 */
@Data
@ApiModel("动态数据填报创建dto")
public class DatasetDynamicReportDataCreateDTO {

    @ApiModelProperty(value = "数据名称", example = "数据名称")
    private String dataName;

    @ApiModelEnumProperty(enumClass = DatasetDynamicReportDataType.class, value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "默认值", example = "默认值")
    private String defaultValue;
}

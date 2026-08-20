package com.bmos.mes.service.lotrelease.manage.vo;

import com.bmos.mes.service.dataset.enums.DatasetDynamicReportDataType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批签发动态字段数据VO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/30 09:22
 */
@Data
@ApiModel(value = "批签发动态字段数据VO")
public class LotReleaseDynamicReportItemVO {

    @ApiModelProperty(value = "动态数据点名称", example = "1")
    private String name;

    @ApiModelProperty(value = "动态数据点key", example = "1")
    private String datasetPointKey;

    @ApiModelProperty(value = "数据集key", example = "1")
    private String datasetKey;

    @ApiModelEnumProperty(value = "动态填报数据类型", enumClass = DatasetDynamicReportDataType.class)
    private DatasetDynamicReportDataType dynamicDataType;

    @ApiModelProperty(value = "默认值", example = "1")
    private String defaultValue;
}

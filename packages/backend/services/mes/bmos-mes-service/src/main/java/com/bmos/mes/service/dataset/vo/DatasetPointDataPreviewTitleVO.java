package com.bmos.mes.service.dataset.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据点预览标题vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 18:22
 */
@Data
@ApiModel("数据点预览标题vo")
public class DatasetPointDataPreviewTitleVO {

    @ApiModelProperty(value = "数据点预览id", example = "123")
    private Long lotReleaseSummaryItemId;

    @ApiModelProperty(value = "字段标题", example = "1")
    private String name;

    @ApiModelProperty(value = "字段id", example = "123")
    private Long fieldId;

    @ApiModelProperty(value = "工步id", example = "123")
    private Long procedureStepId;
}

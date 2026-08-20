package com.bmos.mes.service.dataset.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据点分页vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:13
 */
@Data
@ApiModel("数据点分页vo")
public class DatasetPointPageVO {

    @ApiModelProperty(value = "数据点id", example = "1")
    private Long id;

    @ApiModelProperty(value = "数据集名称", example = "数据集名称")
    private String name;

    @ApiModelProperty(value = "数据点key(流水号)", example = "1")
    private String datasetPointKey;

    @ApiModelProperty(value = "扩展字段", example = "{}")
    private String extra;
}

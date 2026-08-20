package com.bmos.mes.service.lotsummary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批次摘要详情数据vo
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 14:37
 */
@ApiModel("批次摘要详情数据vo")
@Data
public class LotSummaryDetailItemVO {

    @ApiModelProperty(value = "明细id", example = "1")
    private Long id;

    @ApiModelProperty(value = "数据名称", example = "数据名称")
    private String labelName;

    @ApiModelProperty(value = "数据集id", example = "1")
    private Long datasetId;

    @ApiModelProperty(value = "数据集名称", example = "数据集名称")
    private String datasetName;

    @ApiModelProperty(value = "数据点id", example = "1")
    private Long datasetPointId;

    @ApiModelProperty(value = "数据点名称", example = "数据点名称")
    private String datasetPointName;
}

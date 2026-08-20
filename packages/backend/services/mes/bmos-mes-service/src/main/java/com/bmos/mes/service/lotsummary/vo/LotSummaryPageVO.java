package com.bmos.mes.service.lotsummary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批次摘要分页VO
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:42
 */
@Data
@ApiModel("批次摘要分页VO")
public class LotSummaryPageVO {

    @ApiModelProperty(value = "批次摘要id", example = "1")
    private Long id;

    @ApiModelProperty(value = "摘要名称", example = "摘要名称")
    private String name;

    @ApiModelProperty(value = "产品名称", example = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品编码", example = "产品编码")
    private String productMergeCode;

    @ApiModelProperty(value = "产品规格", example = "产品规格")
    private String productSpecification;

    @ApiModelProperty(value = "工艺名称", example = "工艺名称")
    private String processName;
}

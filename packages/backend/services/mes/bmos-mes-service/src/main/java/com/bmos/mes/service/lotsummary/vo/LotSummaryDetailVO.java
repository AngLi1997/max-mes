package com.bmos.mes.service.lotsummary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 批次摘要详情vo
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:42
 */
@Data
@ApiModel("批次摘要详情vo")
public class LotSummaryDetailVO {

    @ApiModelProperty(value = "批次摘要id", example = "1")
    private Long id;

    @ApiModelProperty(value = "摘要名称", example = "摘要名称")
    private String name;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "产品名称", example = "产品名称")
    private String productName;

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    @ApiModelProperty(value = "工艺名称", example = "工艺名称")
    private String processName;

    @ApiModelProperty("摘要数据")
    private List<LotSummaryDetailItemVO> list;
}

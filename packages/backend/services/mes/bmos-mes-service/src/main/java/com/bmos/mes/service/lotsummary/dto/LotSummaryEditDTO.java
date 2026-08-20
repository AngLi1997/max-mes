package com.bmos.mes.service.lotsummary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 批次摘要编辑DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:36
 */
@Data
@ApiModel("批次摘要编辑DTO")
public class LotSummaryEditDTO {

    @ApiModelProperty(value = "批次摘要id", example = "1")
    private Long id;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "批次摘要名称", example = "批次摘要名称")
    private String name;

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    @ApiModelProperty("摘要数据")
    private List<LotSummaryItemDTO> list;
}

package com.bmos.mes.service.lotsummary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批次摘要生产数据标题数据
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 11:19
 */
@Data
@ApiModel("批次摘要生产数据标题数据")
public class LotSummaryProductDataTitleVO {

    @ApiModelProperty(value = "字段标题", example = "字段标题")
    private String name;

    @ApiModelProperty(value = "字段id", example = "123")
    private Long fieldId;

    @ApiModelProperty(value = "工步id", example = "123")
    private Long procedureStepId;
}

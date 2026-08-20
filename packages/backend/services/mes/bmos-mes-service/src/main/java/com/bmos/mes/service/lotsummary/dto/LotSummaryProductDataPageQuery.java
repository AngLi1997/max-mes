package com.bmos.mes.service.lotsummary.dto;

import com.bmos.mes.service.dataset.dto.DatasetPointDataPreviewPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批次摘要生产数据分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("批次摘要生产数据分页查询参数")
public class LotSummaryProductDataPageQuery extends DatasetPointDataPreviewPageQuery {

    @ApiModelProperty(value = "批次摘要id", example = "1")
    private Long lotSummaryId;
}

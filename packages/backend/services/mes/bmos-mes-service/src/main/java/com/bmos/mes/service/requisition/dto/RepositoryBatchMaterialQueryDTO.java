package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("仓库领料物料件查询DTO")
@Data
public class RepositoryBatchMaterialQueryDTO {

    @ApiModelProperty("领料单id")
    private Long requisitionId;

    @ApiModelProperty("物料批次id")
    private Long receivedBatchId;

    @ApiModelProperty(hidden = true)
    private Long materialId;

}

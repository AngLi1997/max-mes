package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("仓库物料量查询DTO")
@Data
public class RepositoryQuantityQueryDTO {

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("领料单id")
    private Long requisitionPlanId;


}

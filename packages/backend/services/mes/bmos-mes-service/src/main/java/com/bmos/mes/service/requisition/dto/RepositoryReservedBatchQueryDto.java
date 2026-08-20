package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("仓库已预订批次查询DTO")
public class RepositoryReservedBatchQueryDto {

    @ApiModelProperty("领料单id")
    @NotNull
    private Long requisitionPlanId;

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

}

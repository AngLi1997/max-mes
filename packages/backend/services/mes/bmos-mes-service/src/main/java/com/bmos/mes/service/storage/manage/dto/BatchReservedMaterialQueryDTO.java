package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("批次(生产计划)下已预定暂存物料查询DTO")
@Data
public class BatchReservedMaterialQueryDTO {

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("领料计划id")
    private Long requisitionPlanId;

}

package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("物料预定组件:可用暂存物料件查询DTO")
@Data
public class AvailableStorageMaterialQueryDTO {

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

}

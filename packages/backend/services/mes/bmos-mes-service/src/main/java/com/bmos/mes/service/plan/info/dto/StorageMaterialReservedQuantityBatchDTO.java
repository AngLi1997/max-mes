package com.bmos.mes.service.plan.info.dto;

import com.bmos.mes.service.formula.dto.StorageMaterialReservedQuantityDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("生产批次预定暂存量批量查询DTO")
@Data
public class StorageMaterialReservedQuantityBatchDTO {


    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("配方物料与校验量列表")
    List<StorageMaterialReservedQuantityDTO> materialList;

}

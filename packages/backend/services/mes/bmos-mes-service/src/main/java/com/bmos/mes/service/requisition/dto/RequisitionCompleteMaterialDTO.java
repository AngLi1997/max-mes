package com.bmos.mes.service.requisition.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("领料计划完成物料DTO")
@Data
public class RequisitionCompleteMaterialDTO {

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("物料预定列表")
    private List<RequisitionCompleteReservedDTO> materialReservedList;

}

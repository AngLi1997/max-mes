package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("仓库预定物料DTO")
@Data
public class ReserveRepositoryMaterialDTO {

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("物料预定列表")
    @Valid
    private List<RequisitionCompleteReservedDTO> materialReservedList;

    @ApiModelProperty("领料单id")
    @NotNull
    private Long requisitionPlanId;


}

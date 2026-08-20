package com.bmos.mes.service.preparation.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("配液计划绑定批次")
public class LiquidPreparationBindMaterialBatchDTO {

    @ApiModelProperty("配液计划id")
    @NotNull
    private Long preparationPlanId;

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

    @ApiModelProperty("批次列表")
    @Valid
    private List<LiquidPreparationBindBatchListDTO> materialBatchList;

}

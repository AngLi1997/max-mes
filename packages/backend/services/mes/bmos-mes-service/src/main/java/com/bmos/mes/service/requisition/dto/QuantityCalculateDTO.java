package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("理论量计算DTO")
@Data
public class QuantityCalculateDTO {


    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

    @ApiModelProperty("物料量")
    @NotNull
    private BigDecimal quantity;

    @ApiModelProperty("水分 无则0")
    @NotNull
    private BigDecimal hydration;

    @ApiModelProperty("含量 无或大于100则100")
    @NotNull
    private BigDecimal noHydrationContent;
}

package com.bmos.mes.service.weigh.centre2.requirement.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/23 13:39
 */
@Data
public class TicketCalcFormulaQuantityDTO {

    @ApiModelProperty(value = "需求量", required = true, example = "3001")
    @NotNull
    private BigDecimal requirementQuantity;

    @ApiModelProperty(value = "可用量")
    @NotNull
    private BigDecimal availableQuantity;

    @ApiModelProperty(value = "水分(%)")
    private BigDecimal hydration = BigDecimal.ZERO;

    @ApiModelProperty(value = "无水含量(%)")
    private BigDecimal noHydrationContent = BigDecimal.valueOf(100L);

    @ApiModelProperty(value = "配方物料id", required = true)
    @NotNull
    private Long formulaMaterialId;
}

package com.bmos.mes.service.formula.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/20 10:07
 */
@Data
public class ProductFormulaWeighRequirementInfo {

    @ApiModelProperty("需求量")
    private BigDecimal requirementQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("称量需求用途")
    private String requirementUsage;
}

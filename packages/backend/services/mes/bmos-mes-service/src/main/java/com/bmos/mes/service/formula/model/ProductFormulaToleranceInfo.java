package com.bmos.mes.service.formula.model;

import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 配方物料允差信息
 */
@Getter
@Setter
public class  ProductFormulaToleranceInfo {


    @ApiModelEnumProperty(value = "配液允差类型", enumClass = ToleranceTypeEnum.class)
    private Integer liquidMeasureToleranceType;

    @ApiModelProperty("配液允差上限")
    private BigDecimal liquidMeasureToleranceUpper;

    @ApiModelProperty("配液允差下限")
    private BigDecimal liquidMeasureToleranceLower;

    @ApiModelEnumProperty(value = "余液允差类型", enumClass = ToleranceTypeEnum.class)
    private Integer oddLiquidMeasureToleranceType;

    @ApiModelProperty("余液允差上限")
    private BigDecimal oddLiquidMeasureToleranceUpper;

    @ApiModelProperty("余液允差下限")
    private BigDecimal oddLiquidMeasureToleranceLower;

}

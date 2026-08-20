package com.bmos.mes.service.formula.dto;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.formula.DryAndPureTypeEnum;
import com.bmos.mes.common.enums.formula.QuantityTypeEnum;
import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.mes.service.formula.model.ProductFormulaToleranceInfo;
import com.bmos.mes.service.formula.model.ProductFormulaWeighRequirementInfo;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("产品配方物料DTO")
public class ProductFormulaMaterialDTO {

    @ApiModelProperty("物料类型")
    @ApiModelEnumProperty(value = "数量类型", enumClass = CategoryInfoTypeEnum.class)
    private Integer materialType;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("数量")
    private BigDecimal quantity;

    @ApiModelEnumProperty(value = "数量类型", enumClass = QuantityTypeEnum.class)
    private Integer quantityType;

    @ApiModelProperty("物料精度")
    private BigDecimal scale;

    @ApiModelEnumProperty(value = "修约规则", enumClass = RoundingEnum.class)
    private String rounding;

    @ApiModelEnumProperty(value = "折干折纯类型", enumClass = DryAndPureTypeEnum.class)
    private Integer dryPureType;

    @ApiModelProperty("折干折纯参数")
    private BigDecimal dryPureParam;

    @ApiModelEnumProperty(value = "拆包允差类型", enumClass = ToleranceTypeEnum.class)
    private Integer unpackingToleranceType;

    @ApiModelProperty("拆包允差上限")
    private BigDecimal unpackingToleranceUpper;

    @ApiModelProperty("拆包允差下限")
    private BigDecimal unpackingToleranceLower;

    @ApiModelEnumProperty(value = "配料允差类型", enumClass = ToleranceTypeEnum.class)
    private Integer chargeMixtureToleranceType;

    @ApiModelProperty("配料允差上限")
    private BigDecimal chargeMixtureToleranceUpper;

    @ApiModelProperty("配料允差下限")
    private BigDecimal chargeMixtureToleranceLower;

    @ApiModelEnumProperty(value = "余料允差类型", enumClass = ToleranceTypeEnum.class)
    private Integer oddmentToleranceType;

    @ApiModelProperty("余料允差上限")
    private BigDecimal oddmentToleranceUpper;

    @ApiModelProperty("余料允差下限")
    private BigDecimal oddmentToleranceLower;

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

    @ApiModelProperty("称量需求列表")
    private List<ProductFormulaWeighRequirementInfo> weighRequirementList = new ArrayList<>();

    public BigDecimal getQuantity(){
        return ObjectUtil.equal(quantityType, QuantityTypeEnum.APPROPRIATE_QUANTITY.getValue()) ? BigDecimal.ZERO : quantity;
    }

    public ProductFormulaToleranceInfo getToleranceInfo() {
        ProductFormulaToleranceInfo info = new ProductFormulaToleranceInfo();
        info.setLiquidMeasureToleranceType(liquidMeasureToleranceType);
        info.setLiquidMeasureToleranceUpper(liquidMeasureToleranceUpper);
        info.setLiquidMeasureToleranceLower(liquidMeasureToleranceLower);
        info.setOddLiquidMeasureToleranceType(oddLiquidMeasureToleranceType);
        info.setOddLiquidMeasureToleranceUpper(oddLiquidMeasureToleranceUpper);
        info.setOddLiquidMeasureToleranceLower(oddLiquidMeasureToleranceLower);
        return info;
    }


}
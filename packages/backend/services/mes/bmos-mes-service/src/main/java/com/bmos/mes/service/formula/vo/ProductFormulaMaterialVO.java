package com.bmos.mes.service.formula.vo;

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
import java.util.List;

@ApiModel("产品配方物料VO")
@Getter
@Setter
public class ProductFormulaMaterialVO {

    @ApiModelProperty("物料类型")
    private CategoryInfoTypeEnum materialType;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料规格")
    private String materialSpecification;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("数量")
    private BigDecimal quantity;

    @ApiModelEnumProperty(value = "数量类型", enumClass = QuantityTypeEnum.class)
    private QuantityTypeEnum quantityType;

    @ApiModelProperty("物料精度")
    private BigDecimal scale;

    @ApiModelProperty("精度长度")
    private Integer scaleLength;

    @ApiModelProperty(value = "修约规则code")
    private String rounding;

    @ApiModelEnumProperty(value = "折干折纯类型", enumClass = DryAndPureTypeEnum.class)
    private DryAndPureTypeEnum dryPureType;

    @ApiModelProperty("折干折纯参数")
    private BigDecimal dryPureParam;

    @ApiModelEnumProperty(value = "拆包允差类型", enumClass = ToleranceTypeEnum.class)
    private ToleranceTypeEnum unpackingToleranceType;

    @ApiModelProperty("拆包允差上限")
    private BigDecimal unpackingToleranceUpper;

    @ApiModelProperty("拆包允差下限")
    private BigDecimal unpackingToleranceLower;

    @ApiModelEnumProperty(value = "配料允差类型", enumClass = ToleranceTypeEnum.class)
    private ToleranceTypeEnum chargeMixtureToleranceType;

    @ApiModelProperty("配料允差上限")
    private BigDecimal chargeMixtureToleranceUpper;

    @ApiModelProperty("配料允差下限")
    private BigDecimal chargeMixtureToleranceLower;

    @ApiModelEnumProperty(value = "余料允差类型", enumClass = ToleranceTypeEnum.class)
    private ToleranceTypeEnum oddmentToleranceType;

    @ApiModelProperty("余料允差上限")
    private BigDecimal oddmentToleranceUpper;

    @ApiModelProperty("余料允差下限")
    private BigDecimal oddmentToleranceLower;

    @ApiModelEnumProperty(value = "配液允差类型", enumClass = ToleranceTypeEnum.class)
    private ToleranceTypeEnum liquidMeasureToleranceType;

    @ApiModelProperty("配液允差上限")
    private BigDecimal liquidMeasureToleranceUpper;

    @ApiModelProperty("配液允差下限")
    private BigDecimal liquidMeasureToleranceLower;

    @ApiModelEnumProperty(value = "余液允差类型", enumClass = ToleranceTypeEnum.class)
    private ToleranceTypeEnum oddLiquidMeasureToleranceType;

    @ApiModelProperty("余液允差上限")
    private BigDecimal oddLiquidMeasureToleranceUpper;

    @ApiModelProperty("余液允差下限")
    private BigDecimal oddLiquidMeasureToleranceLower;

    @ApiModelProperty("称重需求列表")
    private List<ProductFormulaWeighRequirementInfo> weighRequirementList;

    public String getScale(){
        return scale.setScale(scaleLength).toPlainString();
    }

    public void parseToleranceInfo(ProductFormulaToleranceInfo info) {
        if (info == null) {
            return;
        }
        if (info.getLiquidMeasureToleranceType() != null) {
            this.liquidMeasureToleranceType = ToleranceTypeEnum.getEnumByValue(info.getLiquidMeasureToleranceType());
            this.liquidMeasureToleranceUpper = info.getLiquidMeasureToleranceUpper();
            this.liquidMeasureToleranceLower = info.getLiquidMeasureToleranceLower();
        }
        if (info.getOddLiquidMeasureToleranceType() != null) {
            this.oddLiquidMeasureToleranceType = ToleranceTypeEnum.getEnumByValue(info.getOddLiquidMeasureToleranceType());
            this.oddLiquidMeasureToleranceUpper = info.getOddLiquidMeasureToleranceUpper();
            this.oddLiquidMeasureToleranceLower = info.getOddLiquidMeasureToleranceLower();
        }
    }
}

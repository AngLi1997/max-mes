package com.bmos.mes.service.utils;

import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.enums.formula.DryAndPureTypeEnum;
import com.bmos.mes.common.enums.formula.QuantityTypeEnum;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MaterialQuantityCalculateUtil {

    /**
     * @param quantity           物料量
     * @param hydrationValue     水分
     * @param noHydrationContent 含量
     * @param formulaMaterial    配方物料
     * @return 理论量
     */
    public static BigDecimal calculateTheoreticalQuantity(BigDecimal quantity, BigDecimal hydrationValue,
                                                          BigDecimal noHydrationContent,
                                                          ProductFormulaMaterial formulaMaterial) {
        if (hydrationValue == null) {
            hydrationValue = BigDecimal.ZERO;
        }
        BigDecimal hundred = new BigDecimal(100);
        if (noHydrationContent == null || noHydrationContent.compareTo(hundred) > 0) {
            noHydrationContent = hundred;
        }
        DryAndPureTypeEnum dryPureType = formulaMaterial.getDryPureType();
        // 物料批次理论量
        BigDecimal theoreticalQuantity = BigDecimal.ZERO;
        int divideScale = 20;
        // 纯度
        BigDecimal content = noHydrationContent.divide(hundred, divideScale, RoundingMode.DOWN);
        // 水分
        BigDecimal hydration = hydrationValue.divide(hundred, divideScale, RoundingMode.DOWN);
        String rounding = formulaMaterial.getRounding();
        switch (dryPureType) {
            case NO_TYPE:
                theoreticalQuantity = roundingOff(quantity, formulaMaterial.getScale(),
                        formulaMaterial.getScaleLength(), RoundingEnum.getEnumByCode(rounding).getMapping());
                break;
            case PURE:
                theoreticalQuantity = roundingOff(quantity.multiply(content), formulaMaterial.getScale(),
                        formulaMaterial.getScaleLength(), RoundingEnum.getEnumByCode(rounding).getMapping());
                break;
            case DRY_PURE:
                BigDecimal multiply = quantity.multiply(BigDecimal.ONE.subtract(hydration)).multiply(content);
                theoreticalQuantity = roundingOff(multiply, formulaMaterial.getScale(),
                        formulaMaterial.getScaleLength(), RoundingEnum.getEnumByCode(rounding).getMapping());
                break;
            case DRY_PURE_WITH_PARAM:
                BigDecimal dryPureParam = formulaMaterial.getDryPureParam();
                BigDecimal dryPure = BigDecimal.ONE.subtract(hydration).multiply(content);
                BigDecimal dryQuantity = quantity.multiply(dryPure.divide(dryPureParam, divideScale,
                        RoundingMode.DOWN));
                theoreticalQuantity = roundingOff(dryQuantity, formulaMaterial.getScale(),
                        formulaMaterial.getScaleLength(), RoundingEnum.getEnumByCode(rounding).getMapping());
                break;
        }
        return theoreticalQuantity;
    }

    /**
     * 根据理论量计算物料量（与calculateTheoreticalQuantity相反的过程）
     *
     * @param theoreticalQuantity  理论量
     * @param hydrationValue       水分
     * @param noHydrationContent   含量
     * @param formulaMaterial      配方物料
     * @return 物料量
     */
    public static BigDecimal calculateFormulaQuantity(BigDecimal theoreticalQuantity, BigDecimal hydrationValue,
                                                      BigDecimal noHydrationContent,
                                                      ProductFormulaMaterial formulaMaterial) {
        if (hydrationValue == null) {
            hydrationValue = BigDecimal.ZERO;
        }
        BigDecimal hundred = new BigDecimal(100);
        if (noHydrationContent == null || noHydrationContent.compareTo(hundred) > 0) {
            noHydrationContent = hundred;
        }
        DryAndPureTypeEnum dryPureType = formulaMaterial.getDryPureType();
        // 物料量
        BigDecimal quantity = BigDecimal.ZERO;
        int divideScale = 20;
        // 纯度
        BigDecimal content = noHydrationContent.divide(hundred, divideScale, RoundingMode.DOWN);
        // 水分
        BigDecimal hydration = hydrationValue.divide(hundred, divideScale, RoundingMode.DOWN);
        String rounding = formulaMaterial.getRounding();

        switch (dryPureType) {
            case NO_TYPE:
                // 理论量 = 物料量，所以 物料量 = 理论量
                quantity = theoreticalQuantity;
                break;
            case PURE:
                // 理论量 = 物料量 * 含量，所以 物料量 = 理论量 / 含量
                quantity = theoreticalQuantity.divide(content, divideScale, RoundingMode.DOWN);
                break;
            case DRY_PURE:
                // 理论量 = 物料量 * (1-水分) * 含量，所以 物料量 = 理论量 / ((1-水分) * 含量)
                BigDecimal divisor = BigDecimal.ONE.subtract(hydration).multiply(content);
                quantity = theoreticalQuantity.divide(divisor, divideScale, RoundingMode.DOWN);
                break;
            case DRY_PURE_WITH_PARAM:
                // 理论量 = 物料量 * (1-水分) * 含量 / dryPureParam
                // 所以 物料量 = 理论量 * dryPureParam / ((1-水分) * 含量)
                BigDecimal dryPureParam = formulaMaterial.getDryPureParam();
                BigDecimal dryPure = BigDecimal.ONE.subtract(hydration).multiply(content);
                quantity = theoreticalQuantity.multiply(dryPureParam).divide(dryPure, divideScale, RoundingMode.DOWN);
                break;
        }

        // 使用相同的四舍五入规则
        return roundingOff(quantity, formulaMaterial.getScale(),
                formulaMaterial.getScaleLength(), RoundingEnum.getEnumByCode(rounding).getMapping());
    }

    /**
     * @param planQuantity 生产计划批量
     * @param formulaQuantity  产品配方批量
     * @param material 配方物料
     * @return 理论批量
     */
    public static BigDecimal calculateQuantity(BigDecimal planQuantity, BigDecimal formulaQuantity, ProductFormulaMaterial material) {
        QuantityTypeEnum quantityType = material.getQuantityType();
        BigDecimal materialQuantity = material.getQuantity();
        String rounding = material.getRounding();
        switch (quantityType) {
            case FIXED_QUANTITY:
                return roundingOff(materialQuantity, material.getScale(), material.getScaleLength(),
                        RoundingEnum.getEnumByCode(rounding).getMapping());
            case STANDARD_QUANTITY:
                BigDecimal result = planQuantity.divide(formulaQuantity, 20,
                        RoundingEnum.getEnumByCode(rounding).getMapping()).multiply(materialQuantity);
                return roundingOff(result, material.getScale(), material.getScaleLength(),
                        RoundingEnum.getEnumByCode(rounding).getMapping());
            default:
                return materialQuantity;
        }
    }

    /**
     * 进行如小数单位修约 如精度0.5 0.2
     *
     * @param value        要修约的值
     * @param scale        修约精度单位 example: 0.02 0.5
     * @param scaleLength  精度长度 保留小数位数
     * @param roundingMode 修约方式 例如四舍五入
     * @return
     */
    public static BigDecimal roundingOff(BigDecimal value, BigDecimal scale, int scaleLength,
                                         RoundingMode roundingMode) {
        BigDecimal divide = value.divide(scale, 20, roundingMode);
        BigDecimal x = divide.setScale(0, roundingMode);
        BigDecimal res = x.multiply(scale).setScale(scaleLength);
        return res;
    }

    public static BigDecimal roundingOff(BigDecimal value, ProductFormulaMaterial productFormulaMaterial) {
        return roundingOff(value,
                productFormulaMaterial.getScale(),
                productFormulaMaterial.getScaleLength(),
                RoundingEnum.getEnumByCode(productFormulaMaterial.getRounding()).getMapping());
    }

}

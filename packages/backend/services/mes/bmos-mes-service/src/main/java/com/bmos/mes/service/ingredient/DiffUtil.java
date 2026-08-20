package com.bmos.mes.service.ingredient;

import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 允差计算工具
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/5/8 19:39
 */
public class DiffUtil {

    /**
     * 计算实际允差范围
     *
     * @param target          标准
     * @param formulaMaterial 配方物料
     * @return 允差范围 【允差下限，标准，允差上限】
     */
    public static BigDecimal[] diff(BigDecimal target, ProductFormulaMaterial formulaMaterial) {
        return diff(target, formulaMaterial.getOddmentToleranceLower(), formulaMaterial.getOddmentToleranceUpper(), formulaMaterial.getOddmentToleranceType(), formulaMaterial.getScale(), formulaMaterial.getScaleLength());
    }

    /**
     * 计算实际允差范围
     *
     * @param target        标准
     * @param maxTolerance  允差上限
     * @param minTolerance  允差下限
     * @param toleranceType 允差类型
     * @return 允差范围 【允差下限，标准，允差上限】
     */
    public static BigDecimal[] diff(BigDecimal target, BigDecimal maxTolerance, BigDecimal minTolerance, ToleranceTypeEnum toleranceType, BigDecimal scale, int scaleLength) {
        if (toleranceType == null) {
            return new BigDecimal[]{null, target, null};
        }
        if (target.compareTo(BigDecimal.ZERO) < 0) {
            target = BigDecimal.ZERO;
        }
        if (Objects.equals(ToleranceTypeEnum.FIXED_VALUE, toleranceType)) {
            // 固定值
            BigDecimal left = null;
            BigDecimal right = null;
            if (minTolerance != null) {
                left = target.subtract(minTolerance);
                left = left.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : left;
            }
            if (maxTolerance != null) {
                right = target.add(maxTolerance);
            }
            return new BigDecimal[]{left, target, right};
        } else {
            // 比例
            BigDecimal left = null;
            BigDecimal right = null;
            if (minTolerance != null) {
                BigDecimal minRate = minTolerance.multiply(new BigDecimal("0.01"));
                if (minRate.compareTo(BigDecimal.ONE) > 0) {
                    minRate = BigDecimal.ONE;
                }
                left = target.subtract(precision(minRate.multiply(target), scale, scaleLength));
            }
            if (maxTolerance != null) {
                BigDecimal maxRate = maxTolerance.multiply(new BigDecimal("0.01"));
                if (maxRate.compareTo(BigDecimal.ONE) > 0) {
                    maxRate = BigDecimal.ONE;
                }
                right = target.add(precision(maxRate.multiply(target), scale, scaleLength));
            }
            return new BigDecimal[]{left, target, right};
        }
    }

    private static BigDecimal precision(BigDecimal value, BigDecimal scale, int scaleLength) {
        return MaterialQuantityCalculateUtil.roundingOff(value, scale, scaleLength, RoundingMode.DOWN);
    }
}

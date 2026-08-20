package com.bmos.mes.service.process.model.stepconfig;


import cn.hutool.core.util.StrUtil;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.enums.record.ScopeLimitTypeEnum;
import com.bmos.mes.common.utils.BigDecimalUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 设备数采绘图设备数据配置
 */
@Data
public class EquipmentPictureDataConfig {

    /**
     * 数采数据code
     */
    private String acquisitionDataCode;

    /**
     * 数值精度-小数位数
     */
    private Integer scale;

    /**
     * 修约方式code
     */
    private String roundCode;

    /**
     * 纠偏线配置
     */
    private LineBaseConfig correctionLineConfig;

    /**
     * 警戒线配置
     */
    private LineBaseConfig warningLineConfig;

    /**
     * 标准线配置
     */
    private LineBaseConfig standardLineConfig;

    /**
     * 计算该配置和传入值的纵轴结果
     * @param maxValue
     * @param minValue
     * @return
     */
    public LineCalculateResult getLineResult(BigDecimal minValue, BigDecimal maxValue) {
        return new LineCalculateResult(getLowerValue(minValue), getUpperValue(maxValue));
    }

    private String getUpperValue(BigDecimal maxValue) {
        BigDecimal upperValue = correctionLineConfig.getUpperValue();
        BigDecimal upperValue1 = warningLineConfig.getUpperValue();
        BigDecimal upperValue2 = standardLineConfig.getUpperValue();
        BigDecimal max = BigDecimalUtil.getMax(upperValue, upperValue1, upperValue2, maxValue);
        BigDecimal multiply = max.multiply(max.compareTo(BigDecimal.ZERO) > 0 ? new BigDecimal("1.05") : new BigDecimal("0.95"));
        return hasScale()  ? multiply.setScale(scale, RoundingEnum.getEnumByCode(roundCode).getMapping()).toPlainString()
                : multiply.setScale(maxValue.scale()).toPlainString();
    }

    private boolean hasScale() {
        return this.scale != null && StrUtil.isNotEmpty(this.roundCode);
    }

    private String getLowerValue(BigDecimal minValue) {
        BigDecimal upperValue = correctionLineConfig.getLowerValue();
        BigDecimal upperValue1 = warningLineConfig.getLowerValue();
        BigDecimal upperValue2 = standardLineConfig.getLowerValue();
        BigDecimal min = BigDecimalUtil.getMin(upperValue, upperValue1, upperValue2, minValue);
        BigDecimal multiply = min.multiply(min.compareTo(BigDecimal.ZERO) > 0 ? new BigDecimal("0.95") : new BigDecimal("1.05"));
        return hasScale()  ? multiply.setScale(scale, RoundingEnum.getEnumByCode(roundCode).getMapping()).toPlainString()
                : multiply.setScale(minValue.scale()).toPlainString();
    }


    /**
     * 计算结果
     */
    @Data
    @AllArgsConstructor
    public static class LineCalculateResult {

        private String lowerValue;

        private String upperValue;

    }


    /**
     * 纠偏线、警戒线、标准线基础配置
     */
    @Data
    public static class LineBaseConfig {

        /**
         * 范围限制配置
         */
        private Scope scopeConfig;

        /**
         * 限制方式
         * {@link ScopeLimitTypeEnum}
         */
        private String limitType;

        /**
         * 固定值配置
         */
        private String fixedValue;


        @Data
        @ApiModel("限定范围")
        public static class Scope {
            /**
             * {@link com.bmos.mes.common.enums.record.ScopeCompareResultEnum}
             */
            @ApiModelProperty(value = "上限值比较")
            private Integer upperLimit;

            /**
             * {@link com.bmos.mes.common.enums.record.ScopeCompareResultEnum}
             */
            @ApiModelProperty(value = "下限值比较")
            private Integer lowerLimit;

            @ApiModelProperty("最大值")
            private String upperValue;

            @ApiModelProperty("最小值")
            private String lowerValue;
        }

        public BigDecimal getUpperValue() {
            if (StrUtil.equals(limitType, ScopeLimitTypeEnum.SCOPE_LIMIT.getValue())) {
                return StrUtil.isEmpty(scopeConfig.getUpperValue()) ? null : new BigDecimal(scopeConfig.getUpperValue());
            }
            if (StrUtil.equals(limitType, ScopeLimitTypeEnum.NUMBER_EQUALS.getValue())) {
                return StrUtil.isEmpty(fixedValue) ? null : new BigDecimal(fixedValue);
            }
            return null;
        }

        public BigDecimal getLowerValue() {
            if (StrUtil.equals(limitType, ScopeLimitTypeEnum.SCOPE_LIMIT.getValue())) {
                return StrUtil.isEmpty(scopeConfig.getLowerValue()) ? null : new BigDecimal(scopeConfig.getLowerValue());
            }
            if (StrUtil.equals(limitType, ScopeLimitTypeEnum.NUMBER_EQUALS.getValue())) {
                return StrUtil.isEmpty(fixedValue) ? null : new BigDecimal(fixedValue);
            }
            return null;
        }


    }


}

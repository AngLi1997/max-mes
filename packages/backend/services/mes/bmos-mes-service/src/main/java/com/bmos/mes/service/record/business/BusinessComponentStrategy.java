package com.bmos.mes.service.record.business;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.enums.formula.QuantityTypeEnum;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @author kl
 */

public interface BusinessComponentStrategy {

    /**
     * @param results   组装的ExecuteFormData
     * @param component 要处理的组件
     * @param info      生产详细信息
     * @param configMap 组件配置map 用于获取绑定信息
     * @return
     */
    void handleBusinessComponent(@NotNull List<ExecuteFormData> results, ComponentListVO component,
                                 ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap,
                                 Integer index);

    static String getLFStrings(List<String> strings) {
        return CollUtil.join(strings, StrUtil.LF);
    }

    static String getDecimalStripString(BigDecimal value) {
        return value == null ? StrUtil.EMPTY : value.stripTrailingZeros().toPlainString();
    }

    /**
     * @param planQuantity 生产计划计划批量
     * @param formulaBatchQuantity 配方生产批量
     * @param material 配方物料
     * @return
     */
    static String calculateQuantity(BigDecimal planQuantity, BigDecimal formulaBatchQuantity, ProductFormulaMaterial material) {
        QuantityTypeEnum quantityType = material.getQuantityType();
        BigDecimal materialQuantity = material.getQuantity();
        String rounding = material.getRounding();
        switch (quantityType) {
            case FIXED_QUANTITY:
                return roundingOff(materialQuantity, material.getScale(), material.getScaleLength(),
                        RoundingEnum.getEnumByCode(rounding).getMapping()).toPlainString();
            case STANDARD_QUANTITY:
                BigDecimal result = planQuantity.divide(formulaBatchQuantity, 20,
                        RoundingEnum.getEnumByCode(rounding).getMapping()).multiply(materialQuantity);
                return roundingOff(result, material.getScale(), material.getScaleLength(),
                        RoundingEnum.getEnumByCode(rounding).getMapping()).toPlainString();
            default:
                return StrUtil.EMPTY;
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
    static BigDecimal roundingOff(BigDecimal value, BigDecimal scale, int scaleLength, RoundingMode roundingMode) {
//        return value.multiply(BigDecimal.ONE.divide(scale)).multiply(scale).setScale(scaleLength, roundingMode);
        BigDecimal divide = value.divide(scale, 20, roundingMode);
        BigDecimal x = divide.setScale(0, roundingMode);
        BigDecimal res = x.multiply(scale).setScale(scaleLength);
        return res;
    }

}

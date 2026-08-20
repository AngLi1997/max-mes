package com.bmos.lims2.server.eln.record.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.eln.entry.dto.ElnEntryContext;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.record.vo.ComponentListVO;

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
     * @return
     */
    void handleBusinessComponent(@NotNull List<ExecuteFormData> results, ComponentListVO component,
                                 ElnEntryContext context);

    static String getLFStrings(List<String> strings) {
        return CollUtil.join(strings, StrUtil.LF);
    }

    static String getDecimalStripString(BigDecimal value) {
        return value == null ? StrUtil.EMPTY : value.stripTrailingZeros().toPlainString();
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

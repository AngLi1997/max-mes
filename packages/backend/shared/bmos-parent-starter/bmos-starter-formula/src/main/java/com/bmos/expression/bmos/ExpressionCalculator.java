package com.bmos.expression.bmos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExpressionCalculator {

    BigDecimal evaluate(String formulaStr, Map<String, String> paramsKv, Integer scale);

    /**
     * @param formulaStr 公式表达式
     * @param keys       参数列表
     * @param values     参数值列表
     * @param scale      精度 为null时默认100精度
     * @return
     */
    BigDecimal evaluate(String formulaStr, List<String> keys, List<String> values, Integer scale);

    /**
     * @param formulaStr
     * @param keys
     * @param values     默认100精度
     * @return
     */
    BigDecimal evaluate(String formulaStr, List<String> keys, List<String> values);


    Set<String> parseParams(String formulaStr);

}

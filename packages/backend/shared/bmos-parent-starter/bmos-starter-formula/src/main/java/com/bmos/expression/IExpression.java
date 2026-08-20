package com.bmos.expression;

import java.math.BigDecimal;
import java.util.Map;

public interface IExpression {
    Map<String, BigDecimal> getVariables();
}

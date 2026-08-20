package com.bmos.expression;

import java.util.Set;

public interface IExpressionBuilder {
    Expression build();

    ExpressionBuilder variables(String... variableNames);

    ExpressionBuilder variables(Set<String> variableNames);

}

package com.bmos.expression.bmos;

import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.expression.Expression;
import com.bmos.expression.ExpressionBuilder;
import com.bmos.expression.IExpressionBuilder;
import com.bmos.expression.ValidationResult;
import com.bmos.expression.function.Function;
import com.bmos.expression.operator.Operator;
import com.bmos.expression.tokenizer.ParamToken;
import com.bmos.expression.tokenizer.Token;
import com.bmos.expression.tokenizer.Tokenizer;

import java.math.BigDecimal;
import java.util.*;

public class ExpressionCalculatorImpl implements ExpressionCalculator {

    private final List<String> constantVar = Arrays.asList("pi", "π", "e", "φ");

    private final Integer defaultScale = 100;

    @Override
    public BigDecimal evaluate(String formulaStr, Map<String, String> paramsKv, Integer scale) {
        if (scale == null) {
            scale = defaultScale;
        }
        Set<String> params = parseParamsIncludeConstant(formulaStr);
        ExpressionBuilder expressionBuilder = new ExpressionBuilder(formulaStr);
        Expression expression = expressionBuilder.variables(params).build();
        Map<String, BigDecimal> map = new HashMap<>();
        try {
            paramsKv.forEach((k, v) -> {
                map.put(k, new BigDecimal(v));
            });
        } catch (NumberFormatException e) {
            throw new BmosException(BaseResponseCode.EVALUATE_WRONG_PARAM_EXCEPTION);
        }
        expression.setVariables(map);
        // 校验参数设值是否完全
        boolean valid = expression.validate(true).isValid();
        if (!valid) {
            throw new BmosException(BaseResponseCode.PARSE_EXCEPTION);
        }
        return expression.setScale(scale).evaluate();
    }

    @Override
    public BigDecimal evaluate(String formulaStr, List<String> keys, List<String> values, Integer scale) {
        if (scale == null) {
            scale = defaultScale;
        }
        Set<String> params = parseParamsIncludeConstant(formulaStr);
        ExpressionBuilder expressionBuilder = new ExpressionBuilder(formulaStr);
        Expression expression = expressionBuilder.variables(params).build();
        Map<String, BigDecimal> map = new HashMap<>();
        try {
            for (int i = 0; i < keys.size(); i++) {
                map.put(keys.get(i), new BigDecimal(values.get(i)));
            }
        } catch (Exception e) {
            throw new BmosException(BaseResponseCode.EVALUATE_WRONG_PARAM_EXCEPTION);
        }
        expression.setVariables(map);
        // 校验参数设值是否完全
        boolean valid = expression.validate(true).isValid();
        if (!valid) {
            throw new BmosException(BaseResponseCode.PARSE_EXCEPTION);
        }
        return expression.setScale(scale).evaluate();
    }

    @Override
    public BigDecimal evaluate(String formulaStr, List<String> keys, List<String> values) {
        return this.evaluate(formulaStr, keys, values, null);
    }


    @Override
    public Set<String> parseParams(String formulaStr) {
        Set<String> variables = new HashSet<>();
        Token[] paramToken = getParamToken(formulaStr);
        for (Token token : paramToken) {
            String name = ((ParamToken) token).getName();
            variables.add(name);
        }
        constantVar.forEach(variables::remove);
        if (variables.isEmpty()) {
            throw new BmosException(BaseResponseCode.PARSE_EXCEPTION);
        }
        IExpressionBuilder expressionBuilder = new ExpressionBuilder(formulaStr);
        expressionBuilder.variables(variables);
        // 解析
        ValidationResult result = expressionBuilder.build().validate(false);
        if (!result.isValid()) {
            throw new BmosException(BaseResponseCode.PARSE_EXCEPTION);
        }
        return variables;
    }

    private Set<String> parseParamsIncludeConstant(String formulaStr) {
        Set<String> variables = new HashSet<>();
        Token[] paramToken = getParamToken(formulaStr);
        for (Token token : paramToken) {
            String name = ((ParamToken) token).getName();
            variables.add(name);
        }
        if (variables.isEmpty()) {
            throw new BmosException(BaseResponseCode.PARSE_EXCEPTION);
        }
        IExpressionBuilder expressionBuilder = new ExpressionBuilder(formulaStr);
        expressionBuilder.variables(variables);
        // 解析
        ValidationResult result = expressionBuilder.build().validate(false);
        if (!result.isValid()) {
            throw new BmosException(BaseResponseCode.PARSE_EXCEPTION);
        }
        return variables;
    }

    /**
     * 获取参数token
     *
     * @param expression 表达式
     * @return 参数
     */
    private Token[] getParamToken(
            String expression) {
        final List<Token> params = new ArrayList<>();
        final Tokenizer tokenizer = new Tokenizer(
                expression, null, null, null, true);
        while (tokenizer.hasNext()) {
            Token token = tokenizer.nextToken();
            if (token.getType() == Token.TOKEN_PARAM) {
                params.add(token);
            }
        }
        return params.toArray(new Token[0]);
    }

    /**
     * 获取参数token
     *
     * @param expression             表达式
     * @param userFunctions          自定义函数
     * @param userOperators          自定义操作服
     * @param variableNames          自定义变量
     * @param implicitMultiplication 是否支持隐式乘法
     * @return 参数
     */
    private Token[] getParamToken(
            String expression,
            final Map<String, Function> userFunctions,
            final Map<String, Operator> userOperators,
            final Set<String> variableNames,
            final boolean implicitMultiplication) {
        final List<Token> params = new ArrayList<>();
        final Tokenizer tokenizer = new Tokenizer(
                expression, userFunctions, userOperators, variableNames, implicitMultiplication);
        while (tokenizer.hasNext()) {
            Token token = tokenizer.nextToken();
            if (token.getType() == Token.TOKEN_PARAM) {
                params.add(token);
            }
        }
        return params.toArray(new Token[0]);
    }
}

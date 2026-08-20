/*
 * Copyright 2014 Frank Asseg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bmos.expression;

import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.expression.function.Function;
import com.bmos.expression.function.Functions;
import com.bmos.expression.operator.Operator;
import com.bmos.expression.tokenizer.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Expression implements IExpression {

    private final Token[] tokens;

    private final Map<String, BigDecimal> variables;

    private final Set<String> userFunctionNames;

    private static final String PI_VALUE_STR = "3.14159265358979323846";
    private static final String PHI_VALUE_STR = "1.61803398874";

    private static final String NATURAL_CONSTANT = "2.7182818284590452354";

    private int EVALUATE_SCALE = 10;

    private static Map<String, BigDecimal> createDefaultVariables() {
        final Map<String, BigDecimal> vars = new HashMap<>(4);
        vars.put("pi", new BigDecimal(PI_VALUE_STR));
        vars.put("π", new BigDecimal(PI_VALUE_STR));
        vars.put("φ", new BigDecimal(PHI_VALUE_STR));
        vars.put("e", new BigDecimal(NATURAL_CONSTANT));
        return vars;
    }

    public Expression setScale(int scale) {
        EVALUATE_SCALE = scale;
        return this;
    }

    public int getScale() {
        return EVALUATE_SCALE;
    }

    /**
     * Creates a new expression that is a copy of the existing one.
     *
     * @param existing the expression to copy
     */
    public Expression(final Expression existing) {
        this.tokens = Arrays.copyOf(existing.tokens, existing.tokens.length);
        this.variables = new HashMap<>();
        this.variables.putAll(existing.variables);
        this.userFunctionNames = new HashSet<>(existing.userFunctionNames);
    }

    Expression(final Token[] tokens) {
        this.tokens = tokens;
        this.variables = createDefaultVariables();
        this.userFunctionNames = Collections.emptySet();
    }

    Expression(final Token[] tokens, Set<String> userFunctionNames) {
        this.tokens = tokens;
        this.variables = createDefaultVariables();
        this.userFunctionNames = userFunctionNames;
    }

    public Expression setVariable(final String name, final BigDecimal value) {
        this.checkVariableName(name);
        this.variables.put(name, value);
        return this;
    }

    private void checkVariableName(String name) {
        if (this.userFunctionNames.contains(name) || Functions.getBuiltinFunction(name) != null) {
            throw new IllegalArgumentException("The variable name '" + name + "' is invalid. Since there exists a function with the same name");
        }
    }

    public Expression setVariables(Map<String, BigDecimal> variables) {
        for (Map.Entry<String, BigDecimal> v : variables.entrySet()) {
            this.setVariable(v.getKey(), v.getValue());
        }
        return this;
    }

    public Expression clearVariables() {
        this.variables.clear();
        return this;
    }

    public Set<String> getVariableNames() {
        final Set<String> variables = new HashSet<>();
        for (final Token t : tokens) {
            if (t.getType() == Token.TOKEN_VARIABLE)
                variables.add(((VariableToken) t).getName());
        }
        return variables;
    }

    public ValidationResult validate(boolean checkVariablesSet) {
        final List<String> errors = new ArrayList<>(0);
        if (checkVariablesSet) {
            /* check that all vars have a value set */
            for (final Token t : this.tokens) {
                if (t.getType() == Token.TOKEN_VARIABLE) {
                    final String var = ((VariableToken) t).getName();
                    if (!variables.containsKey(var)) {
//                        errors.add("The setVariable '" + var + "' has not been set");
                        throw new BmosException(BaseResponseCode.VAR_NOT_BEEN_SET, var);
                    }
                }
            }

        }

        /* Check if the number of operands, functions and operators match.
           The idea is to increment a counter for operands and decrease it for operators.
           When a function occurs the number of available arguments has to be greater
           than or equals to the function's expected number of arguments.
           The count has to be larger than 1 at all times and exactly 1 after all tokens
           have been processed */
        int count = 0;
        for (Token tok : this.tokens) {
            switch (tok.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    count++;
                    break;
                case Token.TOKEN_FUNCTION:
                    final Function func = ((FunctionToken) tok).getFunction();
                    final int argsNum = func.getNumArguments();
                    if (argsNum > count) {
                        errors.add("Not enough arguments for '" + func.getName() + "'");
                        throw new BmosException(BaseResponseCode.INVALID_NUMBER_OF_ARGUMENTS, func.getName());
                    }
                    if (argsNum > 1) {
                        count -= argsNum - 1;
                    } else if (argsNum == 0) {
                        // see https://github.com/fasseg/exp4j/issues/59
                        count++;
                    }
                    break;
                case Token.TOKEN_OPERATOR:
                    Operator op = ((OperatorToken) tok).getOperator();
                    if (op.getNumOperands() == 2) {
                        count--;
                    }
                    break;
            }
            if (count < 1) {
                errors.add("Too many operators");
                return new ValidationResult(false, errors);
            }
        }
        if (count > 1) {
            errors.add("Too many operands");
        }
        return errors.isEmpty() ? ValidationResult.SUCCESS : new ValidationResult(false, errors);

    }

    public ValidationResult validate() {
        return validate(true);
    }

    public Future<BigDecimal> evaluateAsync(ExecutorService executor) {
        return executor.submit(this::evaluate);
    }

    public BigDecimal evaluate() {
        final ArrayStack output = new ArrayStack();
        for (Token t : tokens) {
            if (t.getType() == Token.TOKEN_NUMBER) {
                output.push(((NumberToken) t).getValue());
            } else if (t.getType() == Token.TOKEN_VARIABLE) {
                final String name = ((VariableToken) t).getName();
                final BigDecimal value = this.variables.get(name);
                if (value == null) {
//                    throw new IllegalArgumentException("No value has been set for the setVariable '" + name + "'.");
                    throw new BmosException(BaseResponseCode.VAR_NOT_BEEN_SET, name);
                }
                output.push(value);
            } else if (t.getType() == Token.TOKEN_OPERATOR) {
                OperatorToken op = (OperatorToken) t;
                if (output.size() < op.getOperator().getNumOperands()) {
//                    throw new IllegalArgumentException("Invalid number of operands available for '" + op.getOperator().getSymbol() + "' operator");
                    throw new BmosException(BaseResponseCode.PARSE_EXCEPTION);
                }
                if (op.getOperator().getNumOperands() == 2) {
                    /* pop the operands and push the result of the operation */
                    BigDecimal rightArg = output.pop();
                    BigDecimal leftArg = output.pop();
                    output.push(op.getOperator().apply(EVALUATE_SCALE, leftArg, rightArg));
                } else if (op.getOperator().getNumOperands() == 1) {
                    /* pop the operand and push the result of the operation */
                    BigDecimal arg = output.pop();
                    output.push(op.getOperator().apply(EVALUATE_SCALE, arg));
                }
            } else if (t.getType() == Token.TOKEN_FUNCTION) {
                FunctionToken func = (FunctionToken) t;
                final int numArguments = func.getFunction().getNumArguments();
                if (output.size() < numArguments) {
//                    throw new IllegalArgumentException("Invalid number of arguments available for '" + func.getFunction().getName() + "' function");
                    throw new BmosException(BaseResponseCode.INVALID_NUMBER_OF_ARGUMENTS);
                }
                /* collect the arguments from the stack */
                BigDecimal[] args = new BigDecimal[numArguments];
                for (int j = numArguments - 1; j >= 0; j--) {
                    args[j] = output.pop();
                }
                output.push(func.getFunction().apply(EVALUATE_SCALE, args));
            }
        }
        if (output.size() > 1) {
//            throw new IllegalArgumentException("Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.");
            throw new BmosException(BaseResponseCode.INVALID_NUMBER_OF_ARGUMENTS, "unKnow");
        }
        return output.pop().setScale(EVALUATE_SCALE, RoundingMode.DOWN);
    }

    @Override
    public Map<String, BigDecimal> getVariables() {
        return variables;
    }
}

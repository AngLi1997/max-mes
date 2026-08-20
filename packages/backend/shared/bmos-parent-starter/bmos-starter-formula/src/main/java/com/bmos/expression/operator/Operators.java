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
package com.bmos.expression.operator;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class Operators {
    private static final int INDEX_ADDITION = 0;
    private static final int INDEX_SUBTRACTION = 1;
    private static final int INDEX_MULTIPLICATION = 2;
    private static final int INDEX_DIVISION = 3;
    private static final int INDEX_POWER = 4;
    private static final int INDEX_MODULO = 5;
    private static final int INDEX_UNARY_MINUS = 6;
    private static final int INDEX_UNARY_PLUS = 7;

    private static final Operator[] BUILT_IN_OPERATORS = new Operator[8];

    static {
        BUILT_IN_OPERATORS[INDEX_ADDITION] = new Operator("+", 2, true, Operator.PRECEDENCE_ADDITION) {
            @Override
            public BigDecimal apply(int scale, final BigDecimal... args) {
                return args[0].add(args[1]);
            }
        };
        BUILT_IN_OPERATORS[INDEX_SUBTRACTION] = new Operator("-", 2, true, Operator.PRECEDENCE_ADDITION) {
            @Override
            public BigDecimal apply(int scale, final BigDecimal... args) {
                return args[0].subtract(args[1]);
            }
        };
        BUILT_IN_OPERATORS[INDEX_UNARY_MINUS] = new Operator("-", 1, false, Operator.PRECEDENCE_UNARY_MINUS) {
            @Override
            public BigDecimal apply(int scale, final BigDecimal... args) {
                return args[0].negate();
            }
        };
        BUILT_IN_OPERATORS[INDEX_UNARY_PLUS] = new Operator("+", 1, false, Operator.PRECEDENCE_UNARY_PLUS) {
            @Override
            public BigDecimal apply(int scale, final BigDecimal... args) {
                return args[0];
            }
        };
        BUILT_IN_OPERATORS[INDEX_MULTIPLICATION] = new Operator("*", 2, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public BigDecimal apply(int scale, final BigDecimal... args) {
                return args[0].multiply(args[1]);
            }
        };
        BUILT_IN_OPERATORS[INDEX_DIVISION] = new Operator("/", 2, true, Operator.PRECEDENCE_DIVISION) {
            @Override
            public BigDecimal apply(int scale, final BigDecimal... args) {
                if (args[1].signum() == 0)
                    throw new BmosException(BaseResponseCode.DIVISION_BY_ZERO);
                return args[0].divide(args[1], scale, RoundingMode.DOWN);
            }
        };
        BUILT_IN_OPERATORS[INDEX_POWER] = new Operator("^", 2, false, Operator.PRECEDENCE_POWER) {
            @Override
            public BigDecimal apply(int scale, final BigDecimal... args) {
                return BigDecimalMath.pow(args[0], args[1], new MathContext(scale));
            }
        };
        BUILT_IN_OPERATORS[INDEX_MODULO] = new Operator("%", 2, true, Operator.PRECEDENCE_MODULO) {
            @Override
            public BigDecimal apply(int scale, final BigDecimal... args) {
                if (args[1].signum() == 0) {
                    throw new BmosException(BaseResponseCode.DIVISION_BY_ZERO);
                }
                return args[0].remainder(args[1]);
            }
        };
    }

    public static Operator getBuiltinOperator(final char symbol, final int numArguments) {
        switch (symbol) {
            case '+':
                if (numArguments != 1) {
                    return BUILT_IN_OPERATORS[INDEX_ADDITION];
                }

                return BUILT_IN_OPERATORS[INDEX_UNARY_PLUS];
            case '-':
                if (numArguments != 1) {
                    return BUILT_IN_OPERATORS[INDEX_SUBTRACTION];
                }

                return BUILT_IN_OPERATORS[INDEX_UNARY_MINUS];
            case '*':
                return BUILT_IN_OPERATORS[INDEX_MULTIPLICATION];
            case '÷':
            case '/':
                return BUILT_IN_OPERATORS[INDEX_DIVISION];
            case '^':
                return BUILT_IN_OPERATORS[INDEX_POWER];
//            case '%':
//                return BUILT_IN_OPERATORS[INDEX_MODULO];
            default:
                return null;
        }
    }

}

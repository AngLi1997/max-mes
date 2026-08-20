package com.bmos.expression.config;


import com.bmos.expression.bmos.ExpressionCalculator;
import com.bmos.expression.bmos.ExpressionCalculatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpressionConfiguration {
    @Bean
    public ExpressionCalculator getExpressionCalculator() {
        return new ExpressionCalculatorImpl();
    }
}

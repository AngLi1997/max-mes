package bmos;

import com.bmos.common.exception.BmosException;
import com.bmos.expression.bmos.ExpressionCalculator;
import com.bmos.expression.bmos.ExpressionCalculatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;

@Slf4j
public class EvaluateTest {

    @Test
    public void easyTest() {
        ExpressionCalculator formulaCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "2.0");
        map.put("b", "3.0");
        map.put("c", "5.0");
        BigDecimal evaluate = formulaCalculator.evaluate("(a+b)/100+c", map, 4);
    }

    @Test(expected = BmosException.class)
    public void mismatchTest() {
        ExpressionCalculator formulaCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "2.0");
        map.put("b", "3.0");
        BigDecimal evaluate = formulaCalculator.evaluate("(a+b+c", map, 4);
    }

    @Test
    public void wrongTest() {
        ExpressionCalculator formulaCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "2.0");
        map.put("b", "3.0");
        BigDecimal evaluate = formulaCalculator.evaluate("a(123)+b", map, 4);
        System.out.println(evaluate);
    }

    @Test
    public void divisionByZero() {
        try {
            ExpressionCalculator formulaCalculator = new ExpressionCalculatorImpl();
            HashMap<String, String> map = new HashMap<>();
            map.put("a", "1");
            map.put("b", "3");
            BigDecimal evaluate = formulaCalculator.evaluate("a/b", map, 4);
            System.out.println(evaluate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logTest() {
        try {
            ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
            HashMap<String, String> map = new HashMap<>();
            map.put("a", "10.0");
            log.info(expressionCalculator.evaluate("log10a", map, 4).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logTest2() {
        try {
            ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
            HashMap<String, String> map = new HashMap<>();
            map.put("a", "10.0");
            // 不支持ln 但是可以在functions里添加对ln的支持
            log.info(expressionCalculator.parseParams("ln10a").toString());
            log.info(expressionCalculator.evaluate("log10(a)", map, 4).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logTest3() {
        try {
            ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
            HashMap<String, String> map = new HashMap<>();
            map.put("a", "10.0");
            map.put("b", "100.0");
            log.info(expressionCalculator.evaluate("ln(a)", map, 8).toString());
            System.out.println(Math.log(10.0));
            System.out.println(Math.log10(10.0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sinTest() {
        try {
            ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
            HashMap<String, String> map = new HashMap<>();
            map.put("x", "10.0");
            // 不支持ln 但是可以在functions里添加对ln的支持
//            log.info(expressionCalculator.parseParams("ln10a").toString());
            log.info(expressionCalculator.evaluate("sin(x+10)", map, 4).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void eTest2() {
        try {
            ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
            HashMap<String, String> map = new HashMap<>();
            map.put("a", "10.0");
            log.info(expressionCalculator.evaluate("e*a", map, 4).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMul() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "2.0");
        map.put("b", "2.0");
        map.put("c", "2.0");
        System.out.println(expressionCalculator.evaluate("a*b*c", map, 4));
    }
}

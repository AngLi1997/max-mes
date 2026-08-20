package bmos;

import com.bmos.expression.bmos.ExpressionCalculator;
import com.bmos.expression.bmos.ExpressionCalculatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BigDecimalTest {

    @Test
    public void easyTest() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> paramKv = new HashMap<>();
        paramKv.put("a", "100");
        paramKv.put("b", "50.5");
        paramKv.put("c", "1000.0789");
        log.info(String.valueOf(expressionCalculator.evaluate("(a+b)/100+c", paramKv, 4)));
    }

    @Test
    public void doubleTest() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> paramKv = new HashMap<>();
        paramKv.put("a", "1");
        paramKv.put("b", "20.2");
        paramKv.put("c", "300.03");
        log.info(String.valueOf(expressionCalculator.evaluate("a+b+c", paramKv, 4)));
        double a = 1;
        double b = 20.2;
        double c = 300.03;
        log.info(String.valueOf(a + b + c));
    }

    @Test
    public void doubleTestDegree() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> paramKv = new HashMap<>();
        paramKv.put("a", "1.009");
        paramKv.put("b", "20.2");
        paramKv.put("c", "300.03");
        log.info(String.valueOf(expressionCalculator.evaluate("c-b-a", paramKv, 4)));
        double a = 1.009;
        double b = 20.2;
        double c = 300.03;
        log.info(String.valueOf(c - b - a));
    }

    @Test
    public void doubleTestMul() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> paramKv = new HashMap<>();
        paramKv.put("a", "2.00");
        paramKv.put("b", "0.0005");
        paramKv.put("c", "300.0378921");
        log.info(String.valueOf(expressionCalculator.evaluate("b*a", paramKv, 4)));
        double a = 2.00;
        double b = 0.0005;
        log.info(String.valueOf(b * a));
    }

    @Test
    public void doubleTestDiv() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> paramKv = new HashMap<>();
        paramKv.put("a", "2.00");
        paramKv.put("b", "0.0005");
        paramKv.put("c", "300.0378921");
        log.info(String.valueOf(expressionCalculator.evaluate("b/a-c", paramKv, 4)));
        double a = 2.00;
        double b = 0.0005;
        double c = 300.0378921;
        log.info(String.valueOf(b / a - c));
    }

    @Test
    public void doubleTestNoParam() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> paramKv = new HashMap<>();
        paramKv.put("a", "0.1");
        log.info(String.valueOf(expressionCalculator.evaluate("a", paramKv, 4)));
        double a = 0.1;
        System.out.println(a);
        log.info(String.valueOf(a));
    }

    @Test
    public void testDiv() {
        try {
            ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
            HashMap<String, String> paramKv = new HashMap<>();
            paramKv.put("a", "1");
            paramKv.put("b", "100");
            log.info(String.valueOf(expressionCalculator.evaluate("a/b", paramKv, 2)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testP() {
        BigDecimal bigDecimal = new BigDecimal("1.000000");
        int scale = bigDecimal.scale();
        System.out.println(scale);
        BigDecimal bigDecimal1 = new BigDecimal("3");
        System.out.println(bigDecimal.divide(bigDecimal1, BigDecimal.ROUND_CEILING));
    }

    @Test
    public void modTest() {
        BigDecimal bigDecimal = new BigDecimal("7.2");
        System.out.println(bigDecimal.remainder(new BigDecimal("3")));
    }

    @Test
    public void testBigMath() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        Map<String, String> map = new HashMap<>();
        map.put("a", "20");
        System.out.println(expressionCalculator.evaluate("sina", map, 5));
    }

    @Test
    public void testBigMath2() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        Map<String, String> map = new HashMap<>();
        map.put("a", "8");
        map.put("b", "0.3");
        System.out.println(expressionCalculator.evaluate("pow(a,b)", map, 5));
    }

    @Test
    public void test() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        Map<String, String> map = new HashMap<>();
        map.put("a", "2");
        map.put("b", "-1");
        System.out.println(expressionCalculator.evaluate("a^b", map, 5));
    }

    @Test
    public void testCbrt() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        Map<String, String> map = new HashMap<>();
        map.put("a", "8");
        System.out.println(expressionCalculator.evaluate("cbrt(a)", map, 4));
    }
}
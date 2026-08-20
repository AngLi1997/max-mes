package bmos;

import com.bmos.common.exception.BmosException;
import com.bmos.expression.bmos.ExpressionCalculatorImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ExceptionTest {

    @Test(expected = BmosException.class)
    public void varNotSet() {
        ExpressionCalculatorImpl expressionCalculatorImpl = new ExpressionCalculatorImpl();
        expressionCalculatorImpl.evaluate("a+b", new HashMap<>(), 1);
    }

    @Test
    public void unableParseChar() {
        Assert.assertThrows(BmosException.class, () -> {
            ExpressionCalculatorImpl expressionCalculatorImpl = new ExpressionCalculatorImpl();
            System.out.println(expressionCalculatorImpl.parseParams("，*3"));
        });
    }

    @Test(expected = BmosException.class)
    public void divisionByZero() {
        ExpressionCalculatorImpl expressionCalculatorImpl = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculatorImpl.evaluate("3/0", new HashMap<>(), 1));
    }

    @Test(expected = BmosException.class)
    public void zeroArgFun() {
        ExpressionCalculatorImpl expressionCalculatorImpl = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculatorImpl.evaluate("cot(0)", new HashMap<>(), 1));
    }

    @Test(expected = BmosException.class)
    public void emptyExpression() {
        ExpressionCalculatorImpl expressionCalculatorImpl = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculatorImpl.parseParams(""));
    }

    @Test(expected = BmosException.class)
    public void invalidNumberOfArg() {
        ExpressionCalculatorImpl expressionCalculatorImpl = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculatorImpl.parseParams("sin(a,b)"));
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "0.2");
        map.put("b", "0.1");
        System.out.println(expressionCalculatorImpl.evaluate("sin()", map, 1));
        System.out.println(expressionCalculatorImpl.evaluate("sin(a,b)", map, 1));
    }

    @Test(expected = BmosException.class)
    public void wrongTest() {
        ExpressionCalculatorImpl expressionCalculatorImpl = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculatorImpl.parseParams("a+b)"));
    }

    @Test(expected = BmosException.class)
    public void testNoVar() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("1+4*2"));
    }

    @Test(expected = BmosException.class)
    public void testNoMo() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("a%b"));
    }


}

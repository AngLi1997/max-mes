package bmos;

import com.bmos.common.exception.BmosException;
import com.bmos.expression.bmos.ExpressionCalculator;
import com.bmos.expression.bmos.ExpressionCalculatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;

@Slf4j
public class ParseTest {

    @Test
    public void support1() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        log.info(expressionCalculator.parseParams("(a+b)/100+d+c").toString());
    }

    @Test
    public void support2() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        log.info(expressionCalculator.parseParams("sinx+y+z").toString());
    }

    @Test
    public void support3() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        // 会将3[x]计算出一个值然后sin
        log.info(expressionCalculator.parseParams("sin3[x]").toString());
        HashMap<String, String> map = new HashMap<>();
        map.put("x", "30.0");
        System.out.println(expressionCalculator.evaluate("sin3[x]", map, 4));
    }

    @Test
    public void support4() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        log.info(expressionCalculator.parseParams("sin(x+1)").toString());
    }

    @Test
    public void support5() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        log.info(expressionCalculator.parseParams("sin(a) + cos(b) + aa + bb +ab + c+d").toString());
        log.info(expressionCalculator.parseParams("t sin(a) + cos(b) + aa + bb +ab + c+d").toString());
    }

    @Test(expected = BmosException.class)
    public void exception1() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        // com.bmos.common.exception.BmosException: 括号不匹配,请检查表达式
        log.info(expressionCalculator.parseParams("sin3x]").toString());
    }

    @Test(expected = BmosException.class)
    public void exception2() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        // com.bmos.common.exception.BmosException: 函数[sin]参数数量异常
        log.info(expressionCalculator.parseParams("sin[]").toString());
    }

    @Test(expected = BmosException.class)
    public void exception3() {
        ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
        // com.bmos.common.exception.BmosException: 表达式不可为空
        log.info(expressionCalculator.parseParams("").toString());
    }

    @Test
    public void exception4() {
        try {
            ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
            // 不会抛出异常 只有在计算时才会检查除0异常
            log.info(expressionCalculator.parseParams("a+b+c/0").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void exception5() {
        try {
            ExpressionCalculator expressionCalculator = new ExpressionCalculatorImpl();
            //
            log.info(expressionCalculator.parseParams("log10a").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

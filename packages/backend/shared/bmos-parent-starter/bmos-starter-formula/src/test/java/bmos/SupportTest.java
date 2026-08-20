package bmos;

import com.bmos.common.exception.BmosException;
import com.bmos.expression.bmos.ExpressionCalculatorImpl;
import org.junit.Test;

import java.util.HashMap;

public class SupportTest {

    @Test
    public void test1() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("(a+b)/c"));//[a, b, c]
    }

    @Test
    public void test2() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("a*(aa+ab)/c"));//[aa, a, ab, c]
    }

    @Test
    public void test3() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("sina*(+ab)/c"));//[a, ab, c]
    }

    @Test
    public void test4() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("asinb+d"));//[b, d] asin为函数
    }

    @Test
    public void test5() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("tsinb+d"));//[d, tsinb]
        System.out.println(expressionCalculator.parseParams("tsin(b)+d"));//[tsin, b, d]
        System.out.println(expressionCalculator.parseParams("tsin+d"));//[tsin, d]
        System.out.println(expressionCalculator.parseParams("t*sinx+d"));//[t, d, x]
    }

    @Test
    public void test6() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        System.out.println(expressionCalculator.parseParams("log10a"));// [a]
        map.put("a", "10");
        System.out.println(expressionCalculator.evaluate("log10a", map, 4));// 1.0000
        System.out.println(expressionCalculator.parseParams("log10(a)"));// [a]
        System.out.println(expressionCalculator.evaluate("log10(a)", map, 4));// 1.0000
    }

    @Test
    public void test7() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "3");
        System.out.println(expressionCalculator.evaluate("a/b", map, 8));// 0.33333333
        System.out.println(expressionCalculator.evaluate("π", map, 7));// 3.1415927
        System.out.println(expressionCalculator.evaluate("b*π", map, 7));// 9.4247780 若为bπ则将bπ视为一个参数
    }

    @Test
    public void test8() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "3");
        System.out.println(expressionCalculator.evaluate("sina*b", map, 10));// 0.1411200081
        System.out.println(expressionCalculator.evaluate("sin(a*b)", map, 10));// 0.1411200081
        System.out.println(expressionCalculator.evaluate("b*sina", map, 10));// 2.5244129544
    }

    @Test(expected = BmosException.class)
    public void test9() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "3");
        System.out.println(expressionCalculator.evaluate("（a+b）/2", map, 10));

    }

    @Test(expected = BmosException.class)
    public void test10() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("x", "0");
        System.out.println(expressionCalculator.evaluate("8x*9", map, 4));// 0.0000
        System.out.println(expressionCalculator.evaluate("8x9", map, 4));// x9参数值未指定
    }

    @Test
    public void test11() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "3");
        System.out.println(expressionCalculator.evaluate("a/b*3", map, 8));// 0.99999999
    }

    @Test
    public void test12() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "16");
        System.out.println(expressionCalculator.evaluate("sqrt(sqrt(a))", map, 1)); // 2.0
    }

    @Test
    public void testNewFun() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "16");
        map.put("b", "333");
        System.out.println(expressionCalculator.evaluate("max(a,b)", map, 1)); // 333.0
        System.out.println(expressionCalculator.evaluate("min(a,b)", map, 1)); // 16.0
        System.out.println(expressionCalculator.evaluate("3e", map, 10)); // 8.1548454854
        System.out.println(expressionCalculator.evaluate("sqrt(min(a,b))", map, 2)); // 4.00
        System.out.println(expressionCalculator.parseParams("sqrt(min(a,b))"));  // [a, b]
    }

    @Test
    public void testBlankParse() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("a sinb")); // [a, b]
        System.out.println(expressionCalculator.parseParams("xsiny")); // [xsiny]
        System.out.println(expressionCalculator.parseParams("a max(b,c)")); // [a, b, c]
    }

    @Test
    public void testBlankParse2() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("--A")); // [A]
        System.out.println(expressionCalculator.parseParams("--+A")); // [A]

//        HashMap<String, String> map = new HashMap<>();
//        map.put("A","3");
//        System.out.println(expressionCalculator.evaluate("-+A", map, 4));
//        System.out.println(expressionCalculator.evaluate("SIN(B)", map, 4));
    }


    @Test
    public void testLong() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        System.out.println(expressionCalculator.parseParams("max(a,b)+min(a,b)*abs(c)+sqrt(A)/log10(100)+log2(B)-ln(e)"));
//        HashMap<String, String> map = new HashMap<>();
//        map.put("tes1","10");
//        map.put("啊","12");
//        map.put("acsd","2");
//        System.out.println(expressionCalculator.evaluate("tes1(啊)(acsd)", map, 4));
    }

    @Test
    public void testKh() {
        ExpressionCalculatorImpl expressionCalculator = new ExpressionCalculatorImpl();
        HashMap<String, String> map = new HashMap<>();
        map.put("b", "2");
//        map.put("e","10");
        System.out.println(expressionCalculator.evaluate("log(e)+log2b", map, 4));
    }
}

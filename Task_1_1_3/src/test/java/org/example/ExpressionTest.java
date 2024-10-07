package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpressionTest {

    @Test
    void testAddDerivative() {
        Expression expr = new Add(new Mul(new Number(5), new Variable("x")),
                new Variable("x"));
        Expression de = expr.derivative("x");
        Expression ans = new Add(new Add(new Mul(new Number(0), new Variable("x")),
                new Mul(new Number(5), new Number(1))), new Number(1));
        Assertions.assertTrue(ans.equals(de));
    }

    @Test
    void testSubDerivative() {
        Expression expr = new Sub(new Mul(new Number(5), new Variable("x")),
                new Variable("x"));
        Expression de = expr.derivative("x");
        Expression ans = new Sub(new Add(new Mul(new Number(0), new Variable("x")),
                new Mul(new Number(5), new Number(1))), new Number(1));
        Assertions.assertTrue(ans.equals(de));
    }

    @Test
    void testMulDerivative() {
        Expression expr = new Sub(new Mul(new Number(5), new Variable("x")),
                new Variable("x"));
        Expression de = expr.derivative("x");
        Expression ans = new Sub(new Add(new Mul(new Number(0), new Variable("x")),
                new Mul(new Number(5), new Number(1))),
                new Number(1));
        Assertions.assertTrue(ans.equals(de));
    }

    @Test
    void testDivDerivative() {
        Expression expr = new Div(new Variable("x"), new Variable("y"));
        Expression ans = new Div(new Sub(new Mul(new Number(1), new Variable("y")),
                    new Mul(new Variable("x"), new Number(0))), new Mul(new Variable("y"),
                        new Variable("y")));
        Assertions.assertTrue(ans.equals(expr.derivative("x")));
    }

    @Test
    void testParseCorrectString() {
        Expression expr = Expression.parse("367254 + 456*(X-43*y) / 456");
        Expression ans = new Add(new Number(367254),
                new Div(new Mul(new Number(456), new Sub(new Variable("X"),
                        new Mul(new Number(43), new Variable("y")))), new Number(456)));
        Assertions.assertTrue(expr.equals(ans));
    }

    @Test
    void testSimplification() {
        Expression expr = Expression.parse("(((0 + 13223 - 3) * (1 + (x * (3 - 3) * 1))) / 1)/x");
        Expression simply = new Div(new Number(13220), new Variable("x"));
        Assertions.assertTrue(expr.simplification().equals(simply));
    }

    @Test
    void testEval() {
        try {
            Expression expr = Expression.parse("x * y + z - T");
            expr.eval("x = 3");
            Assertions.fail();
        } catch (EvalException e) {
            Assertions.assertTrue(true);
        }
    }
}
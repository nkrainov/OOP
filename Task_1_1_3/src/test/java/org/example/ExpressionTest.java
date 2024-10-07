package org.example;

import org.junit.jupiter.api.Test;

class ExpressionTest {

    @Test
    void testAddDerivative() {
        Expression expr = new Add(new Mul(new Number(5), new Variable("x")), new Variable("x"));
        Expression de = expr.derivative();
        Expression ans = new Add(new Add(new Mul(new Number(0), new Variable("x")), new Mul(new Number(5), new Number(1))), new Number(1));
        assert ans.equals(de);
    }

    @Test
    void testSubDerivative() {
        Expression expr = new Sub(new Mul(new Number(5), new Variable("x")), new Variable("x"));
        Expression de = expr.derivative();
        Expression ans = new Sub(new Add(new Mul(new Number(0), new Variable("x")), new Mul(new Number(5), new Number(1))), new Number(1));
        assert ans.equals(de);
    }

    @Test
    void testMulDerivative() {
        Expression expr = new Sub(new Mul(new Number(5), new Variable("x")), new Variable("x"));
        Expression de = expr.derivative();
        Expression ans = new Sub(new Add(new Mul(new Number(0), new Variable("x")),
                new Mul(new Number(5), new Number(1))),
                new Number(1));
        assert ans.equals(de);
    }

    @Test
    void testDivDerivative() {
        Expression expr = new Div(new Variable("x"), new Variable("y"));
        Expression ans = new Div(new Sub(new Mul(new Number(1), new Variable("y")),
                new Mul(new Variable("x"), new Number(1))), new Mul(new Variable("y"),
                new Variable("y")));
        assert ans.equals(expr.derivative());
    }
}
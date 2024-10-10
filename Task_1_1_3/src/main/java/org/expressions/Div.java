package org.expressions;

/**
 * Класс, реализующий операцию деления.
 */
public class Div extends BinOp {
    public Div(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * Функция вывода на экран.
     */
    public void print() {
        System.out.print("(");
        super.getFirstOp().print();
        System.out.print("/");
        super.getSecondOp().print();
        System.out.print(")");
    }

    /**
     * Функция нахождения производной.
     */
    public Expression derivative(String derVar) {
        return new Div(new Sub(new Mul(super.getFirstOp().derivative(derVar), super.getSecondOp()),
                new Mul(super.getFirstOp(), super.getSecondOp().derivative(derVar))),
                new Mul(super.getSecondOp(), super.getSecondOp()));
    }

    /**
     * Функция нахождения значения выражения.
     */
    public int eval(String varVal) {
        return super.getFirstOp().eval(varVal) / super.getSecondOp().eval(varVal);
    }

    /**
     * Функция упрощения выражения.
     */
    @Override
    public Expression simplification() {
        Expression op1 = super.getFirstOp().simplification();
        Expression op2 = super.getSecondOp().simplification();

        if (op1 instanceof Number && op1.eval(" ") == 0) {
            return new Number(0);
        } else if (op2 instanceof Number && op2.eval(" ") == 1) {
            return op1;
        }

        Div ans = new Div(op1, op2);
        if (!ans.hasVars()) {
            return new Number(ans.eval(" "));
        }
        return ans;
    }

    /**
     * Функция проверки наличия переменных в выражении.
     */
    public boolean hasVars() {
        return super.getFirstOp().hasVars() || super.getSecondOp().hasVars();
    }

    /**
     * Функция проверки равенства выражений.
     */
    public boolean equals(Expression expr) {
        if (!(expr instanceof Div)) {
            return false;
        }

        Div div = (Div) expr;
        return super.getFirstOp().equals(div.getFirstOp())
                && super.getSecondOp().equals(div.getSecondOp());
    }
}

package org.example;

/**
 * Класс, реализующий игру операцию сложения.
 */
public class Add extends BinOp {
    public Add(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * Функция вывода на экран.
     */
    public void print() {
        System.out.print("(");
        super.getFirstOp().print();
        System.out.print("+");
        super.getSecondOp().print();
        System.out.print(")");
    }

    /**
     * Функция нахождения производной.
     */
    public Expression derivative() {
        return new Add(super.getFirstOp().derivative(), super.getSecondOp().derivative());
    }

    /**
     * Функция нахождения значения выражения.
     */
    public int eval(String varVal) {
        return super.getFirstOp().eval(varVal) + super.getSecondOp().eval(varVal);
    }

    /**
     * Функция нахождения производной.
     */
    @Override
    public Expression simplification() {
        if (!hasVars()) {
            return new Number(eval(" "));
        }
        Expression ans = new Add(super.getFirstOp().simplification(), super.getSecondOp().simplification());
        if (!ans.hasVars()) {
            return new Number(ans.eval(" "));
        }

        return ans;
    }

    /**
     * Эта функция проверяет, имеются ли в выражении переменные.
     */
    public boolean hasVars() {
        return super.getFirstOp().hasVars() || super.getSecondOp().hasVars();
    }

    /**
     * Функция проверки равенства выражений.
     */
    public boolean equals(Expression expr) {
        if (!(expr instanceof Add)) {
            return false;
        }
        Add add = (Add) expr;
        return super.getFirstOp().equals(add.getFirstOp()) && super.getSecondOp().equals(add.getSecondOp());
    }
}

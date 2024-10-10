package org.expressions;

/**
 * Класс, реализующий операцию вычитания.
 */
public class Sub extends BinOp {
    public Sub(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * Функция вывода на экран.
     */
    public void print() {
        System.out.print("(");
        super.getFirstOp().print();
        System.out.print("-");
        super.getSecondOp().print();
        System.out.print(")");
    }

    /**
     * Функция нахождения производной.
     */
    public Expression derivative(String derVar) {
        return new Sub(super.getFirstOp().derivative(derVar),
                super.getSecondOp().derivative(derVar));
    }

    /**
     * Функция нахождения значения выражения.
     */
    public int eval(String varVal) {
        return super.getFirstOp().eval(varVal)
                - super.getSecondOp().eval(varVal);
    }

    /**
     * Функция упрощения выражения.
     */
    @Override
    public Expression simplification() {
        Expression op1 = super.getFirstOp().simplification();
        Expression op2 = super.getSecondOp().simplification();

        if (op1.equals(op2)) {
            return new Number(0);
        }

        Sub ans = new Sub(op1, op2);

        if (!ans.hasVars()) {
            return new Number((ans.eval(" ")));
        }
        return ans;
    }

    /**
     * Эта функция проверяет, имеются ли в выражении переменные.
     */
    public boolean hasVars() {
        return super.getFirstOp().hasVars()
                || super.getSecondOp().hasVars();
    }

    /**
     * Функция проверки равенства выражений.
     */
    public boolean equals(Expression expr) {
        if (!(expr instanceof Sub)) {
            return false;
        }

        Sub sub = (Sub) expr;
        return super.getFirstOp().equals(sub.getFirstOp())
                && super.getSecondOp().equals(sub.getSecondOp());
    }
}


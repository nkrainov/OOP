package org.example;

/**
 * Класс, реализующий операцию умножения.
 */
public class Mul extends BinOp {
    public Mul(Expression first, Expression second) {
        super(first, second);
    }

    /**
     * Функция вывода на экран.
     */
    public void print() {
        System.out.print("(");
        super.getFirstOp().print();
        System.out.print("*");
        super.getSecondOp().print();
        System.out.print(")");
    }

    /**
     * Функция нахождения производной.
     */
    public Expression derivative() {
        return new Add(new Mul(super.getFirstOp().derivative(), super.getSecondOp()), new Mul(super.getFirstOp(), super.getSecondOp().derivative()));
    }

    /**
     * Функция, получающая значение выражения при данном означивании.
     */
    public int eval(String varVal) {
        return super.getFirstOp().eval(varVal) * super.getSecondOp().eval(varVal);
    }

    /**
     * Функция упрощения выражения.
     */
    @Override
    public Expression simplification() {
        Expression op1 = super.getFirstOp().simplification();
        Expression op2 = super.getSecondOp().simplification();

        if (op1 instanceof Number && op1.eval("") == 1) {
            return op2.clone();
        } else if (op1 instanceof Number && op1.eval("") == 0) {
            return new Number(0);
        } else if (op2 instanceof Number && op2.eval("") == 1) {
            return op1.clone();
        } else if (op2 instanceof Number && op2.eval("") == 0) {
            return new Number(0);
        }

        if (!hasVars()) {
            Mul ans = new Mul(op1, op2);
            return new Number(ans.eval(" "));
        }

        return new Mul(op1, op2);

    }

    /**
     * Функция проверки наличия в выражении переменных.
     */
    public boolean hasVars() {
        return super.getFirstOp().hasVars() || super.getSecondOp().hasVars();
    }

    /**
     * Функция проверки равенства выражений.
     */
    public boolean equals(Expression expr) {
        if (!(expr instanceof Mul)) {
            return false;
        }

        Mul mul = (Mul) expr;
        return super.getFirstOp().equals(mul.getFirstOp()) && super.getSecondOp().equals(mul.getSecondOp());
    }
}

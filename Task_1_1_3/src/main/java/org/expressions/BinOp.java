package org.expressions;

/**
 * Этот класс расширяет Expressions для случая бинарных выражений.
 */
public abstract class BinOp extends Expression {
    private final Expression firstOp;
    private final Expression secondOp;

    public BinOp(Expression first, Expression second) {
        firstOp = first.clone();
        secondOp = second.clone();
    }

    /**
     * Геттер для первого операнда.
     */
    Expression getFirstOp() {
        return firstOp.clone();
    }

    /**
     * Геттер для второго операнда.
     */
    Expression getSecondOp() {
        return secondOp.clone();
    }
}

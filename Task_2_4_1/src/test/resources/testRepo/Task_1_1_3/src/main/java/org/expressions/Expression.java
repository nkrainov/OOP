package org.expressions;

/**
 * Класс, являющийся шаблоном для математических выражений.
 * Также реализует функция парсинга выражения из строки
 */
public abstract class Expression implements Cloneable {
    /**
     * Функция вывода на экран.
     */
    public abstract void print();

    /**
     * Функция нахождения производной.
     */
    public abstract Expression derivative(String derVar);


    /**
     * Функция вычисления выражения при данном означивании.
     */
    public abstract int eval(String varVal);

    /**
     * Функция проверки наличия переменных в выражении.
     */
    public abstract boolean hasVars();

    /**
     * Функция упрощения выражения.
     */
    public abstract Expression simplification();

    /**
     * Функция клонирования выражения.
     */
    public Expression clone() {
        try {
            return (Expression) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Функция проверки индентичность выражений.
     */
    public abstract boolean equals(Expression expr);
}

package org.example;

/**
 * Класс, реализующий число.
 */
public class Number extends Expression {
    private String num;

    public Number(int number) {
        num = String.valueOf(number);
    }

    /**
     * Функция вывода на экран.
     */
    public void print() {
        if (Integer.parseInt(num) < 0) {
            System.out.print("(" + num + ")");
        } else {
            System.out.print(num);
        }
    }

    /**
     * Функция нахождения производной.
     */
    public Expression derivative() {
        return new Number(0);
    }

    /**
     * Функция нахождения значения выражения при данном означивании.
     */
    public int eval(String varVal) {
        return Integer.parseInt(num);
    }

    /**
     * Функция упрощения выражения.
     */
    @Override
    public Expression simplification() {
        return this.clone();
    }

    /**
     * Функция проверки наличия переменных в выражении.
     */
    public boolean hasVars() {
        return false;
    }

    /**
     * Функция проверки идентичности двух выражений.
     */
    public boolean equals(Expression expr) {
        if (!(expr instanceof Number)) {
            return false;
        }

        Number number = (Number) expr;
        return num.equals(number.num);
    }
}

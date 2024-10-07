package org.example;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, реализующий переменную.
 */
public class Variable extends Expression {
    private String var;

    public Variable(String newVar) {
        var = newVar;
    }

    /**
     * Функция вывода на экран.
     */
    public void print() {
        System.out.print(var);
    }

    /**
     * Функция нахождения производной.
     */
    public Expression derivative(String derVar) {
        if (derVar.equals(var)) {
            return new Number(1);
        }

        return new Number(0);
    }

    /**
     * Функция нахождения значения выражения. Если значение нужной переменной не было найдено,
     * то выбрасывает исключение.
     */
    public int eval(String varVal) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String str = varVal.replaceAll(" ", "");
        String[] variablesPairs = str.split(";");
        String[] variables;
        for (String pair : variablesPairs) {
            variables = pair.split("=");
            if (variables[0].equals(var)) {
                return Integer.parseInt(variables[1]);
            }
        }

        throw new EvalException(null);
    }

    /**
     * Функция упрощения выражения.
     */
    @Override
    public Expression simplification() {
        return this.clone();
    }

    /**
     * Функция проверки наличия в выражении переменных.
     */
    public boolean hasVars() {
        return true;
    }

    /**
     * Функция проверки равенства выражений.
     */
    public boolean equals(Expression expr) {
        if (!(expr instanceof Variable)) {
            return false;
        }

        Variable variable = (Variable) expr;
        return var.equals(variable.var);
    }
}

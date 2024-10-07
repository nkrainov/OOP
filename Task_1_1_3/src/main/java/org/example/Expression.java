package org.example;

import java.util.Stack;


/**
 * Класс, являющийся шаблоном для математических выражений.
 * Также реализует функция парсинга выражения из строки
 */
abstract class Expression implements Cloneable {
    /**
     * Функция вывода на экран.
     */
    abstract void print();

    /**
     * Функция нахождения производной.
     */
    abstract Expression derivative();


    /**
     * Функция вычисления выражения при данном означивании.
     */
    abstract int eval(String varVal);

    /**
     * Функция проверки наличия переменных в выражении.
     */
    abstract boolean hasVars();

    /**
     * Функция, получающая из строки выражение.
     */
    static public Expression parse(String expr) {
        String str = expr.replaceAll(" ", "");

        Stack<Expression> stackExpr = new Stack<Expression>();
        Stack<String> stackOp = new Stack<String>();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                int start = i;
                boolean flag = false;
                while (i < str.length() && Character.isDigit(str.charAt(i))) {
                    i++;
                    flag = true;
                }
                stackExpr.push(new Number(Integer.parseInt((str.substring(start, i)))));
                if (flag) {
                    i--;
                }
            } else if (Character.isLetter(str.charAt(i))) {
                int start = i;
                boolean flag = false;
                while (i < str.length() && Character.isLetter(str.charAt(i))) {
                    i++;
                    flag = true;
                }
                stackExpr.push(new Variable(str.substring(start, i)));
                if (flag) {
                    i--;
                }
            } else if (str.charAt(i) == '(') {
                stackOp.push("(");
            } else if (str.charAt(i) == ')') {
                while (true) {
                    String op = stackOp.pop();
                    if (op.equals("(")) {
                        break;
                    } else {
                        Expression op2 = stackExpr.pop();
                        Expression op1 = stackExpr.pop();
                        if (op.equals("+")) {
                            stackExpr.push(new Add(op1, op2));
                        } else if (op.equals("-")) {
                            stackExpr.push(new Sub(op1, op2));
                        } else if (op.equals("*")) {
                            stackExpr.push(new Mul(op1, op2));
                        } else if (op.equals("/")) {
                            stackExpr.push(new Div(op1, op2));
                        }
                    }
                }
            } else if (str.charAt(i) == '*') {
                while (!(stackOp.empty()
                        || stackOp.peek().equals("(")
                        || stackOp.peek().equals("+")
                        || stackOp.peek().equals("-"))) {
                    Expression op2 = stackExpr.pop();
                    Expression op1 = stackExpr.pop();
                    String op = stackOp.pop();
                    stackExpr.push(getExprWithOp(op, op1, op2));
                }
                stackOp.push("*");
            } else if (str.charAt(i) == '/') {
                while (!(stackOp.empty()
                        || stackOp.peek().equals("(")
                        || stackOp.peek().equals("+")
                        || stackOp.peek().equals("-"))) {
                    Expression op2 = stackExpr.pop();
                    Expression op1 = stackExpr.pop();
                    String op = stackOp.pop();
                    stackExpr.push(getExprWithOp(op, op1, op2));
                }
                stackOp.push("/");
            } else if (str.charAt(i) == '+') {
                while (!(stackOp.empty()
                        || stackOp.peek().equals("("))) {
                    Expression op2 = stackExpr.pop();
                    Expression op1 = stackExpr.pop();
                    String op = stackOp.pop();
                    stackExpr.push(getExprWithOp(op, op1, op2));
                }
                stackOp.push("+");
            } else if (str.charAt(i) == '-') {
                while (!(stackOp.empty()
                        || stackOp.peek().equals("("))) {
                    Expression op2 = stackExpr.pop();
                    Expression op1 = stackExpr.pop();
                    String op = stackOp.pop();
                    stackExpr.push(getExprWithOp(op, op1, op2));
                }
                stackOp.push("-");
            }
        }

        while (!stackOp.empty()) {
            String op = stackOp.pop();
            Expression op2 = stackExpr.pop();
            Expression op1 = stackExpr.pop();
            stackExpr.push(getExprWithOp(op, op1, op2));
        }

        return stackExpr.pop();

    }

    static private Expression getExprWithOp(String op, Expression op1, Expression op2) {
        Expression ans;
        if (op.equals("+")) {
            ans = new Add(op1, op2);
        } else if (op.equals("-")) {
            ans = new Sub(op1, op2);
        } else if (op.equals("*")) {
            ans = new Mul(op1, op2);
        } else if (op.equals("/")) {
            ans = new Div(op1, op2);
        } else{
            ans = null;
        }

        return ans;
    }

    /**
     * Функция упрощения выражения.
     */
    public Expression simplification() {
        if (!hasVars()) {
            return new Number(eval(" "));
        }
        return this.clone();
    }

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
    abstract boolean equals(Expression expr);
}

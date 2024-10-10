package org.parse;

import org.expressions.*;
import org.expressions.Number;

import org.exceptions.ParseException;

import java.util.Stack;

public class Parser {
    /**
     * Функция, получающая из строки выражение.
     * В случае ошибки или некорректной обработки выражения выкидывает исключение.
     */
    public static Expression parse(String expr) {
        try {
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

            if (stackExpr.size() != 1) {
                throw new Exception();
            }
            return stackExpr.pop();
        } catch (Exception e) {
            throw new ParseException("Parse failed");
        }

    }

    private static Expression getExprWithOp(String op, Expression op1, Expression op2) {
        Expression ans;
        if (op.equals("+")) {
            ans = new Add(op1, op2);
        } else if (op.equals("-")) {
            ans = new Sub(op1, op2);
        } else if (op.equals("*")) {
            ans = new Mul(op1, op2);
        } else if (op.equals("/")) {
            ans = new Div(op1, op2);
        } else {
            ans = null;
        }

        return ans;
    }

}

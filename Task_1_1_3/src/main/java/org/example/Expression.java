package org.example;

import java.util.Stack;

abstract public class Expression implements Cloneable {
    abstract public void print();

    abstract public Expression derivative() ;

    abstract public int eval(String varVal);

    abstract public boolean hasVars();

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
                while (true){
                    String op = stackOp.pop();
                    if (op.equals("(")) {
                        break;
                    } else {
                        Expression op2 = stackExpr.pop();
                        Expression op1 = stackExpr.pop();
                        if (op.equals("+")) {
                            stackExpr.push(new Add(op1, op2));
                        } else if (op.equals("-")){
                            stackExpr.push(new Sub(op1, op2));
                        } else if (op.equals("*")){
                            stackExpr.push(new Mul(op1, op2));
                        } else if (op.equals("/")){
                            stackExpr.push(new Div(op1, op2));
                        }
                    }
                }
            } else if (str.charAt(i) == '*') {
                if (stackOp.empty()
                    || stackOp.peek().equals("(")
                    || stackOp.peek().equals("+")
                    || stackOp.peek().equals("-")) {
                    stackOp.push("*");
                } else {
                    while (!(stackOp.empty()
                            || stackOp.peek().equals("(")
                            || stackOp.peek().equals("+")
                            || stackOp.peek().equals("-"))) {
                        Expression op2 = stackExpr.pop();
                        Expression op1 = stackExpr.pop();
                        stackExpr.push(new Mul(op1, op2));
                    }
                }
            }
            else if (str.charAt(i) == '/') {
                if (stackOp.empty()
                        || stackOp.peek().equals("(")
                        || stackOp.peek().equals("+")
                        || stackOp.peek().equals("-")) {
                    stackOp.push("/");
                } else {
                    while (!(stackOp.empty()
                            || stackOp.peek().equals("(")
                            || stackOp.peek().equals("+")
                            || stackOp.peek().equals("-"))) {
                        Expression op2 = stackExpr.pop();
                        Expression op1 = stackExpr.pop();
                        stackExpr.push(new Div(op1, op2));
                    }
                }
            }
            else if (str.charAt(i) == '+') {
                if (stackOp.empty()
                    ||stackOp.peek().equals("(")) {
                    stackOp.push("+");
                } else {
                    while (!(stackOp.empty()
                            ||stackOp.peek().equals("("))) {
                        Expression op2 = stackExpr.pop();
                        Expression op1 = stackExpr.pop();
                        stackExpr.push(new Add(op1, op2));
                    }
                }
            }
            else if (str.charAt(i) == '-') {
                if (stackOp.empty()
                    ||stackOp.peek().equals("(")) {
                    stackOp.push("-");
                } else {
                    while (!(stackOp.empty()
                            ||stackOp.peek().equals("("))) {
                        Expression op2 = stackExpr.pop();
                        Expression op1 = stackExpr.pop();
                        stackExpr.push(new Sub(op1, op2));
                    }
                }
            }
        }

        while (!stackOp.empty()) {
            String op = stackOp.pop();
            Expression op2 = stackExpr.pop();
            Expression op1 = stackExpr.pop();
            if (op.equals("+")) {
                stackExpr.push(new Add(op1, op2));
            } else if (op.equals("-")){
                stackExpr.push(new Sub(op1, op2));
            } else if (op.equals("*")){
                stackExpr.push(new Mul(op1, op2));
            } else if (op.equals("/")){
                stackExpr.push(new Div(op1, op2));
            }
        }

        return stackExpr.pop();

    }


    public Expression simplification(){
        if (!hasVars()){
            return new Number(eval(" "));
        }
        return this.clone();
    }

    public Expression clone() {
        try{
            return (Expression) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    abstract public boolean equals(Expression expr);
}

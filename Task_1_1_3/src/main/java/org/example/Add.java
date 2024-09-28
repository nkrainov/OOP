package org.example;

public class Add extends Expression{
    private Expression firstOp;
    private Expression secondOp;
    public Add(Expression first, Expression second){
        firstOp = first;
        secondOp = second;
    }

    public void print(){
        System.out.print("(");
        firstOp.print();
        System.out.print("+");
        secondOp.print();
        System.out.print(")");
    }

    public Expression derivative() {
        return new Add(firstOp.derivative(), secondOp.derivative());
    }

    public int eval(String varVal){
        return firstOp.eval(varVal) + secondOp.eval(varVal);
    }

    public Expression simplification(){
        return super.simplification();
    }

    public boolean hasVars() {
        return firstOp.hasVars() || secondOp.hasVars();
    }
}

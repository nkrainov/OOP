package org.example;

public class Sub extends Expression{
    private Expression firstOp;
    private Expression secondOp;

    public Sub(Expression first, Expression second) {
        firstOp = first.clone();
        secondOp = second.clone();
    }

    public void print(){
        System.out.print("(");
        firstOp.print();
        System.out.print("-");
        secondOp.print();
        System.out.print(")");
    }

    public Expression derivative() {
        return new Sub(firstOp.derivative(), secondOp.derivative());
    }


    public int eval(String varVal){
        return firstOp.eval(varVal) - secondOp.eval(varVal);
    }

    public Expression simplification(){
        return super.simplification();
    }

    public boolean hasVars() {
        return firstOp.hasVars() || secondOp.hasVars();
    }
}

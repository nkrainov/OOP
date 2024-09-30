package org.example;

public class Add extends Expression{
    private Expression firstOp;
    private Expression secondOp;
    public Add(Expression first, Expression second){
        firstOp = first.clone();
        secondOp = second.clone();
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
        return new Add(firstOp.simplification(), secondOp.simplification());
    }

    public boolean hasVars() {
        return firstOp.hasVars() || secondOp.hasVars();
    }

    public boolean equals(Expression expr) {
        if (!(expr instanceof Add add)){
            return false;
        }

        return firstOp.equals(add.firstOp) && secondOp.equals(add.secondOp);
    }
}

package org.example;

public class Div extends Expression{
    private Expression firstOp;
    private Expression secondOp;
    public Div(Expression first, Expression second){
        firstOp = first;
        secondOp = second;
    }

    public void print(){
        System.out.print("(");
        firstOp.print();
        System.out.print("/");
        secondOp.print();
        System.out.print(")");
    }

    public Expression derivative(){
        return new Div(new Sub(new Mul(firstOp.derivative(), secondOp), new Mul(firstOp, secondOp.derivative())), new Mul(secondOp, secondOp));
    }

    public int eval(String varVal){
        return firstOp.eval(varVal) / secondOp.eval(varVal);
    }

    public Expression simplification(){
        return super.simplification();
    }

    public boolean hasVars() {
        return firstOp.hasVars() || secondOp.hasVars();
    }

    public boolean equals(Expression expr) {
        if (!(expr instanceof Div add)){
            return false;
        }

        return firstOp.equals(add.firstOp) && secondOp.equals(add.secondOp);
    }
}

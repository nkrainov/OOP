package org.example;

public class Mul extends Expression{
    private Expression firstOp;
    private Expression secondOp;
    public Mul(Expression first, Expression second){
        firstOp = first;
        secondOp = second;
    }

    public void print(){
        System.out.print("(");
        firstOp.print();
        System.out.print("*");
        secondOp.print();
        System.out.print(")");
    }

    public Expression derivative(){
        return new Add(new Mul(firstOp.derivative(), secondOp), new Mul(firstOp, secondOp.derivative()));
    }

    public int eval(String varVal){
        return firstOp.eval(varVal) * secondOp.eval(varVal);
    }

    @Override
    public Expression simplification() {
        if (!hasVars()){
            return new Number(eval(" "));
        }
        if (!firstOp.hasVars() && firstOp.eval("") == 1){
            return secondOp.clone();
        } else if (!firstOp.hasVars() && firstOp.eval("") == 0) {
            return new Number(0);
        } else if (!secondOp.hasVars() && secondOp.eval("") == 1){
            return firstOp.clone();
        } else if (!secondOp.hasVars() && secondOp.eval("") == 0) {
            return new Number(0);
        }

        return this.clone();

    }

    public boolean hasVars() {
        return firstOp.hasVars() || secondOp.hasVars();
    }
}

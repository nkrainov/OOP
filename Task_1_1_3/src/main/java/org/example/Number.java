package org.example;

public class Number extends Expression{
    private String num;
    public Number(int number){
        num = String.valueOf(number);
    }

    public void print(){
        if (Integer.parseInt(num) < 0){ System.out.print("(" + num + ")");}
        else {System.out.print(num); }
    }

    public Expression derivative(){
        return new Number(0);
    }

    public int eval(String varVal){
        return Integer.parseInt(num);
    }

    @Override
    public Expression simplification(){
        return this.clone();
    }

    public boolean hasVars() {
        return false;
    }

    public boolean equals(Expression expr) {
        if (!(expr instanceof Number)){
            return false;
        }

        Number number = (Number) expr;
        return num.equals(number.num);
    }
}

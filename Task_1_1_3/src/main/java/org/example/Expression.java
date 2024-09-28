package org.example;

abstract public class Expression implements Cloneable {
    abstract public void print();

    abstract public Expression derivative() ;

    abstract public int eval(String varVal);

    abstract public boolean hasVars();

    static public Expression parse(String expr) {
        String str = expr.replaceAll(" ", "");
        Expression ans;

        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '('){
                count++;
            }
            else if (str.charAt(i) == ')'){
                count--;
            }
            else if (count == 0){
                if (str.charAt(i) == '+') {
                    return new Add(parse(str.substring(0, i)), parse(str.substring(i+1)));
                } else if (str.charAt(i) == '-'){
                    return new Sub(parse(str.substring(0, i)), parse(str.substring(i+1)));
                }
            }
        }

        count = 0;
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '('){
                count++;
            }
            else if (str.charAt(i) == ')'){
                count--;
            }
            else if (count ==0 ) {
                if (str.charAt(i) == '*') {
                    return new Mul(parse(str.substring(0, i)), parse(str.substring(i+1)));
                } else if (str.charAt(i) == '/'){
                    return new Div(parse(str.substring(0, i)), parse(str.substring(i+1)));
                }
            }
        }

        if (str.charAt(0) == '(' && str.charAt(str.length()-1) == ')') {
            return parse(str.substring(1, str.length()-1));
        }

        if (Character.isDigit(str.charAt(0))){
            return new Number(Integer.parseInt(str));
        } else {
            return new Variable(str);
        }
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
}
